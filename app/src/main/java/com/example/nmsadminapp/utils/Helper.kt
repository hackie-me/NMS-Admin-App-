package com.example.nmsadminapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.nmsadminapp.fragments.HomeFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.ByteArrayOutputStream

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
        fun showAlertDialog(
            context: Context,
            title: String,
            message: String,
            s: String,
            s1: String,
            function: () -> Unit,
            function1: () -> Unit
        ) {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(s) { _, _ ->
                function()
            }
            builder.setNegativeButton(s1) { _, _ ->
                function1()
            }
            builder.show()
        }

        fun replaceFragment(supportFragmentManager: FragmentManager, homeFragment: HomeFragment, fragmentContainer: Int) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(fragmentContainer, homeFragment)
            fragmentTransaction.commit()
        }

        // Function to show Error Message
        fun showErrorMessage(context: Context, message: String) {
            showToast(context, message)
        }

        // Function to convert image to base64
        fun convertImageToBase64(image: ImageView): String {
            // convert imageView to int
            image.isDrawingCacheEnabled = true
            image.buildDrawingCache()
            val bitmap = image.drawingCache

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }

    }
}