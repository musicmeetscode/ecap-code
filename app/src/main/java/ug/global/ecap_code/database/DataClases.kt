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

