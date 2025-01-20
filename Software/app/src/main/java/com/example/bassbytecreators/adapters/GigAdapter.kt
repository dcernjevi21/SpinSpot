import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.DJGig

class GigAdapter(public var gigs: List<DJGig>) : RecyclerView.Adapter<GigAdapter.GigViewHolder>() {

    inner class GigViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GigViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gig_item_layout, parent, false)
        return GigViewHolder(view)
    }

    override fun onBindViewHolder(holder: GigViewHolder, position: Int) {
        val gig = gigs[position]
        holder.tvLocation.text = gig.location
        holder.tvDate.text = gig.gigDate
        holder.tvPrice.text = "$${gig.gigFee}"
    }

    fun updateList(newList: List<DJGig>) {
        gigs = newList
        Log.d("azurirana lista je", newList.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = gigs.size
}
