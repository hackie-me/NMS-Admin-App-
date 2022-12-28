package com.example.nmsadminapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.nmsadminapp.utils.Helper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display Admin Name in the Action Bar
        val actionBar = supportActionBar
        actionBar!!.title = "Welcome, Admin"


    }

    // Add three dots to the Action Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.three_dot_menu, menu)
        return true
    }

    // Three dots menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.three_dot_menu_item_logout -> {
                // Logout user and go to Login Activity
                Helper.showToast(this, "Logout")
                true
            }
            R.id.three_dot_menu_item_about -> {
                // Show About Dialog
                Helper.showToast(this, "About")
                true
            }
            R.id.three_dot_menu_item_help -> {
                // Show Help Dialog
                Helper.showToast(this, "Help")
                true
            }
            R.id.three_dot_menu_item_exit -> {
                // Exit the app
                Helper.showToast(this, "Exit")
                true
            }
            R.id.three_dot_menu_item_share -> {
                // Share the app
                Helper.showToast(this, "Share")
                shareApp()
                true
            }
            R.id.three_dot_menu_item_rate -> {
                // Rate the app
                Helper.showToast(this, "Rate")
                rateApp()
                true
            }
            R.id.three_dot_menu_item_feedback -> {
                // Send Feedback
                Helper.showToast(this, "Feedback")
                true
            }
            R.id.three_dot_menu_item_privacy_policy -> {
                // Show Privacy Policy
                Helper.showToast(this, "Privacy Policy")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Function to exit the app
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    // Function to Share Application
    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "NMS Admin App")
        var shareMessage = "\nLet me recommend you this application\n\n"
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    // Function to Rate Application
    private fun rateApp() {
        val uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                )
            )
        }
    }

    // Function to Send Feedback
    private fun sendFeedback()
    {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I have a feedback for you.")
    }
}