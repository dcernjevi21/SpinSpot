package com.example.bassbytecreators

import BaseActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.bassbytecreators.helpers.RetrofitClient
import com.google.android.material.navigation.NavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : BaseActivity() {
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)

        val deleteAccountLayout = findViewById<LinearLayout>(R.id.llDeleteAccount)
        val logoutLayout = findViewById<LinearLayout>(R.id.llLogout)
        val btnBack = findViewById<Button>(R.id.btnBack)

        deleteAccountLayout.setOnClickListener {
            showDeleteAccountConfirmation()
        }

        logoutLayout.setOnClickListener {
            logout()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Izbriši račun")
            .setMessage("Jeste li sigurni da želite izbrisati račun?")
            .setPositiveButton("Izbriši") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Odustani", null)
            .show()
    }


    private fun deleteAccount() {
        val userId = intent.getIntExtra("user_id", -1)
        Log.d("SettingsActivity", "User ID from intent: $userId")
        val finalUserId = if (userId != -1) {
            userId
        } else {
            getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                .getInt("logged_in_user_id", -1)
        }
        Log.d("SettingsActivity", "Final User ID for deletion: $finalUserId")

        if (finalUserId != -1) {
            RetrofitClient.apiService.deleteUser(finalUserId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    try {
                        val responseBody = response.body()?.string()
                        Log.d("SettingsActivity", "Delete Response: $responseBody")

                        if (response.isSuccessful) {
                            // Clear preferences and logout only after successful deletion
                            getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply()

                            Toast.makeText(this@SettingsActivity, "Račun je uspješno izbrisan", Toast.LENGTH_SHORT).show()

                            // Navigate to login after successful deletion and preference clearing
                            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SettingsActivity", "Delete failed. Error: $errorBody")
                            Toast.makeText(
                                this@SettingsActivity,
                                "Neuspješno brisanje računa. Greška: $errorBody",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("SettingsActivity", "Error processing response: ${e.message}")
                        Toast.makeText(
                            this@SettingsActivity,
                            "Greška prilikom obrade odgovora",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("SettingsActivity", "Network error: ${t.message}")
                    Toast.makeText(
                        this@SettingsActivity,
                        "Greška kod spajanja na server!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(this, "user_id nije pronađen.", Toast.LENGTH_LONG).show()
        }
    }


    private fun logout() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
