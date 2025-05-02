package com.example.budgetbee

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var saveButton: Button
    private var currentUserEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        saveButton = findViewById(R.id.buttonSaveProfile)

        userDao = AppDatabase.getDatabase(this).userDao()
        currentUserEmail = getSharedPreferences("BudgetBeePrefs", Context.MODE_PRIVATE)
            .getString("email", null)

        if (currentUserEmail == null) {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDao.getUserByUserEmail(currentUserEmail!!)
            }

            user?.let {
                editName.setText(it.userName)
                editEmail.setText(it.userEmail)
                editPhone.setText(it.userPhone)
            }
        }

        saveButton.setOnClickListener {
            val newName = editName.text.toString()
            val newEmail = editEmail.text.toString()
            val newPhone = editPhone.text.toString()

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    userDao.updateUserInfo(currentUserEmail!!, newName, newEmail, newPhone)
                }
                Toast.makeText(this@EditProfileActivity, "Profile updated", Toast.LENGTH_SHORT).show()

                // Update SharedPreferences if email was changed
                getSharedPreferences("BudgetBeePrefs", Context.MODE_PRIVATE).edit()
                    .putString("email", newEmail).apply()

                finish() // Return to profile
            }
        }
    }
}
