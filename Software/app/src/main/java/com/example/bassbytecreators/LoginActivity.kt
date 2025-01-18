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
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bassbytecreators.entities.User
import com.example.bassbytecreators.helpers.ApiService
import com.example.bassbytecreators.helpers.RetrofitClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

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
        Log.d("LoginActivity", "Sending login request for username: $username")
        RetrofitClient.apiService.loginUser(username, password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("LoginActivity", "Login successful: ${user.username}, Role: ${user.role}")
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Dobrodošli, ${user.username}! Ulogirani ste kao ${user.role}.",
                            Snackbar.LENGTH_LONG
                        ).show()

                        // Navigate based on the role
                        when (user.role) {
                            "DJ" -> {
                                // Open DJ-specific activity (for now, MainActivity)
                                navigateToMainActivity("DJ")
                            }
                            "Korisnik" -> {
                                // Open Korisnik-specific activity (for now, MainActivity)
                                navigateToMainActivity("Korisnik")
                            }
                            else -> {
                                Log.e("LoginActivity", "Unknown role: ${user.role}")
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Neispravan role: ${user.role}",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Log.e("LoginActivity", "Invalid credentials")
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Neispravni podaci!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.e("LoginActivity", "Response error: ${response.message()}")
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Greška: ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("LoginActivity", "Connection error: ${t.message}")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Greška kod spajanja na server.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    // Function to navigate to MainActivity (you can customize this for role-specific activities later)
    private fun navigateToMainActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_ROLE", role) // Pass the role to MainActivity
        }
        startActivity(intent)
        finish()
    }
}
