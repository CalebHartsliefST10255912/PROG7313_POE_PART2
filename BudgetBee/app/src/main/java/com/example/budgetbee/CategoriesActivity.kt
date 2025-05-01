package com.example.budgetbee



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CategoriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val categories = listOf(
            Category("Food", R.drawable.blue_circle),
            Category("Transport", R.drawable.blue_circle),
            Category("Medicine", R.drawable.blue_circle),
            Category("Groceries", R.drawable.blue_circle),
            Category("Rent", R.drawable.blue_circle),
            Category("Gifts", R.drawable.blue_circle),
            Category("Savings", R.drawable.blue_circle),
            Category("Entertainment", R.drawable.blue_circle),
            Category("More", R.drawable.blue_circle)
        )

        adapter = CategoryAdapter(categories) { selected ->
            val intent = Intent(this@CategoriesActivity, CategoryDetailsActivity::class.java)
//            intent.putExtra("CATEGORY_NAME", selected.name)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}
