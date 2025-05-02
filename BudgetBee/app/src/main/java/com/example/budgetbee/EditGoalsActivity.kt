package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
//This page will alow the user to edit their goals
class EditGoalsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var editMin: EditText
    private lateinit var editMax: EditText
    private var userId: Int = -1

    //Show the edit goals activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goals)

        db = AppDatabase.getDatabase(this)

        //Shared prefrences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        //Will make sure that the user will be able to see their own data other
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        //Will retrieve what the user input
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
                db.goalsDao().clearGoals(userId)
                db.goalsDao().insertGoal(
                    GoalsEntity(
                        userId = userId,
                        minMonthlyGoal = min,
                        maxMonthlyGoal = max
                    )
                )
                withContext(Dispatchers.Main) {
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

    //Will load the goals
    private fun loadGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            val goal = db.goalsDao().getGoal(userId)
            withContext(Dispatchers.Main) {
                goal?.let {
                    editMin.setText(it.minMonthlyGoal.toString())
                    editMax.setText(it.maxMonthlyGoal.toString())
                }
            }
        }
    }
}
