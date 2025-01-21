package com.example.bassbytecreators

import BaseActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.example.bassbytecreators.entities.Review
import com.example.bassbytecreators.api.RetrofitClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewActivity : BaseActivity() {
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val etOpis = findViewById<EditText>(R.id.etReviewOpis)
        val btnSubmitReview = findViewById<Button>(R.id.btnSubmitReview)

        val djId = intent.getIntExtra("dj_id", -1)
        val userId = intent.getIntExtra("user_id", -1)
        Log.d("ReviewActivity", "dj_id: $djId, user_id: $userId")

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)

        btnSubmitReview.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val opis = etOpis.text.toString().trim()

            if (rating > 0 && opis.isNotEmpty()) {
                submitReview(rating, opis, userId, djId)
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Molimo unesite zvjezdice i recenziju!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun submitReview(rating: Int, opis: String, userId: Int, djId: Int) {
        val review = Review(0, rating, opis, userId, djId)
        RetrofitClient.apiService.submitReview(review).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Recenzija uspješno objavljena!",
                        Snackbar.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Greška kod objave recenzije: ${response.message()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Greška kod spajanja na server.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}
