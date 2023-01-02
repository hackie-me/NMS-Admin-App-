package com.example.nmsadminapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.nmsadminapp.adapters.CategoryAdapter
import com.example.nmsadminapp.models.CategoryModel
import com.example.nmsadminapp.service.CategoryService
import com.example.nmsadminapp.utils.Helper
import kotlinx.coroutines.*

class AddNewCategoryActivity : AppCompatActivity() {

    private lateinit var txtCategoryName: EditText
    private lateinit var txtCategoryDescription: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var btnCancel: Button
    private lateinit var ivCategoryImage: ImageView
    private lateinit var btnSelectImage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_category)

        // Initialize the views
        initViews()

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
            addCategory()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }
    }

    companion object {
        private const val pickImage = 100
    }

    // Function to get the image from the gallery
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            ivCategoryImage.setImageURI(data?.data)
        }
    }
    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Handle the result of the activity here
        uri?.let {
            Glide.with(this)
                .load(it)
                .into(ivCategoryImage)
        }
    }

    // Function to add the category to the database
    private fun addCategory() {
        val categoryName = txtCategoryName.text.toString()
        val categoryDescription = txtCategoryDescription.text.toString()

        // Convert the image to base64
        var categoryImage = Helper.convertImageToBase64(ivCategoryImage)
        categoryImage = "data:image/jpeg;base64,$categoryImage"

        if (categoryName.isNotEmpty() && categoryDescription.isNotEmpty() && categoryImage.isNotEmpty()) {
            val categoryModal = CategoryModel(categoryName = categoryName, categoryDescription = categoryDescription, categoryImage = categoryImage)
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
}