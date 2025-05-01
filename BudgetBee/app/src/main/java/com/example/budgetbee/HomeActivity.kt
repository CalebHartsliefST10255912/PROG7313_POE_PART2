package com.example.budgetbee

import android.content.Intent                  //
import android.os.Bundle
import android.widget.Button                   //
import androidx.appcompat.app.AppCompatActivity //

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonGoals = findViewById<Button>(R.id.buttonGoals)
        buttonGoals.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }
    }
}
