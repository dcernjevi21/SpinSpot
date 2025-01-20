package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class UserMyProfileActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_my_profile)

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "User ID nije pronaden!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        setupNavigationMenu(navigationView)
        setupProfileElements()
    }

    private fun setupNavigationMenu(navigationView: NavigationView) {
        val menu = navigationView.menu
        menu.findItem(R.id.nav_login)?.isVisible = false
        menu.findItem(R.id.nav_registration)?.isVisible = false
        menu.findItem(R.id.nav_djstatistics)?.isVisible = false

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_my_profile -> {
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupProfileElements() {

        findViewById<LinearLayout>(R.id.llUserPersonalDetailsRow).setOnClickListener {
            val intent = Intent(this, UserPersonalDetailsActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.llUserSettingsRow).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }
}