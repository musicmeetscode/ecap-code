package ug.global.ecap_code.database

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VisitInfo(
    @ColumnInfo @Bindable var visitReason: String? = "",
    @ColumnInfo @Bindable var chiefComplaint: String? = "",
    @ColumnInfo @Bindable var illnessHistory: String? = "",
    @ColumnInfo @Bindable var reviewOfSystems: String? = "",
    @ColumnInfo @Bindable var physicalExam: String? = "",
    @ColumnInfo @Bindable var triage: String? = "",
    @ColumnInfo @Bindable var attendance: String? = "",
    @ColumnInfo @Bindable var specialProgram: String? = "",
    @ColumnInfo @Bindable var encounterClass: String? = "",
    @ColumnInfo @Bindable var admissionType: String? = "",
    @ColumnInfo @Bindable var encounterType: String? = "",
    @ColumnInfo @Bindable var visitStatus: String? = "",
    @ColumnInfo @Bindable var modeOfArrival: String? = "",
    @ColumnInfo @Bindable var levelOfCare: String? = "",
    @ColumnInfo @Bindable var patientCondition: String? = ""
) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    @Bindable
    var id: Int = 0

    @ColumnInfo(defaultValue = "0")
    var patient: Int = 0
}

@Entity
data class AssessmentForm(
    @ColumnInfo @Bindable var forgetfulness: Boolean = false, // Switch for "Forgetfulness"
    @ColumnInfo @Bindable var apathy: Boolean = false, // Switch for "Apathy"
    @ColumnInfo @Bindable var emotion: Boolean = false, // Switch for "Emotion"
    @ColumnInfo @Bindable var activities: Boolean = false, // Switch for "Activities"

    @ColumnInfo @Bindable var memoryProblems: String = "yes", // Selected option for "Memory Problems" radio group
    @ColumnInfo @Bindable var keyRoles: String = "no", // Selected option for "Key Roles" radio group

    @ColumnInfo @Bindable var progressing: String = "no",// Selected option for "Progressing" radio group
    @ColumnInfo @Bindable var anyOf: String = "no", // Selected option for "Any Of" radio group

    @ColumnInfo @Bindable var depress: String = "no", // Selected option for "Depress" radio group
    @ColumnInfo @Bindable var otherAny: String = "no", // Selected option for "Other Any" radio group
    @ColumnInfo @Bindable var anaemia: String = "no", // Selected option for "Anaemia" radio group
    @ColumnInfo @Bindable var cardio: String = "no",// Selected option for "Cardio" radio group

    @ColumnInfo @Bindable var caretaker: String = "no", // Selected option for "Caretaker" radio group
    @ColumnInfo @Bindable var careMood: String = "no", // Selected option for "Care Mood" radio group
    @ColumnInfo @Bindable var careIncome: String = "no",// Selected option for "Care Income" radio group

    @ColumnInfo @Bindable var behavior: String = "no", // Selected option for "Behavior" radio group
) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0

    @ColumnInfo(defaultValue = "0")
    var patient: Int = 0

    fun hasMemoryIssues(): Int {
        return if (this.memoryProblems == "Yes") View.VISIBLE else View.GONE
    }
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
    @ColumnInfo @Bindable var firstName: String = "",
    @ColumnInfo @Bindable var lastName: String = "",
    @ColumnInfo @Bindable var otherName: String = "",
    @ColumnInfo @Bindable var sexGender: String = "",
    @ColumnInfo @Bindable var ninPassport: String = "",
    @ColumnInfo @Bindable var phoneNumber: String = "",
    @ColumnInfo @Bindable var suffix: String = "",
    @ColumnInfo @Bindable var prefix: String = "",
    @ColumnInfo @Bindable var bloodGroup: String = "",
    @ColumnInfo @Bindable var maritalStatus: String = "",
    @ColumnInfo @Bindable var occupation: String = "",
    @ColumnInfo @Bindable var dateOfBirth: String = "",
    @ColumnInfo @Bindable var age: String = "",
    @ColumnInfo @Bindable var tribe: String = "",
    @ColumnInfo @Bindable var mainLanguageSpoken: String = "",
    @ColumnInfo @Bindable var nationality: String = "",
    @ColumnInfo @Bindable var district: String = "",
    @ColumnInfo @Bindable var village: String = "",
    @ColumnInfo @Bindable var religion: String = ""
) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0

    @ColumnInfo
    var timeStamp: Long = System.currentTimeMillis()

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
//        return quizItems.map { item ->
        // Parse the answer, checked, and postChecked fields into lists of integers
        val answerList = this.answer.split(",").map { it.trim().toInt() }
        val checkedList = this.checked.replace("[", "").replace("]", "").split(",").filter { it.isNotEmpty() }.map { it.trim().toInt() }
        val postCheckedList =
            this.postChecked.replace("[", "").replace("]", "").split(",").filter { it.isNotEmpty() }.map { it.trim().toInt() }

        val firstScore = (100 * checkedList.count { it in answerList }) / answerList.size
        val secondScore = (100 * postCheckedList.count { it in answerList }) / answerList.size

        // Return the pair of scores
        return Pair(firstScore, secondScore)
//        }
    }


    @ColumnInfo
    var timeStamp: Long = System.currentTimeMillis()

    val isAnswerChecked: Boolean
        get() = postChecked.isEmpty()

}

data class PatientHolder(var name: String, var hash: String, var created: String, var id: Int)
