package com.example.bassbytecreators.Fragments

import GigAdapter
import com.example.bassbytecreators.R
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.helpers.RetrofitClient
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Date

class DJDetailActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var gigAdapter: GigAdapter
    private lateinit var calendar: CalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dj_detail)
        recyclerView = findViewById(R.id.gigsRecyclerView)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Dobivanje podataka iz I  ntenta
        calendar = findViewById(R.id.calendarView)

        val djName = intent.getStringExtra("DJ_NAME")
        val djGenre = intent.getStringExtra("DJ_GENRE")
        val djId = intent.getStringExtra("DJ_ID")
        Log.d("DJ ID u detaljima", djId.toString())
        RetrofitClient.apiService.getDj(djId.toString()).enqueue(object : Callback<List<DJperson>> {

            override fun onResponse(
                call: Call<List<DJperson>>,
                response: Response<List<DJperson>>
            ) {
                Log.d("Dj osoba", response.body().toString())
                if (response.isSuccessful) {
                    val dj = response.body()?.get(0)
                    findViewById<TextView>(R.id.djName).text = dj?.dj_name
                    findViewById<TextView>(R.id.djGenre).text = dj?.genres
                    findViewById<TextView>(R.id.djBiography).text = dj?.biography

                }
            }

            override fun onFailure(call: Call<List<DJperson>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        // Postavljanje podataka u tekstualne prikaze
        findViewById<TextView>(R.id.djName).text = djName
        findViewById<TextView>(R.id.djGenre).text = djGenre
    }

    private fun fetchUpcomingGigs(djId: Int) {
        RetrofitClient.apiService.getUpcomingGigs(djId).enqueue(object : Callback<List<DJGig>> {
            override fun onResponse(call: Call<List<DJGig>>, response: Response<List<DJGig>>) {
                if (response.isSuccessful) {
                    val gigs = response.body() ?: emptyList()
                    gigAdapter = GigAdapter(gigs)
                    recyclerView.adapter = gigAdapter
                } else {
                    //Toast.makeText(this@UpcomingGigsActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DJGig>>, t: Throwable) {
                //Toast.makeText(this@UpcomingGigsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}