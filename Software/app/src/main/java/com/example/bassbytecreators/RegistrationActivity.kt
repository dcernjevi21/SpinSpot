package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registration_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEditText = findViewById<EditText>(R.id.etEmailRegister)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordRegister)
        val userTypeSpinner = findViewById<Spinner>(R.id.spinnerUserType)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val userType = userTypeSpinner.selectedItem.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val newUser = User(email, password, userType)
                MainActivity.users.add(newUser)
                Toast.makeText(this, "Registracija uspješna!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Molimo ispunite sva polja", Toast.LENGTH_SHORT).show()
            }
        }

        val loginLink = findViewById<TextView>(R.id.tvLoginLink)
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
