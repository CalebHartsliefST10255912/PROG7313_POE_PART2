package com.example.budgetbee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryDetailsFragment : Fragment() {

    private lateinit var adapter: ExpenseAdapter
    private lateinit var db: AppDatabase

    private var categoryId: Int = -1
    private var categoryName: String = "Category"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt("CATEGORY_ID", -1)
            categoryName = it.getString("CATEGORY_NAME") ?: "Category"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())

        view.findViewById<TextView>(R.id.textCategoryTitle).text = categoryName

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerExpenses)
        adapter = ExpenseAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        loadExpenses(view)
    }

    private fun loadExpenses(view: View) {
        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesForCategory(categoryId)
            }

            adapter.submitList(expenses)
            val totalExpense = expenses.sumOf { it.amount }

            view.findViewById<TextView>(R.id.textTotalExpense).text =
                "Total Spent: R %.2f".format(totalExpense)

            val prefs = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
            val userId = prefs.getInt("userId", -1)

            val goal = withContext(Dispatchers.IO) {
                db.goalsDao().getGoal(userId)
            }

            if (goal != null) {
                val budget = goal.maxMonthlyGoal
                val remaining = (budget - totalExpense).coerceAtLeast(0.0)
                val percentage = ((totalExpense / budget) * 100).toInt().coerceAtMost(100)

                view.findViewById<TextView>(R.id.textTotalBalance).text = "R %.2f".format(remaining)
                view.findViewById<ProgressBar>(R.id.expenseProgress)?.progress = percentage
                view.findViewById<TextView>(R.id.textProgressSummary)?.text = "$percentage% of your goal budget used."
            } else {
                view.findViewById<TextView>(R.id.textProgressSummary)?.text = "Set a goal to track your spending."
            }
        }
    }

    companion object {
        fun newInstance(categoryId: Int, categoryName: String) = CategoryDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt("CATEGORY_ID", categoryId)
                putString("CATEGORY_NAME", categoryName)
            }
        }
    }
}
