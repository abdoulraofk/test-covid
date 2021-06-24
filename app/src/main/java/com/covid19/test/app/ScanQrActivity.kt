package com.covid19.test.app

import android.R.attr.bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.zxing.WriterException


class ScanQrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

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
    }
}