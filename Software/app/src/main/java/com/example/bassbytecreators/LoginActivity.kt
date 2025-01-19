package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bassbytecreators.entities.User
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    companion object {
        val users = mutableListOf<User>()
    }

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Već ste na ekranu za prijavu.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                R.id.nav_registration -> {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                }
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_layout)) { v, insets ->
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

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Molimo unesite sve podatke.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
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
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
