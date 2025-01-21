package com.example.bassbytecreators

import BaseActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.adapters.SearchAdapter
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.helpers.RetrofitClient
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {
    private lateinit var navView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var djAdapter: SearchAdapter
    private var djList = ArrayList<DJperson>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dj_search_layout)

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)

        recyclerView = findViewById(R.id.dj_recyclerview_search)
        searchEditText = findViewById(R.id.pretrazivanjeTekst)

        djAdapter = SearchAdapter(djList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = djAdapter
        RetrofitClient.apiService.getDJs("").enqueue(object : Callback<List<DJperson>>{
            override fun onResponse(call: Call<List<DJperson>>, response: Response<List<DJperson>>) {
                if(response.isSuccessful){
                    var djevi = response.body()
                    djList = djevi as ArrayList<DJperson>
                    Log.d("djevi", djList[0].dj_name)
                    djAdapter.updateList(djList)
                }
            }

            override fun onFailure(call: Call<List<DJperson>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                RetrofitClient.apiService.getDJs(searchEditText.text.toString()).enqueue(object : Callback<List<DJperson>>{
                    override fun onResponse(
                        call: Call<List<DJperson>>,
                        response: Response<List<DJperson>>
                    ) {
                        var djevi = response.body()
                        djList = djevi as ArrayList<DJperson>
                        //Log.d("djevi", djList[0].dj_name)
                        djAdapter.updateList(djList)
                    }

                    override fun onFailure(call: Call<List<DJperson>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
                //djAdapter.updateList(filteredList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }


    fun filter(text: String){

        val filteredList: ArrayList<DJperson> = ArrayList()
        for (item in djList){
            if (item.dj_name.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show()
        } else djAdapter.updateList(filteredList)
    }
}