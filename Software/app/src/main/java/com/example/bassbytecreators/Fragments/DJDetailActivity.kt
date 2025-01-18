package com.example.bassbytecreators.Fragments

import com.example.bassbytecreators.R
import android.widget.TextView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DJDetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dj_detail)

        // Dobivanje podataka iz Intenta
        val djName = intent.getStringExtra("DJ_NAME")
        val djGenre = intent.getStringExtra("DJ_GENRE")

        // Postavljanje podataka u tekstualne prikaze
        findViewById<TextView>(R.id.djNameTextView).text = djName
        findViewById<TextView>(R.id.djGenreTextView).text = djGenre
    }
}