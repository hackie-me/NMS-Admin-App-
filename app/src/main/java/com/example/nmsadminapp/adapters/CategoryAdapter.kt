package com.example.nmsadminapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nmsadminapp.R
import com.example.nmsadminapp.models.CategoryModel

class CategoryAdapter(
    private val category: Array<CategoryModel>,
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rc_list_item_category, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = category[position]
        holder.bind(itemsViewModel, clickListener)

        // sets the image to the imageview from our itemHolder class
        Glide.with(holder.itemView.context).load(itemsViewModel.categoryImage)
            .into(holder.categoryImage)

        // sets the text to the textview from our itemHolder class
        holder.categoryName.text = itemsViewModel.categoryName

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return category.size
    }


    interface ClickListener {
        fun onEditClick(category: CategoryModel)
        fun onDeleteClick(category: CategoryModel)
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        private val categoryEditButton: AppCompatButton = itemView.findViewById(R.id.editCategory)
        private val categoryDeleteButton: AppCompatButton =
            itemView.findViewById(R.id.deleteCategory)

        fun bind(category: CategoryModel, clickListener: ClickListener) {
            itemView.findViewById<Button>(R.id.editCategory).setOnClickListener {
                clickListener.onEditClick(category)
            }
            itemView.findViewById<Button>(R.id.deleteCategory).setOnClickListener {
                clickListener.onDeleteClick(category)
            }
        }
    }
}
