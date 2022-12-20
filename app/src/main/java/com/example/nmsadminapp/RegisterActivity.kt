package com.example.nmsadminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {

    // global variables
    private lateinit var txtName: EditText
    private lateinit var txtMobile : EditText
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
}