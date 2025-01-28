package com.example.bassbytecreators

import BaseActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationView

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val videoView: VideoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.intro_video
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)
        videoView.setOnCompletionListener {
            // Prelazak na LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Zatvori SplashActivity
        }

        videoView.start()
    }
}