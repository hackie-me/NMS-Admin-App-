package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nmsadminapp.models.CategoryModel
import com.example.nmsadminapp.service.CategoryService
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.*

class AddNewCategoryActivity : AppCompatActivity() {

    private lateinit var txtCategoryName: EditText
    private lateinit var txtCategoryDescription: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var btnCancel: Button
    private lateinit var ivCategoryImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var catId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_category)

        // Initialize the views
        initViews()
        Helper.hasSharedPreference(this, "category").let {
            // if the shared preference has the category data
            // get the category data from shared preference
            val category = Gson().fromJson(
                Helper.fetchSharedPreference(this, "category"),
                CategoryModel::class.java
            )
            // set the category data to the views
            txtCategoryName.setText(category.categoryName)
            txtCategoryDescription.setText(category.categoryDescription)
            Glide.with(this).load(category.categoryImage).into(ivCategoryImage)
            catId = category.categoryId

            // set the text of the button to update
            btnAddCategory.text = "Update Category"
        }

        // Set the click listeners
        setClickListeners()
    }

    private fun initViews() {
        txtCategoryName = findViewById(R.id.etCategoryName)
        txtCategoryDescription = findViewById(R.id.etCategoryDescription)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnCancel = findViewById(R.id.btnCancel)
        ivCategoryImage = findViewById(R.id.ivCategoryImage)
        btnSelectImage = findViewById(R.id.btnSelectImage)
    }

    private fun setClickListeners() {
        btnAddCategory.setOnClickListener {
            // Check if this is an update or a new category
            if (Helper.hasSharedPreference(this, "category")) {
                // Update the category
                updateCategory()
                Helper.showToast(this, "Category updated successfully")
                finish()
            } else {
                // Add a new category
                addNewCategory()
                Helper.showToast(this, "Category added successfully")
                finish()
            }
        }
        btnCancel.setOnClickListener {
            finish()
        }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }
    }

    // Function to get the image from the gallery
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            Glide.with(this).load(data?.data).into(ivCategoryImage)
        }
    }

    // Function to add the category to the database
    private fun addNewCategory() {
        val categoryName = txtCategoryName.text.toString()
        val categoryDescription = txtCategoryDescription.text.toString()

        // Convert the image to base64
        var categoryImage = Helper.convertImageToBase64(ivCategoryImage)
        categoryImage = "data:image/jpeg;base64,$categoryImage"

        if (categoryName.isNotEmpty() && categoryDescription.isNotEmpty() && categoryImage.isNotEmpty()) {
            val categoryModal = CategoryModel(
                categoryName = categoryName,
                categoryDescription = categoryDescription,
                categoryImage = categoryImage
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response = CategoryService.add(categoryModal, this@AddNewCategoryActivity)
                withContext(Dispatchers.Main) {
                    if (response.code == 201) {
                        Helper.showToast(this@AddNewCategoryActivity, "Category added successfully")
                        finish()
                    } else {
                        Helper.showToast(this@AddNewCategoryActivity, "Error " + response.message)
                    }
                }
            }
        }
    }

    // Function to update the category
    private fun updateCategory() {
        val categoryName = txtCategoryName.text.toString()
        val categoryDescription = txtCategoryDescription.text.toString()

        // Convert the image to base64
        var categoryImage = Helper.convertImageToBase64(ivCategoryImage)
        categoryImage = "data:image/jpeg;base64,$categoryImage"

        if (categoryName.isNotEmpty() && categoryDescription.isNotEmpty() && categoryImage.isNotEmpty()) {
            val categoryModal = CategoryModel(
                categoryId = catId,
                categoryName = categoryName,
                categoryDescription = categoryDescription,
                categoryImage = categoryImage
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response = CategoryService.update(categoryModal, this@AddNewCategoryActivity)
                withContext(Dispatchers.Main) {
                    if (response.code == 204) {
                        Helper.showToast(
                            this@AddNewCategoryActivity,
                            "Category updated successfully"
                        )
                        // Remove the shared preference
                        Helper.removeSharedPreference(this@AddNewCategoryActivity, "category")
                        finish()
                    } else {
                        Helper.showToast(this@AddNewCategoryActivity, "Error " + response.message)
                    }
                }
            }
        }
    }
}