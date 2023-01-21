package com.example.nmsadminapp.repo

import android.content.Context
import com.example.nmsadminapp.service.Authentication
import com.example.nmsadminapp.utils.api.ApiRequest
import com.example.nmsadminapp.utils.api.ApiResponse
import okhttp3.MultipartBody

class UploadImageRepository {
    companion object {
        // Function to upload image
        fun uploadImage(
            context: Context,
            filePart: MultipartBody.Part,
            lastRecordId: String,
            url: String
        ): ApiResponse {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg", filePart.body)
                .addFormDataPart("last_record_id", lastRecordId)
                .build()
            return ApiRequest.postRequestWithImage(
                url,
                requestBody,
                Authentication.getToken(context)!!
            )
        }
    }
}