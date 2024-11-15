package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        val users = mutableListOf<User>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEditText = findViewById<EditText>(R.id.etEmailPrijava)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordPrijava)
        val loginButton = findViewById<Button>(R.id.btnPrijava)
        val registerLink = findViewById<TextView>(R.id.tvRegistracijaLink)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val user = users.find { it.email == email && it.password == password }
            if (user != null) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Uspješna prijava!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Neispravni podaci!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
