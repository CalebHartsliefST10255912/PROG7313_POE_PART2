package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        //Values for the UI components in activity_login
        val editUserEmail = findViewById<EditText>(R.id.editUsername)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val textForgotPassword = findViewById<TextView>(R.id.textForgotPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        //SetClickListner that will register when the loginbutton is pressed
        buttonLogin.setOnClickListener(){
            val userEmail = editUserEmail.text.toString()
            val password = editPassword.text.toString()


            //email = admin ; password = admin
            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUserEmail(userEmail)
                }

                if (user == null) {
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                } else if (user.password == password) {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT)
                        .show()

                    Log.d("LoginPassword", "Email: ${user.userEmail}, Phone: ${user.userPhone}, Password: ${user.password}")


                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()


                } else {
                    Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }

        textForgotPassword.setOnClickListener(){
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }


    }
}
