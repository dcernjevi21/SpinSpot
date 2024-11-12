package com.example.bassbytecreators

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Console

class MainActivity : AppCompatActivity() {

    //za dodavanje gaže
    private lateinit var btnAddGig: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //za dodavanje gaže
        btnAddGig = findViewById(R.id.fab_add_gig)
        btnAddGig.setOnClickListener {
            showDialog()
            Log.i("ispis","test")
        }
    }

    //dijalog za dodavanje gaže
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
                Log.d("ispis","test")
                Log.d("ispis", "Gig Info: Date: ${newGig.gigDate}, Location: ${newGig.location}, Type: ${newGig.gigType}, Name: ${newGig.name}, Start Time: ${newGig.gigStartTime}, Fee: ${newGig.gigFee}")
            }
            .show()
        dialogHelper.activateDateTimeListeners()
    }
}