package com.example.bassbytecreators

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//za rad s bazom
import com.example.bassbytecreators.helpers.RetrofitClient
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.helpers.RetrofitClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DJStatisticsActivity : AppCompatActivity() {

    private val selectedStartDate: Calendar = Calendar.getInstance()
    private val selectedEndDate: Calendar = Calendar.getInstance()
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private val sdfDate2 = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    lateinit var startDateSelection: EditText
    lateinit var endDateSelection: EditText

    lateinit var generatePDFBtn: Button
    var pageHeight = 1120
    var pageWidth = 792

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dj_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statistics_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startDateSelection = findViewById(R.id.et_dj_statistics_start_date)
        endDateSelection = findViewById(R.id.et_dj_statistics_end_date)

        generatePDFBtn = findViewById(R.id.btnGenerirajPdf)


        if(checkPermission()) {
            Toast.makeText(this, "Dozvola data...", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        activateDateRangeListeners()

        generatePDFBtn.setOnClickListener {
            generatePDF()
        }
    }

    fun generatePDF() {

        //ako datum nije dati onda defaultno za trenutnu godinu, dodati da kad generira PDF da uzima u obzir taj datum

        var pdfDocument: PdfDocument = PdfDocument()

        //za crtanje oblika i title za dodavanje teksta u pdf datoteku
        var paint: Paint = Paint()
        var title: Paint = Paint()

        //informacije o stranicama
        var myPageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        //prva stranica
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        var canvas: Canvas = myPage.canvas

        //stil
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        //velicina fonta
        title.textSize = 20F
        //boja teksta
        title.setColor(ContextCompat.getColor(this, R.color.black))
        //pisanje teksta u pdf, prvo ide tekst, pa startna pozicija pa pozicija gledano od gore i title je za boju
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("DJ Statistika", 50F, 85F, title)
        title.textAlign = Paint.Align.LEFT
        canvas.drawText("DJ: (Domzz)", 50F, 105F, title)
        canvas.drawText("Početni datum: ${sdfDate.format(selectedStartDate.time)}", 50F, 125F, title)
        canvas.drawText("Krajnji datum: ${sdfDate.format(selectedEndDate.time)}", 50F, 145F, title)

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(this, R.color.black))
        title.textSize = 15F

        //sad pisem tekst na sredini
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("Ukupan broj gaži: ", 396F, 540F, title)
        canvas.drawText("Ukupna zarada: ", 396F, 560F, title)
        canvas.drawText("Najčešći tip gaže: ", 396F, 580F, title)
        canvas.drawText("Ovo je probni pdf dokument.", 396F, 600F, title)

        //za odrediti kraj stranice
        pdfDocument.finishPage(myPage)

        //naziv i putanja datoteke
        val file: File = File(getExternalFilesDir(null), "dj_statistika_${sdfDate2.format(selectedStartDate.time)}.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(applicationContext, "PDF datoteka uspješno stvorena...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Greška kod generiranje PDF datoteke...", Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()
    }

    fun activateDateRangeListeners() {
        startDateSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedStartDate.set(year, monthOfYear, dayOfMonth)
                        startDateSelection.setText(sdfDate.format(selectedStartDate.time).toString())
                        endDateSelection.requestFocus()
                    },
                    selectedStartDate.get(Calendar.YEAR),
                    selectedStartDate.get(Calendar.MONTH),
                    selectedStartDate.get(Calendar.DAY_OF_MONTH)
                ).show()
                view.clearFocus()
            }
        }

        endDateSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedEndDate.set(year, monthOfYear, dayOfMonth)

                        if(selectedEndDate.before(selectedStartDate)) {
                            Toast.makeText(this, "Krajnji datum ne može biti pre početnog!", Toast.LENGTH_SHORT).show()
                            selectedEndDate.time = selectedStartDate.time // Resetuj krajnji datum
                        } else {
                            endDateSelection.setText(sdfDate.format(selectedEndDate.time).toString())
                            onDateRangeSelected()
                        }
                    },
                    selectedEndDate.get(Calendar.YEAR),
                    selectedEndDate.get(Calendar.MONTH),
                    selectedEndDate.get(Calendar.DAY_OF_MONTH)
                ).show()
                view.clearFocus()
            }
        }

    }

    fun onDateRangeSelected() {
        fetchGigsAndUpdateUI()
        //Toast.makeText(this, "Datum odabran: ${sdfDate.format(selectedStartDate.time)}", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Datum odabran: ${sdfDate.format(selectedEndDate.time)}", Toast.LENGTH_LONG).show()
    }

    fun checkPermission(): Boolean {
        var writeStoragePermission = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)

        var readStoragePermission = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // on below line we are checking if the
        // request code is equal to permission code.
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    // if permissions are granted we are displaying a toast message.
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {

                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun fetchGigsAndUpdateUI() {
        val startDate = sdfDate2.format(selectedStartDate.time)
        val endDate = sdfDate2.format(selectedEndDate.time)
        Log.d("DJStatistics", "Šaljem datum start: $startDate, end: $endDate")
        RetrofitClient.apiService.getGigs(startDate, endDate).enqueue(object : Callback<List<DJGig>> {
            override fun onResponse(call: Call<List<DJGig>>, response: Response<List<DJGig>>) {
                Log.d("API_CALL", "URL: ${call.request()}")
                if (response.isSuccessful) {
                    val gigs = response.body()
                    if (gigs != null) {
                        gigs.forEach {
                            Log.d("DJStatistics", "Ispis gigova redom: ${it.gigFee}, ${it.gigType}, ${it.gigDate}")
                        }
                        updateUIWithGigs(gigs)
                    }
                } else {
                    Log.e("API_ERROR", "Greška kod odgovora: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DJGig>>, t: Throwable) {
                Log.e("API_ERROR", "Greška kod povezivanja s API-jem: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun updateUIWithGigs(gigs: List<DJGig>) {

        val totalGigs = gigs.size
        val totalFee = gigs.sumOf { gig ->
            Log.d("DJStatistics", "Dodajem gigFee: ${gig.gigFee},")
            gig.gigFee}
        val mostCommonType = gigs.groupingBy { it.gigType }.eachCount().maxByOrNull { it.value }?.key ?: "N/A"

        // Ažuriranje TextView-ova
        findViewById<TextView>(R.id.tv_dj_statistics_gig_number).text = "Ukupan broj gaži: $totalGigs"
        findViewById<TextView>(R.id.tv_dj_statistics_zarada).text = "Ukupna zarada: $totalFee"
        findViewById<TextView>(R.id.tv_dj_statistics_gig_type).text = "Najčešći tip gaže: $mostCommonType"
    }
}