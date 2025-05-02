package com.example.budgetbee

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button

class TransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        // Set up button click listener
        val buttonViewTransactions: Button = view.findViewById(R.id.buttonViewTransactions)
        buttonViewTransactions.setOnClickListener {
            // Start TransactionActivity when the button is clicked
            val intent = Intent(activity, TransactionActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
