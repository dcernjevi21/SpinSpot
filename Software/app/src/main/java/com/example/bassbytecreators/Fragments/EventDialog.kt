import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.EventsDates

class EventDialog(
    private val date: String, // Datum koji se prosljeđuje
    private val events: List<EventsDates> // Eventovi za prikaz
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_event_list, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEvents)
        val buttonClose = view.findViewById<Button>(R.id.buttonClose)

        // Postavi RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = EventAdapter(events)

        // Postavi gumb za zatvaranje
        buttonClose.setOnClickListener {
            dismiss() // Zatvori dialog
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Eventovi za $date")
            .setView(view)
            .create()
    }
}