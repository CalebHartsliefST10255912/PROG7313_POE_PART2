package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbee.R.*


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_welcome)

        //Values for the Welcome page buttons
        val loginButton = findViewById<Button>(id.buttonLogin)
        val registerButton = findViewById<Button>(id.buttonSignUp)

        //ClickListener to take user to Login Page
        loginButton.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //ClickListener to take user to Register Page
        registerButton.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
