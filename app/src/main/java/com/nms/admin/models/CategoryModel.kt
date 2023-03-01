package com.nms.admin.models

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("id") val categoryId: String = "",
    @SerializedName("name") val categoryName: String = "",
    @SerializedName("description") val categoryDescription: String = "",
    @SerializedName("image") val categoryImage: String = "https://source.unsplash.com/random?products,stationery,categories"
)