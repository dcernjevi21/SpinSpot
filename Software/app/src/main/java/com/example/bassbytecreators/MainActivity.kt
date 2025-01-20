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

class MainActivity : AppCompatActivity() {
    private var userRole: String? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var btnAddGig: FloatingActionButton
    private lateinit var txt1: TextView
    private lateinit var txt2: TextView
    private lateinit var txt3: TextView
    private lateinit var txt4: TextView
    private lateinit var txt5: TextView
    private lateinit var txt6: TextView
    private lateinit var txt7: TextView
    private lateinit var txt8: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .getInt("logged_in_user_id", -1)
        Log.d("MainActivity", "User ID za slanje notifikacija: ${userId}")
        scheduleNotificationWorker(userId)

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
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun scheduleNotificationWorker(userId: Int) {
        val workManager = WorkManager.getInstance(applicationContext)

        val workRequest = PeriodicWorkRequestBuilder<DJGigWorker>(1, TimeUnit.DAYS)
            .setInputData(workDataOf("userId" to userId))
            .build()

        workManager.enqueueUniquePeriodicWork(
            "DJGigNotifications",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}