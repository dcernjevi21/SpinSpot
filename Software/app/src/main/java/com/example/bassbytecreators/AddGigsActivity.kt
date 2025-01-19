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

class AddGigsActivity : AppCompatActivity() {
    private var userId: Int = -1

    private lateinit var drawerLayout: DrawerLayout

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
        setContentView(R.layout.activity_add_gigs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "User ID nije pronaden!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        setupNavigationMenu(navigationView)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Close the activity and return to the previous screen
        }

        btnAddGig = findViewById(R.id.fab_add_gig)
        btnAddGig.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val newAddGigDialogView = LayoutInflater
            .from(this@AddGigsActivity)
            .inflate(R.layout.add_djgig_dialog, null)
        val dialogHelper = AddGigDialogHelper(newAddGigDialogView)
        AlertDialog.Builder(this@AddGigsActivity)
            .setView(newAddGigDialogView)
            .setTitle(getString(R.string.add_new_gig))
            .setPositiveButton(getString(R.string.add_new_gig)) { _, _ ->
                val newGig = dialogHelper.buildGig()
                Log.d(
                    "ispis",
                    "Gig Info: Date: ${newGig.gigDate}, Location: ${newGig.location}, Type: ${newGig.gigType}, Name: ${newGig.name}, Start Time: ${newGig.gigStartTime}, Fee: ${newGig.gigFee}"
                )
                txt1 = findViewById(R.id.textView2)
                txt2 = findViewById(R.id.textView3)
                txt3 = findViewById(R.id.textView4)
                txt4 = findViewById(R.id.textView5)
                txt5 = findViewById(R.id.textView6)
                txt6 = findViewById(R.id.textView7)
                txt7 = findViewById(R.id.textView8)
                txt8 = findViewById(R.id.textView9)
                txt1.text = newGig.gigDate
                txt2.text = newGig.location
                txt3.text = newGig.gigType
                txt4.text = newGig.name
                txt5.text = newGig.gigStartTime
                txt6.text = newGig.gigFee.toString()
                txt7.text = newGig.gigEndTime
                txt8.text = newGig.description
            }
            .show()
        dialogHelper.activateDateTimeListeners()
    }

    private fun setupNavigationMenu(navigationView: NavigationView) {
        val menu = navigationView.menu
        menu.findItem(R.id.nav_login)?.isVisible = false
        menu.findItem(R.id.nav_registration)?.isVisible = false
        menu.findItem(R.id.nav_djstatistics)?.isVisible = true

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_my_profile -> {
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