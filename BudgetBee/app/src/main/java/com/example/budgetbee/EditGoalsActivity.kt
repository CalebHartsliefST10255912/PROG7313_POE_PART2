package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class EditGoalsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var editMin: EditText
    private lateinit var editMax: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goals)

        db = AppDatabase.getDatabase(this)

        editMin = findViewById(R.id.editMinGoal)
        editMax = findViewById(R.id.editMaxGoal)
        val saveBtn = findViewById<Button>(R.id.buttonSaveGoal)
        val backBtn = findViewById<Button>(R.id.buttonBack)

        loadGoals()

        saveBtn.setOnClickListener {
            val min = editMin.text.toString().toDoubleOrNull()
            val max = editMax.text.toString().toDoubleOrNull()

            if (min == null || max == null) {
                Toast.makeText(this, "Enter valid numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                db.goalsDao().clearGoals()
                db.goalsDao().insertGoal(GoalsEntity(minMonthlyGoal = min, maxMonthlyGoal = max))
                runOnUiThread {
                    Toast.makeText(this@EditGoalsActivity, "Goals saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EditGoalsActivity, GoalsActivity::class.java))
                    finish()
                }
            }
        }

        backBtn.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
            finish()
        }
    }

    private fun loadGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            val goal = db.goalsDao().getGoal()
            runOnUiThread {
                goal?.let {
                    editMin.setText(it.minMonthlyGoal.toString())
                    editMax.setText(it.maxMonthlyGoal.toString())
                }
            }
        }
    }
}
