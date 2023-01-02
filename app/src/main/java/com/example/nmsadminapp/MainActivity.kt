package com.example.nmsadminapp

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.nmsadminapp.fragments.*
import com.example.nmsadminapp.utils.Helper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: AccountFragment
    private lateinit var manageFragment: ManageFragment
    private lateinit var ordersFragment: OrdersFragment
    private lateinit var productsFragment: ProductsFragment

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display Admin Name in the Action Bar
        val actionBar = supportActionBar
        actionBar!!.title = "Nilkanth Medical Store"

        // Bottom Navigation View
        bottomNavView = findViewById(R.id.bottomNavigation)
        bottomNavView.setOnItemSelectedListener(::bottomNavItemSelected)

        // Home Fragment
        homeFragment = HomeFragment()
        profileFragment = AccountFragment()
        manageFragment = ManageFragment()
        ordersFragment = OrdersFragment()
        productsFragment = ProductsFragment()

        // Set Home Fragment as Default
        showHomeFragment()
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
                logoutAdmin()
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

    // Function to logout admin
    private fun logoutAdmin() {
        // remove admin data from shared preferences
        Helper.removeSharedPreference(this, "token")
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
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

    // Function to handle bottom navigation item click
    private fun bottomNavItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.bottom_navigation_home -> showHomeFragment()
            R.id.bottom_navigation_orders -> showOrderFragment()
            R.id.bottom_navigation_manage -> showManageFragment()
            R.id.bottom_navigation_products -> showProductsFragment()
            R.id.bottom_navigation_profile -> showProfileFragment()
            else -> return false
        }
        return true
    }

    // Function to show Home Fragment
    private fun showHomeFragment()
    {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit()
    }

    // Function to show Orders Fragment
    private fun showOrderFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ordersFragment).commit()
    }

    // Function to show Manage Fragment
    private fun showManageFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, manageFragment).commit()
    }

    // Function to show Products Fragment
    private fun showProductsFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, productsFragment).commit()
    }

    // Function to show Profile Fragment
    private fun showProfileFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit()
    }


}