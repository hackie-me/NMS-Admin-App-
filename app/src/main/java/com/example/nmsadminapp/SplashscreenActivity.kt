package com.example.nmsadminapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nmsadminapp.repo.AdminRepository
import com.example.nmsadminapp.service.Authentication
import com.example.nmsadminapp.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule({
            // check if Internet is available
            if (!Helper.isInternetAvailable(this)) {
                // if not, redirect to connection lost activity
                startActivity(Intent(this, ConnectionLost::class.java))
                finish()
            }

            // if already logged in, redirect to main activity
            if (Authentication.isLoggedIn(this)) {
                // Fetch New Token from API
                CoroutineScope(Dispatchers.IO).launch {
                    val repository = AdminRepository()
                    val response =
                        AdminRepository.refreshToken(Authentication.getToken(this@SplashscreenActivity)!!)
                    if (response.code == 200) {
                        Authentication.storeToken(
                            this@SplashscreenActivity,
                            response.data!!.toString()
                        )
                        withContext(Dispatchers.Main) {
                            startActivity(
                                Intent(
                                    this@SplashscreenActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                }
            }
            // else redirect to login activity
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 1, TimeUnit.SECONDS)

    }
}