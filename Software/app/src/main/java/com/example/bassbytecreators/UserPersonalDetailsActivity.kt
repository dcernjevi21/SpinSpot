package com.example.bassbytecreators

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bassbytecreators.entities.User
import com.example.bassbytecreators.ws.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPersonalDetailsActivity : AppCompatActivity() {
    private var userId: Int = -1
    private lateinit var etUsername: EditText
    private lateinit var etFirstLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_personal_details)

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initializeViews()
        setupClickListeners()
        loadUserData()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etFirstLastName = findViewById(R.id.etFirstLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupClickListeners() {
        btnSaveChanges.setOnClickListener {
            updateUserData()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadUserData() {
        RetrofitClient.apiService.getUser(action = "get_user", userId = userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        etUsername.setText(user.username)
                        etFirstLastName.setText(user.first_last_name)
                        etEmail.setText(user.email)
                    }
                } else {
                    Toast.makeText(
                        this@UserPersonalDetailsActivity,
                        "Greška prilikom dohvaćanja podataka",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    this@UserPersonalDetailsActivity,
                    "Greška prilikom povezivanja sa serverom",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateUserData() {
        val username = etUsername.text.toString().trim()
        val firstLastName = etFirstLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty() || firstLastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Molimo popunite sva obavezna polja", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = User(
            user_id = userId,
            username = username,
            first_last_name = firstLastName,
            email = email,
            password = if (password.isNotEmpty()) password else null,
            role = "Korisnik"
        )

        RetrofitClient.apiService.updateUser(updatedUser).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@UserPersonalDetailsActivity,
                        "Podaci uspješno ažurirani",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@UserPersonalDetailsActivity,
                        "Greška prilikom ažuriranja podataka",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@UserPersonalDetailsActivity,
                    "Greška prilikom povezivanja sa serverom",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}