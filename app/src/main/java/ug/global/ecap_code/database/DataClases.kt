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
    fun getAssessmentSummary(): String {
        val statements = listOf(
            "The person has %s forgetfulness and orientation problems." to forgetfulness,
            "The person %s mood or behavioral problems such as apathy or irritability." to apathy,
            "The person %s difficulty controlling emotions, often becoming upset, irritable, or tearful." to emotion,
            "The person %s difficulties carrying out usual work, domestic, or social activities." to activities,
            "There %s memory and/or orientation problems." to memoryProblems,
            "The person %s difficulties performing key roles or activities." to keyRoles,
            "The symptoms have %s present and slowly progressing for at least 6 months." to progressing,
            "The person %s moderate to severe depression." to depress,
            "The person %s poor dietary intake, malnutrition, or anaemia." to anaemia,
            "The person %s cardiovascular risk factors." to cardio,
            "The carer %s difficulty coping or experiencing strain." to caretaker,
            "The carer %s a depressed mood." to careMood,
            "The carer %s facing loss of income or additional expenses due to care needs." to careIncome
        )

        return buildString {
            statements.forEach { (template, condition) ->
                appendLine(template.format(if (condition as Boolean) "has" else "does not have"))
            }
        }
    }

    fun buildAbstract(): String {
        return "Decline or problems with memory (severe forgetfulness) and orientation ${this.forgetfulness}\n" +
                "Mood or behavioural problems such as apathy (appearing uninterested) or irritability  ${this.apathy}\n" +
                "Loss of emotional control-easily upset, irritable, or tearful  ${this.emotion}\n" +
                "Difficulties in carrying out usual work, domestic, or social activities ${this.activities}\n" +
                "Are there problems with memory and/or orientation? ${this.memoryProblems}\n" +
                "Does the person have difficulties in performing key roles/activities? ${this.keyRoles}\n" +
                "Have the symptoms been present and slowly progressing for at least 6 months? ${this.progressing}\n" +
                "Does the person have moderate to severe DEPRESSION? ${this.depress}\n" +
                "Does the person have poor dietary intake, malnutrition, or anaemia? ${this.anaemia}\n" +
                "Does the person have cardiovascular risk factors? ${this.cardio}\n" +
                "Is the carer having difficulty coping or experiencing strain? ${this.caretaker}\n" +
                "Is the carer experiencing depressed mood?  ${this.careMood}\n" +
                "Is the carer facing loss of income and/or additional expenses because of the needs for care? ${this.careIncome}\n"
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
            json.put("reg_no", "#SJHAPA${id}")
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

    fun isValid(): Boolean {
        return f_name.isNotEmpty() && l_name.isNotEmpty() && gender.isNotEmpty() && (age.isNotEmpty() or dob.isNotEmpty())
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
