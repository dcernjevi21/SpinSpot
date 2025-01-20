package com.example.bassbytecreators.entities

import java.util.Date

import com.google.gson.annotations.SerializedName

data class DJGig(
    @SerializedName("Name")
    val name: String,
    @SerializedName("date")
    var gigDate: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("type")
    val gigType: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("start_time")
    var gigStartTime: String,
    @SerializedName("end_time")
    var gigEndTime: String,
    @SerializedName("fee")
    val gigFee: Double
)