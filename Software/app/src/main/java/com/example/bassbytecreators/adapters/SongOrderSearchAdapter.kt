package com.example.bassbytecreators.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.Fragments.DJDetailActivity
import com.example.bassbytecreators.Fragments.OrderSongDjDetail
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.DJperson

class SongOrderSearchAdapter(private var djList: List<DJperson>): RecyclerView.Adapter<SongOrderSearchAdapter.DJViewHolder>() {
    inner class DJViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivTextView: TextView = view.findViewById(R.id.djName)
        val zanrTextView: TextView = view.findViewById(R.id.djGenre)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongOrderSearchAdapter.DJViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_song_dj_search_item, parent, false)
        return DJViewHolder(view)
    }

    override fun onBindViewHolder(holder: DJViewHolder, position: Int) {
        val dj = djList[position]
        holder.nazivTextView.text = dj.dj_name
        holder.zanrTextView.text = dj.genres
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, OrderSongDjDetail()::class.java)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}