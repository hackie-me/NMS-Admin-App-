package com.nms.admin

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nms.admin.models.CategoryModel
import com.nms.admin.repo.CategoryRepository
import com.nms.admin.utils.HandlePermissions
import com.nms.admin.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        setContentView(com.nms.admin.R.layout.activity_add_new_category)

        // Initialize the views
        initViews()

        // Check if this is an update request
        if (Helper.hasSharedPreference(this, "category")) {
            val category = Gson().fromJson(
                Helper.fetchSharedPreference(this, "category"),
                CategoryModel::class.java
            )
            catId = category.categoryId
            txtCategoryName.setText(category.categoryName)
            txtCategoryDescription.setText(category.categoryDescription)
            Glide.with(this).load(category.categoryImage).into(ivCategoryImage)

            // Set the title
            title = "Update Category"

            // Set the button text
            btnAddCategory.text = "Update Category"
        }

        // Set the click listeners
        setClickListeners()
    }

    private fun initViews() {
        txtCategoryName = findViewById(com.nms.admin.R.id.etCategoryName)
        txtCategoryDescription = findViewById(com.nms.admin.R.id.etCategoryDescription)
        btnAddCategory = findViewById(com.nms.admin.R.id.btnAddCategory)
        btnCancel = findViewById(com.nms.admin.R.id.btnCancel)
        ivCategoryImage = findViewById(com.nms.admin.R.id.ivCategoryImage)
        btnSelectImage = findViewById(com.nms.admin.R.id.btnSelectImage)

        // get permission to access the gallery
        HandlePermissions.checkAndRequestPermissions(this)
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
            val pickImageIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val chooserIntent = Intent.createChooser(pickImageIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
            startActivityForResult(chooserIntent, 1)
        }
    }

    // get the image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ivCategoryImage.setImageURI(data?.data)
        }
    }

    // Function to add the category to the database
    private fun addNewCategory() {
        val categoryName = txtCategoryName.text.toString()
        val categoryDescription = txtCategoryDescription.text.toString()

        // check if the image is selected or not
//        if (ivCategoryImage.drawable == null) {
//            Helper.showToast(this, "Please select an image")
//            return
//        }

        if (categoryName.isNotEmpty() && categoryDescription.isNotEmpty()) {
            val categoryModal = CategoryModel(
                categoryName = categoryName,
                categoryDescription = categoryDescription,
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response = CategoryRepository.add(categoryModal, this@AddNewCategoryActivity)
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
                val response = CategoryRepository.update(categoryModal, this@AddNewCategoryActivity)
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

    // Function Remove Category Shared Preference on Activity Destroy to avoid memory leak and data inconsistency issues in the app when the user navigates back to the previous activity and the shared preference is still present in the memory.
    override fun onDestroy() {
        super.onDestroy()
        // Remove the shared preference
        Helper.removeSharedPreference(this, "category")
    }
}