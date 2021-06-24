package com.sunu.mhr.service

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class VolleyRequest {
    companion object {
        public const val BASE_URL = "https://sunuapps.digital/mrh/api"
        private lateinit var queue: RequestQueue

        fun post(context: Context, path: String, data: HashMap<String, String?>, callback: Callback) {
            queue = Volley.newRequestQueue(context)
            val stringRequest = object: StringRequest(Request.Method.POST, path, Response.Listener<String> { response ->
                callback.onSuccess(response)
            }, Response.ErrorListener {
                callback.onFail()
            }) {
                override fun getParams(): Map<String, String?> {
                    val params = HashMap<String, String?>()
                    params.putAll(data)
                    return params
                }
            }

            queue.add(stringRequest)
        }

        fun get(context: Context, path: String, callback: Callback) {
            queue = Volley.newRequestQueue(context)
            val stringRequest = object: StringRequest(Request.Method.GET, path, Response.Listener<String> { response ->
                callback.onSuccess(response)
            }, Response.ErrorListener {
                callback.onFail()
            }) {
                override fun getParams(): Map<String, String?> {
                    val params = HashMap<String, String?>()
                    return super.getParams()
                }
            }

            queue.add(stringRequest)
        }
    }

    abstract class Callback() {
        abstract fun onSuccess(data: String)
        abstract fun onFail()
    }
}