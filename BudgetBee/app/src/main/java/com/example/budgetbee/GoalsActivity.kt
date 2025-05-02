package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
//This is the goals activity, will manage what display for the goals will look like
class GoalsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var textMin: TextView
    private lateinit var textMax: TextView
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_goals)


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

    //Will load the current users goals information
    private fun loadGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            val goal = db.goalsDao().getGoal(userId)

            if (goal == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GoalsActivity, "No goals set yet.", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val expenses = db.expenseDao().getAllExpensesForUser(userId)
            val totalSpent = expenses.sumOf { it.amount }

            val percentOfMax = ((totalSpent / goal.maxMonthlyGoal) * 100).toInt().coerceAtMost(100)

            withContext(Dispatchers.Main) {
                textMin.text = "Min Goal: R ${goal.minMonthlyGoal}"
                textMax.text = "Max Goal: R ${goal.maxMonthlyGoal}"

                findViewById<TextView>(R.id.textSpent).text =
                    "Total Spent: R %.2f".format(totalSpent)

                findViewById<TextView>(R.id.textProgressPercent).text =
                    "$percentOfMax% of max goal used"

                findViewById<ProgressBar>(R.id.progressBar).progress = percentOfMax
            }
        }
    }


}
