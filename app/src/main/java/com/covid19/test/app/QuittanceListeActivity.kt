package com.covid19.test.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONObject

class QuittanceListeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quittance_liste)

        val listView = findViewById<ListView>(R.id.listView)
        val adapter = QuittanceListeAdapter(this@QuittanceListeActivity, setListe())
        listView.adapter = adapter
    }

    fun setListe(): JSONArray {
        val liste = JSONArray()
        var item = JSONObject()
        val form = getForm()

        item.put("nom", form.get("nom").toString())
        item.put("created_at", form.get("date").toString())
        liste.put(item)

//        for(i in 1..10) {
//            item.put("nom", "Kuela Abdoul Raof")
//            item.put("created_at", "21/03/2021 15:33:33")
//            liste.put(item)
//        }

        return liste
    }

    fun getForm(): JSONObject {
        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val form = JSONObject(preferences.getString("form", ""))
        return form
    }
}