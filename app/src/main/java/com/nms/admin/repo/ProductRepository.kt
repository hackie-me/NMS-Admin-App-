package com.nms.nmsadminapp.repo

import android.content.Context
import com.example.admin.models.ProductModel
import com.example.admin.utils.Helper
import com.example.admin.utils.api.ApiRequest
import com.example.admin.utils.api.ApiResponse
import com.google.gson.Gson

class ProductRepository {
    companion object {
        // Function to fetch all products
        fun fetchAll(context: Context): ApiResponse {
            return ApiRequest.getRequest(
                ApiRequest.URL_GET_PRODUCTS,
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to add product
        fun add(product: ProductModel, context: Context): ApiResponse {
            return ApiRequest.postRequest(
                ApiRequest.URL_ADD_PRODUCT,
                Gson().toJson(product),
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to update product
        fun update(product: ProductModel, context: Context): ApiResponse {
            return ApiRequest.postRequest(
                ApiRequest.URL_UPDATE_PRODUCT,
                Gson().toJson(product),
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to delete product
        fun delete(id: String, context: Context): ApiResponse {
            val json = "{\"id\": \"$id\"}"
            return ApiRequest.postRequest(
                ApiRequest.URL_DELETE_PRODUCT,
                json,
                Helper.fetchSharedPreference(context, "token")
            )
        }
    }
}