package com.example.nmsadminapp.repo

import android.util.Log
import com.example.nmsadminapp.models.AdminModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AdminRepo {
    private val client = OkHttpClient()
    private val url = "http://"
    private val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    private var response = ""


    // function to register user
    public fun registerUser(admin: AdminModel): String {
        // Make a call to the okHttp client to register the user
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        // generate JSON from the admin object
        val body: RequestBody = admin.toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url("http://192.168.1.2/nms/admin/auth/register.php")
            .post(body)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            response = client.newCall(request).execute()
                .use { response ->
                    response.body!!.string()
                }
            }
        return response
    }
}