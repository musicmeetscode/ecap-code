package ug.global.ecap_code.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert

@Dao
interface EcapDAO {
    @Query("SELECT * FROM FillerData WHERE type=:type")
    fun getFillerData(type: String): List<FillerData>

    @Query("DELETE FROM FillerData")
    fun deleteFillers()

    @Upsert
    fun addFillerData(fillerData: FillerData)

    @Query("SELECT * FROM patient")
    fun getAllPatients(): List<Patient>

    @Upsert
    fun insertPatient(patient: Patient)
}

@Database(
    entities = [Patient::class, FillerData::class, AssessmentForm::class, VisitInfo::class],
    version = 5,
)
abstract class EcapDatabase : RoomDatabase() {
    abstract fun ecapDao(): EcapDAO

    companion object {
        private var instance: EcapDatabase? = null


        fun getInstance(context: Context): EcapDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context.applicationContext, EcapDatabase::class.java, "ecap_code").fallbackToDestructiveMigration()
                        .build()
            }
            return instance as EcapDatabase
        }
    }
}
