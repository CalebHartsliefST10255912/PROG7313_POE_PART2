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

/*
*
* GeeksforGeeks. (2023). Android - Image Picker From Gallery using ActivityResultContracts in Kotlin. [online] Available at: https://www.geeksforgeeks.org/android-image-picker-from-gallery-using-activityresultcontracts-in-kotlin/.

‌
*
* Used this for helping understand how to pick images
*
*
* Android Developers. (2019). SharedPreferences  |  Android Developers. [online] Available at: https://developer.android.com/reference/android/content/SharedPreferences.

‌
*
* Used this to help make sure the user data is linked to their data
*
* */


class AddExpenseActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1  //Requests code for image selection
    private var categoryId = -1
    private lateinit var categoryName: String
    private lateinit var categoriesList: List<CategoryEntity> // List to hold categories

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        //Initialize Room database instance
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

    //Save expense entry to Room DB
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

        //Create expense entity
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

        //Insert expense into DB
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.expenseDao().insertExpense(expense)
            }
            Toast.makeText(this@AddExpenseActivity, "Expense Added", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}


