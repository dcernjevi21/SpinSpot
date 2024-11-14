package com.example.bassbytecreators

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.adapters.SearchAdapter
import com.example.bassbytecreators.entities.DJperson
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bassbytecreators.helpers.AddGigDialogHelper
import com.example.bassbytecreators.helpers.MockDataLoader
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Console

class MainActivity : AppCompatActivity() {
    lateinit var djRecyclerView : RecyclerView
    var djList : List<DJperson> = MockDataLoader().getDemoDJdata()
    lateinit var searchAdapter: SearchAdapter


    //za dodavanje gaže
    private lateinit var btnAddGig: FloatingActionButton
    private lateinit var txt1: TextView
    private lateinit var txt2: TextView
    private lateinit var txt3: TextView
    private lateinit var txt4: TextView
    private lateinit var txt5: TextView
    private lateinit var txt6: TextView

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
                Log.d("ispis", "Gig Info: Date: ${newGig.gigDate}, Location: ${newGig.location}, Type: ${newGig.gigType}, Name: ${newGig.name}, Start Time: ${newGig.gigStartTime}, Fee: ${newGig.gigFee}")
                txt1 = findViewById(R.id.textView2)
                txt2 = findViewById(R.id.textView3)
                txt3 = findViewById(R.id.textView4)
                txt4 = findViewById(R.id.textView5)
                txt5 = findViewById(R.id.textView6)
                txt6 = findViewById(R.id.textView7)
                txt1.text = newGig.gigDate
                txt2.text = newGig.location
                txt3.text = newGig.gigType
                txt4.text = newGig.name
                txt5.text = newGig.gigStartTime
                txt6.text = newGig.gigFee.toString()
            }
            .show()
        dialogHelper.activateDateTimeListeners()
    }
}



