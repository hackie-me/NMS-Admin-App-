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
import com.nms.admin.R
import com.nms.admin.adapters.CategoryAdapter
import com.nms.admin.models.CategoryModel
import com.nms.admin.repo.CategoryRepository
import com.nms.admin.utils.Helper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TabCategoryFragment : Fragment(), CategoryAdapter.ClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the click listeners to add new category button
        view.findViewById<FloatingActionButton>(R.id.btnAddNewCategory).setOnClickListener {
            // Navigate to the AddNewCategoryActivity
            val intent = Intent(activity, com.nms.admin.AddNewCategoryActivity::class.java)
            startActivity(intent)
        }

        // Set the click listeners to swipe refresh layout
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_category_tab)
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh code here
            swipeRefreshLayout.isRefreshing = true
            fetchCategories(requireView())
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        fetchCategories(requireView())
    }

    // Fetch all categories from Api
    private fun fetchCategories(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = CategoryRepository.fetchAll(requireContext())
                val categories = Gson().fromJson(response.data, Array<CategoryModel>::class.java)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        200 -> {
                            // Check if categories is empty
                            val categoryTabRecyclerView =
                                view.findViewById<RecyclerView>(R.id.categoryTabRecyclerView)
                            val emptyView =
                                view.findViewById<LinearLayout>(R.id.empty_category_view)

                            // if category list is empty, show the empty view
                            if (categories.isNotEmpty()) {
                                emptyView.visibility = View.GONE
                                categoryTabRecyclerView.visibility = View.VISIBLE
                            } // else show the recycler view
                            else {
                                emptyView.visibility = View.VISIBLE
                                categoryTabRecyclerView.visibility = View.GONE
                            }
                            // Update the UI
                            val categoryAdapter =
                                CategoryAdapter(categories, this@TabCategoryFragment)
                            categoryTabRecyclerView.adapter = categoryAdapter
                        }
                        else -> {
                            // Log Error Code
                            Log.e(
                                "TabCategoryFragment",
                                "Error fetching categories: ${response.data}"
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }

    // EditClickListener methods
    override fun onEditClick(category: CategoryModel) {
        // Navigate to the AddNewCategoryActivity
        val intent = Intent(activity, com.nms.admin.AddNewCategoryActivity::class.java)

        // Store Category data in shared preferences
        Helper.storeSharedPreference(requireContext(), "category", Gson().toJson(category))
        startActivity(intent)
    }

    // DeleteClickListener methods
    override fun onDeleteClick(category: CategoryModel) {
        // Ask for confirmation
        Helper.showConfirmationDialog(
            requireContext(),
            "Delete Category",
            "Are you sure you want to delete this category?",
            "Yes",
            "No",
            {deleteCategory(category)}
            , {}
        )
    }

    // Function to delete category
    private fun deleteCategory(category: CategoryModel){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = CategoryRepository.delete(category.categoryId, requireContext())
                // Log category id
                when (response.code) {
                    200 -> {
                        // Update the UI
                        fetchCategories(requireView())
                    }
                    401 -> {
                        Helper.showSnackBar(requireView(), "Error: ${response.data}")
                    }
                    404 -> {
                        Helper.showSnackBar(requireView(), "Error: ${response.data}")
                    }
                    500 -> {
                        Helper.showSnackBar(requireView(), "Error: ${response.data}")
                    }
                    else -> {
                        Helper.showSnackBar(requireView(), "Error: ${response.data}")
                    }
                }
            } catch (ex: Exception) {
                Helper.showSnackBar(requireView(), "Error: ${ex.toString()}")
            }
        }
    }
}