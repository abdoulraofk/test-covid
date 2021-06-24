package com.covid19.test.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.system.exitProcess

class PaiementValideActivity : AppCompatActivity() {
    lateinit var btnQUittance: Button
    lateinit var btnNouveau: Button
    lateinit var btnAccueil: Button
    lateinit var btnQuitter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paiement_valide)
        setBtn()
    }

    fun setBtn() {
        btnQUittance = findViewById(R.id.btnQuittance)
        btnNouveau = findViewById(R.id.btnNouveau)
        btnAccueil = findViewById(R.id.btnAccueil)
        btnQuitter = findViewById(R.id.btnQuitter)

        btnQUittance.setOnClickListener { startActivity(Intent(this@PaiementValideActivity, QuittancePdfActivity::class.java)) }
        btnNouveau.setOnClickListener { startActivity(Intent(this@PaiementValideActivity, FormulaireActivity::class.java)) }
        btnAccueil.setOnClickListener { startActivity(Intent(this@PaiementValideActivity, AccueilActivity::class.java)) }
        btnQuitter.setOnClickListener { quitter() }
    }

    fun quitter() {
        moveTaskToBack(true);
        exitProcess(-1)
    }
}