package com.nms.admin.models

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("id") val productId: String = "",
    @SerializedName("name") val productName: String = "",
    @SerializedName("description") val productDescription: String = "",
    @SerializedName("price") val productPrice: String = "",
    @SerializedName("mrp") val productMrp: String = "",
    @SerializedName("discount") val productDiscount: String = "",
    @SerializedName("brand_name") val productBrandName: String = "",
    @SerializedName("expiry_date") val productExpiryDate: String = "",
    @SerializedName("thumbnail") val productThumbnail: String = "",
    @SerializedName("images") val productImages: String = "",
    @SerializedName("ingredients") val productIngredients: String = "",
    @SerializedName("status") val productStatus: String = "",
    @SerializedName("unit") val productUnit: String = "",
    @SerializedName("quantity") val productStock: String = "",
    @SerializedName("category_id") val productCategoryId: String = "",
)