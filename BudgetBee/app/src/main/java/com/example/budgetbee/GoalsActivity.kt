package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class GoalsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var textMin: TextView
    private lateinit var textMax: TextView
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        // ✅ Get userId from SharedPreferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        db = AppDatabase.getDatabase(this)

        textMin = findViewById(R.id.textMinGoal)
        textMax = findViewById(R.id.textMaxGoal)

        val buttonEdit = findViewById<Button>(R.id.buttonEditGoals)
        buttonEdit.setOnClickListener {
            startActivity(Intent(this, EditGoalsActivity::class.java))
        }

        loadGoals()
    }

    private fun loadGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            val goal = db.goalsDao().getGoal(userId)
            withContext(Dispatchers.Main) {
                if (goal != null) {
                    textMin.text = "Min Goal: R ${goal.minMonthlyGoal}"
                    textMax.text = "Max Goal: R ${goal.maxMonthlyGoal}"
                } else {
                    Toast.makeText(this@GoalsActivity, "No goals set yet.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
