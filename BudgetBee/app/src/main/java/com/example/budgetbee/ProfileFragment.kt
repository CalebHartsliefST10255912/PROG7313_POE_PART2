package com.example.budgetbee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val textFullName = view.findViewById<TextView>(R.id.textFullName)
        val textEmail = view.findViewById<TextView>(R.id.textEmail)
        val textPhone = view.findViewById<TextView>(R.id.textPhone)

        // Fetch current user's email from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPref.getString("user_email", null)

        if (currentUserEmail != null) {
            userDao = AppDatabase.getDatabase(requireContext()).userDao()

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUserEmail(currentUserEmail)
                }

                user?.let {
                    textFullName.text = it.userName
                    textEmail.text = it.userEmail
                    textPhone.text = it.userPhone
                }
            }

            val buttonLogout = view.findViewById<Button>(R.id.buttonLogout)

            buttonLogout.setOnClickListener {
                // Clear SharedPreferences
                val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    clear()
                    apply()
                }

                // Navigate back to login screen
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        return view
    }
}
