package com.nms.admin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.nms.admin.fragments.HomeFragment
import com.nms.admin.service.Authentication
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.ByteArrayOutputStream

class Helper {
    companion object {
        // Global variables
        const val prefName: String = "NMSAdminApp"

        // Function to Show Toast
        fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, message, duration).show()
        }

        // FetchJson data from response
        fun fetchTokenFromJsonData(context: Context, response: String): String? {
            val element = Gson().fromJson(response, JsonObject::class.java)
            return element.get("token").asString
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

        // Function to check if the shared preference has the key
        fun hasSharedPreference(context: Context, key: String): Boolean {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.contains(key)
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

        fun replaceFragment(
            supportFragmentManager: FragmentManager,
            homeFragment: HomeFragment,
            fragmentContainer: Int
        ) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(fragmentContainer, homeFragment)
            fragmentTransaction.commit()
        }

        // Function to convert image to base64
        fun convertImageToBase64(image: ImageView): String {
            val drawable = image.drawable
            val bitmap = (drawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
        }

        // Function to show Confirmation Dialog
        fun showConfirmationDialog(
            context: Context,
            title: String,
            message: String,
            yes: String,
            no: String,
            function: () -> Unit,
            function1: () -> Unit
        ) {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(yes) { _, _ ->
                function()
            }
            builder.setNegativeButton(no) { _, _ ->
                function1()
            }
            builder.show()
        }

        // Function to Decode JWT Token
        fun decodeJWTToken(token: String): String {
            val split = token.split(".")
            val payload = split[1]
            val decoded = Base64.decode(payload, Base64.DEFAULT)
            return String(decoded)
        }

        // Function to fetch user id from JWT Token
        fun getDataFromToken(context: Context, value: String): String? {
            return Gson().fromJson(
                decodeJWTToken(
                    Authentication.getToken(context)!!
                ), JsonObject::class.java
            ).getAsJsonObject("data")?.get(value)?.asString
        }

        // Function to show snack-bar
        fun showSnackBar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        // Function to show snack-bar with action
        fun showSnackBarWithAction(
            view: View,
            message: String,
            action: String,
            function: () -> Unit
        ) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(action) {
                function()
            }.show()
        }

        // Function to toggle progress Dialog
        fun toggleProgressBar(progressDialog: android.widget.ProgressBar, show: Boolean) {
            if (show) {
                progressDialog.visibility = View.VISIBLE
            } else {
                progressDialog.visibility = View.GONE
            }
        }


        // Function to Generate Random String
        fun generateRandomString(i: Int): Any {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            val sb = StringBuilder()
            for (j in 0 until i) {
                val index = (Math.random() * chars.length).toInt()
                sb.append(chars[index])
            }
            return sb.toString()
        }

        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}