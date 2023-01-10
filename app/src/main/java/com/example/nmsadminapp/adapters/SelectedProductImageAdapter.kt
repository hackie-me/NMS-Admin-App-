package com.example.nmsadminapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nmsadminapp.R

class SelectedProductImageAdapter(
    private val list: Array<String>
) : RecyclerView.Adapter<SelectedProductImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedProductImageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rc_list_item_selected_product_image, parent, false)
        return SelectedProductImageAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = list[position]
        Glide.with(holder.itemView.context).load(image).into(holder.ivSelectedProductImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSelectedProductImage: ImageView =
            itemView.findViewById(R.id.multi_select_product_image)
    }
}