package com.example.budgetbee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.data.ExpenseEntryEntity
import kotlinx.coroutines.*
import java.time.LocalDate

class SearchFragment : Fragment() {

    private lateinit var categorySpinner: Spinner
    private lateinit var dateSpinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var noItemsTextView: TextView
    private lateinit var adapter: ExpenseAdapter

    private lateinit var db: AppDatabase
    private var userId: Int = -1
    private var allExpenses: List<ExpenseEntryEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        categorySpinner = view.findViewById(R.id.categorySpinner)
        dateSpinner = view.findViewById(R.id.dateSpinner)
        recyclerView = view.findViewById(R.id.recyclerView)
        noItemsTextView = view.findViewById(R.id.noItemsTextView)

        db = AppDatabase.getDatabase(requireContext())

        val prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        setupRecyclerView()
        loadDataFromDb()

        return view
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun loadDataFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val categories = db.categoryDao().getCategoriesForUser(userId)
            allExpenses = db.expenseDao().getAllExpensesForUser(userId)

            val categoryNames = listOf("All") + categories.map { it.name }

            withContext(Dispatchers.Main) {
                setupSpinners(categoryNames)
                filterItems()
            }
        }
    }

    private fun setupSpinners(categoryOptions: List<String>) {
        val dateOptions = listOf("All", "Last 7 Days", "Last 30 Days", "This Month")

        categorySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryOptions
        )

        dateSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            dateOptions
        )

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterItems()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        categorySpinner.onItemSelectedListener = listener
        dateSpinner.onItemSelectedListener = listener
    }

    private fun filterItems() {
        val selectedCategory = categorySpinner.selectedItem.toString()
        val selectedDate = dateSpinner.selectedItem.toString()
        val now = LocalDate.now()

        val filtered = allExpenses.filter { expense ->
            val categoryMatches = selectedCategory == "All" || expense.category == selectedCategory
            val expenseDate = LocalDate.parse(expense.date)

            val dateMatches = when (selectedDate) {
                "All" -> true
                "Last 7 Days" -> expenseDate.isAfter(now.minusDays(7))
                "Last 30 Days" -> expenseDate.isAfter(now.minusDays(30))
                "This Month" -> expenseDate.month == now.month && expenseDate.year == now.year
                else -> true
            }

            categoryMatches && dateMatches
        }

        adapter.submitList(filtered)
        updateNoItemsMessage(filtered)
    }

    private fun updateNoItemsMessage(filtered: List<ExpenseEntryEntity>) {
        noItemsTextView.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {
        private var items: List<ExpenseEntryEntity> = emptyList()

        fun submitList(data: List<ExpenseEntryEntity>) {
            items = data
            notifyDataSetChanged()
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.textView.text = "> ${item.name} - ${item.category} (${item.date})"
        }

        override fun getItemCount(): Int = items.size
    }
}
