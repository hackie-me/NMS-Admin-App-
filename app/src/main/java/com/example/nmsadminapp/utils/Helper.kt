package com.example.nmsadminapp.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject

class Helper {
    companion object{
        // Global variables
        public const val prefName: String = "NMSAdminApp"

        // Function to Show Toast
        fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, message, duration).show()
        }

        // Show progress bar while API call is in progress
        fun showProgressBar(context: Context) {

        }

        // Handle HTTP response codes
        fun handleHttpResponseCode(context: Context, code: Int) {
            when (code) {
                HttpResponseCode.OK -> {
                    showToast(context, "Success")
                }
                HttpResponseCode.UNAUTHORIZED -> {
                    showToast(context, "Unauthorized")
                }
                HttpResponseCode.BAD_REQUEST -> {
                    showToast(context, "Bad Request")
                }
                HttpResponseCode.NOT_FOUND -> {
                    showToast(context, "Not Found")
                }
                HttpResponseCode.INTERNAL_SERVER_ERROR -> {
                    showToast(context, "Internal Server Error")
                }
                HttpResponseCode.SERVICE_UNAVAILABLE -> {
                    showToast(context, "Service Unavailable")
                }
                else -> {
                    showToast(context, "Something went wrong")
                }
            }
        }


        // FetchJson data from response
        fun fetchTokenFromJsonData(context: Context, response: String): String?
        {
            val element = Gson().fromJson(response, JsonObject::class.java)
            return element.get("token").asString
        }

        // Function to Store Admin
        fun storeAdmin(context: Context, admin: String) {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("admin", admin)
                commit()
            }
        }

        // Function to Fetch Admin
        fun fetchAdmin(context: Context): String {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.getString("admin", "")!!
        }

        // Function to Remove Admin
        fun removeAdmin(context: Context) {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("admin")
                commit()
            }
        }

        // Function to Check if Admin Exists
        fun adminExists(context: Context): Boolean {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.contains("admin")
        }

        // Function to store shared preference
        fun storeSharedPreference(context: Context, key: String, value: String) {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(key, value)
                commit()
            }
        }

        // Function to fetch shared preference
        fun fetchSharedPreference(context: Context, key: String): String {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.getString(key, "")!!
        }

        // Function to remove shared preference
        fun removeSharedPreference(context: Context, key: String) {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove(key)
                commit()
            }
        }

        // Function to show alert dialog
        fun showAlertDialog(context: Context, title: String, message: String) {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }
}