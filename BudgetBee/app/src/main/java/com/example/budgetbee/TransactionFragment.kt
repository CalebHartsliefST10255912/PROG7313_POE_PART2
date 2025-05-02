package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class TransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        val buttonAddExpense = view.findViewById<Button>(R.id.buttonAddExpense)
        val buttonViewTransactions = view.findViewById<Button>(R.id.buttonViewTransactions)

        buttonAddExpense.setOnClickListener {
            startActivity(Intent(requireContext(), AddExpenseActivity::class.java))
        }

        buttonViewTransactions.setOnClickListener {
            startActivity(Intent(requireContext(), TransactionActivity::class.java))
        }

        return view
    }
}
