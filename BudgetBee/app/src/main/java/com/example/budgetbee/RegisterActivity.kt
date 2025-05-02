package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editDOB = findViewById<EditText>(R.id.editDOB)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)

        registerButton.setOnClickListener {
            val userEmail = editEmail.text.toString()
            val password = editPassword.text.toString()
            val phone = editPhone.text.toString()
            val name = editName.text.toString()
            val userDOB = editDOB.text.toString()

            lifecycleScope.launch {
                val existingUser = withContext(Dispatchers.IO) {
                    userDao.getUserByUserEmail(userEmail)
                }

                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "User Email already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = UserEntity(userEmail = userEmail, password = password, userDOB = userDOB, userPhone = phone, userName = name)
                    var insertedUserId = -1
                    withContext(Dispatchers.IO) {
                        insertedUserId = userDao.insertUser(newUser).toInt()
                    }

                    // ✅ Save userId to SharedPreferences
                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.edit().putInt("userId", insertedUserId).apply()

                    Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
