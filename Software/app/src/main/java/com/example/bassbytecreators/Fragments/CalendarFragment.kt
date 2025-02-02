// CalendarFragment.kt
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.bassbytecreators.R
import com.example.bassbytecreators.api.RetrofitClient
import com.example.bassbytecreators.api.RetrofitClient.apiService
import com.example.bassbytecreators.entities.DJGig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


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
        fetchAndDisplayDates()
    }

    private fun fetchAndDisplayDates()  {
                Log.d("Retrofit", "Došli smo do retrofita")
                RetrofitClient.apiService.getAllGigs().enqueue(object: Callback<List<DJGig>>{
                    override fun onResponse(
                        call: Call<List<DJGig>>,
                        response: Response<List<DJGig>>
                    ) {
                        Log.d("Zahtjev", response.body().toString())
                        val gigs = response.body() as List<DJGig>
                        val datumi: List<String> = (gigs.map { it.gigDate })
                        Log.d("Datumi su", datumi.toString())
                        val dates = parseDates(datumi)
                        val events = createEvents(dates)
                        calendarView.setEvents(events)
                    }

                    override fun onFailure(call: Call<List<DJGig>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                } )
                // Zamijeni sa stvarnim API pozivom
                //val dates = parseDates(response.body() ?: emptyList()) // Prilagodi prema stvarnom API odgovoru
                //val events = createEvents(dates)
                //calendarView.setEvents(events)


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
                EventDay(calendar, R.color.purple_501)
            }
        }
    }
}