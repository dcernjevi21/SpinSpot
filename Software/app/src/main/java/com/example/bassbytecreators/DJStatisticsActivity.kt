package com.example.bassbytecreators

import BaseActivity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.bassbytecreators.entities.DJGig
import com.example.bassbytecreators.helpers.RetrofitClient.apiService
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DJStatisticsActivity : BaseActivity() {

    private lateinit var navView: NavigationView
    private var userId: Int = -1

    private val selectedStartDate: Calendar = Calendar.getInstance()
    private val selectedEndDate: Calendar = Calendar.getInstance()
    private val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private val sdfDate2 = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    lateinit var startDateSelection: EditText
    lateinit var endDateSelection: EditText
    private var djGigs: List<DJGig> = emptyList()

    lateinit var generatePDFBtn: Button
    var pageHeight = 1120
    var pageWidth = 792

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dj_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "User ID nije pronaden!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        drawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)
        setupNavigationDrawer(navView)

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

    private fun onDateRangeSelected() {
        fetchGigsAndUpdateUI()
        //Toast.makeText(this, "Datum odabran: ${sdfDate.format(selectedStartDate.time)}", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Datum odabran: ${sdfDate.format(selectedEndDate.time)}", Toast.LENGTH_LONG).show()
    }

    private fun fetchGigsAndUpdateUI() {
        val startDate = sdfDate2.format(selectedStartDate.time)
        val endDate = sdfDate2.format(selectedEndDate.time)
        Log.d("DJStatistics", "Šaljem userId i datume: userId: $userId, start date: $startDate, end date: $endDate")
        apiService.getGigsStats(userId, startDate, endDate).enqueue(object : Callback<List<DJGig>> {
            override fun onResponse(call: Call<List<DJGig>>, response: Response<List<DJGig>>) {
                Log.d("API_CALL", "URL: ${call.request()}")
                if (response.isSuccessful) {
                    djGigs = response.body()!!
                    if (djGigs != null) {
                        djGigs.forEach {
                            Log.d("DJStatistics", "Ispis gigova redom: ${it.gigFee}, ${it.gigType}, ${it.gigDate}")
                        }
                        updateUIWithGigs(djGigs)
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

    fun generatePDF() {

        if (djGigs.isEmpty()) {
            Toast.makeText(this, "Nema podataka za generiranje PDF-a", Toast.LENGTH_SHORT).show()
            return
        }
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

        // Stil za title
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        title.textSize = 24F
        title.setColor(ContextCompat.getColor(this, R.color.black))
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("DJ Statistika", pageWidth / 2F, 85F, title)

        // Stil za datum
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.textSize = 18F
        title.textAlign = Paint.Align.LEFT
        canvas.drawText("Početni datum: ${sdfDate.format(selectedStartDate.time)}", 50F, 125F, title)
        canvas.drawText("Krajnji datum: ${sdfDate.format(selectedEndDate.time)}", 50F, 145F, title)

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.textSize = 15F
        title.textAlign = Paint.Align.LEFT
        canvas.drawText("======================================", 50F, 175F, title)

        // Dodavanje podataka o gažama
        var yPosition = 200F  // Početna pozicija za ispisivanje podataka
        var totalEarnings = 0.0

        djGigs.forEachIndexed { index, gig ->
            canvas.drawText("Gaza ${index + 1}:", 50F, yPosition, title)
            canvas.drawText("Naziv: ${gig.name}", 70F, yPosition + 20F, title)
            canvas.drawText("Datum: ${gig.gigDate}", 70F, yPosition + 40F, title)
            canvas.drawText("Lokacija: ${gig.location}", 70F, yPosition + 60F, title)
            canvas.drawText("Vrsta: ${gig.gigType}", 70F, yPosition + 80F, title)
            canvas.drawText("Opis: ${gig.description}", 70F, yPosition + 100F, title)
            canvas.drawText("Početak: ${gig.gigStartTime}", 70F, yPosition + 120F, title)
            canvas.drawText("Kraj: ${gig.gigEndTime}", 70F, yPosition + 140F, title)
            canvas.drawText("Naknada: ${gig.gigFee} €", 70F, yPosition + 160F, title)
            canvas.drawText("======================================", 50F, yPosition + 180F, title)

            // Dodajemo zaradu za ovu gazu
            totalEarnings += gig.gigFee

            // Povećaj poziciju za sljedeću gazu
            yPosition += 180F
        }

        // Dodavanje ukupne zarade
        canvas.drawText("Ukupna zarada: ${totalEarnings} €", 50F, yPosition, title)
        yPosition += 40F

        // Dodavanje dizajna sa završnim detaljima
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC))
        title.textSize = 14F
        title.setColor(ContextCompat.getColor(this, R.color.black))
        canvas.drawText("Izvještaj generiran na: ${sdfDate.format(Calendar.getInstance().time)}", pageWidth / 2F, yPosition, title)

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

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkPermission(): Boolean {
        return Environment.isExternalStorageManager()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun requestPermission() {
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    fun activateDateRangeListeners() {
        startDateSelection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedStartDate.set(year, monthOfYear, dayOfMonth)
                        startDateSelection.setText(
                            sdfDate.format(selectedStartDate.time).toString()
                        )
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
}