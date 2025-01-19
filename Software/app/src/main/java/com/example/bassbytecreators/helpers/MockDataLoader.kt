package com.example.bassbytecreators.helpers

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import java.sql.Time
import java.time.LocalDate
import java.util.Date

class MockDataLoader {
    fun getDemoGigData(): List<DJGig> {

        return listOf(
            DJGig("H2O", "Date()","Varaždin", "Klub", "H20 je klub u VZ", "22:00", "04:00",250.00),
            DJGig("H22", "Date()","Varaždin2", "Klub", "H20 je klub u VZ2", "22:02", "04:02",250.02),
            DJGig("H23", "Date()","Varaždin3", "Klub", "H20 je klub u VZ3", "22:03", "04:03",250.03),
        )
    }

    public fun getDemoDJdata(): ArrayList<DJperson> {
        val djList = ArrayList<DJperson>()

        return djList
    }
}