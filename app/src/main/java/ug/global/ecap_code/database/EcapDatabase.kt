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

    @Query("SELECT dataid FROM FillerData WHERE name=:type")
    fun getFillerDataId(type: String): Int?

    @Query("DELETE FROM FillerData")
    fun deleteFillers()

    @Query("DELETE FROM QuizItem")
    fun deleteQuiz()

    @Upsert
    fun addFillerData(fillerData: FillerData)

    @Upsert
    fun addQuiz(quiz: QuizItem)

    @Query("SELECT * FROM QuizItem")
    fun getQuiz(): List<QuizItem>

    @Query("SELECT * FROM patient")
    fun getAllPatients(): List<Patient>

    @Upsert
    fun insertPatient(patient: Patient): Long

    @Upsert
    fun insertAssessment(assessmentForm: AssessmentForm): Long


    @Query("SELECT * FROM AssessmentForm WHERE visit=:visitInfo LIMIT 1")
    fun getPatientAssessment(visitInfo: Int): AssessmentForm


    @Query("SELECT * FROM VisitInfo WHERE patient=:patient LIMIT 1")
    fun getPatientVisit(patient: Int): VisitInfo

    @Upsert
    fun insertVisit(visitInfo: VisitInfo): Long

}

@Database(
    entities = [Patient::class, FillerData::class, AssessmentForm::class, VisitInfo::class, QuizItem::class],
    version = 13,
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
