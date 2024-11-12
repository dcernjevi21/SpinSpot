package com.example.bassbytecreators.entities

import java.util.Date

data class DJGig(
    val gigDate : Date,
    val location : String,
    val gigType: GigType,
    val clubName : String? = null,
    val hostName : String? = null,
    val gigStartTime : String,
    val gigFee : Double,
    )

enum class GigType {
    CLUB,
    PRIVATE_EVENT
}
