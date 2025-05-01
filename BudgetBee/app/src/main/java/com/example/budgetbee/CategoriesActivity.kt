package com.example.budgetbee



import android.content.Intent
import android.os.Bundle
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

    val db = AppDatabase.getDatabase(this)
    val categoryDao = db.categoryDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)


        lifecycleScope.launch {

            val existingCategories = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(this@CategoriesActivity).categoryDao().getAll()
            }

            if (existingCategories.isEmpty()) {
                val predefined = listOf(
                    CategoryEntity(name = "Food", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Transport", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Medicine", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Groceries", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Rent", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Gifts", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Savings", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Entertainment", iconResId = R.drawable.blue_circle),
                    CategoryEntity(name = "Add", iconResId = R.drawable.blue_circle)
                )


                predefined.forEach { categoryDao.insert(it) }

            }

            val savedCategories = categoryDao.getAll()

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
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Add New Category")

        val input = android.widget.EditText(this)
        input.hint = "Enter category name"
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val categoryName = input.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                val newCategory = CategoryEntity(name = categoryName, iconResId = R.drawable.blue_circle)

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
                categoryDao.getAll()
            }
            adapter.updateCategories(updatedCategories)
        }
    }
}
