package com.example.bassbytecreators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bassbytecreators.helpers.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val deleteAccountLayout = findViewById<LinearLayout>(R.id.llDeleteAccount)
        val logoutLayout = findViewById<LinearLayout>(R.id.llLogout)

        deleteAccountLayout.setOnClickListener {
            showDeleteAccountConfirmation()
        }

        logoutLayout.setOnClickListener {
            logout()
        }
    }

    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Izbriši račun")
            .setMessage("Jeste li sigurni da želite obrisati račun?")
            .setPositiveButton("Obriši") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Odustani", null)
            .show()
    }

    private fun deleteAccount() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("logged_in_user_id", -1)

        if (userId != -1) {
            RetrofitClient.apiService.deleteUser(userId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        sharedPreferences.edit().clear().apply() // Clear saved user data
                        val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Nepoznata greška"
                        Toast.makeText(
                            this@SettingsActivity,
                            "Neuspješno brisanje računa. Greška: $errorMessage",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Greška")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
