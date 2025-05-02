package com.example.budgetbee

import ExpenseAdapter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.data.ExpenseDao
import com.example.budgetbee.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class CategoryDetailsActivity : AppCompatActivity() {

    private lateinit var expenseDao: ExpenseDao
    private lateinit var adapter: ExpenseAdapter
    private var categoryId: Int = -1
    private var categoryName: String = ""
    private lateinit var goalsDao: GoalsDao
    private lateinit var db: AppDatabase

    //Displays the activity category details page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        db = AppDatabase.getDatabase(this)
        expenseDao = db.expenseDao()
        goalsDao = db.goalsDao()

        categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Category"


        // Set the category name as title
        findViewById<TextView>(R.id.textCategoryTitle).text = categoryName

        // Setup RecyclerView and Adapter for displaying expenses
        adapter = ExpenseAdapter()
        findViewById<RecyclerView>(R.id.recyclerExpenses).adapter = adapter
        findViewById<RecyclerView>(R.id.recyclerExpenses).layoutManager = LinearLayoutManager(this)

        loadExpenses()
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            // Fetch expenses for the selected category
            val expenses = withContext(Dispatchers.IO) {
                expenseDao.getExpensesForCategory(categoryId)
            }

            if (expenses.isEmpty()) {
                Log.d("CategoryDetails", "No expenses found for this category.")
            }

            // Submit the list to the adapter
            adapter.submitList(expenses)

            // Calculate the total expenses
            val totalExpense = expenses.sumOf { it.amount }

            // Update the total expenses UI
            findViewById<TextView>(R.id.textTotalExpense).text =
                "Total Spent: R %.2f".format(totalExpense)

            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val userId = prefs.getInt("userId", -1)

            val goal = withContext(Dispatchers.IO) {
                db.goalsDao().getGoal(userId)
            }

            // Update the remaining balance and progress bar
            if (goal != null) {
                val budget = goal.maxMonthlyGoal
                val remaining = (budget - totalExpense).coerceAtLeast(0.0)
                val percentage = ((totalExpense / budget) * 100).toInt().coerceAtMost(100)

                findViewById<TextView>(R.id.textTotalBalance).text =
                    "R %.2f".format(remaining)

                findViewById<ProgressBar>(R.id.expenseProgress)?.progress = percentage
                findViewById<TextView>(R.id.textProgressSummary)?.text =
                    "$percentage% of your goal budget used."
            } else {
                findViewById<TextView>(R.id.textProgressSummary)?.text =
                    "Set a goal to track your spending."
            }
        }
    }
}
