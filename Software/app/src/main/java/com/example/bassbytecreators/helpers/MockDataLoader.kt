package com.example.bassbytecreators.helpers

import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.entities.GigType
import java.sql.Time
import java.time.LocalDate
import java.util.Date

class MockDataLoader {
    fun getDemoGigData(): List<DJGig> {

        return listOf(
            DJGig(Date(), "Varaždin", GigType.CLUB, "Strauss", null, "12:00", 150.00),
            DJGig(Date(), "Čakovec", GigType.PRIVATE_EVENT, null, "Ivo Ivić", "22:00", 300.00),
            DJGig(Date(), "Zabok", GigType.PRIVATE_EVENT, null, "Marko Markić", "15:00", 400.00),
            DJGig(Date(), "Split", GigType.CLUB, "Terazzo", null, "21:00", 350.00)
        )
    }
}