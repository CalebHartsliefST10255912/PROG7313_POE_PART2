package com.example.budgetbee

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbee.AppDatabase


import com.example.budgetbee.data.ExpenseEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var categorySpinner: Spinner
    private lateinit var imageView: ImageView

    private var photoUri: String? = null
    private var userId: Int = -1
    private lateinit var categories: List<CategoryEntity>

    companion object {
        private const val REQUEST_IMAGE_PICK = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        db = AppDatabase.getDatabase(this)
        userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("userId", -1)

        val nameField = findViewById<EditText>(R.id.editName)
        val amountField = findViewById<EditText>(R.id.editAmount)
        val dateField = findViewById<EditText>(R.id.editDate)
        val startTimeField = findViewById<EditText>(R.id.editStartTime)
        val endTimeField = findViewById<EditText>(R.id.editEndTime)
        val descField = findViewById<EditText>(R.id.editDescription)
        val locationField = findViewById<EditText>(R.id.editLocation)
        val buttonSave = findViewById<Button>(R.id.buttonSaveExpense)
        val buttonSelectImage = findViewById<Button>(R.id.buttonSelectImage)
        imageView = findViewById(R.id.imageViewPreview)
        categorySpinner = findViewById(R.id.spinnerCategory)

        loadCategoriesIntoSpinner()

        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        buttonSave.setOnClickListener {
            val name = nameField.text.toString()
            val amount = amountField.text.toString().toDoubleOrNull()
            val date = dateField.text.toString()
            val start = startTimeField.text.toString()
            val end = endTimeField.text.toString()
            val desc = descField.text.toString()
            val location = locationField.text.toString()
            val categoryName = categorySpinner.selectedItem.toString()

            if (name.isEmpty() || amount == null || categoryName.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val actualAmount = if (categoryName == "Income") amount else -amount
            val categoryId = categories.firstOrNull { it.name == categoryName }?.categoryId ?: -1

            if (categoryId == -1) {
                Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = ExpenseEntryEntity(
                userId = userId,
                categoryId = categoryId,
                name = name,
                amount = actualAmount,
                date = date,
                startTime = start,
                endTime = end,
                category = categoryName,
                description = desc,
                location = location,
                photoPath = photoUri
            )

            lifecycleScope.launch(Dispatchers.IO) {
                db.expenseDao().insertExpense(expense)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddExpenseActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun loadCategoriesIntoSpinner() {
        lifecycleScope.launch(Dispatchers.IO) {
            categories = db.categoryDao().getAll(userId)
            val names = categories.map { it.name }
            withContext(Dispatchers.Main) {
                categorySpinner.adapter = ArrayAdapter(this@AddExpenseActivity, android.R.layout.simple_spinner_dropdown_item, names)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                imageView.setImageURI(it)
                photoUri = it.toString()
            }
        }
    }
}
