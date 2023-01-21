package com.example.nmsadminapp.fragments

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
import com.example.nmsadminapp.repo.CategoryRepository
import com.example.nmsadminapp.utils.Helper
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
        view.findViewById<AppCompatButton>(R.id.btnAddNewCategory).setOnClickListener {
            // Navigate to the AddNewCategoryActivity
            val intent = Intent(activity, AddNewCategoryActivity::class.java)
            startActivity(intent)
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
        val intent = Intent(activity, AddNewCategoryActivity::class.java)

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
            "No", {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response =
                            CategoryRepository.delete(category.categoryId, requireContext())
                        // Log category id
                        Log.d("TabCategoryFragment", "Category id: ${category.categoryId}")
                        when (response.code) {
                            200 -> {
                                // Log Success Message
                                Log.d("TabCategoryFragment", "Category deleted successfully")
                                // Update the UI
                                fetchCategories(requireView())
                            }
                            401 -> {
                                // Log Error Code
                                Log.e(
                                    "TabCategoryFragment",
                                    "Error deleting category: ${response.data}"
                                )
                            }
                            404 -> {
                                // Log Error Code
                                Log.e(
                                    "TabCategoryFragment",
                                    "Error deleting category: ${response.data}"
                                )
                            }
                            500 -> {
                                // Log Error Code
                                Log.e(
                                    "TabCategoryFragment",
                                    "Error deleting category: ${response.data}"
                                )
                            }
                            else -> {
                                // Log Error Code
                                Log.e(
                                    "TabCategoryFragment",
                                    "Error deleting category: ${response.data}"
                                )
                            }
                        }
                    } catch (ex: Exception) {
                        Log.d("Error", ex.toString())
                    }
                }
            }, {})
    }
}