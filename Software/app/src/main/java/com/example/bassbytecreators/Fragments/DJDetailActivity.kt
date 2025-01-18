package com.example.bassbytecreators.Fragments

import com.example.bassbytecreators.R
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.helpers.RetrofitClient
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DJDetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dj_detail)

        // Dobivanje podataka iz Intenta
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
            }

            override fun onFailure(call: Call<List<DJperson>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        // Postavljanje podataka u tekstualne prikaze
        findViewById<TextView>(R.id.djName).text = djName
        findViewById<TextView>(R.id.djGenre).text = djGenre
    }
}