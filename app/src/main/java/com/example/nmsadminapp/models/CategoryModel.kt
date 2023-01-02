package com.example.nmsadminapp.models

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("id") val categoryId: String = "",
    @SerializedName("name") val categoryName: String = "",
    @SerializedName("description") val categoryDescription: String = "",
    @SerializedName("image") val categoryImage: String = ""
)