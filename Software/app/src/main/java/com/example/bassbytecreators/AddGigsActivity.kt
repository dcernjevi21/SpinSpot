package com.example.bassbytecreators

import android.annotation.SuppressLint
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
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.example.bassbytecreators.helpers.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AddGigsActivity : AppCompatActivity() {
    private var userId: Int = -1

    private lateinit var drawerLayout: DrawerLayout
    private var sdfDate  = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private var sdfDate2 = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private lateinit var fabAddGig: FloatingActionButton
    private lateinit var txt1: TextView
    private lateinit var txt2: TextView
    private lateinit var txt3: TextView
    private lateinit var txt4: TextView
    private lateinit var txt5: TextView
    private lateinit var txt6: TextView
    private lateinit var txt7: TextView
    private lateinit var txt10: TextView

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
            finish()
        }

        fabAddGig = findViewById(R.id.fab_add_gig)
        fabAddGig.setOnClickListener {
            showDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog() {
        val newAddGigDialogView = LayoutInflater
            .from(this@AddGigsActivity)
            .inflate(R.layout.add_djgig_dialog, null)
        val dialogHelper = AddGigDialogHelper(newAddGigDialogView)
        AlertDialog.Builder(this@AddGigsActivity)
            .setView(newAddGigDialogView)
            .setTitle(getString(R.string.add_new_gig))
            .setPositiveButton(getString(R.string.add_new_gig)) { _, _ ->
                var newGig = dialogHelper.buildGig()

                //za provjeru na zaslonu
                txt1 = findViewById(R.id.textView2)
                txt2 = findViewById(R.id.textView3)
                txt3 = findViewById(R.id.textView4)
                txt4 = findViewById(R.id.textView5)
                txt5 = findViewById(R.id.textView6)
                txt10 = findViewById(R.id.textView10) //kraj
                txt6 = findViewById(R.id.textView7)
                txt7 = findViewById(R.id.textView8) //opis
                txt1.text = "${txt1.text}: ${newGig.gigDate}"
                txt2.text = "${txt2.text}: ${newGig.location}"
                txt3.text = "${txt3.text}: ${newGig.gigType}"
                txt4.text = "${txt4.text}: ${newGig.name}"
                txt5.text = "${txt5.text}: ${newGig.gigStartTime}"
                txt10.text = "${txt10.text}: ${newGig.gigEndTime}"
                txt6.text = "${txt6.text}: ${newGig.gigFee.toString()}"
                txt7.text = "${txt7.text}: ${newGig.description}"


                try {
                    val parsedDate = sdfDate.parse(newGig.gigDate)
                    val formattedDate = sdfDate2.format(parsedDate!!)
                    newGig.gigDate = formattedDate
                } catch (e: ParseException) {
                    Log.e("AddGigs", "Greška pri parsiranju datuma: ${e.message}")
                }
                addNewGig(newGig, userId)
            }
            .show()
        dialogHelper.activateDateTimeListeners()
    }

    private fun addNewGig(newGig: DJGig, userId: Int) {
        Log.d(
            "AddGigs",
            "GigActivity: Date: ${newGig.gigDate}, Location: ${newGig.location}, Type: ${newGig.gigType}, Name: ${newGig.name}, Start Time: ${newGig.gigStartTime}, End Time: ${newGig.gigEndTime}, Fee: ${newGig.gigFee}, Description: ${newGig.description}"
        )
        RetrofitClient.apiService.addNewGig(newGig, userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("AddGigs", "Response: ${response.body()?.string()}")
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Uspješno dodavanje gaža!",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Log.e("AddGigs", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Greška kod dodavanje gaža: ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("AddGigs", "Connection error: ${t.message}")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Greška kod spajanja na server.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun setupNavigationMenu(navigationView: NavigationView) {
        val menu = navigationView.menu
        menu.findItem(R.id.nav_login)?.isVisible = false
        menu.findItem(R.id.nav_registration)?.isVisible = false
        menu.findItem(R.id.nav_djstatistics)?.isVisible = true
        menu.findItem(R.id.nav_addgigs)?.isVisible = false

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
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }

                else -> false
            }
        }
    }
}