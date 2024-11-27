package ug.global.ecap_code.util

object URLS {
    const val BASE_URL = "http://192.168.100.212:8000"
    private const val API_URL = "${BASE_URL}/api"
    const val LOGIN_URL = "$API_URL/init/login/"

    const val PATIENT_POST = "$API_URL/data/patients/"
    const val VISIT_POST = "$API_URL/data/visits/"
    const val CLERK_POST = "$API_URL/data/clerk/"
}
