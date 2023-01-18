package com.example.nmsadminapp.repo

import com.example.nmsadminapp.models.AdminModel
import com.example.nmsadminapp.utils.api.ApiRequest
import com.example.nmsadminapp.utils.api.ApiResponse
import com.google.gson.Gson

class AdminRepository {
    companion object {
        // Function to login admin
        fun login(admin: AdminModel): ApiResponse {
            return ApiRequest.postRequest(ApiRequest.URL_LOGIN, Gson().toJson(admin))
        }

        // function to register admin
        fun register(admin: AdminModel): ApiResponse {
            return ApiRequest.postRequest(ApiRequest.URL_REGISTER, Gson().toJson(admin))
        }

        // function to update admin
        fun update(admin: AdminModel, token: String): ApiResponse {
            return ApiRequest.postRequest(ApiRequest.URL_UPDATE_ADMIN, Gson().toJson(admin), token)
        }

        // function to fetch admin
        fun fetch(token: String): ApiResponse {
            return ApiRequest.getRequest(ApiRequest.URL_FETCH_ADMIN, token)
        }

        // Function to refresh token
        fun refreshToken(token: String): ApiResponse {
            return ApiRequest.postRequest(ApiRequest.URL_REFRESH_TOKEN, "", token)
        }
    }
}