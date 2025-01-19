package com.example.bassbytecreators

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bassbytecreators.entities.User
import com.example.bassbytecreators.helpers.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registration_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameEditText = findViewById<EditText>(R.id.etImePrezime)
        val userNameEditText = findViewById<EditText>(R.id.etUserName)
        val emailEditText = findViewById<EditText>(R.id.etEmailRegister)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordRegister)
        val userTypeSpinner = findViewById<Spinner>(R.id.spinnerUserType)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val userName = userNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val role = userTypeSpinner.selectedItem.toString()

            if (name.isNotEmpty() && userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role != "Odaberi ulogu") {
                val newUser = User(0, userName, name, email, password, role) // user_id is 0 as it will be generated by the server
                registerUser(newUser)
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Molimo ispunite sva polja!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        val loginLink = findViewById<TextView>(R.id.tvLoginLink)
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(user: User) {
        RetrofitClient.apiService.registerUser(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RegistrationActivity", "User registered successfully!")
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Uspješna registracija!",
                        Snackbar.LENGTH_LONG
                    ).show()

                    // Navigate to LoginActivity
                    val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("RegistrationActivity", "Error: ${response.message()}")
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Greška kod registracije: ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("RegistrationActivity", "Connection error: ${t.message}")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Greška kod spajanja na server.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}
