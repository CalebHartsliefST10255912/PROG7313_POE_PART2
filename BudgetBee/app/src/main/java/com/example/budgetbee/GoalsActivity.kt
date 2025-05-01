package com.example.budgetbee

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        db = AppDatabase.getDatabase(this)

        val minGoal = findViewById<EditText>(R.id.editMinGoal)
        val maxGoal = findViewById<EditText>(R.id.editMaxGoal)
        val otherGoal = findViewById<EditText>(R.id.editOtherGoal)
        val buttonSave = findViewById<Button>(R.id.buttonSaveGoal)

        buttonSave.setOnClickListener {
            val min = minGoal.text.toString().toDoubleOrNull()
            val max = maxGoal.text.toString().toDoubleOrNull()
            val other = otherGoal.text.toString()

            if (min == null || max == null) {
                Toast.makeText(this, "Enter valid min/max goals", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                db.goalsDao().insertGoal(GoalsEntity(minMonthlyGoal = min, maxMonthlyGoal = max, otherGoal = other))
                runOnUiThread {
                    Toast.makeText(this@GoalsActivity, "Goals saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
