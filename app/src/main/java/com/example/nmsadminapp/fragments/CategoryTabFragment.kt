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
import com.example.nmsadminapp.repo.CategoryRepository
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryTabFragment : Fragment(), CategoryAdapter.ClickListener {
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
        fetchCategories(requireView())
    }

    // Fetch all categories from Api
    @SuppressLint("CutPasteId")
    private fun fetchCategories(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = CategoryRepository.fetchAll(requireContext())
                if (response.code == 200) {
                    val categories =
                        Gson().fromJson(response.data, Array<CategoryModel>::class.java)
                    activity?.runOnUiThread {

                        // if category list is empty, show the empty view
                        if (categories.isNotEmpty()) {
                            view.findViewById<LinearLayout>(R.id.empty_category_view).visibility =
                                View.GONE
                            view.findViewById<RecyclerView>(R.id.categoryTabRecyclerView).visibility =
                                View.VISIBLE
                        }
                        // Update the UI
                        val categoryAdapter = CategoryAdapter(categories, this@CategoryTabFragment)
                        view.findViewById<RecyclerView>(R.id.categoryTabRecyclerView).adapter =
                            categoryAdapter
                    }
                } else {
                    Log.e("CategoryTabFragment", "Error fetching categories")
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
                        Log.d("CategoryTabFragment", "Category id: ${category.categoryId}")
                        if (response.code == 200) {
                            withContext(Dispatchers.Main) {
                                Helper.showToast(requireContext(), "Category deleted successfully")
                                // Fetch all categories from Api
                                fetchCategories(requireView())
                            }
                        } else {
                            // Log Error Code
                            Log.e(
                                "CategoryTabFragment",
                                "Error deleting category: ${response.code}"
                            )
                        }
                    } catch (ex: Exception) {
                        Log.d("Error", ex.toString())
                    }
                }
            }, {})
    }
}