package ug.global.ecap_code.util

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MySingleton private constructor(private val context: Context) {
    private var requestQueue: RequestQueue?
    private fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue as RequestQueue
    }

    fun <T> addToRequestQueue(request: Request<T>?) {
        getRequestQueue().add(request)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MySingleton? = null

        @Synchronized
        fun getInstance(context: Context): MySingleton? {
            if (instance == null) {
                instance = MySingleton(context)
            }
            return instance
        }
    }

    init {
        requestQueue = getRequestQueue()
    }
}