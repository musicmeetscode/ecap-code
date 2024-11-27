package ug.global.ecap_code

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.FillerData
import ug.global.ecap_code.database.QuizItem
import ug.global.ecap_code.databinding.ActivityMainBinding
import ug.global.ecap_code.sheets.LoginBottomSheet
import ug.global.ecap_code.util.AppCallBacks
import ug.global.ecap_code.util.MySingleton
import ug.global.ecap_code.util.URLS

class ActivityMain : AppCompatActivity(), AppCallBacks {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val loginBottomSheet by lazy { LoginBottomSheet() }
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_quizzed_post", true)) {
            binding.bottomNavigationView.menu.removeItem(R.id.patientsFragment)
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("token", "none") == "none") {
            loginBottomSheet.show(supportFragmentManager, "login_sheet", this, this@ActivityMain)
            loginBottomSheet.isCancelable = false
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_quizzed_", true)) {
                finish()
                startActivity(Intent(this, ActivityQuiz::class.java))
            } else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_trained", true)) {
                finish()
                startActivity(Intent(this, ActivityMhGap::class.java))
            } else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_quizzed_post", true)) {
                finish()
                startActivity(Intent(this, ActivityQuiz::class.java))
            }
        }
    }

    override fun loggedIn(json: JSONObject) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("ecap_file", json.getJSONObject("file").getString("training"))
            .apply()
        lifecycleScope.launch(IO) {
            val quizzes = json.getJSONArray("quizzes")
            for (i in 0 until quizzes.length()) {
                val quiz = quizzes.getJSONObject(i)
                EcapDatabase.getInstance(this@ActivityMain).ecapDao().addQuiz(
                    QuizItem(
                        quiz.getString("question"),
                        quiz.getString("options1"),
                        quiz.getString("options2"),
                        quiz.getString("options3"),
                        quiz.getString("options4"),
                        quiz.getString("options5"),
                        quiz.getString("options6"),
                        quiz.getString("options7"),
                        quiz.getString("answer"),
                    )
                )
            }
            val keys = json.getJSONObject("data").keys()
            while (keys.hasNext()) {
                val key = keys.next()

                val value = json.getJSONObject("data").getJSONArray(key)
                for (i in 0 until value.length()) {
                    val inner = value.getJSONObject(i)
                    EcapDatabase.getInstance(this@ActivityMain).ecapDao().addFillerData(
                        FillerData(
                            inner.getInt("id"),
                            inner.getString("name"),
                            key.lowercase(),
                            key.lowercase(),
                        )
                    )
                }
            }
        }
        loginBottomSheet.dismiss()
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_quizzed_", true)) {
            startActivity(Intent(this, ActivityQuiz::class.java))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_account -> {
                val logout = MaterialAlertDialogBuilder(this).create()
                logout.setTitle("Logout")
                logout.setMessage(getString(R.string.logout_warning))
                logout.setButton(BUTTON_POSITIVE, getString(R.string.yes_logout)) { _, _ ->
                    PreferenceManager.getDefaultSharedPreferences(this).edit().remove("token").remove("user").apply()
                    lifecycleScope.launch(IO) {
                        EcapDatabase.getInstance(this@ActivityMain).ecapDao().deleteFillers()
                    }
                    recreate()
                }
                logout.setButton(BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ -> }
                logout.show()
                return true
            }

            R.id.action_upload -> {
                val uploadAlert = MaterialAlertDialogBuilder(this).create()
                uploadAlert.setTitle("Data Upload")
                uploadAlert.setCancelable(false)
                uploadAlert.setButton(BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ -> }
                lifecycleScope.launch(IO) {
                    var uploaded = 0
                    val db = EcapDatabase.getInstance(this@ActivityMain).ecapDao()
                    val patients = db.getAllPatients()
                    MainScope().launch {
                        uploadAlert.setMessage(getString(R.string.please_wait, patients.size))
                    }
                    patients.forEach { patient ->
                        val patientJson = patient.toJson(this@ActivityMain)
                        val visit = db.getPatientVisit(patient.id)
                        val assessment = db.getPatientAssessment(visit.id)
                        val visitJson = visit.toJson(this@ActivityMain)

                        val clerkJson = JSONObject().put("chief_complain", visit.chief_complaint)
                            .put("history_of_present_illiness", visit.history_of_present_illiness)
                            .put("physical_examination", visit.physical_examination).put("review_of_systems", visit.review_of_systems)
                            .put("recommendations", assessment.management).put(
                                "preliminary_diagnosis", if (assessment.hasDementia == "yes") {
                                    "Dementia"
                                } else {
                                    "None/Delirium"
                                }
                            ).put("abstract", assessment.buildAbstract())
                        val patientJsonObject = JSONObject().put("patient", patientJson).put("visit", visitJson).put("clerk", clerkJson)
                        Log.e("TAG", "onOptionsItemSelected: " + patientJsonObject)
                        MySingleton.getInstance(this@ActivityMain)
                            ?.addToRequestQueue(object : JsonObjectRequest(POST, URLS.PATIENT_POST, patientJsonObject, { respo ->
                                patient.online_id = respo.getJSONObject("patient").getInt("id")
                                uploaded += 1
                                if (uploaded == patients.size) {
                                    MainScope().launch {
                                        uploadAlert.dismiss()
                                        Snackbar.make(binding.root, "Upload success", LENGTH_LONG).show()
                                    }
                                }
                                lifecycleScope.launch(IO) {
                                    db.insertPatient(patient)
                                }
                            }, { error ->
                                Log.e("TAG", "onOptionsItemSelected:" + error)
                                uploaded += 1
                                if (uploaded == patients.size) {
                                    MainScope().launch {
                                        uploadAlert.dismiss()
                                        Snackbar.make(binding.root, "Upload success with some errors", LENGTH_LONG).show()
                                    }
                                }
                            }) {
                                override fun getHeaders(): Map<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["Accept"] = "application/json;"
                                    headers["Authorization"] = "Token " + PreferenceManager.getDefaultSharedPreferences(this@ActivityMain)
                                        .getString("token", "token")
                                    return headers
                                }
                            })
                    }
                }
                uploadAlert.show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}