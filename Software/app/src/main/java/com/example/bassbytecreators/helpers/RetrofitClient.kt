package com.example.bassbytecreators.helpers

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://157.230.8.219/spinspot/"
    private const val USERNAME = "bassbyte"
    private const val PASSWORD = "y<6zKk"

    //da se uvijek stavi username i lozinka u authorization header
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val credential = Credentials.basic(USERNAME, PASSWORD)
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", credential)
            .build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }
}