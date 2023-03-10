package com.nms.admin.repo

import android.content.Context
import com.nms.admin.models.FaqModel
import com.nms.admin.utils.Helper
import com.nms.admin.utils.api.ApiRequest
import com.nms.admin.utils.api.ApiResponse
import com.google.gson.Gson

class FaqRepository {
    companion object {
        // Function to fetch all faqs
        fun fetchAll(): ApiResponse {
            return ApiRequest.getRequest(ApiRequest.URL_GET_FAQ)
        }

        // Function to add faq
        fun add(faq: FaqModel, context: Context): ApiResponse {
            return ApiRequest.postRequest(
                ApiRequest.URL_ADD_FAQ,
                Gson().toJson(faq),
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to update faq
        fun update(faq: FaqModel, context: Context): ApiResponse {
            return ApiRequest.postRequest(
                ApiRequest.URL_UPDATE_FAQ,
                Gson().toJson(faq),
                Helper.fetchSharedPreference(context, "token")
            )
        }

        // Function to delete faq
        fun delete(id: String, context: Context): ApiResponse {
            val json = "{\"id\": \"$id\"}"
            return ApiRequest.postRequest(
                ApiRequest.URL_DELETE_FAQ,
                json,
                Helper.fetchSharedPreference(context, "token")
            )
        }
    }
}