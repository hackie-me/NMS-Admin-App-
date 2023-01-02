package com.example.nmsadminapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.nmsadminapp.AddNewCategoryActivity
import com.example.nmsadminapp.R
import com.example.nmsadminapp.adapters.CategoryAdapter
import com.example.nmsadminapp.models.CategoryModel
import com.example.nmsadminapp.service.CategoryService
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryTabFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_tab, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the click listeners to add new category button
        view.findViewById<AppCompatButton>(R.id.btnAddNewCategory).setOnClickListener {
            // Navigate to the AddNewCategoryActivity
            val intent = Intent(activity, AddNewCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { Helper.showToast(it, "onResume") }
        // get the view
        val view = view
        if (view != null) {
            // Fetch all categories from Api
            fetchCategories(view)
        }else{
            Log.d("CategoryTabFragment", "View is null")
        }
    }

    // Fetch all categories from Api
    @SuppressLint("CutPasteId")
    private fun fetchCategories(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = CategoryService.fetchAll(requireContext())
                if (response.code == 200) {
                    val categories = Gson().fromJson(response.data, Array<CategoryModel>::class.java)
                    activity?.runOnUiThread {

                        // if category list is empty, show the empty view
                        if (categories.isNotEmpty()) {
                            view.findViewById<LinearLayout>(R.id.empty_category_view).visibility = View.GONE
                            view.findViewById<RecyclerView>(R.id.categoryTabRecyclerView).visibility = View.VISIBLE
                        }
                        // Update the UI
                        view.findViewById<RecyclerView>(R.id.categoryTabRecyclerView).adapter = CategoryAdapter(categories)
                    }
                } else {
                    Log.e("CategoryTabFragment", "Error fetching categories")
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }
}