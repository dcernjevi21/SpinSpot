package com.example.bassbytecreators.Fragments

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bassbytecreators.R
import com.example.bassbytecreators.R.layout.activity_main

class DjSearch : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dj_search_layout)
    }
}