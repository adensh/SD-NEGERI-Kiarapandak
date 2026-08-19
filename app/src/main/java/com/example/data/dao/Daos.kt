package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AttendanceRecord
import com.example.data.model.GradeRecord
import com.example.data.model.GuidanceRecord
import com.example.data.model.SavedDocument
import com.example.data.model.SchoolClass
import com.example.data.model.SchoolConfig
import com.example.data.model.Student
import com.example.data.model.TeacherJournal
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Long)
}

@Dao
interface SchoolConfigDao {
    @Query("SELECT * FROM school_config WHERE id = 1 LIMIT 1")
    fun getConfig(): Flow<SchoolConfig?>

    @Query("SELECT * FROM school_config WHERE id = 1 LIMIT 1")
    suspend fun getConfigOnce(): SchoolConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfig(config: SchoolConfig)
}

@Dao
interface SchoolClassDao {
    @Query("SELECT * FROM school_classes ORDER BY name ASC")
    fun getAllClasses(): Flow<List<SchoolClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(schoolClass: SchoolClass): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classes: List<SchoolClass>)

    @Update
    suspend fun updateClass(schoolClass: SchoolClass)

    @Query("DELETE FROM school_classes WHERE id = :id")
    suspend fun deleteClassById(id: Long)
}

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY className ASC, name ASC")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE className = :className ORDER BY name ASC")
    fun getStudentsByClass(className: String): Flow<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudentById(id: Long)

    @Query("DELETE FROM students")
    suspend fun deleteAllStudents()
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_records WHERE date = :date AND className = :className ORDER BY studentName ASC")
    fun getAttendanceByDateAndClass(date: String, className: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE className = :className ORDER BY date DESC")
    fun getAttendanceByClass(className: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records ORDER BY date DESC")
    fun getAllAttendance(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(records: List<AttendanceRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleAttendance(record: AttendanceRecord): Long
}

@Dao
interface GradeDao {
    @Query("SELECT * FROM grade_records WHERE className = :className AND subjectName = :subjectName ORDER BY studentName ASC")
    fun getGradesByClassAndSubject(className: String, subjectName: String): Flow<List<GradeRecord>>

    @Query("SELECT * FROM grade_records WHERE className = :className ORDER BY studentName ASC")
    fun getGradesByClass(className: String): Flow<List<GradeRecord>>

    @Query("SELECT * FROM grade_records ORDER BY id DESC")
    fun getAllGrades(): Flow<List<GradeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrades(grades: List<GradeRecord>)

    @Update
    suspend fun updateGrade(grade: GradeRecord)

    @Query("DELETE FROM grade_records WHERE id = :id")
    suspend fun deleteGradeById(id: Long)
}

@Dao
interface TeacherJournalDao {
    @Query("SELECT * FROM teacher_journals ORDER BY date DESC")
    fun getAllJournals(): Flow<List<TeacherJournal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: TeacherJournal): Long

    @Query("DELETE FROM teacher_journals WHERE id = :id")
    suspend fun deleteJournalById(id: Long)
}

@Dao
interface GuidanceDao {
    @Query("SELECT * FROM guidance_records WHERE className = :className ORDER BY date DESC")
    fun getGuidanceByClass(className: String): Flow<List<GuidanceRecord>>

    @Query("SELECT * FROM guidance_records ORDER BY date DESC")
    fun getAllGuidance(): Flow<List<GuidanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuidance(guidance: GuidanceRecord): Long

    @Update
    suspend fun updateGuidance(guidance: GuidanceRecord)

    @Query("DELETE FROM guidance_records WHERE id = :id")
    suspend fun deleteGuidanceById(id: Long)
}

@Dao
interface SavedDocumentDao {
    @Query("SELECT * FROM saved_documents ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<SavedDocument>>

    @Query("SELECT * FROM saved_documents WHERE docType = :docType ORDER BY id DESC")
    fun getDocumentsByType(docType: String): Flow<List<SavedDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: SavedDocument): Long

    @Query("DELETE FROM saved_documents WHERE id = :id")
    suspend fun deleteDocumentById(id: Long)
}
