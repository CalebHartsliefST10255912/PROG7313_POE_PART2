package com.example.budgetbee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.R
import com.example.budgetbee.data.ExpenseEntryEntity

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses: List<ExpenseEntryEntity> = listOf()

    fun submitList(newList: List<ExpenseEntryEntity>) {
        expenses = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.bind(expense)
    }

    override fun getItemCount(): Int = expenses.size

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName = itemView.findViewById<TextView>(R.id.textExpenseName)
        private val textDateTime = itemView.findViewById<TextView>(R.id.textDateTime)
        private val textAmount = itemView.findViewById<TextView>(R.id.textAmount)

        fun bind(expense: ExpenseEntryEntity) {
            textName.text = expense.name
            textDateTime.text = "${expense.startTime} - ${expense.date}"
            textAmount.text = "-$%.2f".format(expense.amount)
        }
    }
}
