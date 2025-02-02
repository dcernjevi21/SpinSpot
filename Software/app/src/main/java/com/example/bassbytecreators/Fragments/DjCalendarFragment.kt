package com.example.bassbytecreators.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.bassbytecreators.R
import com.example.bassbytecreators.api.RetrofitClient
import com.example.bassbytecreators.entities.DJGig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DjCalendarFragment: Fragment() {
    private lateinit var calendarView: CalendarView
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    companion object {
        private const val ARG_ID = "fragment_id"

        fun newInstance(id: String): DjCalendarFragment {
            return DjCalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Retrofit", "Upalio se calendar fragment")
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView = view.findViewById(R.id.calendarView)
        Log.d("Retrofit", "Došli smo do retrofita1")
        //fetchAndDisplayDates()
    }

    public fun fetchAndDisplayDates(id: String){
        RetrofitClient.apiService.getGigsSync(id.toInt()).enqueue(object:
            Callback<List<DJGig>> {
            override fun onResponse(call: Call<List<DJGig>>, response: Response<List<DJGig>>) {
                val gigs = response.body() as List<DJGig>
                val datumi: List<String> = (gigs.map { it.gigDate })
                Log.d("Datumi su", datumi.toString())
                val dates = parseDates(datumi)
                val events = createEvents(dates)
                calendarView.setEvents(events)
            }

            override fun onFailure(call: Call<List<DJGig>>, t: Throwable) {
                Log.d("Greska", "Doslo je do greske u DjCalendarFragment")
            }
        })
    }
    private fun parseDates(dateStrings: List<String>): List<Calendar> {
        return dateStrings.mapNotNull { dateStr ->
            try {
                val date = dateFormat.parse(dateStr)
                Calendar.getInstance().apply { time = date }
            } catch (e: Exception) {
                null // Ignoriši nevalidne datume
            }
        }
    }

    private fun createEvents(dates: List<Calendar>): List<EventDay> {
        val indicator = ContextCompat.getDrawable(requireContext(), R.drawable.calendar_dot_indicator)
        return dates.map { calendar ->
            EventDay(calendar, indicator!!).apply {
                // Opcija za bojanje cijelog dana
                EventDay(calendar, R.color.purple_500)
            }
        }
    }


}