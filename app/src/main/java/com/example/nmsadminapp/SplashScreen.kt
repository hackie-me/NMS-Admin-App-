package com.example.nmsadminapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Splash screen timer
        Thread(Runnable {
            try
            {
                Thread.sleep(3000)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            finally
            {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }).start()
    }
}