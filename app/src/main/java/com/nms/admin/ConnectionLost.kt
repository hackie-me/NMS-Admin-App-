package com.nms.nmsadminapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.admin.utils.Helper

class ConnectionLost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection_lost)

        // Hide action bar
        supportActionBar?.hide()
        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_connection).setOnRefreshListener {
            // check if internet is available
            if (Helper.isInternetAvailable(this)) {
                // if internet is available, redirect to splashscreen
                startActivity(Intent(this, SplashscreenActivity::class.java))
                finish()
            }
            finish()
        }
    }
}