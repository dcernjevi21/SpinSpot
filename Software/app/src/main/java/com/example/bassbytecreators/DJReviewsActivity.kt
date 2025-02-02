package com.example.bassbytecreators

import BaseActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.adapters.ReviewAdapter
import com.example.bassbytecreators.entities.Review
import com.example.bassbytecreators.api.RetrofitClient
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DJReviewsActivity : BaseActivity() {

    private lateinit var navView: NavigationView
    private var djId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_djreviews)

        val recyclerViewReviews = findViewById<RecyclerView>(R.id.recyclerViewReviews)
        recyclerViewReviews.layoutManager = LinearLayoutManager(this)

        djId = intent.getIntExtra("dj_id", 1)
        if (djId != -1) {
            fetchReviews(djId, recyclerViewReviews)
        } else {
            Toast.makeText(this, "Greška: dj_id nije pronađen!", Toast.LENGTH_LONG).show()
            finish()
        }

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
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
