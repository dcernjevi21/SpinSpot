package com.example.bassbytecreators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
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


        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer()

        val intent = Intent(this, SearchActivity::class.java)
        val gumbic: Button = findViewById(R.id.button)
        gumbic.setOnClickListener {
            startActivity(intent)
        }

        btnAddGig = findViewById(R.id.fab_add_gig)
        btnAddGig.setOnClickListener {
            showDialog()
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
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDialog() {
        val newAddGigDialogView = LayoutInflater
            .from(this@MainActivity)
            .inflate(R.layout.add_djgig_dialog, null)
        val dialogHelper = AddGigDialogHelper(newAddGigDialogView)
        AlertDialog.Builder(this@MainActivity)
            .setView(newAddGigDialogView)
            .setTitle(getString(R.string.add_new_gig))
            .setPositiveButton(getString(R.string.add_new_gig)) { _, _ ->
                val newGig = dialogHelper.buildGig()
                Log.d("ispis", "Gig Info: Date: ${newGig.gigDate}, Location: ${newGig.location}, Type: ${newGig.gigType}, Name: ${newGig.name}, Start Time: ${newGig.gigStartTime}, Fee: ${newGig.gigFee}")
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
}