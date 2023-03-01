package com.nms.admin.repo

import android.content.Context
import com.nms.admin.models.CategoryModel
import com.nms.admin.utils.Helper
import com.nms.admin.utils.api.ApiRequest
import com.nms.admin.utils.api.ApiResponse
import com.google.gson.Gson

class CategoryRepository {
    companion object {
        // Function to fetch all categories
        fun fetchAll(context: Context): ApiResponse {
            return ApiRequest.getRequest(
                ApiRequest.URL_GET_CATEGORIES
            )
        }

        // Function to add category
        fun add(category: CategoryModel, context: Context): ApiResponse {
            return ApiRequest.postRequest(
                ApiRequest.URL_ADD_CATEGORY,
                Gson().toJson(category),
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to update category
        fun update(category: CategoryModel, context: Context): ApiResponse {
            return ApiRequest.postRequest(
                ApiRequest.URL_UPDATE_CATEGORY,
                Gson().toJson(category),
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to delete category
        fun delete(id: String, context: Context): ApiResponse {
            val json = "{\"id\": \"$id\"}"
            return ApiRequest.postRequest(
                ApiRequest.URL_DELETE_CATEGORY,
                json,
                Helper.fetchSharedPreference(context, "token")
            )
        }
    }
}