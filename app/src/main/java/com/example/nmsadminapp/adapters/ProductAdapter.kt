package com.example.nmsadminapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nmsadminapp.R
import com.example.nmsadminapp.models.ProductModel

class ProductAdapter(
    private val products: Array<ProductModel>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rc_list_item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.productName
        holder.productPrice.text = product.productPrice
        Glide.with(holder.itemView.context).load(product.productThumbnail)
            .placeholder(R.drawable.circle_loading_lines)
            .error(R.drawable.soon)
            .into(holder.productThumbnail)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productThumbnail: ImageView = itemView.findViewById(R.id.product_thumbnail)
    }
}