package com.nms.nmsadminapp.service

import android.content.Context
import com.example.admin.utils.Helper

class Authentication(
    private val context: Context
) {
    companion object {

        // Function to check if user is logged in
        fun isLoggedIn(context: Context): Boolean {
            return tokenExists(context)
        }

        // Function to Store Token
        fun storeToken(context: Context, token: String) {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("token", token)
                commit()
            }
        }

        // Function to Check if Token Exists
        private fun tokenExists(context: Context): Boolean {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            return sharedPref.contains("token")
        }

        // Function to Get Token
        fun getToken(context: Context): String? {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            return sharedPref.getString("token", null)
        }

        // Function to Clear Token
        fun clearToken(context: Context) {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("token")
                commit()
            }
        }
    }
}