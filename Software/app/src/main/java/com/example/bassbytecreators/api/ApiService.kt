package com.example.bassbytecreators.api

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import com.example.bassbytecreators.entities.Review
import com.example.bassbytecreators.entities.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @GET("gigs.php")
    fun getGigsStats(
        @Query("user_id") userId: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<List<DJGig>>


    @POST("gigs.php")
    fun addNewGig(
        @Body newGig: DJGig,
        @Query("user_id") userId: Int): Call<ResponseBody>

    @GET("gigs.php")
    suspend fun getGigs(
        @Query("user_id") userId: Int): List<DJGig>

    @GET("gigs.php")
    fun getGigsSync(
        @Query("user_id") userId: Int): Call<List<DJGig>>



    @GET("get_single_dj.php")
    fun getDj(
        @Query("id") id: String
    ): Call<List<DJperson>>

    @GET("get_all_gigs.php")
    fun getAllGigs(): Call<List<DJGig>>

    @GET("get_all_gigs_dates.php")
    fun getAllGigsDates() : Call<List<String>>


    @GET("get_DJs.php")
    fun getDJs(
        @Query("name") name: String?
    ): Call<List<DJperson>>


    @GET("get_upcoming_gigs.php")
    fun getUpcomingGigs(
        @Query("id") djId: Int
    ): Call<List<DJGig>>

     @GET("gigs.php")
    fun getUpcomingGigsInMonth(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("user_id") id: String
    ): Call<List<DJGig>>
     @GET("users.php")
    fun loginUser(
        @Query("action") action: String = "login",
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<User>

    @GET("users.php")
    fun getUser(@Query("action") action: String = "get_user", @Query("user_id") userId: Int): Call<User>

    @DELETE("users.php")
    fun deleteUser(@Query("user_id") userId: Int): Call<ResponseBody>

    @POST("users.php")
    fun registerUser(@Body user: User): Call<ResponseBody>

    @PUT("users.php")
    fun updateUser(@Body user: User): Call<ResponseBody>

    @POST("reviews.php")
    fun submitReview(@Body review: Review): Call<ResponseBody>

    @GET("reviews.php")
    fun fetchReviews(@Query("dj_id") djId: Int): Call<List<Review>>
}
