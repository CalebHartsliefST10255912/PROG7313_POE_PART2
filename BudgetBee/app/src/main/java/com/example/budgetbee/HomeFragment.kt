package com.example.budgetbee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.budgetbee.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        try {
            val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("userName", "User") ?: "User"
            binding.textWelcome.text = "Welcome, $userName"
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error loading user name", e)
            binding.textWelcome.text = "Welcome"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.buttonGoals.setOnClickListener {
                startActivity(Intent(requireContext(), GoalsActivity::class.java))
            }

            binding.buttonViewTransactions.setOnClickListener {
                startActivity(Intent(requireContext(), AddExpenseActivity::class.java))
            }
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error setting button listeners", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
