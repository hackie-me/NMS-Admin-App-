package com.example.nmsadminapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nmsadminapp.models.CategoryModel
import com.example.nmsadminapp.repo.CategoryRepository
import com.example.nmsadminapp.repo.UploadImageRepository
import com.example.nmsadminapp.utils.HandlePermissions
import com.example.nmsadminapp.utils.Helper
import com.example.nmsadminapp.utils.api.ApiRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class AddNewCategoryActivity : AppCompatActivity() {

    private lateinit var txtCategoryName: EditText
    private lateinit var txtCategoryDescription: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var btnCancel: Button
    private lateinit var ivCategoryImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var catId: String
    private var imageData: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_category)

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
        txtCategoryName = findViewById(R.id.etCategoryName)
        txtCategoryDescription = findViewById(R.id.etCategoryDescription)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnCancel = findViewById(R.id.btnCancel)
        ivCategoryImage = findViewById(R.id.ivCategoryImage)
        btnSelectImage = findViewById(R.id.btnSelectImage)

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
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ivCategoryImage.setImageURI(data.data)
            imageData = data
        }
    }

    // Function to Upload image
    private fun uploadImage(data: Intent?, lastRecordId: String) {
        val selectedImage = data?.extras?.get("data") as Bitmap?
        // convert selected image to ByteArray
        val stream = ByteArrayOutputStream()
        selectedImage?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageBytes = stream.toByteArray()
        val filePart = MultipartBody.Part.createFormData(
            "image", "image.jpg",
            imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, imageBytes.size)
        )
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                UploadImageRepository.uploadImage(
                    this@AddNewCategoryActivity,
                    filePart,
                    lastRecordId,
                    ApiRequest.URL_UPLOAD_CATEGORY_IMAGE
                )
            withContext(Dispatchers.Main) {
                when (response.code) {
                    201 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Image uploaded successfully")
                    }
                    406 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Image not uploaded")
                    }
                    409 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Image not uploaded")
                    }
                    401 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Image not uploaded")
                    }
                    500 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Server error")
                    }
                    else -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Error")
                    }
                }
            }
        }
    }

    // Function to add the category to the database
    private fun addNewCategory() {
        // check validation
        if (validateCategory()) {
            val categoryName = txtCategoryName.text.toString()
            val categoryDescription = txtCategoryDescription.text.toString()

            // check if the image is selected or not
            // if (ivCategoryImage.drawable == null) {
            //      Helper.showToast(this, "Please select an image")
            //      return
            // }

            val categoryModal = CategoryModel(
                categoryName = categoryName,
                categoryDescription = categoryDescription,
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response = CategoryRepository.add(categoryModal, this@AddNewCategoryActivity)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        201 -> {
                            Helper.showToast(
                                this@AddNewCategoryActivity,
                                "Category added successfully"
                            )
                        }
                        406 -> {
                            Helper.showToast(this@AddNewCategoryActivity, "Category not added")
                        }
                        409 -> {
                            Helper.showToast(this@AddNewCategoryActivity, "Category not added")
                        }
                        401 -> {
                            Helper.showToast(this@AddNewCategoryActivity, "Category not added")
                        }
                        500 -> {
                            Helper.showToast(this@AddNewCategoryActivity, "Server error")
                        }
                        else -> {
                            Helper.showToast(
                                this@AddNewCategoryActivity,
                                "Something went wrong ${response.data}}"
                            )
                        }
                    }
                }
            }
        }
    }

    // Function to validate the category
    private fun validateCategory(): Boolean {
        val categoryName = txtCategoryName.text.toString()
        val categoryDescription = txtCategoryDescription.text.toString()
        return when {
            categoryName.isEmpty() -> {
                txtCategoryName.error = "Please enter the category name"
                txtCategoryName.requestFocus()
                false
            }
            categoryDescription.isEmpty() -> {
                txtCategoryDescription.error = "Please enter the category description"
                txtCategoryDescription.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    // Function to update the category
    private fun updateCategory() {
        val categoryName = txtCategoryName.text.toString()
        val categoryDescription = txtCategoryDescription.text.toString()
        val categoryModal = CategoryModel(
            categoryId = catId,
            categoryName = categoryName,
            categoryDescription = categoryDescription,
        )
        CoroutineScope(Dispatchers.IO).launch {
            val response = CategoryRepository.update(categoryModal, this@AddNewCategoryActivity)
            withContext(Dispatchers.Main) {
                when (response.code) {
                    204 -> {
                        Helper.showToast(
                            this@AddNewCategoryActivity,
                            "Category updated successfully"
                        )
                    }
                    406 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Category not updated")
                    }
                    409 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Category not updated")
                    }
                    401 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Category not updated")
                    }
                    500 -> {
                        Helper.showToast(this@AddNewCategoryActivity, "Server error")
                    }
                    else -> {
                        Helper.showToast(
                            this@AddNewCategoryActivity,
                            "Something went wrong ${response.data}}"
                        )
                    }
                }
            }
        }
    }

    /* Function Remove Category Shared Preference on Activity Destroy to
        avoid memory leak and data inconsistency issues in
        the app when the user navigates back to the previous activity and
        the shared preference is still present in the memory. */
    override fun onDestroy() {
        super.onDestroy()
        // Remove the shared preference
        Helper.removeSharedPreference(this, "category")
    }
}