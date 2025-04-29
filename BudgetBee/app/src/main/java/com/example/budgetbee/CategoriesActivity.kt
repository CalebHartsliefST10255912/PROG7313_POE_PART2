package com.example.budgetbee



import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class CategoriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        // Bottom navigation buttons
        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navTransactions = findViewById<ImageButton>(R.id.navTransactions)
        val navCategories = findViewById<ImageButton>(R.id.navCategories)
        val navProfile = findViewById<ImageButton>(R.id.navProfile)

        navHome.setOnClickListener {
            // Optional: avoid reopening if already on home
            if (this !is CategoriesActivity) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        navTransactions.setOnClickListener {
            startActivity(Intent(this, TransactionsActivity::class.java))
        }

        navCategories.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
