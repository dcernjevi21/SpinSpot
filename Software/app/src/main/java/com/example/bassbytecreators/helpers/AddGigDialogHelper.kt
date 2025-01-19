package com.example.bassbytecreators.helpers

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.DJGig
import java.text.SimpleDateFormat
import java.util.Locale

class AddGigDialogHelper(private val view: View) {

    private val selectedDateTime: Calendar = Calendar.getInstance()

    private val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.US)

    private val spinner = view.findViewById<Spinner>(R.id.spn_add_djgig_gigType)
    private val dateSelection = view.findViewById<EditText>(R.id.et_add_djgig_dialog_date)
    private val timeSelection = view.findViewById<EditText>(R.id.et_add_djgig_dialog_gigStartTime)

    fun activateDateTimeListeners() {
        dateSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedDateTime.set(year, monthOfYear, dayOfMonth)
                        dateSelection.setText(sdfDate.format(selectedDateTime.time).toString())
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
                ).show()
                view.clearFocus()
            }
        }
        timeSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(
                    view.context, { _, hourOfDay, minute ->
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedDateTime.set(Calendar.MINUTE, minute)
                        timeSelection.setText(sdfTime.format(selectedDateTime.time).toString())
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE), true
                ).show()
                view.clearFocus()
            }
        }
    }

    fun buildGig(): DJGig {
        val locationEt = view.findViewById<EditText>(R.id.et_add_djgig_dialog_location)
        val gigTypeEt = view.findViewById<Spinner>(R.id.spn_add_djgig_gigType)
        val nameEt = view.findViewById<EditText>(R.id.et_add_djgig_dialog_name)
        val descriptionEt = view.findViewById<EditText>(R.id.et_add_djgig_dialog_description)

        val feeEt = view.findViewById<EditText>(R.id.et_add_djgig_dialog_gigFee)
        var feeDouble: Double
        val fee = feeEt.text.toString()

        try {
            if (fee.contains(".")) {
                fee.toDouble()  // parsiraj ako već ima decimalni dio
                feeDouble = fee.toDouble()
            } else {
                // Ako nema decimalnog dijela, dodaj .00 i parsiraj
                feeDouble = "${fee}.00".toDouble()
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(view.context, "Unos nije validan broj", Toast.LENGTH_LONG).show()
            feeDouble = 0.0
        }

        val dateOnly = sdfDate.format(selectedDateTime.time)
        val timeOnly = sdfTime.format(selectedDateTime.time)

        return DJGig(
            gigDate = dateOnly,
            location = locationEt.text.toString(),
            gigType = gigTypeEt.selectedItem.toString(),
            description = descriptionEt.text.toString(),
            name = nameEt.text.toString(),
            gigStartTime = timeOnly,
            gigEndTime = timeOnly,
            gigFee = feeDouble
        )
    }
}