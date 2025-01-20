package com.example.bassbytecreators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bassbytecreators.entities.User
import com.example.bassbytecreators.helpers.RetrofitClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        val menu = navigationView.menu
        menu.findItem(R.id.nav_my_profile)?.isVisible = false
        menu.findItem(R.id.nav_djstatistics)?.isVisible = false
        menu.findItem(R.id.nav_main)?.isVisible = false
        menu.findItem(R.id.nav_addgigs)?.isVisible = false
        menu.findItem(R.id.nav_search)?.isVisible = false

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Već ste na ekranu za prijavu.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.nav_registration -> {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }


        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameEditText = findViewById<EditText>(R.id.etUsernamePrijava)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordPrijava)
        val loginButton = findViewById<Button>(R.id.btnPrijava)
        val registerLink = findViewById<TextView>(R.id.tvRegistracijaLink)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Molimo unesite sve podatke.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                loginUser(username, password)
            }
        }


        registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        RetrofitClient.apiService.loginUser("login",username, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Dobrodošli, ${user.username}! Ulogirani ste kao ${user.role}.",
                            Snackbar.LENGTH_LONG
                        ).show()

                        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit()
                            .putInt("logged_in_user_id", user.user_id)
                            .apply()

                        when (user.role) {
                            "DJ" -> {
                                navigateToMainActivity("DJ")
                            }
                            "Korisnik" -> {
                                navigateToMainActivity("Korisnik")
                            }
                            else -> {
                                Log.e("LoginActivity", "Unknown role: ${user.role}")
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Neispravni podaci: ${user.role}",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Neispravni podaci!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Greška: ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Greška kod spajanja na server.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun navigateToMainActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_ROLE", role)
        }
        startActivity(intent)
        finish()
    }
}
