package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_category)

        // ✅ Get userId from SharedPreferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        if (userId == -1) {
            // User not logged in properly, redirect to login or handle error
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        db = AppDatabase.getDatabase(this)
        categoryDao = db.categoryDao()

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        lifecycleScope.launch {
            val existingCategories = withContext(Dispatchers.IO) {
                categoryDao.getAll(userId)
            }

            if (existingCategories.isEmpty()) {
                val predefined = listOf(
                    CategoryEntity(userId = userId, name = "Food", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Transport", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Medicine", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Groceries", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Rent", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Gifts", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Savings", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Entertainment", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Add", iconResId = R.drawable.blue_circle)
                )

                withContext(Dispatchers.IO) {
                    predefined.forEach { categoryDao.insert(it) }
                }
            }

            val savedCategories = withContext(Dispatchers.IO) {
                categoryDao.getAll(userId)
            }

            val (moreCategory, otherCategories) = savedCategories.partition { it.name == "Add" }
            val orderedCategories = otherCategories + moreCategory

            adapter = CategoryAdapter(orderedCategories) { selectedCategory ->
                if (selectedCategory.name == "Add") {
                    showAddCategoryDialog()
                } else {
                    val intent = Intent(this@CategoriesActivity, CategoryDetailsActivity::class.java)
                    intent.putExtra("CATEGORY_NAME", selectedCategory.name)
                    startActivity(intent)
                }
            }

            recyclerView.adapter = adapter
        }
    }

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Category")

        val input = EditText(this)
        input.hint = "Enter category name"
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val categoryName = input.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                val newCategory = CategoryEntity(
                    userId = userId,
                    name = categoryName,
                    iconResId = R.drawable.blue_circle
                )

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        categoryDao.insert(newCategory)
                    }
                    refreshCategoryList()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun refreshCategoryList() {
        lifecycleScope.launch {
            val updatedCategories = withContext(Dispatchers.IO) {
                categoryDao.getAll(userId)
            }
            adapter.updateCategories(updatedCategories)
        }
    }
}
