package com.covid19.test.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuittanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quittance)

        val pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView.fromAsset("quittance.pdf")
            .password(null) // if password protected, then write password
            .defaultPage(0) // set the default page to open
            .load()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this@QuittanceActivity, ScanQrActivity::class.java))
        }
    }
}