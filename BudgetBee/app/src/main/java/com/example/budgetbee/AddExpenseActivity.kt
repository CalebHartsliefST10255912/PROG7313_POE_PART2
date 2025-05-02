package com.example.budgetbee

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbee.data.ExpenseEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private var categoryId = -1
    private lateinit var categoryName: String
    private lateinit var categoriesList: List<CategoryEntity> // List to hold categories

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        db = AppDatabase.getDatabase(this)

        // Get userId from shared preferences
        val userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("userId", -1)
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch categories from the database
        lifecycleScope.launch {
            categoriesList = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUser(userId)
            }

            // Populate the Spinner
            val categoryNames = categoriesList.map { it.name } // Extract category names
            val adapter = ArrayAdapter(this@AddExpenseActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            findViewById<Spinner>(R.id.categorySpinner).adapter = adapter

            // Set the spinner item selected listener
            findViewById<Spinner>(R.id.categorySpinner).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    categoryId = categoriesList[position].categoryId
                    categoryName = categoriesList[position].name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle case where nothing is selected, if needed
                }
            }
        }

        // Image picker button
        findViewById<Button>(R.id.buttonPickImage).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Add expense button
        findViewById<Button>(R.id.buttonAddExpense).setOnClickListener {
            saveExpense()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            findViewById<ImageView>(R.id.imagePreview).setImageURI(imageUri)
        }
    }

    private fun saveExpense() {
        val name = findViewById<EditText>(R.id.inputExpenseName).text.toString()
        val amount = findViewById<EditText>(R.id.inputExpenseAmount).text.toString().toDoubleOrNull() ?: 0.0
        val description = findViewById<EditText>(R.id.inputExpenseDescription).text.toString()
        val location = findViewById<EditText>(R.id.inputExpenseLocation).text.toString()
        val startTime = findViewById<EditText>(R.id.inputStartTime).text.toString()
        val endTime = findViewById<EditText>(R.id.inputEndTime).text.toString()

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("userId", -1)
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = ExpenseEntryEntity(
            userId = userId,
            categoryId = categoryId,
            name = name,
            amount = amount,
            date = date,
            startTime = startTime,
            endTime = endTime,
            category = categoryName,
            description = description,
            location = location,
            photoPath = imageUri?.toString()
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.expenseDao().insertExpense(expense)
            }
            Toast.makeText(this@AddExpenseActivity, "Expense Added", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}


