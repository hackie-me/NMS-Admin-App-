package com.example.nmsadminapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nmsadminapp.AddNewProductActivity
import com.example.nmsadminapp.R
import com.example.nmsadminapp.adapters.ProductAdapter
import com.example.nmsadminapp.models.ProductModel
import com.example.nmsadminapp.repo.ProductRepository
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TabProductFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // open add new product activity
        view.findViewById<Button>(R.id.btnAddNewProduct).setOnClickListener {
            // Navigate to the AddNewProductActivity
            val intent = Intent(activity, AddNewProductActivity::class.java)
            startActivity(intent)
        }
        val swipeRefreshLayout =
            view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_product_tab)
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh code here
            swipeRefreshLayout.isRefreshing = true
            fetchProducts(requireView())
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        fetchProducts(requireView())
    }

    // Function to fetch all products from Api
    private fun fetchProducts(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ProductRepository.fetchAll(requireContext())
                val products = Gson().fromJson(response.data, Array<ProductModel>::class.java)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        200 -> {
                            // Check if products is empty
                            if (products.isEmpty()) {
                                view.findViewById<RecyclerView>(R.id.productTabRecyclerView).visibility =
                                    View.GONE
                                view.findViewById<LinearLayout>(R.id.empty_product_view).visibility =
                                    View.VISIBLE
                            } else {
                                view.findViewById<RecyclerView>(R.id.productTabRecyclerView).visibility =
                                    View.VISIBLE
                                view.findViewById<LinearLayout>(R.id.empty_product_view).visibility =
                                    View.GONE
                                val productAdapter = ProductAdapter(products)
                                view.findViewById<RecyclerView>(R.id.productTabRecyclerView).adapter =
                                    productAdapter
                            }
                        }
                        else -> {
                            Helper.showSnackBar(view, "Something went wrong")
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }


}