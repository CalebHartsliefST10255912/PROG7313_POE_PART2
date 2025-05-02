package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewExpensesActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_expenses)

        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)
        if (userId == -1) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        db = AppDatabase.getDatabase(this)
        recyclerView = findViewById(R.id.expenseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExpenseAdapter(emptyList())
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.buttonAddExpense).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        loadExpenses()
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesByUser(userId)
            }
            adapter.updateExpenses(expenses)
        }
    }
}
