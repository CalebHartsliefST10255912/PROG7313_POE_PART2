package com.example.budgetbee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var userDao: UserDao
    private var currentUserEmail: String? =
        null // ideally passed via arguments or shared preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val textFullName = view.findViewById<TextView>(R.id.textFullName)
        val textEmail = view.findViewById<TextView>(R.id.textEmail)
        val textPhone = view.findViewById<TextView>(R.id.textPhone)

        val sharedPref =
            requireActivity().getSharedPreferences("BudgetBeePrefs", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPref.getString("user_email", null)

        userDao = AppDatabase.getDatabase(requireContext()).userDao()

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDao.getUserByUserEmail(currentUserEmail ?: "")
            }
            user?.let {
                textFullName.text = it.userName
                textEmail.text = it.userEmail
                textPhone.text = it.userPhone
            }
        }

        val buttonLogout = view.findViewById<Button>(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("BudgetBeePrefs", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val buttonEditProfile = view.findViewById<Button>(R.id.buttonEditProfile)
        buttonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }



        return view
    }
}