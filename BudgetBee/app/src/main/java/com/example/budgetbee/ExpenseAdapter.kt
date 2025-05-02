package com.example.budgetbee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.data.ExpenseEntryEntity

class ExpenseAdapter(
    private var expenses: List<ExpenseEntryEntity>
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textExpenseTitle)
        val amount: TextView = itemView.findViewById(R.id.textExpenseAmount)
        val category: TextView = itemView.findViewById(R.id.textCategory)
        val dateTime: TextView = itemView.findViewById(R.id.textDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val context = holder.itemView.context

        holder.title.text = expense.name
        holder.amount.text = context.getString(R.string.expense_amount, expense.amount)
        holder.category.text = expense.category // Ensure this is a String
        holder.dateTime.text = context.getString(
            R.string.expense_datetime,
            expense.date,
            expense.startTime,
            expense.endTime
        )
    }

    override fun getItemCount(): Int = expenses.size

    fun updateExpenses(newList: List<ExpenseEntryEntity>) {
        expenses = newList
        notifyDataSetChanged()
    }
}
