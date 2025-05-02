import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbee.AppDatabase
import com.example.budgetbee.CategoryAdapter
import com.example.budgetbee.CategoryDao
import com.example.budgetbee.CategoryDetailsActivity
import com.example.budgetbee.CategoryEntity
import com.example.budgetbee.LoginActivity
import com.example.budgetbee.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var adapter: CategoryAdapter
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
            .getInt("userId", -1)

        if (userId == -1) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        db = AppDatabase.getDatabase(requireContext())
        categoryDao = db.categoryDao()

        val recyclerView = view.findViewById<RecyclerView>(R.id.categoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        viewLifecycleOwner.lifecycleScope.launch {
            val existingCategories = withContext(Dispatchers.IO) {
                categoryDao.getAll(userId)
            }

            if (existingCategories.isEmpty()) {
                val predefined = listOf(
                    CategoryEntity(userId = userId, name = "Food", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Transport", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Medicine", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Groceries", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Rent", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Gifts", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Savings", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Entertainment", iconResId = R.drawable.blue_circle),
                    CategoryEntity(userId = userId, name = "Add", iconResId = R.drawable.blue_circle)
                )
                withContext(Dispatchers.IO) {
                    predefined.forEach { categoryDao.insert(it) }
                }
            }

            val updatedCategories = withContext(Dispatchers.IO) {
                categoryDao.getAll(userId)
            }

            val (moreCategory, otherCategories) = updatedCategories.partition { it.name == "Add" }
            val orderedCategories = otherCategories + moreCategory

            adapter = CategoryAdapter(orderedCategories) { selectedCategory ->
                if (selectedCategory.name == "Add") {
                    showAddCategoryDialog()
                } else {
                    val intent = Intent(requireContext(), CategoryDetailsActivity::class.java)
                    intent.putExtra("CATEGORY_NAME", selectedCategory.name)
                    startActivity(intent)
                }
            }

            recyclerView.adapter = adapter
        }
    }

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Category")

        val input = EditText(requireContext())
        input.hint = "Enter category name"
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val categoryName = input.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                val newCategory = CategoryEntity(
                    userId = userId,
                    name = categoryName,
                    iconResId = R.drawable.blue_circle
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        categoryDao.insert(newCategory)
                    }
                    refreshCategoryList()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun refreshCategoryList() {
        viewLifecycleOwner.lifecycleScope.launch {
            val updatedCategories = withContext(Dispatchers.IO) {
                categoryDao.getAll(userId)
            }
            adapter.updateCategories(updatedCategories)
        }
    }
}
