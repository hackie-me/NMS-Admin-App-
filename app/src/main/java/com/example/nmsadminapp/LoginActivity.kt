package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nmsadminapp.models.AdminModel
import com.example.nmsadminapp.service.AdminService
import com.example.nmsadminapp.utils.Helper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    // global variables
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtBtnRegister: TextView
    private lateinit var txtBtnForgotPassword: TextView
    private lateinit var btnGoogle: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize variables
        init()

        // set on click listeners
        btnLogin.setOnClickListener {
            // validate inputs
            if (validateInputs()) {
                // login user httpclient
                 val admin = AdminModel(
                     adminEmail = txtEmail.text.toString(),
                     adminPassword = txtPassword.text.toString()
                 )
                 CoroutineScope(Dispatchers.IO).launch {
                     val response = AdminService.login(admin)
                     withContext(Dispatchers.Main) {
                         if (response.code == 200) {
                             val listTutorialType = object : TypeToken<List<String>>() {}.type
                             val gson = Gson()
                             val data: List<String> = gson.fromJson(response.data?.get(0)?.toString(), listTutorialType)
                             Log.i("data", data.toString())
                             Helper.showAlertDialog(this@LoginActivity, "Login Successful", "Welcome, ${data[0]}")
                         } else {
                             val data = response.data
                             Helper.showAlertDialog(this@LoginActivity, "Login Failed", data.toString())
                         }
                     }
                 }
            }
        }

        txtBtnRegister.setOnClickListener {
            // Opens the register activity
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        txtBtnForgotPassword.setOnClickListener {
            // Opens the forgot password activity
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            // TODO: Add Google sign in
        }

    }

    // function to initialize the variables
    private fun init() {
        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtBtnRegister = findViewById(R.id.txtBtnRegister)
        txtBtnForgotPassword = findViewById(R.id.txtBtnForgotPassword)
        btnGoogle = findViewById(R.id.btnGoogle)
        // progressBar = findViewById(R.id.progressBar)
    }

    // function to validate the inputs
    private fun validateInputs(): Boolean {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        if (email.isEmpty()) {
            txtEmail.error = "Email is required"
            txtEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            txtPassword.error = "Password is required"
            txtPassword.requestFocus()
            return false
        }
        return true
    }
}
