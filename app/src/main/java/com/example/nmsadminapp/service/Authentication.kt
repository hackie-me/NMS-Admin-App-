package com.example.nmsadminapp.service

import android.app.Activity
import android.app.AppComponentFactory
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.nmsadminapp.utils.Helper

class Authentication(
    private val context: Context
) {
    companion object {
        // Function to check if user is logged in
        fun isLoggedIn(context: Context): Boolean {
            return tokenExists(context)
        }

        // Function to logout user
        fun logout(context: Context) {
            removeToken(context)
        }

        // Function to Store Token
        fun storeToken(context: Context, token: String) {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("token", token)
                commit()
            }
        }

        // Function to Fetch Token
        fun fetchToken(context: Context): String {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            return sharedPref.getString("token", "")!!
        }

        // Function to Remove Token
        fun removeToken(context: Context) {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("token")
                commit()
            }
        }

        // Function to Check if Token Exists
        fun tokenExists(context: Context): Boolean {
            val sharedPref = context.getSharedPreferences(Helper.prefName, Context.MODE_PRIVATE)
            return sharedPref.contains("token")
        }
    }
}