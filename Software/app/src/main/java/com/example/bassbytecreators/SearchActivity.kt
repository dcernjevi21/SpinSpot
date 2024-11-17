package com.example.bassbytecreators

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.adapters.SearchAdapter
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.helpers.MockDataLoader

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var djAdapter: SearchAdapter

    private val djList = listOf(
        DJperson("DJ Snake", "EDM",""),
        DJperson("David Guetta", "House",""),
        DJperson("Armin van Buuren", "Trance",""),
        DJperson("Tiesto", "Trance",""),
        DJperson("Carl Cox", "Techno","")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dj_search_layout)

        recyclerView = findViewById(R.id.dj_recyclerview_search)
        searchEditText = findViewById(R.id.pretrazivanjeTekst)

        djAdapter = SearchAdapter(djList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = djAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = djList.filter {
                    it.name.contains(s.toString(), ignoreCase = true) ||
                            it.genre.contains(s.toString(), ignoreCase = true)
                }
                djAdapter.updateList(filteredList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
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
        } else djAdapter.updateList(filteredList)
    }
}