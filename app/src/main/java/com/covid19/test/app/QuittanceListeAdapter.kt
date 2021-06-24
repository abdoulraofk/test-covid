package com.covid19.test.app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class QuittanceListeAdapter: BaseAdapter {
    private lateinit var listeQuittance: JSONArray
    private lateinit var _context: Context

    constructor(__context: Context, liste: JSONArray) {
        _context = __context
        listeQuittance = liste
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(_context)
        val v = inflater.inflate(R.layout.list_quittance_item, parent, false)

        val nomTextView = v?.findViewById<TextView>(R.id.nom)
        val created_at = v?.findViewById<TextView>(R.id.created_at)
        val quittance = getItem(position)

        nomTextView?.setText(quittance.getString("nom"))
        created_at?.setText(quittance.getString("created_at"))

        setClickItem(v!!)

        return v!!
    }

    override fun getItem(position: Int): JSONObject {
        return listeQuittance.getJSONObject(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listeQuittance.length()
    }

    fun setClickItem(view: View) {
        view.setOnClickListener {
            _context.startActivity(Intent(_context, QuittancePdfActivity::class.java))
        }
    }
}