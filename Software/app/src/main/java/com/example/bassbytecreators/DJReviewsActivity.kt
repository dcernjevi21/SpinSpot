package com.example.bassbytecreators

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.adapters.ReviewAdapter
import com.example.bassbytecreators.entities.Review
import com.example.bassbytecreators.helpers.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DJReviewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_djreviews)

        val recyclerViewReviews = findViewById<RecyclerView>(R.id.recyclerViewReviews)
        recyclerViewReviews.layoutManager = LinearLayoutManager(this)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Close the activity and return to the previous screen
        }

        // Get DJ ID from Intent
        val djId = intent.getIntExtra("dj_id", 1)

        if (djId != -1) {
            fetchReviews(djId, recyclerViewReviews)
        } else {
            Toast.makeText(this, "Greška: dj_id nije pronađen!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun fetchReviews(djId: Int, recyclerView: RecyclerView) {
        RetrofitClient.apiService.fetchReviews(djId).enqueue(object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    val reviews = response.body() ?: emptyList()
                    recyclerView.adapter = ReviewAdapter(reviews)
                } else {
                    Log.e("DJReviewsActivity", "Error fetching reviews: ${response.message()}")
                    Toast.makeText(this@DJReviewsActivity, "Greška kod dohvaćanja recenzija.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e("DJReviewsActivity", "Error: ${t.message}")
                Toast.makeText(this@DJReviewsActivity, "Greška kod spajanja na server.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
