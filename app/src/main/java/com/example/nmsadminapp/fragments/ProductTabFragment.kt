package com.example.nmsadminapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.nmsadminapp.AddNewProductActivity
import com.example.nmsadminapp.R
import com.example.nmsadminapp.adapters.ProductAdapter
import com.example.nmsadminapp.models.ProductModel

class ProductTabFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // test code
        val something = view.findViewById<RecyclerView>(R.id.productTabRecyclerView)
        view.findViewById<LinearLayout>(R.id.empty_product_view).visibility = View.GONE
        something.visibility = View.VISIBLE

        val productArray: Array<ProductModel> = arrayOf(
            ProductModel(
                productName = "Product 1",
                productPrice = "145",
                productThumbnail = "https://picsum.photos/200"
            ),
            ProductModel(
                productName = "Product 2",
                productPrice = "145",
                productThumbnail = "https://picsum.photos/200"
            ),
        )

        val productAdapter = ProductAdapter(productArray)
        something.adapter = productAdapter

        // open add new product activity
        view.findViewById<Button>(R.id.btnAddNewProduct).setOnClickListener {
            // Navigate to the AddNewProductActivity
            val intent = Intent(activity, AddNewProductActivity::class.java)
            startActivity(intent)
        }
    }
}