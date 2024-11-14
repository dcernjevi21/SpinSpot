package com.example.bassbytecreators

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.adapters.SearchAdapter
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.helpers.MockDataLoader

class SearchActivity : AppCompatActivity() {
    lateinit var djRecyclerView : RecyclerView
    var djList : ArrayList<DJperson> = MockDataLoader().getDemoDJdata()
    lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dj_search_layout)
        djRecyclerView = findViewById(R.id.dj_recyclerview_search)

        searchAdapter = SearchAdapter(djList)
        djRecyclerView.adapter = searchAdapter
        djList.add(DJperson("Boris breycha", "House", "image"))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dj_search_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        searchAdapter.notifyDataSetChanged()
    }


    fun filter(text: String){

        val filteredList: ArrayList<DJperson> = ArrayList()
        for (item in djList){
            if (item.name.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show()
        } else searchAdapter.filtriraj(filteredList)
    }
}