package com.example.bassbytecreators.helpers

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.DJperson
import java.sql.Time
import java.time.LocalDate
import java.util.Date

class MockDataLoader {
    fun getDemoGigData(): List<DJGig> {

        return listOf(
            DJGig("Date()", "Varaždin", "Klub", "Strauss", "12:00", 150.00),
            DJGig("Date()", "Čakovec", "Privatni događaj", "Ivo Ivić", "22:00", 300.00),
            DJGig("Date()", "Zabok", "Privatni događaj",  "Marko Markić", "15:00", 400.00),
            DJGig("Date()", "Split", "Klub", "Terazzo",  "21:00", 350.00)
        )
    }

    public fun getDemoDJdata(): List<DJperson> {
        return listOf(
            DJperson("Aviici", "Techno", "image"),
            DJperson("DeadMau5", "Techno", "image"),
            DJperson("Skrillex", "Dubstep", "image")
        )
    }
}