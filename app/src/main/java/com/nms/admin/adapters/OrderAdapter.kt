package com.nms.admin.adapters

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nms.admin.R
import com.nms.admin.models.CategoryModel
import com.nms.admin.models.OrderModel
import com.nms.admin.models.ProductModel
import com.nms.admin.models.UsersModel
import com.nms.admin.repo.ProductRepository
import com.nms.admin.repo.UserRepository
import com.nms.admin.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private val order: Array<OrderModel>,
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rc_list_item_order_completed, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = order[position]
        holder.bind(itemsViewModel, clickListener)

        holder.tvOrderId.text = "# ORD-${itemsViewModel.id}"
        holder.tvOrderStatus.text = itemsViewModel.status
        holder.tvInvoiceNumber.text = "# INV-${itemsViewModel.pdf}"
        holder.tvOrderTotal.text = "Rs. ${itemsViewModel.total}"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val outputTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = inputFormat.parse(itemsViewModel.created_at)
        holder.tvOrderDate.text = date?.let { outputDateFormat.format(it) }
        holder.tvOrderTime.text = date?.let { outputTimeFormat.format(it) }

        // Get Random Image
        Glide.with(holder.itemView.context)
            .load("https://picsum.photos/200/300?random=${itemsViewModel.id}")
            .into(holder.ivProductImage)

        // Getting the user details
        CoroutineScope(Dispatchers.IO).launch {
            val response = UserRepository.fetchById(UsersModel(userid = itemsViewModel.uid))
            if(response.code == 200){
                val user = Gson().fromJson(response.data, UsersModel::class.java)
                withContext(Dispatchers.Main){
                    holder.tvUserFullName.text = user.full_name
                    holder.tvUserPhoneNumber.text = user.phone
                }
            }
        }

        // Getting the product details
        CoroutineScope(Dispatchers.IO).launch {
            val response = ProductRepository.fetchById(itemsViewModel.pid)
            if(response.code == 200){
                val product = Gson().fromJson(response.data, ProductModel::class.java)
                withContext(Dispatchers.Main){
                    holder.tvProductName.text = product.productName
                }
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int = order.size

    interface ClickListener {
        fun onInvoiceLinkClick(order: OrderModel)
        fun onOrderDetailClick(order: OrderModel)
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvOrderId: TextView = ItemView.findViewById(R.id.tvOrderId)
        var tvOrderDate: TextView = ItemView.findViewById(R.id.tvOrderDate)
        var tvOrderTime: TextView = ItemView.findViewById(R.id.tvOrderTime)
        var tvUserFullName: TextView = ItemView.findViewById(R.id.tvUserFullName)
        var tvUserPhoneNumber: TextView = ItemView.findViewById(R.id.tvUserPhoneNumber)
        var tvProductName: TextView = ItemView.findViewById(R.id.tvProductName)
        var ivProductImage: ImageView = ItemView.findViewById(R.id.ivProductImage)
        var tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        var tvOrderTotal: TextView = ItemView.findViewById(R.id.tvOrderTotal)
        var tvInvoiceNumber: TextView = ItemView.findViewById(R.id.tvInvoiceNumber)
        var llInvoiceLink: LinearLayout = ItemView.findViewById(R.id.llInvoiceNumberLink)
        var ivOrderDetailButton: ImageView = ItemView.findViewById(R.id.ivOrderDetailsButton)

        fun bind(order: OrderModel, clickListener: ClickListener) {
            llInvoiceLink.setOnClickListener {
                clickListener.onInvoiceLinkClick(order)
            }

            ivOrderDetailButton.setOnClickListener {
                clickListener.onOrderDetailClick(order)
            }
        }
    }
}
