package com.example.nmsadminapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nmsadminapp.models.ProductModel
import com.example.nmsadminapp.repo.ProductRepository
import com.example.nmsadminapp.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewProductActivity : AppCompatActivity() {

    // Initialize Variables
    private lateinit var productName: EditText
    private lateinit var productDescription: EditText
    private lateinit var productPrice: EditText
    private lateinit var productMrp: EditText
    private lateinit var productDiscount: EditText
    private lateinit var productStock: EditText
    private lateinit var productBrandName: EditText
    private lateinit var productExpiryDate: EditText
    private lateinit var productIngredients: EditText
    private lateinit var productThumbnail: ImageView
    private lateinit var productImagesRecyclerView: RecyclerView
    private lateinit var btnAddProduct: Button
    private lateinit var btnSelectMultipleImages: Button
    private lateinit var btnSelectThumbnail: Button
    private val arr = ArrayList<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_product)

        // Init views
        initializeViews()

        // Set on click listener on thumbnail
        btnSelectThumbnail.setOnClickListener {
            // Open the gallery
            selectThumbnailImage()
        }
        // Set on click listener on Images
        btnSelectMultipleImages.setOnClickListener {
            // Open the gallery
            // selectMultipleImages()
            // TODO:: Add the images to the recycler view
        }

        // Set on click listener on Add Product
        btnAddProduct.setOnClickListener {
            // Add the product to the database
            addNewProduct()
        }
    }

    // Function to Initialize Views
    private fun initializeViews() {
        productName = findViewById(R.id.product_name)
        productDescription = findViewById(R.id.product_description)
        productPrice = findViewById(R.id.product_price)
        productMrp = findViewById(R.id.product_mrp)
        productDiscount = findViewById(R.id.product_discount)
        productStock = findViewById(R.id.product_quantity)
        productBrandName = findViewById(R.id.product_brand_name)
        productExpiryDate = findViewById(R.id.product_expiry_date)
        productIngredients = findViewById(R.id.product_ingredients)
        productThumbnail = findViewById(R.id.product_thumbnail)
        productImagesRecyclerView = findViewById(R.id.multi_product_images_recycler_view)
        btnAddProduct = findViewById(R.id.btn_add_product)
        btnSelectMultipleImages = findViewById(R.id.btn_select_multiple_images)
        btnSelectThumbnail = findViewById(R.id.btn_select_thumbnail)

    }

    //  Function to validate the data
    private fun validateData(): Boolean {
        return when {
            productName.text.toString().isEmpty() -> {
                productName.error = "Please Enter Product Name"
                false
            }
            productDescription.text.toString().isEmpty() -> {
                productDescription.error = "Please Enter Product Description"
                false
            }
            productPrice.text.toString().isEmpty() -> {
                productPrice.error = "Please Enter Product Price"
                false
            }
            productMrp.text.toString().isEmpty() -> {
                productMrp.error = "Please Enter Product MRP"
                false
            }
            productDiscount.text.toString().isEmpty() -> {
                productDiscount.error = "Please Enter Product Discount"
                false
            }
            productStock.text.toString().isEmpty() -> {
                productStock.error = "Please Enter Product Quantity"
                false
            }
            productBrandName.text.toString().isEmpty() -> {
                productBrandName.error = "Please Enter Product Brand Name"
                false
            }
            productExpiryDate.text.toString().isEmpty() -> {
                productExpiryDate.error = "Please Enter Product Expiry Date"
                false
            }
            productIngredients.text.toString().isEmpty() -> {
                productIngredients.error = "Please Enter Product Ingredients"
                false
            }

            else -> true
        }
    }

    // Function to add new product
    private fun addNewProduct() {
        if (!validateData()) {
            return
        }

        // static image array
        val arr = arrayListOf(
            "image 1",
            "image 2",
            "image 3",
        )

        val productModel = ProductModel(
            productName = productName.text.toString(),
            productDescription = productDescription.text.toString(),
            productPrice = productPrice.text.toString(),
            productMrp = productMrp.text.toString(),
            productDiscount = productDiscount.text.toString(),
            productBrandName = productBrandName.text.toString(),
            productExpiryDate = productExpiryDate.text.toString(),
            productThumbnail = "image 1",
            productImages = arr,
            productIngredients = productIngredients.text.toString(),
            productStatus = "active",
            productUnit = "kg",
            productStock = productStock.text.toString(),
            productCategoryId = "1",
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ProductRepository.add(productModel, this@AddNewProductActivity)
                with(Dispatchers.Main) {
                    if (response.code == 201) {
                        Helper.showToast(this@AddNewProductActivity, "Product Added Successfully")
                        finish()
                    } else {
                        Log.d("Response", response.message.toString())
                        Helper.showToast(
                            this@AddNewProductActivity,
                            "Error Occurred : ${response.message}"
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }

    // Function to select multiple images
    private fun selectMultipleImages() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Multiple Pictures"), 1)
        Log.i("Multiple Images", "Images clicked")
    }

    // Function to select thumbnail image
    private fun selectThumbnailImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
    }

    // Function to handle the result of the activity
    // TODO: Handle multiple upload images and send to API
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("Result", "Result received")
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    arr.add(imageUri)
                    Log.d("Image List", "onActivityResult: {$imageUri.toString()}")
                }
            } else {
                val imageUri = data?.data
                arr.add(imageUri!!)
                Log.d("Image List else", "onActivityResult: {$imageUri.toString()}")
            }
        } else {
            Log.i("Result", "Result not received")
        }
    }
}
