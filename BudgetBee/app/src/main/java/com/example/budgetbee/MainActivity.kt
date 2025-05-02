package com.example.budgetbee

import CategoryFragment
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.budgetbee.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            replaceFragment(HomeFragment())

            binding.bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        replaceFragment(HomeFragment())
                        true
                    }
                    R.id.profile -> {
                        replaceFragment(ProfileFragment())
                        true
                    }
                    R.id.transaction -> {
                        replaceFragment(TransactionFragment())
                        true
                    }
                    R.id.Categories -> {
                        replaceFragment(CategoryFragment())
                        true
                    }
                    R.id.search -> {
                        replaceFragment(SearchFragment())
                        true
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error inflating layout or setting up navigation", e)
            Toast.makeText(this, "Error loading app UI: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            finish() // optionally close the activity to prevent a blank screen
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }
}
