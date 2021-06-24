package com.covid19.test.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.sunu.mhr.service.VolleyRequest
import kotlinx.coroutines.*
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnListeQuittances = findViewById<Button>(R.id.btnListeQuittances)
        val btnSinscrire = findViewById<Button>(R.id.btnSinscrire)

        btnListeQuittances.setOnClickListener {
//            validerPaiement()
            startActivity(Intent(this@DashboardActivity, QuittanceListeActivity::class.java))
        }
        btnSinscrire.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, FormulaireActivity::class.java))
        }
    }

    fun validerPaiement() {
        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val id = preferences.getString("id", "0")
        val info = JSONObject(preferences.getString("form", ""))
        val data = HashMap<String, String?>()
        data.put("id", id)
        data.put("nom_prenom", info.getString("nom") + " " + info.getString("prenom"))
        data.put("telephone", info.getString("telephone"))
        data.put("numero_paiement", "55555555")

        // request
        VolleyRequest.post(this@DashboardActivity, ApiCovidConst.PATH_SAVE_PAIEMENT, data, object: VolleyRequest.Callback() {
            override fun onSuccess(data: String) {
                Toast.makeText(this@DashboardActivity, "Request success " + id, Toast.LENGTH_LONG).show()
            }

            override fun onFail() {
                CoroutineScope(Job() + Dispatchers.Default).launch {
                    Toast.makeText(this@DashboardActivity, "Error " + id, Toast.LENGTH_LONG).show()
                    delay(500)
                    validerPaiement()
                }
            }
        })
    }
}