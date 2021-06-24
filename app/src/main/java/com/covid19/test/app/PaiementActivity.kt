package com.covid19.test.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import android.net.Uri
import android.Manifest
import android.os.Build
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.SubscriptionEventListener
import com.sunu.mhr.service.VolleyRequest
import kotlinx.coroutines.*
import org.json.JSONObject

class PaiementActivity : AppCompatActivity() {

    lateinit var siteTextView: TextView
    lateinit var nomTextView: TextView
    lateinit var programmerTextView: TextView
    lateinit var prixTextView: TextView
    lateinit var telephoneTextView: TextView
    lateinit var spinnerModePaiement: AutoCompleteTextView
    lateinit var progressBar: ProgressBar
    lateinit var btnValider: Button
    lateinit var telephone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paiement)

        askPermission()
        setSpinnerModePaiement()
        setBtnValider()
        getForm()
    }

    fun _attentePaiement() {
        CoroutineScope(Job() + Dispatchers.Main).launch {
            delay(6000)
            startActivity(Intent(this@PaiementActivity, PaiementValideActivity::class.java))
        }
    }

    fun attentePaiement() {
        CoroutineScope(Job() + Dispatchers.Default).launch {
            //configuration
            val options: PusherOptions = PusherOptions()
            options.setCluster("eu")
            val pusher: Pusher = Pusher("dfb357a2de95b4f04b78", options)
            pusher.connect()

            //suscription à l'évènement d'attente de paiement
            val channel: Channel = pusher.subscribe("paiement")
            channel.bind(telephone, object : SubscriptionEventListener {
                override fun onEvent(event: PusherEvent?) {
                    CoroutineScope(Job() + Dispatchers.Main).launch {
                        validerPaiement()
                        setProgressBar(false)
                        startActivity(Intent(this@PaiementActivity, PaiementValideActivity::class.java))
                    }
                }
            })
        }
    }

    fun setTextView(site: String, nom: String, programmer: String, prix: String) {
        siteTextView = findViewById(R.id.site)
        nomTextView = findViewById(R.id.nom)
        programmerTextView = findViewById(R.id.programmer)
        prixTextView = findViewById(R.id.prix)
        telephoneTextView = findViewById(R.id.telephone)

        siteTextView.setText(site)
        nomTextView.setText(nom)
        programmerTextView.setText(programmer)
        prixTextView.setText(prix)
    }

    fun askPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1);
            }
        }
    }

    fun sendUssd() {
        val encodedHash: String = Uri.encode("#")
        val ussd = "*144*2*1*65755685*50" + encodedHash
        startActivityForResult(Intent("android.intent.action.CALL", Uri.parse("tel:$ussd")), 1)
    }

    fun setSpinnerModePaiement() {
        spinnerModePaiement = findViewById(R.id.spinner_mode_paiement)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this@PaiementActivity,
            android.R.layout.simple_spinner_item,
            arrayOf("Orange Money")
        )
        spinnerModePaiement.setAdapter(adapter)
    }

    fun setProgressBar(stat: Boolean) {
        progressBar.visibility = when(stat) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    fun setBtnValider() {
        btnValider = findViewById(R.id.btnValider)
        progressBar = findViewById(R.id.progressBar)
        btnValider.setOnClickListener {
            if (checkTelephone()) {
                setProgressBar(true)
                sendUssd()
                attentePaiement()
            }
        }
    }

    fun getForm() {
        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val form = JSONObject(preferences.getString("form", ""))
        setTextView(form.get(
            "site").toString(),
            form.get("nom").toString() + " " + form.get("prenom").toString(),
            form.get("programmer").toString(),
            form.get("prix").toString()
        )
    }

    fun checkTelephone(): Boolean {
        telephone = telephoneTextView.text.toString()
        if (TextUtils.isEmpty(telephone)) {
            Toast.makeText(this@PaiementActivity, "Veuillez saisir le numéro de paiement s'il vous plaît", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun validerPaiement() {
        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val id = preferences.getString("id", "0")
        val info = JSONObject(preferences.getString("form", ""))
        val data = HashMap<String, String?>()
        data.put("id", id)
        data.put("nom_prenom", info.getString("nom") + " " + info.getString("prenom"))
        data.put("telephone", info.getString("telephone"))
        data.put("numero_paiement", telephone.trim())

        // request
        VolleyRequest.post(this@PaiementActivity, ApiCovidConst.PATH_SAVE_PAIEMENT, data, object: VolleyRequest.Callback() {
            override fun onSuccess(data: String) {
                //
            }

            override fun onFail() {
                CoroutineScope(Job() + Dispatchers.Default).launch {
                    delay(500)
                    validerPaiement()
                }
            }
        })
    }
}