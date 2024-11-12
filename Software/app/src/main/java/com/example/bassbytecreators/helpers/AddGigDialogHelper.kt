package com.example.bassbytecreators.helpers

import android.icu.util.Calendar
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.DJGig
import java.text.SimpleDateFormat
import java.util.Locale

class AddGigDialogHelper(private val view: View) {

    private val selectedDate: Calendar = Calendar.getInstance()

    private val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.US)

    private val spinner = view.findViewById<Spinner>(R.id.spn_add_djgig_gigType)
    private val dateSelection = view.findViewById<EditText>(R.id.et_add_djgig_dialog_date)
    private val timeSelection = view.findViewById<EditText>(R.id.et_add_djgig_dialog_gigStartTime)

    /*fun activateDateTimeListeners() {
        dateSelection.setOnFocusChangeListener {}
        dateSelection.setOnFocusChangeListener {}
    }

    fun buildGig(): DJGig {}
    */
}