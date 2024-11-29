package ug.global.ecap_code.util

import android.widget.AutoCompleteTextView
import org.json.JSONObject
import ug.global.ecap_code.database.AssessmentForm
import ug.global.ecap_code.database.Patient
import ug.global.ecap_code.database.VisitInfo

interface AppCallBacks {
    fun loggedIn(json: JSONObject)
}

interface PatientDataCallBacks {
    fun infoComplete(patient: Patient)
    fun vitalsComplete(visit: VisitInfo)
    fun assessmentComplete(asses: AssessmentForm)
    fun getFillerData(type: String, view: AutoCompleteTextView)
    fun clearErrors()
    fun setErrors(stringArray: List<String>)
}

interface PatientCallBacksFinish {
    fun newPatientAdded(patient: Patient)
}