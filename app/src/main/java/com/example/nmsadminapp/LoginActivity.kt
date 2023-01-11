package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nmsadminapp.models.AdminModel
import com.example.nmsadminapp.repo.AdminRepository
import com.example.nmsadminapp.service.Authentication
import com.example.nmsadminapp.utils.Helper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // if already logged in, redirect to main activity
        if (Authentication.isLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
                    val response = AdminRepository.login(admin)
                    withContext(Dispatchers.Main) {
                        // Store token in shared preferences
                        if (response.code == 200) {
                            Helper.showToast(this@LoginActivity, "Logged In Successfully")
                            response.data?.let { it1 ->
                                Authentication.storeToken(
                                    this@LoginActivity,
                                    it1
                                )
                            }
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Helper.showAlertDialog(
                                this@LoginActivity,
                                "Error",
                                "Invalid Credentials",
                                "OK",
                                "",
                                {}) {}
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
