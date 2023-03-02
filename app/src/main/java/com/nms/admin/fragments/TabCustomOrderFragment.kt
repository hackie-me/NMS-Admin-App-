package com.nms.admin.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.nms.admin.OrderDetailActivity
import com.nms.admin.PDFViewerActivity
import com.nms.admin.R
import com.nms.admin.adapters.CustomOrderAdapter
import com.nms.admin.adapters.OrderAdapter
import com.nms.admin.models.CustomOrderModel
import com.nms.admin.models.OrderModel
import com.nms.admin.repo.OrderRepository
import com.nms.admin.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TabCustomOrderFragment : Fragment(), CustomOrderAdapter.ClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_order_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_custom_order_tab)
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh code here
            swipeRefreshLayout.isRefreshing = true
            fetchOrders(requireView())
            swipeRefreshLayout.isRefreshing = false
        }
    }
    override fun onResume() {
        super.onResume()
        fetchOrders(requireView())
    }

    private fun fetchOrders(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = OrderRepository.fetchAllCustomOrders()
                val orders = Gson().fromJson(response.data, Array<CustomOrderModel>::class.java)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        200 -> {
                            val orderTabRecyclerView =
                                view.findViewById<RecyclerView>(R.id.customOrderTabRecyclerView)
                            val emptyView =
                                view.findViewById<LinearLayout>(R.id.empty_custom_order_view)

                            if (orders.isNotEmpty()) {
                                emptyView.visibility = View.GONE
                                orderTabRecyclerView.visibility = View.VISIBLE
                            } else {
                                emptyView.visibility = View.VISIBLE
                                orderTabRecyclerView.visibility = View.GONE
                            }
                            // Update the UI
                            val orderAdapter = CustomOrderAdapter(orders, this@TabCustomOrderFragment)
                            orderTabRecyclerView.adapter = orderAdapter
                        }
                        else -> {
                            // show error code
                            val emptyView = view.findViewById<LinearLayout>(R.id.empty_order_view)
                            emptyView.visibility = View.VISIBLE
                            Helper.showToast(requireContext(), response.code.toString())
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }

    override fun onInvoiceLinkClick(order: CustomOrderModel) {
        // Open the invoice link in PDF viewer
        val intent = Intent(requireContext(), PDFViewerActivity::class.java)
        startActivity(intent)
    }

    override fun onOrderDetailClick(order: CustomOrderModel) {
        // start the order detail activity with order id
        val intent = Intent(requireContext(), OrderDetailActivity::class.java)
        intent.putExtra("order_id", order.id)
        startActivity(intent)
    }
}