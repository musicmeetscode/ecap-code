package ug.global.ecap_code.database

import android.content.Context
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.preference.PreferenceManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import org.json.JSONObject

@Entity
data class VisitInfo(
    @ColumnInfo @Bindable var visit_reason: String = "",
    @ColumnInfo @Bindable var chief_complaint: String = "",
    @ColumnInfo @Bindable var history_of_present_illiness: String = "",
    @ColumnInfo @Bindable var review_of_systems: String = "",
    @ColumnInfo @Bindable var physical_examination: String = "",
    @ColumnInfo @Bindable var triage: String = "",
    @ColumnInfo @Bindable var attendance: String = "",
    @ColumnInfo @Bindable var special_program: String = "",
    @ColumnInfo @Bindable var encouter_class: String = "",
    @ColumnInfo @Bindable var admission_type: String = "",
    @ColumnInfo @Bindable var encouter_type: String = "",
    @ColumnInfo @Bindable var visit_status: String = "",
    @ColumnInfo @Bindable var mode_of_arrival: String = "",
    @ColumnInfo @Bindable var level_of_care: String = "",
    @ColumnInfo @Bindable var patient_condition: String = ""
) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    @Bindable
    var id: Int = 0

    @ColumnInfo(defaultValue = "0")
    var online_id: Int = 0

    @ColumnInfo(defaultValue = "0")
    var patient: Int = 0


    fun toJson(context: Context): JSONObject {
        val json = JSONObject(Gson().toJson(this))
        EcapDatabase.getInstance(context).ecapDao().apply {
            json.put("triage", this.getFillerDataId(triage))
            json.put("attendance", this.getFillerDataId(attendance))
            json.put("special_program", this.getFillerDataId(special_program))
            json.put("encounter_class", this.getFillerDataId(encouter_class))
            json.put("admission_type", this.getFillerDataId(admission_type))
            json.put("encounter_type", this.getFillerDataId(encouter_type))
            json.put("visit_status", this.getFillerDataId(visit_status))
            json.put("mode_of_arrival", this.getFillerDataId(mode_of_arrival))
            json.put("level_of_care", this.getFillerDataId(level_of_care))
            json.put("patient_condition", this.getFillerDataId(patient_condition))
            json.put("staff", PreferenceManager.getDefaultSharedPreferences(context).getInt("staff", 0))
            val keys = json.keys() // Iterator of keys
            while (keys.hasNext()) {
                val key = keys.next()
                val value = json.opt(key)

                if ((value is Int && value == 0) || (value is String && value == "0")) {
                    json.put(key, JSONObject.NULL) // Set zero values to null
                }
            }
            return json
        }
    }
}

@Entity
data class AssessmentForm(
    @ColumnInfo @Bindable var forgetfulness: Boolean = false,
    @ColumnInfo @Bindable var apathy: Boolean = false,
    @ColumnInfo @Bindable var emotion: Boolean = false,
    @ColumnInfo @Bindable var activities: Boolean = false,
    @ColumnInfo @Bindable var memoryProblems: String = "yes",
    @ColumnInfo @Bindable var keyRoles: String = "no",
    @ColumnInfo @Bindable var progressing: String = "no",
    @ColumnInfo @Bindable var anyOf: String = "no",
    @ColumnInfo @Bindable var depress: String = "no",
    @ColumnInfo @Bindable var otherAny: String = "no",
    @ColumnInfo @Bindable var anaemia: String = "no",
    @ColumnInfo @Bindable var cardio: String = "no",
    @ColumnInfo @Bindable var caretaker: String = "no",
    @ColumnInfo @Bindable var careMood: String = "no",
    @ColumnInfo @Bindable var careIncome: String = "no",
    @ColumnInfo @Bindable var behavior: String = "no",
    @ColumnInfo @Bindable var hasDementia: String = "no",
    @ColumnInfo @Bindable var management: String = "",
) : BaseObservable() {

    fun matchDifference(): String {
        val diagnosis = getDiagnosis()
        val matchDifference = if (diagnosis.first == hasDementia) {
            "Correctly Identified. Match"
        } else {
            "Mismatch: Expected '${diagnosis.first}', but got '$hasDementia'\nReason for diagnosis: ${diagnosis.second}"
        }

        return "$hasDementia\n$matchDifference"
    }

    private fun getDiagnosis(): Pair<String, String> {
        val hasMemoryIssues = memoryProblems == "yes"
        val hasActivityDifficulties = activities
        val isProgressing = progressing == "yes"
        val isDepressed = depress == "yes"
        val unUsualCase = otherAny == "yes"
        val hasBehavioralSymptoms = behavior == "yes"
        val hasAbruptSymptoms = anyOf == "yes"

        when {
            hasMemoryIssues || hasActivityDifficulties -> {
                when {
                    isProgressing -> {
                        // If symptoms are progressing
                        when {
                            !isDepressed && !unUsualCase -> {
                                // No depression and no unusual features, likely dementia
                                return Pair(
                                    "Dementia",
                                    "Symptoms are progressing with memory issues and difficulty in activities, indicating dementia."
                                )
                            }

                            unUsualCase -> {
                                // Unusual features, further investigation needed
                                return Pair(
                                    "Unusual dementia features",
                                    "Symptoms have unusual features, which requires further investigation to rule out other conditions."
                                )
                            }

                            isDepressed -> {
                                // Depression is present
                                return Pair(
                                    "Depression",
                                    "Symptoms with depression suggest that depression could be the cause, requiring treatment for depression."
                                )
                            }
                        }
                    }

                    else -> {
                        // If symptoms have not been progressing for 6 months
                        return when {
                            hasAbruptSymptoms -> {
                                // Abrupt onset, likely delirium
                                Pair(
                                    "Delirium",
                                    "Sudden onset of symptoms without a progressive history suggests delirium, requiring immediate medical attention."
                                )
                            }
                            // Symptoms haven't been progressing but no abrupt onset, likely dementia
                            else -> Pair(
                                "Dementia",
                                "Symptoms have not progressed over 6 months, but the pattern suggests dementia. Proceed to evaluate for depression."
                            )
                        }
                    }
                }
            }
        }
        // If the person doesn't have memory or activity difficulties, return not dementia
        return Pair("Dementia Unlikely", "No memory issues and no difficulty performing activities, so dementia is unlikely.")
    }

    fun buildAbstract(): String {
        return "Memory and Orientation Issues:\n" +
                "- The patient ${if (this.forgetfulness) "has" else "doesn't have"} severe forgetfulness or a decline in memory and orientation.\n" +
                "\nMood and Behavioral Issues:\n" +
                "- The patient ${if (this.apathy) "has" else "doesn't have"} apathy (appearing uninterested) or irritability.\n" +
                "- The patient ${if (this.emotion) "has" else "doesn't have"} loss of emotional control (easily upset, irritable, or tearful).\n" +
                "\nFunctional Challenges:\n" +
                "- The patient ${if (this.activities) "has" else "doesn't have"} difficulties carrying out usual work, domestic, or social activities.\n" +
                "- There ${if (this.memoryProblems == "yes") "are" else "are no"} problems with memory and/or orientation.\n" +
                "- The patient ${if (this.keyRoles == "yes") "has" else "doesn't have"} difficulties performing key roles or activities.\n" +
                "- The symptoms ${if (this.progressing == "yes") "have" else "haven't"} been present and slowly progressing for at least 6 months.\n" +
                "\nHealth and Lifestyle Issues:\n" +
                "- The patient ${if (this.depress == "yes") "has" else "doesn't have"} moderate to severe depression.\n" +
                "- The patient ${if (this.anaemia == "yes") "has" else "doesn't have"} poor dietary intake, malnutrition, or anemia.\n" +
                "- The patient ${if (this.cardio == "yes") "has" else "doesn't have"} cardiovascular risk factors.\n" +
                "\nCarer Strain and Challenges:\n" +
                "- The carer ${if (this.caretaker == "yes") "is" else "isn't"} having difficulty coping or experiencing strain.\n" +
                "- The carer ${if (this.careMood == "yes") "has" else "doesn't have"} a depressed mood.\n" +
                "- The carer ${if (this.careIncome == "yes") "is" else "isn't"} facing loss of income and/or additional expenses due to caregiving.\n"

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0


    @ColumnInfo(defaultValue = "0")
    var online_id: Int = 0

    @ColumnInfo(defaultValue = "0")
    var visit: Int = 0

}

@Entity
data class FillerData(
    @ColumnInfo var dataid: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var others: String,
    @ColumnInfo var type: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0
    override fun toString(): String {
        return this.name
    }
}

@Entity
data class Patient(
    @ColumnInfo @Bindable var f_name: String = "",
    @ColumnInfo @Bindable var l_name: String = "",
    @ColumnInfo @Bindable var mo_name: String = "",
    @ColumnInfo @Bindable var gender: String = "",
    @ColumnInfo @Bindable var nin: String = "",
    @ColumnInfo @Bindable var phone: String = "",
    @ColumnInfo @Bindable var suffix: String = "",
    @ColumnInfo @Bindable var prefix: String = "",
    @ColumnInfo @Bindable var blood_group: String = "",
    @ColumnInfo @Bindable var marital_status: String = "",
    @ColumnInfo @Bindable var occupation: String = "",
    @ColumnInfo @Bindable var dob: String = "",
    @ColumnInfo @Bindable var age: String = "",
    @ColumnInfo @Bindable var tribe: String = "",
    @ColumnInfo @Bindable var language: String = "",
    @ColumnInfo @Bindable var nationality: String = "",
    @ColumnInfo @Bindable var district: String = "",
    @ColumnInfo @Bindable var village: String = "",
    @ColumnInfo @Bindable var religion: String = "",
) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0

    fun getHash(context: Context): String {
        return "#SJHA${timeStamp}ECA${id}" + PreferenceManager.getDefaultSharedPreferences(context).getInt("staff", 0)
    }

    @ColumnInfo(defaultValue = "0")
    var online_id: Int = 0

    fun toJson(context: Context): JSONObject {
        val json = JSONObject(Gson().toJson(this))
        EcapDatabase.getInstance(context).ecapDao().apply {
            json.put("gender", this.getFillerDataId(gender))
            json.put("suffix", this.getFillerDataId(suffix))
            json.put("prefix", this.getFillerDataId(prefix))
            json.put("blood_group", this.getFillerDataId(blood_group))
            json.put("marital_status", this.getFillerDataId(marital_status))
            json.put("tribe", this.getFillerDataId(tribe))
            json.put("language", this.getFillerDataId(language))
            json.put("district", this.getFillerDataId(district))
            json.put("nationality", this.getFillerDataId(nationality))
            json.put("village", this.getFillerDataId(village))
            json.put("religion", this.getFillerDataId(religion))
            json.put("from_ecap", true)
            json.put("reg_no", "#SJHA${timeStamp}ECA${id}" + PreferenceManager.getDefaultSharedPreferences(context).getInt("staff", 0))
            json.put("staff", PreferenceManager.getDefaultSharedPreferences(context).getInt("staff", 0))
            json.remove("dob")
            val keys = json.keys() // Iterator of keys
            while (keys.hasNext()) {
                val key = keys.next()
                val value = json.opt(key)

                if ((value is Int && value == 0) || (value is String && value == "0")) {
                    json.put(key, JSONObject.NULL) // Set zero values to null
                }
            }
        }

        return json
    }

    @ColumnInfo
    var timeStamp: Long = System.currentTimeMillis()

    fun isValid(): Pair<Boolean, List<String>> {
        val errors = mutableListOf<String>()

        // Check if the first name is empty
        if (f_name.isEmpty()) {
            errors.add("First name is required.")
        }

        // Check if the last name is empty
        if (l_name.isEmpty()) {
            errors.add("Last name is required.")
        }

        // Check if gender is empty
        if (gender.isEmpty()) {
            errors.add("Gender is required.")
        }

        // Check if either age or date of birth is provided
        if (age.isEmpty() && dob.isEmpty()) {
            errors.add("Either age or date of birth is required.")
        }

        // Return true if no errors, otherwise return false and the error messages
        return if (errors.isEmpty()) {
            Pair(true, emptyList())
        } else {
            Pair(false, errors)
        }
    }


}

@Entity
class QuizItem(
    @ColumnInfo var question: String,
    @ColumnInfo var options1: String,
    @ColumnInfo var options2: String,
    @ColumnInfo var options3: String,
    @ColumnInfo var options4: String,
    @ColumnInfo var options5: String,
    @ColumnInfo var options6: String,
    @ColumnInfo var options7: String,
    @ColumnInfo var answer: String,
    @ColumnInfo(defaultValue = "") var checked: String = "",
    @ColumnInfo(defaultValue = "") var postChecked: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0

    fun getQId(): String {
        return "QN. ${this.id}"
    }

    fun getOptionVisibility(optionIndex: Int): Int {
        return when (optionIndex) {
            1 -> if (options1.isNotBlank()) View.VISIBLE else View.GONE
            2 -> if (options2.isNotBlank()) View.VISIBLE else View.GONE
            3 -> if (options3.isNotBlank()) View.VISIBLE else View.GONE
            4 -> if (options4.isNotBlank()) View.VISIBLE else View.GONE
            5 -> if (options5.isNotBlank()) View.VISIBLE else View.GONE
            6 -> if (options6.isNotBlank()) View.VISIBLE else View.GONE
            7 -> if (options7.isNotBlank()) View.VISIBLE else View.GONE
            else -> View.GONE
        }
    }

    fun getPassString(): Pair<String, String> {
        return Pair(
            if (calculateScores().first > 60) {
                "Pass"
            } else {
                "Fail"
            }, if (calculateScores().second > 60) {
                "Pass"
            } else {
                "Fail"
            }
        )
    }

    fun calculateScores(): Pair<Int, Int> {


        val answerList = this.answer.split(",").map { it.trim().toInt() }
        val checkedList = this.checked.replace("[", "").replace("]", "").split(",").filter { it.isNotEmpty() }.map { it.trim().toInt() }
        val postCheckedList =
            this.postChecked.replace("[", "").replace("]", "").split(",").filter { it.isNotEmpty() }.map { it.trim().toInt() }

        val firstScore = (100 * checkedList.count { it in answerList }) / answerList.size
        val secondScore = (100 * postCheckedList.count { it in answerList }) / answerList.size


        return Pair(firstScore, secondScore)

    }


    @ColumnInfo
    var timeStamp: Long = System.currentTimeMillis()

    val isAnswerChecked: Boolean
        get() = postChecked.isEmpty()

}

data class PatientHolder(var name: String, var hash: String, var created: String, var id: Int)
data class FeedbackItem(var name: String, var hash: String, var diagosis: String)