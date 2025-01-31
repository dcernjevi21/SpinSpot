package com.example.bassbytecreators

import BaseActivity
import CalendarFragment
import DJGigWorker
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .getInt("logged_in_user_id", -1)
        Log.d("MainActivity", "User ID za slanje notifikacija: ${userId}")
        //scheduleNotificationWorker(userId)

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CalendarFragment())
            .commit()
    }
    }

    private fun scheduleNotificationWorker(userId: Int) {
        //val workManager = WorkManager.getInstance(applicationContext)

        val workRequest = PeriodicWorkRequestBuilder<DJGigWorker>(1, TimeUnit.DAYS)
            .setInputData(workDataOf("userId" to userId))
            .build()

        //workManager.enqueueUniquePeriodicWork(
        //    "DJGigNotifications",
        //    ExistingPeriodicWorkPolicy.UPDATE,
        //    workRequest
        //)
    }

