package com.example.budgetbee


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val intent =  Intent(this@HomeActivity, CategoriesActivity::class.java)
        startActivity(intent)
    }
}
