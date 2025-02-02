package com.example.bassbytecreators.Fragments

import BaseActivity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.example.bassbytecreators.R
import com.google.android.material.navigation.NavigationView

class OrderSongDjDetail : BaseActivity() {
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val OrderSongButton = findViewById<Button>(R.id.btnOrderSong)
        setContentView(R.layout.order_song_dj_details)
        val djName = intent.getStringExtra("DJ_NAME")
        val djGenre = intent.getStringExtra("DJ_GENRE")
        var djId = intent.getStringExtra("DJ_ID")
        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)
        OrderSongButton.setOnClickListener {
            ShowOrderSongDialog()
        }
    }

    private fun ShowOrderSongDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.order_song_popup)
        dialog.setCancelable(true)
        val btnConfirm: Button = dialog.findViewById(R.id.btnConfirmOrder)
        btnConfirm.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}