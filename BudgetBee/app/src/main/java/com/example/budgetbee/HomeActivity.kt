package com.example.budgetbee


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val categories = findViewById<Button>(R.id.buttonGoToCategories)

        categories.setOnClickListener(){
            val intent =  Intent(this@HomeActivity, CategoriesActivity::class.java)
            startActivity(intent)
        }

    }
}
