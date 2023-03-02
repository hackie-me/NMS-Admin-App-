package com.nms.admin.models

data class OrderModel(
    val id: String = "",
    val uid: String = "",
    val pid: String = "",
    val note: String = "",
    val quantity: String = "",
    val address: String = "",
    val pdf: String = "",
    val total: String = "",
    val status: String = "",
    var created_at: String = "",
)
