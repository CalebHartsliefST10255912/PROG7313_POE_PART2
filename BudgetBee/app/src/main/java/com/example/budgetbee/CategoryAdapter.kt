package com.example.budgetbee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//recycler view adapter for diplaying a list of categories
class CategoryAdapter(
    private var categories: List<CategoryEntity>,   //list of categories
    private val onClick: (CategoryEntity) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    //Holds the view for each item
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    //Binds each ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryText.text = category.name  // category.name should be accessible here
        holder.itemView.setOnClickListener {
            onClick(category)
        }

    }

    //returns the number of items
    override fun getItemCount(): Int = categories.size

    //updates the data
    fun updateCategories(newList: List<CategoryEntity>) {
        categories = newList
        notifyDataSetChanged()
    }

}
