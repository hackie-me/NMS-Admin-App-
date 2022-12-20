package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
            // TODO: Login the user

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
}