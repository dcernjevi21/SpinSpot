package com.example.bassbytecreators

import BaseActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.navigation.NavigationView

class DJMyProfileActivity : BaseActivity() {
    private lateinit var navView: NavigationView
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

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)

        setupProfileElements()
    }


    private fun setupProfileElements() {

        findViewById<LinearLayout>(R.id.llProfileReviewsRow).setOnClickListener {
            val intent = Intent(this, DJReviewsActivity::class.java)
            intent.putExtra("dj_id", userId)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.llDJPersonalDetailsRow).setOnClickListener {
            val intent = Intent(this, DJPersonalDetailsActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.llSettingsRow).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }
}