package com.example.nmsadminapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

class Helper {
    companion object{
        // Global variables
        private const val prefName: String = "NMSAdminApp"

        // Function to Show Toast
        fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, message, duration).show()
        }

        // Function to Store Token
        fun storeToken(context: Context, token: String) {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("token", token)
                commit()
            }
        }

        // Function to Fetch Token
        fun fetchToken(context: Context): String {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.getString("token", "")!!
        }

        // Function to Remove Token
        fun removeToken(context: Context) {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("token")
                commit()
            }
        }

        // Function to Check if Token Exists
        fun tokenExists(context: Context): Boolean {
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.contains("token")
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