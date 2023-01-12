package com.example.nmsadminapp.models

import com.google.gson.annotations.SerializedName

data class AdminModel(
    @SerializedName("id") val adminId: String = "",
    @SerializedName("name") val adminName: String = "",
    @SerializedName("email") val adminEmail: String = "",
    @SerializedName("phone") val adminPhone: String = "",
    @SerializedName("password") val adminPassword: String = "",
    @SerializedName("confirm_password") val adminConfirmPassword: String = "",
    @SerializedName("image") val adminImage: String = "https://i.pravatar.cc/150"
)
