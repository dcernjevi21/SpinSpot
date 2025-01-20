package com.example.bassbytecreators

import DJGigWorker
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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit
import com.google.android.material.snackbar.Snackbar

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

        scheduleNotificationWorker()

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

        menu.findItem(R.id.nav_djstatistics)?.isVisible = userRole == "DJ"
        menu.findItem(R.id.nav_addgigs)?.isVisible = userRole == "DJ"

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
                            val intent = Intent(this, UserMyProfileActivity::class.java)
                            val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                                .getInt("logged_in_user_id", -1)
                            intent.putExtra("user_id", userId)
                            startActivity(intent)
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
                R.id.nav_main -> {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Već jeste na početnom ekranu.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun scheduleNotificationWorker() {
        val workManager = WorkManager.getInstance(applicationContext)

        val workRequest = PeriodicWorkRequestBuilder<DJGigWorker>(1, TimeUnit.DAYS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "DJGigNotifications",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}