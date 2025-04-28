package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 3 seconds before transitioning to WelcomeActivity
        Handler().postDelayed({
            // Start WelcomeActivity after the delay
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()  // Close SplashActivity to prevent going back to it
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}
