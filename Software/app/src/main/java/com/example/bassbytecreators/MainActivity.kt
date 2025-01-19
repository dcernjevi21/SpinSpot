package com.example.bassbytecreators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private var userRole: String? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer()

        val intent = Intent(this, SearchActivity::class.java)
        val gumbic: Button = findViewById(R.id.button)
        gumbic.setOnClickListener {
            startActivity(intent)
        }
    }

    private fun setupNavigationDrawer() {
        userRole = intent.getStringExtra("USER_ROLE")
        val menu = navView.menu

        // Skrij prijavu i registraciju
        menu.findItem(R.id.nav_login)?.isVisible = false
        menu.findItem(R.id.nav_registration)?.isVisible = false

        // DJ statistika
        menu.findItem(R.id.nav_djstatistics)?.isVisible = userRole == "DJ"

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_my_profile -> {
                    when (userRole) {
                        "DJ" -> {
                            val intent = Intent(this, DJMyProfileActivity::class.java)
                            val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                                .getInt("logged_in_user_id", -1)
                            intent.putExtra("user_id", userId)
                            startActivity(intent)
                        }
                        "Korisnik" -> {
                            Toast.makeText(this, "Dolazi uskoro...", Toast.LENGTH_SHORT).show()
                        }
                    }
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_djstatistics -> {
                    val intent = Intent(this, DJStatisticsActivity::class.java)
                    val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        .getInt("logged_in_user_id", -1) // Dohvati userId
                    intent.putExtra("user_id", userId) // Proslijedi userId

                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_addgigs -> {
                    val intent = Intent(this, AddGigsActivity::class.java)
                    val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        .getInt("logged_in_user_id", -1) // Dohvati userId
                    intent.putExtra("user_id", userId) // Proslijedi userId

                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }
}