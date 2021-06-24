package com.covid19.test.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AccueilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

//        if(checkData()) startActivity(Intent(this@AccueilActivity, DashboardActivity::class.java))

        val button = findViewById<Button>(R.id.remplir_formulaire)
        button.setOnClickListener {
            startActivity(Intent(this@AccueilActivity, FormulaireActivity::class.java))
        }
    }

    fun checkData(): Boolean {
        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val content = preferences.getBoolean("content", false)
        return content
    }
}