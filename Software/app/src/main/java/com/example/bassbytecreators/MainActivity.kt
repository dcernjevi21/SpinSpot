package com.example.bassbytecreators

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    //za dodavanja gaže
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
        }
    }

    private fun showDialog() {
        val newAddGigDialogView = LayoutInflater
            .from(this@MainActivity)
            .inflate(R.layout.add_djgig_dialog, null)

        AlertDialog.Builder(this@MainActivity)
            .setView(newAddGigDialogView)
            .setTitle(getString(R.string.add_new_gig))
            .show()

        val dialogHelper = AddGigDialogHelper(newAddGigDialogView)

    }

}