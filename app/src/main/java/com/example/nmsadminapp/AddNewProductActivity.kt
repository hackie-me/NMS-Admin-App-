package com.example.nmsadminapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nmsadminapp.models.CategoryModel
import com.example.nmsadminapp.models.ProductModel
import com.example.nmsadminapp.repo.CategoryRepository
import com.example.nmsadminapp.repo.ProductRepository
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddNewProductActivity : AppCompatActivity() {

    // Initialize Variables
    private lateinit var productName: EditText
    private lateinit var productDescription: EditText
    private lateinit var productCategorySpinner: Spinner
    private lateinit var productCategoryId: String
    private lateinit var productStatusSpinner: Spinner
    private lateinit var productUnitSpinner: Spinner
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_product)

        // Init views
        initializeViews()
        // Initialize the spinner
        displayCategoryList()
        displayStatusList()
        displayUnitList()

        // If its an edit product
        if (intent.hasExtra("editMode")) {
            // Get the product from the intent
            val product = Gson().fromJson(Helper.fetchSharedPreference(this, "editProductModel"), ProductModel::class.java)
            // Set the product details to the views
            productName.setText(product.productName)
            productDescription.setText(product.productDescription)
            productPrice.setText(product.productPrice)
            productMrp.setText(product.productMrp)
            productDiscount.setText(product.productDiscount)
            productStock.setText(product.productStock)
            productBrandName.setText(product.productBrandName)
            productExpiryDate.setText(product.productExpiryDate)
            productIngredients.setText(product.productIngredients)

            // Update the button text
            btnAddProduct.text = "Update Product"
        }

        // Set on click listener on thumbnail
        btnSelectThumbnail.setOnClickListener {
            // TODO: Open the gallery and select a thumbnail
        }
        // Set on click listener on Images
        btnSelectMultipleImages.setOnClickListener {
            // TODO: Open the gallery and select multiple images
        }

        //set on click Listener on expiry date
        productExpiryDate.setOnClickListener {
            // Open the date picker and select the expiry date
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // Set the selected date to the EditText
                    productExpiryDate.setText("$dayOfMonth-${month + 1}-$year")
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Set on click listener on Add Product
        btnAddProduct.setOnClickListener {
            // Validate the data
            if (validateData()) {
                // If its an edit product
                if (intent.hasExtra("editMode")) {
                    // Update the product
                    updateProduct()
                } else {
                    // Create a new product
                    addNewProduct()
                }
            }
        }
    }

    // Function to Initialize Views
    private fun initializeViews() {
        productName = findViewById(R.id.product_name)
        productDescription = findViewById(R.id.product_description)
        productCategorySpinner = findViewById(R.id.product_category_spinner)
        productStatusSpinner = findViewById(R.id.product_status_spinner)
        productUnitSpinner = findViewById(R.id.product_unit_spinner)
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
            productStatusSpinner.selectedItem.toString() == "Select Status" -> {
                Toast.makeText(this, "Please Select Status", Toast.LENGTH_SHORT).show()
                false
            }
            productUnitSpinner.selectedItem.toString() == "Select Unit" -> {
                Toast.makeText(this, "Please Select Unit", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    // Function to add new product
    private fun addNewProduct() {
        if (!validateData()) {
            Helper.showToast(this, "Please fill all the fields")
            return
        }

        // get the data from the views
        val productName = productName.text.toString()
        val productDescription = productDescription.text.toString()
        val productPrice = productPrice.text.toString()
        val productMrp = productMrp.text.toString()
        val productDiscount = productDiscount.text.toString()
        val productBrandName = productBrandName.text.toString()
        val productExpiryDate = productExpiryDate.text.toString()
        val productIngredients = productIngredients.text.toString()
        val productStatus = productStatusSpinner.selectedItem.toString()
        val productUnit = productUnitSpinner.selectedItem.toString()
        val productStock = productStock.text.toString()

        val productModel = ProductModel(
            productName = productName,
            productDescription = productDescription,
            productPrice = productPrice,
            productMrp = productMrp,
            productDiscount = productDiscount,
            productBrandName = productBrandName,
            productExpiryDate = productExpiryDate,
            productIngredients = productIngredients,
            productStock = productStock,
            productStatus = productStatus,
            productUnit = productUnit,
            productCategoryId = productCategoryId
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ProductRepository.add(productModel, this@AddNewProductActivity)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        201 -> {
                            Helper.showToast(
                                this@AddNewProductActivity,
                                "Product Added Successfully"
                            )
                            finish()
                        }
                        else -> {
                            Helper.showToast(
                                this@AddNewProductActivity,
                                "Error Occurred : ${response.code}"
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }

    // Function to update the product
    private fun updateProduct(){
        if (!validateData()) {
            Helper.showToast(this, "Please fill all the fields")
            return
        }

        // get the data from the views
        val productName = productName.text.toString()
        val productDescription = productDescription.text.toString()
        val productPrice = productPrice.text.toString()
        val productMrp = productMrp.text.toString()
        val productDiscount = productDiscount.text.toString()
        val productBrandName = productBrandName.text.toString()
        val productExpiryDate = productExpiryDate.text.toString()
        val productIngredients = productIngredients.text.toString()
        val productStatus = productStatusSpinner.selectedItem.toString()
        val productUnit = productUnitSpinner.selectedItem.toString()
        val productStock = productStock.text.toString()

        val productModel = ProductModel(
            productName = productName,
            productDescription = productDescription,
            productPrice = productPrice,
            productMrp = productMrp,
            productDiscount = productDiscount,
            productBrandName = productBrandName,
            productExpiryDate = productExpiryDate,
            productIngredients = productIngredients,
            productStock = productStock,
            productStatus = productStatus,
            productUnit = productUnit,
            productCategoryId = productCategoryId
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ProductRepository.update(productModel, this@AddNewProductActivity)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        204 -> {
                            Helper.showToast(
                                this@AddNewProductActivity,
                                "Product Updated Successfully"
                            )
                            finish()
                        }
                        else -> {
                            Helper.showToast(
                                this@AddNewProductActivity,
                                "Error Occurred : ${response.code}"
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }

    // Function to Display category list in Spinner
    private fun displayCategoryList() {
        val productCategory = findViewById<Spinner>(R.id.product_category_spinner)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = CategoryRepository.fetchAll(this@AddNewProductActivity)
                withContext(Dispatchers.Main) {
                    when (response.code) {
                        200 -> {
                            val items =
                                Gson().fromJson(response.data, Array<CategoryModel>::class.java)
                            val adapter = ArrayAdapter(
                                this@AddNewProductActivity,
                                android.R.layout.simple_spinner_item,
                                items.map { it.categoryName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            productCategory.adapter = adapter

                            productCategory.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val category = items[position]
                                    productCategoryId = category.categoryId
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Force user to select category
                                    productCategoryId = "1"
                                }
                            }
                        }
                        else -> {
                            Toast.makeText(
                                this@AddNewProductActivity,
                                "Error Occurred ${response.data}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.d("Error", ex.toString())
            }
        }
    }

    // Function to display unit list in Spinner
    private fun displayUnitList() {
        val items: Array<String> = arrayOf("Select Unit", "kg", "g", "mg", "l", "ml", "piece")
        val adapter = ArrayAdapter(
            this@AddNewProductActivity,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        productUnitSpinner.adapter = adapter
    }

    // Function to display status list in Spinner
    private fun displayStatusList() {
        val items: Array<String> = arrayOf("Select Status", "Active", "Inactive")
        val adapter = ArrayAdapter(
            this@AddNewProductActivity,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        productStatusSpinner.adapter = adapter
    }
}
