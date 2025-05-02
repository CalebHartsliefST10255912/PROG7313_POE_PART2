package com.example.budgetbee

import ExpenseAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.data.ExpenseDao
import com.example.budgetbee.data.ExpenseEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private lateinit var expenseDao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the DAO
        val db = AppDatabase.getDatabase(this)
        expenseDao = db.expenseDao()

        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                expenseDao.getAllExpensesForUser(getUserId())  // Assuming you have a method to get userId
            }

            adapter = ExpenseAdapter()
            adapter.submitList(expenses)
            recyclerView.adapter = adapter
        }
    }

    private fun getUserId(): Int {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return prefs.getInt("userId", -1)
    }
}
