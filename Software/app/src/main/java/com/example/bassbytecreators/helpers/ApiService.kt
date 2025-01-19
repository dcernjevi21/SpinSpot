package com.example.bassbytecreators.helpers

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
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
        @Query("user_id") userId: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<List<DJGig>>

    @GET("get_single_dj.php")
    fun getDj(
        @Query("id") id: String
    ): Call<List<DJperson>>

    @GET("get_DJs.php")
    fun getDJs(
        @Query("name") name: String?
    ): Call<List<DJperson>>

    @GET("get_upcoming_gigs.php")
    fun getUpcomingGigs(
        @Query("id") djId: Int
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

