package com.covid19.test.app

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.sunu.mhr.service.VolleyRequest
import kotlinx.android.synthetic.main.activity_paiement.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors.toMap
import kotlin.collections.HashMap

class FormulaireActivity : AppCompatActivity() {

    lateinit var spinnerSite: AutoCompleteTextView
    lateinit var spinnerSexe: AutoCompleteTextView
    lateinit var spinnerProfession: AutoCompleteTextView
    lateinit var buttonValider: Button
    lateinit var nomInput: TextInputEditText
    lateinit var prenomInput: TextInputEditText
    lateinit var dateNaissancePicker: LinearLayout
    lateinit var dateNaissance: TextInputEditText
    lateinit var telephoneInput: TextInputEditText
    lateinit var programmer: RadioGroup
    lateinit var ordinaire: RadioButton
    lateinit var rdv: RadioButton
    lateinit var programmerSatut: String
    lateinit var programmerPrix: String
    var isFormCorrect: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        initDateNaissance()
        setSpinnerSite()
        setSpinnerSexe()
        setSpinnerProfession()
        setRadioGroup()
        setButtonValider()
    }

    fun setSpinnerSite() {
        spinnerSite = findViewById(R.id.spinner_site)
        val adapter: ArrayAdapter<String> = ArrayAdapter(this@FormulaireActivity,
            android.R.layout.simple_spinner_item,
            arrayOf("CMU POGBI", "CHU TENGANDGO", "CERBA (EX CANDAF)", "AEROPORT HALL PELERINS")
        )
        spinnerSite.setAdapter(adapter)
    }

    fun setSpinnerSexe() {
        spinnerSexe = findViewById(R.id.spinner_sexe)
        val adapter: ArrayAdapter<String> = ArrayAdapter(this@FormulaireActivity,
            android.R.layout.simple_spinner_item,
            arrayOf("Masculin", "Féminin")
        )
        spinnerSexe.setAdapter(adapter)
    }

    fun setSpinnerProfession() {
        spinnerProfession = findViewById(R.id.spinner_profession)
        val adapter: ArrayAdapter<String> = ArrayAdapter(this@FormulaireActivity,
            android.R.layout.simple_spinner_item,
            arrayOf("Élève", "Étudiant", "Fonctionnaire", "Commerçant", "Autre")
        )
        spinnerProfession.setAdapter(adapter)
    }

    fun setRadioGroup() {
        programmerSatut = getString(R.string.ordinaire)
        programmerPrix = "50000 FCFA"
        programmer = findViewById(R.id.programmer)
        ordinaire = findViewById(R.id.ordinaire)
        rdv = findViewById(R.id.rdv)
        programmer.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.ordinaire -> {
                    programmerSatut = getString(R.string.ordinaire)
                    programmerPrix = "50000 FCFA"
                }
                R.id.rdv -> {
                    programmerSatut = getString(R.string.rdv)
                    programmerPrix = "60000 FCFA"
                }
            }
        }
    }

    fun setButtonValider() {
        nomInput = findViewById(R.id.nom)
        prenomInput = findViewById(R.id.prenom)
        telephoneInput = findViewById(R.id.telephone)
        buttonValider = findViewById(R.id.btnValider)
        buttonValider.setOnClickListener {
            if(checkForm())
                saveForm()
        }
    }

    fun saveForm() {
        val sdf = SimpleDateFormat("dd/MM/yyyy 'à' HH:mm:ss")
        val currentDateandTime = sdf.format(Date())

        val preferences = this.getSharedPreferences("form", Context.MODE_PRIVATE)
        val edit = preferences.edit()
        val form = JSONObject()
        form.put("site", spinnerSite.text.toString())
        form.put("nom", nomInput.text.toString())
        form.put("prenom", prenomInput.text.toString())
        form.put("telephone", telephoneInput.text.toString())
        form.put("programmer", programmerSatut)
        form.put("date", currentDateandTime)
        form.put("prix", programmerPrix)

        // save in preference
        edit.putBoolean("content", true)
        edit.putString("form", form.toString())
        edit.commit()

        // save to the server
        val data = HashMap<String, String?>()
        data.put("site", spinnerSite.text.toString())
        data.put("nom_prenom", nomInput.text.toString() + " " + prenomInput.text.toString())
        data.put("date_naissance", dateNaissance.text.toString())
        data.put("telephone", telephoneInput.text.toString())
        data.put("profession", spinnerProfession.text.toString())
        data.put("sexe", spinnerSexe.text.toString())
        data.put("programmer", programmerSatut)
        data.put("prix", programmerPrix)

        // before request
        buttonValider.isEnabled = false

        // request
        VolleyRequest.post(this@FormulaireActivity, ApiCovidConst.PATH_SAVE_INFO, data, object: VolleyRequest.Callback() {
            override fun onSuccess(data: String) {
                val res = JSONObject(data)
                val preferences = this@FormulaireActivity.getSharedPreferences("form", Context.MODE_PRIVATE)
                val edit = preferences.edit()
                edit.putString("id", res.getString("id"))
                edit.commit()

                buttonValider.isEnabled = true
                startActivity(Intent(this@FormulaireActivity, PaiementActivity::class.java))
            }

            override fun onFail() {
                buttonValider.isEnabled = true
                startActivity(Intent(this@FormulaireActivity, PaiementActivity::class.java))
                Toast.makeText(this@FormulaireActivity, "Problème de connexion!!!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun initDateNaissance() {
        dateNaissancePicker = findViewById(R.id.dateNaissancePicker)
        dateNaissance = findViewById(R.id.dateNaissance)
        dateNaissance.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) showDatePicker()
        }
    }

    fun showDatePicker() {
        val day = 1
        val month = 1
        val year = 2021
        val picker = DatePickerDialog(this@FormulaireActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dateNaissance.setText(
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                )
            }, year, month, day
        )
        if(!picker.isShowing) picker.show()
    }

    fun checkForm(): Boolean {
        isFormCorrect = true
        if(TextUtils.isEmpty(spinnerSite.text.toString())) isFormCorrect = false
        if(TextUtils.isEmpty(nomInput.text.toString())) isFormCorrect = false
        if(TextUtils.isEmpty(prenomInput.text.toString())) isFormCorrect = false
        if(TextUtils.isEmpty(dateNaissance.text.toString())) isFormCorrect = false
        if(TextUtils.isEmpty(telephoneInput.text.toString())) isFormCorrect = false
        if(TextUtils.isEmpty(spinnerProfession.text.toString())) isFormCorrect = false
        if(!isFormCorrect) Toast.makeText(this@FormulaireActivity, "Veuillez remplir tous les champs s'il vous plaît", Toast.LENGTH_LONG).show()
        return isFormCorrect
    }
}