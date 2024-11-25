package ug.global.ecap_code.util

import android.widget.AutoCompleteTextView
import org.json.JSONObject

interface AppCallBacks {
    fun loggedIn(json: JSONObject)
}

interface PatientDataCallBacks {
    fun infoComplete()
    fun vitalsComplete()
    fun assessmentComplete()
    fun getFillerData(type: String, view: AutoCompleteTextView)
}