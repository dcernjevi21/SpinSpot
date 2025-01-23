package com.example.bassbytecreators.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CalendarView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.bassbytecreators.R
import com.example.bassbytecreators.ReviewActivity
import com.example.bassbytecreators.api.RetrofitClient
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.concurrent.TimeUnit
import BaseActivity
import DJGigWorker
import GigAdapter


class DjDetailsFragment : Fragment() {

    private lateinit var navView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var gigAdapter: GigAdapter
    private var id_od_dja =  ""
    private lateinit var calendar: CalendarView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dj_details, container, false)


        recyclerView = view.findViewById(R.id.gigsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // Dobivanje podataka iz Intenta
        val btnChooseMonth: Button = view.findViewById<Button>(R.id.btnChooseMonth)
        val btnReviewDJ: Button = view.findViewById<Button>(R.id.btnReviewDJ)
        gigAdapter = GigAdapter(emptyList())
        val djName = intent.getStringExtra("DJ_NAME")
        val djGenre = intent.getStringExtra("DJ_GENRE")
        var djId = intent.getStringExtra("DJ_ID")
        id_od_dja = intent.getStringExtra("DJ_ID").toString()
        val dj_id = intent.getStringExtra("DJ_ID")?.toIntOrNull() ?: -1
        Log.d("DJ ID u detaljima", djId.toString())
        val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .getInt("logged_in_user_id", -1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
        scheduleNotificationWorker(userId)

        btnReviewDJ.setOnClickListener {
            Log.d("DJDetailActivity", "dj_id: $djId, user_id: $userId")
            if(dj_id != userId)
            {
                val intent = Intent(requireContext(), ReviewActivity::class.java)
                intent.putExtra("dj_id", dj_id)
                intent.putExtra("user_id", userId)
                startActivity(intent)
            }
            else
            {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Ne možete ocijeniti samog sebe.",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }
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

                    view.findViewById<TextView>(R.id.djName).text = dj?.dj_name
                    view.findViewById<TextView>(R.id.djGenre).text = dj?.genres
                    view.findViewById<TextView>(R.id.djBiography).text = dj?.biography
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
        view.findViewById<TextView>(R.id.djName).text = djName
        view.findViewById<TextView>(R.id.djGenre).text = djGenre

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMonthPickerDialog() {
        // Stvaranje dijaloga
        val dialog = Dialog(requireContext())
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
    private fun scheduleNotificationWorker(userId: Int) {
        val workManager = WorkManager.getInstance(requireContext())

        val workRequest = PeriodicWorkRequestBuilder<DJGigWorker>(1, TimeUnit.DAYS)
            .setInputData(workDataOf("userId" to userId))
            .build()

        workManager.enqueueUniquePeriodicWork(
            "DJGigNotifications",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}