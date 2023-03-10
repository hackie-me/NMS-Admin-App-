package com.nms.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.nms.admin.fragments.*
import com.nms.admin.service.Authentication
import com.nms.admin.utils.Helper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: AccountFragment
    private lateinit var manageFragment: InvoiceFragment
    private lateinit var ordersFragment: OrdersFragment
    private lateinit var productsFragment: CatalogueFragment

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if user is logged in
        if (!Authentication.isLoggedIn(this)) {
            // If user is not logged in, go to Login Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Display Admin Name in the Action Bar
        val actionBar = supportActionBar
        actionBar!!.title = "Nilkanth Medical Store"

        // Bottom Navigation View
        bottomNavView = findViewById(R.id.bottomNavigation)
        bottomNavView.setOnItemSelectedListener(::bottomNavItemSelected)

        // Home Fragment
        homeFragment = HomeFragment()
        profileFragment = AccountFragment()
        manageFragment = InvoiceFragment()
        ordersFragment = OrdersFragment()
        productsFragment = CatalogueFragment()

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

    // Function to handle bottom navigation item click
    private fun bottomNavItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, homeFragment)
            .commit()
    }

    // Function to show Orders Fragment
    private fun showOrderFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ordersFragment)
            .commit()
    }

    // Function to show Manage Fragment
    private fun showManageFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, manageFragment)
            .commit()
    }

    // Function to show Products Fragment
    private fun showProductsFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, productsFragment)
            .commit()
    }

    // Function to show Profile Fragment
    private fun showProfileFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, profileFragment)
            .commit()
    }


}