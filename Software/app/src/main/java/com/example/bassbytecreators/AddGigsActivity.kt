package com.example.bassbytecreators

import BaseActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.example.bassbytecreators.api.RetrofitClient
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

class AddGigsActivity : BaseActivity() {
    private var userId: Int = -1

    private lateinit var navView: NavigationView
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

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)

        setupNavigationDrawer(navView)

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

                val tvDate = findViewById<TextView>(R.id.textViewDate)
                val tvLocation = findViewById<TextView>(R.id.textViewLocation)
                val tvGigType = findViewById<TextView>(R.id.textViewGigType)
                val tvGigHost = findViewById<TextView>(R.id.textViewGigHost)
                val tvStartTime = findViewById<TextView>(R.id.textViewStartTime)
                val tvEndTime = findViewById<TextView>(R.id.textViewEndTime)
                val tvGigFee = findViewById<TextView>(R.id.textViewGigFee)
                val tvDescription = findViewById<TextView>(R.id.textViewDescription)

// Postavi tekst u TextView-ove s opisima i vrednostima iz newGig
                tvDate.text = "Datum: ${newGig.gigDate}"
                tvLocation.text = "Lokacija: ${newGig.location}"
                tvGigType.text = "Tip: ${newGig.gigType}"
                tvGigHost.text = "Host: ${newGig.name}"
                tvStartTime.text = "Početak: ${newGig.gigStartTime}"
                tvEndTime.text = "Kraj: ${newGig.gigEndTime}"
                tvGigFee.text = "Naknada: ${newGig.gigFee} €"
                tvDescription.text = "Opis: ${newGig.description}"


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
}