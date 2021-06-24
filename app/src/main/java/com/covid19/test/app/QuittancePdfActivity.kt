package com.covid19.test.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.WriterException
import org.json.JSONObject

class QuittancePdfActivity : AppCompatActivity() {

    lateinit var siteTextView: TextView
    lateinit var nomTextView: TextView
//    lateinit var programmerTextView: TextView
    lateinit var prixTextView: TextView
    lateinit var telephoneTextView: TextView
    lateinit var dateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quittance_pdf)

        getForm()

        val qrImage = findViewById<ImageView>(R.id.codeQr)
        val qrgEncoder: QRGEncoder = QRGEncoder(
            "codeqr", null,
            QRGContents.Type.TEXT,
            600
        )

        try {
            val bitmap = qrgEncoder.encodeAsBitmap()
            qrImage.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.v("com.test.covid", e.toString())
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this@QuittancePdfActivity, ScanQrActivity::class.java))
        }
    }

    fun setTextView(site: String, nom: String, programmer: String, prix: String, date: String) {
        siteTextView = findViewById(R.id.site)
        nomTextView = findViewById(R.id.nom)
//        programmerTextView = findViewById(R.id.programmer)
        prixTextView = findViewById(R.id.prix)
        telephoneTextView = findViewById(R.id.telephone)
        dateTextView = findViewById(R.id.date)

        siteTextView.setText(site)
        nomTextView.setText(nom)
//        programmerTextView.setText(programmer)
        prixTextView.setText(prix)
        dateTextView.setText(date)
    }

    fun getForm() {
        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val form = JSONObject(preferences.getString("form", ""))
        setTextView(form.get(
            "site").toString(),
            form.get("nom").toString() + " " + form.get("prenom").toString(),
            form.get("programmer").toString(),
            form.get("prix").toString(),
            form.get("date").toString()
        )
    }
}