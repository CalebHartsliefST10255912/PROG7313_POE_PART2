package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.budgetbee.R.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

//This is the reset password class and will manage how the user will reset their passwords
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_reset_password)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "budgetbee_db").build()
        userDao = db.userDao()

        val userEmail = findViewById<EditText>(id.editTextUserEmail)
        val userPhone = findViewById<EditText>(id.editTextUserPhone)
        val newPassword = findViewById<EditText>(id.editTextNewPassword)
        val resetButton = findViewById<Button>(id.ResetPasswordButton)

        resetButton.setOnClickListener(){
            val email = userEmail.text.toString()
            val phone = userPhone.text.toString()
            val password = newPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUserEmail(email)
                }

                val allUsers = userDao.getAllUsers()
                Log.d("ResetPassword", "All users in DB: ${allUsers.size}")
                for (u in allUsers) {
                    Log.d("ResetPassword", "User: ${u.userEmail}, Phone: ${u.userPhone}, Password: ${u.password}")
                }


                if (user != null) {
                    if (user.userPhone == phone) {
                        Log.d("ResetPassword", "Phone matches. Email: ${user.userEmail}, Phone: ${user.userPhone}")

                        val updatedUser = user.copy(password = password)
                        userDao.updateUser(updatedUser)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "Password reset successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Log.d("ResetPassword", "Phone mismatch for email: $email. Entered: $phone, Expected: ${user.userPhone}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "Phone number doesn't match this email", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.d("ResetPassword", "No user found for email: $email phone: $phone")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Email not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

}