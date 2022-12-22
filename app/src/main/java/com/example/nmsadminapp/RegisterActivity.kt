package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nmsadminapp.models.AdminModel
import com.example.nmsadminapp.service.AdminService
import com.example.nmsadminapp.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    // global variables
    private lateinit var txtName: EditText
    private lateinit var txtMobile: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // initialize variables
        init()

        // set on click listeners
        btnRegister.setOnClickListener {
            // validate inputs
            if (validateInputs()) {
                // register user httpclient
                val admin = AdminModel(
                    adminName = txtName.text.toString(),
                    adminPhone = txtMobile.text.toString(),
                    adminEmail = txtEmail.text.toString(),
                    adminPassword = txtPassword.text.toString(),
                    adminConfirmPassword = txtConfirmPassword.text.toString()
                )
                CoroutineScope(Dispatchers.IO).launch {
                    val response = AdminService.register(admin)
                    withContext(Dispatchers.Main) {
                        if (response.code == 200) {
                            Helper.showToast(this@RegisterActivity, "Registered Successfully")
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            val data = response.data
                            Helper.showToast(this@RegisterActivity, data.toString())
                        }
                    }
                }
            } else {
                Helper.showToast(this, "Please fill all the fields", Toast.LENGTH_LONG)
            }
        }

        btnLogin.setOnClickListener {
            // Opens the login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    // function to initialize the variables
    private fun init() {
        txtName = findViewById(R.id.etFullName)
        txtMobile = findViewById(R.id.etMobile)
        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        txtConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnBackToLogin)
    }

    // function to validate inputs
    private fun validateInputs(): Boolean {
        // get the values from the edit texts
        val name = txtName.text.toString()
        val mobile = txtMobile.text.toString()
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()
        val confirmPassword = txtConfirmPassword.text.toString()

        // check if the name is empty
        if (name.isEmpty()) {
            txtName.error = "Please enter your name"
            txtName.requestFocus()
            return false
        }

        // check if the mobile number is empty
        if (mobile.isEmpty()) {
            txtMobile.error = "Please enter your mobile number"
            txtMobile.requestFocus()
            return false
        }

        // regex to check if the mobile number is valid
        val regex = Regex("^\\d{10}\$")
        if (!regex.matches(mobile)) {
            txtMobile.error = "Please enter a valid mobile number"
            txtMobile.requestFocus()
            return false
        }

        // check if the email is empty
        if (email.isEmpty()) {
            txtEmail.error = "Please enter your email"
            txtEmail.requestFocus()
            return false
        }

        // regex to check if the email is valid
        val emailRegex = Regex("^[A-Za-z](.*)([@])(.+)(\\.)(.+)")
        if (!emailRegex.matches(email)) {
            txtEmail.error = "Please enter a valid email"
            txtEmail.requestFocus()
            return false
        }

        // check if the password is empty
        if (password.isEmpty()) {
            txtPassword.error = "Please enter your password"
            txtPassword.requestFocus()
            return false
        }

        // check if the confirm password is empty
        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.error = "Please enter your confirm password"
            txtConfirmPassword.requestFocus()
            return false
        }

        // check if the password and confirm password are same
        if (password != confirmPassword) {
            txtConfirmPassword.error = "Password and confirm password should be same"
            txtConfirmPassword.requestFocus()
            return false
        }

        // check if the password must be 6 characters long
        if (password.length < 6) {
            txtPassword.error = "Password must be 6 characters long"
            txtPassword.requestFocus()
            return false
        }

        return true
    }

}