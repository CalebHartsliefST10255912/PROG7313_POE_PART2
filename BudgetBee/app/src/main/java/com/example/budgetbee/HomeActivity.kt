//package com.example.budgetbee
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentTransaction
//import com.example.budgetbee.databinding.ActivityHomeBinding
//
//class HomeActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityHomeBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        replaceFragment(HomeFragment())
//
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.home -> {
//                    replaceFragment(HomeFragment())
//                    true
//                }
//                R.id.profile -> {
//                    replaceFragment(ProfileFragment())
//                    true
//                }
//                R.id.transaction -> {
//                    replaceFragment(TransactionFragment())
//                    true
//                }
//                R.id.Categories -> {
//                    replaceFragment(CategoryFragment())
//                    true
//                }
//                R.id.search -> {
//                    replaceFragment(SearchFragment())
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//
//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager: FragmentManager = supportFragmentManager
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
//    }
//}