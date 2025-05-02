
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.R
import com.example.budgetbee.data.ExpenseEntryEntity

//This is the expense adapter, it will help with the recylerView and formating for expenses
class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses: List<ExpenseEntryEntity> = listOf()

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseName: TextView = itemView.findViewById(R.id.textExpenseName)
        val expenseAmount: TextView = itemView.findViewById(R.id.textExpenseAmount)
        val expenseDate: TextView = itemView.findViewById(R.id.textExpenseDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseName.text = expense.name
        holder.expenseAmount.text = "R %.2f".format(expense.amount)
        holder.expenseDate.text = expense.date
    }

    override fun getItemCount(): Int = expenses.size

    fun submitList(list: List<ExpenseEntryEntity>) {
        expenses = list
        notifyDataSetChanged()
    }
}


