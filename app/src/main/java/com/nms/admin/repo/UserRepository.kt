package com.nms.admin.repo

import com.google.gson.Gson
import com.nms.admin.models.UsersModel
import com.nms.admin.utils.api.ApiRequest
import com.nms.admin.utils.api.ApiResponse

class UserRepository {
    companion object{
        // Function to fetch all users
        fun fetchAll(): ApiResponse {
            return ApiRequest.getRequest(ApiRequest.URL_GET_USERS)
        }

        // Function to fetch user by id
        fun fetchById(usersModel: UsersModel): ApiResponse {
            return ApiRequest.getRequest(ApiRequest.URL_GET_USER_BY_ID, Gson().toJson(usersModel))
        }
    }
}