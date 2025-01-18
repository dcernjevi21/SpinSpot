package com.example.bassbytecreators.helpers

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("get_gigs_stats.php")
    fun getGigs(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<List<DJGig>>

    @GET("get_DJs.php")
    fun getDJs(
        @Query("name") name: String?
    ): Call<List<DJperson>>
}

