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
    private val products: Array<ProductModel>,
    private val clickListener: ClickListener,
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
        holder.bind(product, clickListener)
        holder.productName.text = product.productName
        holder.productPrice.text = "Rs. ${product.productPrice}"
        holder.productMrp.text = "Rs. ${product.productMrp}"
        Glide.with(holder.itemView.context).load(product.productThumbnail)
            .placeholder(R.drawable.circle_loading_lines)
            .error(R.drawable.soon)
            .into(holder.productThumbnail)
    }

    override fun getItemCount(): Int = products.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productMrp: TextView = itemView.findViewById(R.id.product_mrp)
        val productThumbnail: ImageView = itemView.findViewById(R.id.product_thumbnail)

        fun bind(productModel: ProductModel, clickListener: ClickListener) {
            itemView.findViewById<ImageView>(R.id.deleteProduct).setOnClickListener {
                clickListener.onDeleteClick(productModel)
            }
            itemView.setOnClickListener {
                clickListener.onEditClick(productModel)
            }
        }
    }

    interface ClickListener {
        fun onEditClick(productModel: ProductModel)
        fun onDeleteClick(productModel: ProductModel)
    }
}