package com.example.bassbytecreators.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.R
import com.example.bassbytecreators.adapters.SearchAdapter.SearchViewHolder
import com.example.bassbytecreators.entities.DJperson

class SearchAdapter(private var DJlist : ArrayList<DJperson> ): RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dj_search_item, parent, false)
        return SearchViewHolder(itemView)
    }

    fun filtriraj(filterList: ArrayList<DJperson>) {
        DJlist = filterList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.djName.text = DJlist.get(position).name
        holder.djGenre.text = DJlist.get(position).genre

    }

    override fun getItemCount(): Int {
        return DJlist.size
    }
    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val djName : TextView = itemView.findViewById(R.id.textView8)
        val djGenre : TextView = itemView.findViewById(R.id.textView9)
    }

}