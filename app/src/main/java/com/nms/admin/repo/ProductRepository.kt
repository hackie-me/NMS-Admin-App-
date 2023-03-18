package com.nms.admin.repo

import android.content.Context
import com.google.gson.Gson
import com.nms.admin.models.ProductModel
import com.nms.admin.utils.Helper
import com.nms.admin.utils.api.ApiRequest
import com.nms.admin.utils.api.ApiResponse

class ProductRepository {
    companion object {
        // Function to fetch all products
        fun fetchAll(context: Context): ApiResponse {
            return ApiRequest.getRequest(
                ApiRequest.URL_GET_PRODUCTS,
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to fetch product by id
        fun fetchById(id: String): ApiResponse {
            val json = "{\"id\": \"$id\"}"
            return ApiRequest.getRequest(
                ApiRequest.URL_GET_PRODUCT_BY_ID.plus("?id=$id"),
                json,
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