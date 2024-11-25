package ug.global.ecap_code.database

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FillerData(
    @ColumnInfo var dataid: Int, @ColumnInfo var name: String, @ColumnInfo var others: String, @ColumnInfo var type: String
) {
    @PrimaryKey(autoGenerate = true)
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
    var id: Int = 0

    @ColumnInfo
    var timeStamp: Long = System.currentTimeMillis()

}

