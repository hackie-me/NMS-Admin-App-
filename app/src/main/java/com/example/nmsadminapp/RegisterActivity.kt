package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nmsadminapp.models.AdminModel
import com.example.nmsadminapp.repo.AdminRepo
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
                )
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val adminRepo = AdminRepo()
                        val response = adminRepo.registerUser(admin)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registered Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error : ${ex.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
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
        val regex = Regex("^[6-9]\\d{9}\$")
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

        return true
    }

    // function to register user
    private fun registerUser() {
        // get the values from the edit texts
        val name = txtName.text.toString()
        val mobile = txtMobile.text.toString()
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        // create a database handler object
        val adminRepo = AdminRepo()
        // Coroutine Scope to handle network on main thread exception
        CoroutineScope(Dispatchers.IO).launch {
            // create a admin object
            val admin = AdminModel(
                adminName = name,
                adminPhone = mobile,
                adminEmail = email,
                adminPassword = password
            )
            // call the register user function
            val response = adminRepo.registerUser(admin)
            withContext(Dispatchers.Main) {
                Log.d("Response", response.toString())
                Toast.makeText(
                    this@RegisterActivity,
                    "User registered successfully",
                    Toast.LENGTH_SHORT
                ).show()
                // open the login activity
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }

        adminRepo.registerUser(AdminModel(name, mobile, email, password))
    }

    // function to show toast message
    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}