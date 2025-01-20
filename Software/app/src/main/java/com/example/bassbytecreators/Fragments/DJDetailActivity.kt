package com.example.bassbytecreators.Fragments

import GigAdapter
import android.app.Dialog
import android.os.Build
import com.example.bassbytecreators.R
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.CalendarView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.Dialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.helpers.RetrofitClient
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

class DJDetailActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var gigAdapter: GigAdapter
    private var id_od_dja =  ""
    private lateinit var calendar: CalendarView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dj_detail)
        recyclerView = findViewById(R.id.gigsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Dobivanje podataka iz I  ntenta
        val btnChooseMonth: Button = findViewById<Button>(R.id.btnChooseMonth)
        gigAdapter = GigAdapter(emptyList())
        val djName = intent.getStringExtra("DJ_NAME")
        val djGenre = intent.getStringExtra("DJ_GENRE")
        var djId = intent.getStringExtra("DJ_ID")
        id_od_dja = intent.getStringExtra("DJ_ID").toString()
        Log.d("DJ ID u detaljima", djId.toString())
        btnChooseMonth.setOnClickListener {
            showMonthPickerDialog()
        }
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
                    var id = dj?.user_id
                    djId = id.toString()
                    if (id != null) {
                        Log.d("DJ id za gažu je", id.toString())
                        fetchUpcomingGigs(id)
                    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMonthPickerDialog() {
        // Stvaranje dijaloga
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_month_picker)
        dialog.setCancelable(true)

        val numberPicker: NumberPicker = dialog.findViewById(R.id.numberPicker)
        val btnConfirm: Button = dialog.findViewById(R.id.btnConfirm)

        // Postavljanje granica za NumberPicker (1 - 12)
        numberPicker.minValue = 1
        numberPicker.maxValue = 12
        numberPicker.wrapSelectorWheel = true

        // Klik na potvrdu
        btnConfirm.setOnClickListener {
            val selectedMonth = numberPicker.value
            dialog.dismiss() // Zatvori dijalog

            // Pozovi funkciju za dohvat gaža
            dohvatiGazeZaTajMjesec(selectedMonth)
        }

        // Prikaz dijaloga
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dohvatiGazeZaTajMjesec(month: Int) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val (prviDan, ZadnjiDan) = PrviIzadnjiDanUMjesecu(currentYear, month)
        Log.d("prvi dan", prviDan.toString())
        Log.d("zadnji dan", ZadnjiDan.toString())
        Log.d("Dj id za gažu", id_od_dja)
        RetrofitClient.apiService.getUpcomingGigsInMonth(
            prviDan.toString(),
            ZadnjiDan.toString(),id_od_dja).enqueue(object: Callback<List<DJGig>>{
            override fun onResponse(call: Call<List<DJGig>>, response: Response<List<DJGig>>) {
                val gigs = response.body() ?: emptyList()

                gigAdapter.updateList(gigs)
                Log.d("Upcoming gigs that month", gigAdapter.gigs.toString())
                recyclerView.adapter = gigAdapter
            }

            override fun onFailure(call: Call<List<DJGig>>, t: Throwable) {
                Log.d("Error", "nije ucitao Gaze")
            }
        })

    }

    private fun fetchUpcomingGigs(djId: Int) {
        RetrofitClient.apiService.getUpcomingGigs(djId).enqueue(object : Callback<List<DJGig>> {
            override fun onResponse(call: Call<List<DJGig>>, response: Response<List<DJGig>>) {
                if (response.isSuccessful) {
                    val gigs = response.body() ?: emptyList()

                    gigAdapter.updateList(gigs)
                    Log.d("Upcoming gigs", gigAdapter.gigs.toString())
                    recyclerView.adapter = gigAdapter

                } else {
                    //Toast.makeText("Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DJGig>>, t: Throwable) {
                //Toast.makeText(this@UpcomingGigsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun PrviIzadnjiDanUMjesecu(year: Int, month: Int): Pair<LocalDate, LocalDate> {
        val yearMonth = YearMonth.of(year, month)
        val firstDay = yearMonth.atDay(1)
        val lastDay = yearMonth.atEndOfMonth()
        return Pair(firstDay, lastDay)
    }

}