package com.example.bassbytecreators.entities

import java.util.Date

data class DJGig(
    val name : String,
    val location : String,
    val gigType: String,
    val description: String,
    val gigStartTime : String,
    val gigEndTime: String,
    val gigDate : String,
    val gigFee : Double,
    val djId: Int
    )