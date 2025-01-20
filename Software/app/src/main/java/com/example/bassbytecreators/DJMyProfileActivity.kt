package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DJMyProfileActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_djmy_profile)

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
        menu.findItem(R.id.nav_djstatistics)?.isVisible = true

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_my_profile -> {
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_djstatistics -> {
                    val intent = Intent(this, DJStatisticsActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupProfileElements() {

        findViewById<LinearLayout>(R.id.llProfileReviewsRow).setOnClickListener {
            val intent = Intent(this, DJReviewsActivity::class.java)
            intent.putExtra("dj_id", userId)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.llDJPersonalDetailsRow).setOnClickListener {
            Toast.makeText(this, "Dolazi uskoro...", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.llSettingsRow).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }
}