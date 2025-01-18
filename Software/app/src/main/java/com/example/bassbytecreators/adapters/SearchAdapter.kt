package com.example.bassbytecreators.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.Fragments.DJDetailActivity
import com.example.bassbytecreators.R
import com.example.bassbytecreators.adapters.SearchAdapter.DJViewHolder
import com.example.bassbytecreators.entities.DJperson

class SearchAdapter(private var djList: List<DJperson>):  RecyclerView.Adapter<SearchAdapter.DJViewHolder>(){
    inner class DJViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivTextView: TextView = view.findViewById(R.id.textView8)
        val zanrTextView: TextView = view.findViewById(R.id.textView9)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DJViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dj_search_item, parent, false)
        return DJViewHolder(view)
    }
    override fun onBindViewHolder(holder: DJViewHolder, position: Int) {
        val dj = djList[position]
        holder.nazivTextView.text = dj.dj_name
        holder.zanrTextView.text = dj.genres
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DJDetailActivity::class.java)
            intent.putExtra("DJ_NAME", dj.dj_name)
            intent.putExtra("DJ_GENRE", dj.genres)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = djList.size

    fun updateList(newList: List<DJperson>) {
        djList = newList
        notifyDataSetChanged()
    }

}