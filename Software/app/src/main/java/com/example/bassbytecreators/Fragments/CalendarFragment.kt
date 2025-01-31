import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applandeo.materialcalendarview.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.example.bassbytecreators.R
import com.example.bassbytecreators.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Calendar

class CalendarFragment : Fragment() {
    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView = view.findViewById(R.id.calendarView)
        fetchDatesFromApi()
    }

    private fun fetchDatesFromApi() {
        try {
            RetrofitClient.apiService.getAllGigsDates().enqueue(object: Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    Log.d("Dates", response.body().toString())
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
            //val dates = ApiService.getDates() // Pretpostavka: API vraća List<String>
            //val calendarDays = parseDatesToCalendarDays(dates)
            //addDecorator(calendarDays)
        } catch (e: Exception) {
            //Toast.makeText(this@CalendarActivity, "Greška pri dohvatu podataka", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDatesToEventDays(dates: List<String>): List<EventDay> {
        return dates.mapNotNull { dateStr ->
            try {
                val localDate = LocalDate.parse(dateStr)

                // Stvori Calendar objekt i postavi datum
                val calendar = Calendar.getInstance().apply {
                    set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)
                }

                EventDay(
                    calendar,        // Prvi parametar: Calendar objekt
                    R.drawable.dot_icon  // Drugi parametar: resurs ID ikone
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun updateCalendar(eventDays: List<EventDay>) {
        calendarView.setEvents(eventDays)
    }
}