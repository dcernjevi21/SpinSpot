package com.example.bassbytecreators.helpers

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.Review
import com.example.bassbytecreators.entities.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("get_gigs_stats.php")
    fun getGigs(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<List<DJGig>>

    @GET("users.php")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<User>

    @POST("users.php")
    fun registerUser(@Body user: User): Call<ResponseBody>

    @POST("reviews.php")
    fun submitReview(@Body review: Review): Call<ResponseBody>

    @GET("reviews.php")
    fun fetchReviews(@Query("dj_id") djId: Int): Call<List<Review>>
}

