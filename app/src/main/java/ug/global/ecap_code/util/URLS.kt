package ug.global.ecap_code.util

object URLS {
    const val BASE_URL = "http://192.168.100.69:8000"
    private const val API_URL = "${BASE_URL}/api"
    const val LOGIN_URL = "$API_URL/init/login/"

    const val PATIENT_POST = "$API_URL/patients/post/"
}
