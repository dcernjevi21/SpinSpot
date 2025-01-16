package com.example.bassbytecreators

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Button
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

class DJStatisticsActivity : AppCompatActivity() {

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

        generatePDFBtn = findViewById(R.id.btnGenerirajPdf)

        if(checkPermission()) {
            Toast.makeText(this, "Dozvola data...", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        generatePDFBtn.setOnClickListener {
            generatePDF()
        }
    }

    fun generatePDF() {
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
        title.textSize = 15F
        //boja teksta
        title.setColor(ContextCompat.getColor(this, R.color.black))

        //pisanje teksta u pdf, prvo ide tekst, pa startna pozicija pa pozicija gledano od gore i title je za boju
        canvas.drawText("Testni tekst...", 209F, 100F, title)
        canvas.drawText("Dominik Černjević", 209F, 80F, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(this, R.color.black))
        title.textSize = 15F

        //sad pisem tekst na sredini
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)

        //za odrediti kraj stranice
        pdfDocument.finishPage(myPage)

        //naziv i putanja datoteke
        val file: File = File(getExternalFilesDir(null), "TestniPDF.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(applicationContext, "PDF datoteka uspješno stvorena...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Greška kod generiranje PDF datoteke...", Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()
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
}