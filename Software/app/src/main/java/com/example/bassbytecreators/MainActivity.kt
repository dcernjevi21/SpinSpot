package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    companion object {
        val users = mutableListOf<User>() // Lista za pohranu korisnika
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
        val registerLink = findViewById<TextView>(R.id.tvPrijavaLink)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = users.find { it.email == email && it.password == password }
            if (user != null) {
                Toast.makeText(this, "Prijava uspješna!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Neispravni podaci!", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
