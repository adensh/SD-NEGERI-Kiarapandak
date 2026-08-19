package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AttendanceDao
import com.example.data.dao.GradeDao
import com.example.data.dao.GuidanceDao
import com.example.data.dao.SavedDocumentDao
import com.example.data.dao.SchoolClassDao
import com.example.data.dao.SchoolConfigDao
import com.example.data.dao.StudentDao
import com.example.data.dao.TeacherJournalDao
import com.example.data.dao.UserDao
import com.example.data.model.AttendanceRecord
import com.example.data.model.GradeRecord
import com.example.data.model.GuidanceRecord
import com.example.data.model.SavedDocument
import com.example.data.model.SchoolClass
import com.example.data.model.SchoolConfig
import com.example.data.model.Student
import com.example.data.model.TeacherJournal
import com.example.data.model.User

@Database(
    entities = [
        User::class,
        SchoolConfig::class,
        SchoolClass::class,
        Student::class,
        AttendanceRecord::class,
        GradeRecord::class,
        TeacherJournal::class,
        GuidanceRecord::class,
        SavedDocument::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun schoolConfigDao(): SchoolConfigDao
    abstract fun schoolClassDao(): SchoolClassDao
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun gradeDao(): GradeDao
    abstract fun teacherJournalDao(): TeacherJournalDao
    abstract fun guidanceDao(): GuidanceDao
    abstract fun savedDocumentDao(): SavedDocumentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "school_admin_kbc.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
