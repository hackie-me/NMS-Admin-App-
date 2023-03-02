package com.nms.admin.repo

import android.content.Context
import com.nms.admin.utils.api.ApiRequest
import com.nms.admin.utils.api.ApiResponse

class OrderRepository {
    companion object {
        // Function to fetch all orders
        fun fetchAll(): ApiResponse {
            return ApiRequest.getRequest(ApiRequest.URL_GET_ORDERS)
        }

        // Function to fetch All Custom Orders
        fun fetchAllCustomOrders(): ApiResponse {
            return ApiRequest.getRequest(ApiRequest.URL_GET_CUSTOM_ORDERS)
        }
    }
}