package com.nms.nmsadminapp.utils.api

data class ApiResponse(
    val code: Int = 0,
    val data: String? = null,
    val message: String? = null
)