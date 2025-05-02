
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.R
import com.example.budgetbee.data.ExpenseEntryEntity

//class ExpenseAdapter :
//    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>
//        () {
//    private var expenses: List<ExpenseEntryEntity> =
//        listOf()
//    fun submitList(newList: List<ExpenseEntryEntity>)
//    {
//        expenses = newList
//        notifyDataSetChanged()
//    }
//    override fun onCreateViewHolder(parent: ViewGroup,
//                                    viewType: Int): ExpenseViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_expense, parent,
//                false)
//        return ExpenseViewHolder(view)
//    }
//    override fun onBindViewHolder(holder:
//                                  ExpenseViewHolder, position: Int) {
//        val expense = expenses[position]
//        holder.bind(expense)
//    }
//    override fun getItemCount(): Int = expenses.size
//    class ExpenseViewHolder(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        private val textName =
//            itemView.findViewById<TextView>(R.id.textExpenseName)
//        private val textDateTime =
//            itemView.findViewById<TextView>(R.id.textDateTime)
//        private val textAmount =
//            itemView.findViewById<TextView>(R.id.textAmount)
////        private val imagePhoto =
////            itemView.findViewById<ImageView>(R.id.imagePhoto)
//        fun bind(expense: ExpenseEntryEntity) {
//            textName.text = expense.name
//            textDateTime.text = "${expense.startTime} - "
//             "${expense.date}"
//            val isIncome = expense.amount >= 0
//            textAmount.text = if (isIncome)
//                "+$%.2f".format(expense.amount)
//            else "-$%.2f".format(expense.amount)
//            textAmount.setTextColor(if (isIncome)
//                0xFF2E7D32.toInt() else 0xFFD32F2F.toInt())
////            if (!expense.photoPath.isNullOrEmpty()) {
////                imagePhoto.visibility = View.VISIBLE
////
////                imagePhoto.setImageURI(Uri.parse(expense.photoPath))
////            } else {
////                imagePhoto.visibility = View.GONE
////            }
//        }
//    }
//}

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


