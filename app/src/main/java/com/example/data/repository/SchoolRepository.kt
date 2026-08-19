package com.example.data.repository

import com.example.data.db.AppDatabase
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
import kotlinx.coroutines.flow.firstOrNull

class SchoolRepository(private val db: AppDatabase) {

    // Config
    val config: Flow<SchoolConfig?> = db.schoolConfigDao().getConfig()
    suspend fun getConfigOnce(): SchoolConfig = db.schoolConfigDao().getConfigOnce() ?: SchoolConfig()
    suspend fun saveConfig(config: SchoolConfig) = db.schoolConfigDao().insertOrUpdateConfig(config)

    // Users
    val allUsers: Flow<List<User>> = db.userDao().getAllUsers()
    suspend fun getUserByUsername(username: String): User? = db.userDao().getUserByUsername(username)
    suspend fun saveUser(user: User) = db.userDao().insertUser(user)
    suspend fun updateUser(user: User) = db.userDao().updateUser(user)
    suspend fun deleteUser(id: Long) = db.userDao().deleteUserById(id)

    // Classes
    val allClasses: Flow<List<SchoolClass>> = db.schoolClassDao().getAllClasses()
    suspend fun saveClass(schoolClass: SchoolClass) = db.schoolClassDao().insertClass(schoolClass)
    suspend fun updateClass(schoolClass: SchoolClass) = db.schoolClassDao().updateClass(schoolClass)
    suspend fun deleteClass(id: Long) = db.schoolClassDao().deleteClassById(id)

    // Students
    val allStudents: Flow<List<Student>> = db.studentDao().getAllStudents()
    fun getStudentsByClass(className: String): Flow<List<Student>> = db.studentDao().getStudentsByClass(className)
    suspend fun saveStudent(student: Student) = db.studentDao().insertStudent(student)
    suspend fun saveStudents(students: List<Student>) = db.studentDao().insertStudents(students)
    suspend fun updateStudent(student: Student) = db.studentDao().updateStudent(student)
    suspend fun deleteStudent(id: Long) = db.studentDao().deleteStudentById(id)

    // Attendance
    fun getAttendance(date: String, className: String): Flow<List<AttendanceRecord>> =
        db.attendanceDao().getAttendanceByDateAndClass(date, className)
    fun getAttendanceByClass(className: String): Flow<List<AttendanceRecord>> =
        db.attendanceDao().getAttendanceByClass(className)
    val allAttendance: Flow<List<AttendanceRecord>> = db.attendanceDao().getAllAttendance()
    suspend fun saveAttendanceRecords(records: List<AttendanceRecord>) =
        db.attendanceDao().insertAttendance(records)
    suspend fun saveSingleAttendance(record: AttendanceRecord) =
        db.attendanceDao().insertSingleAttendance(record)

    // Grades
    fun getGrades(className: String, subjectName: String): Flow<List<GradeRecord>> =
        db.gradeDao().getGradesByClassAndSubject(className, subjectName)
    fun getGradesByClass(className: String): Flow<List<GradeRecord>> =
        db.gradeDao().getGradesByClass(className)
    val allGrades: Flow<List<GradeRecord>> = db.gradeDao().getAllGrades()
    suspend fun saveGrade(grade: GradeRecord) = db.gradeDao().insertGrade(grade)
    suspend fun saveGrades(grades: List<GradeRecord>) = db.gradeDao().insertGrades(grades)
    suspend fun updateGrade(grade: GradeRecord) = db.gradeDao().updateGrade(grade)
    suspend fun deleteGrade(id: Long) = db.gradeDao().deleteGradeById(id)

    // Journals
    val allJournals: Flow<List<TeacherJournal>> = db.teacherJournalDao().getAllJournals()
    suspend fun saveJournal(journal: TeacherJournal) = db.teacherJournalDao().insertJournal(journal)
    suspend fun deleteJournal(id: Long) = db.teacherJournalDao().deleteJournalById(id)

    // Guidance
    fun getGuidanceByClass(className: String): Flow<List<GuidanceRecord>> =
        db.guidanceDao().getGuidanceByClass(className)
    val allGuidance: Flow<List<GuidanceRecord>> = db.guidanceDao().getAllGuidance()
    suspend fun saveGuidance(guidance: GuidanceRecord) = db.guidanceDao().insertGuidance(guidance)
    suspend fun updateGuidance(guidance: GuidanceRecord) = db.guidanceDao().updateGuidance(guidance)
    suspend fun deleteGuidance(id: Long) = db.guidanceDao().deleteGuidanceById(id)

    // Documents
    val allDocuments: Flow<List<SavedDocument>> = db.savedDocumentDao().getAllDocuments()
    fun getDocumentsByType(docType: String): Flow<List<SavedDocument>> =
        db.savedDocumentDao().getDocumentsByType(docType)
    suspend fun saveDocument(doc: SavedDocument) = db.savedDocumentDao().insertDocument(doc)
    suspend fun deleteDocument(id: Long) = db.savedDocumentDao().deleteDocumentById(id)

    suspend fun seedInitialDataIfNeeded() {
        val existingConfig = db.schoolConfigDao().getConfigOnce()
        if (existingConfig == null) {
            db.schoolConfigDao().insertOrUpdateConfig(
                SchoolConfig(
                    id = 1,
                    governmentName = "PEMERINTAH KABUPATEN BOGOR\nDINAS PENDIDIKAN",
                    schoolName = "SMP NEGERI 1 SUKAJAYA",
                    schoolAddress = "Jl. Raya Sukajaya No. 123, Kec. Sukajaya, Kab. Bogor",
                    signatureCityDate = "Sukajaya, 18 Agustus 2026",
                    headmasterName = "Dr. H. Muhammad Ilyas, M.Pd.I",
                    headmasterNip = "19720515 199803 1 003",
                    academicYear = "2024/2025",
                    activeSemester = "Semester Ganjil",
                    primaryCurriculum = "Kurikulum Merdeka & Kurikulum Berbasis Cinta (KBC)"
                )
            )
        }

        val users = db.userDao().getAllUsers().firstOrNull()
        if (users.isNullOrEmpty()) {
            db.userDao().insertUsers(
                listOf(
                    User(
                        username = "admin",
                        passwordHash = "admin123",
                        fullName = "Drs. H. Mulyadi, M.Pd. (Admin)",
                        nip = "19700312 199501 1 002",
                        role = "Admin",
                        subjectAssigned = "Semua Mata Pelajaran",
                        assignedClass = "-"
                    ),
                    User(
                        username = "guru",
                        passwordHash = "guru123",
                        fullName = "Drs. Ahmad Fauzi, M.Pd.",
                        nip = "19780410 200212 1 005",
                        role = "Guru Mapel",
                        subjectAssigned = "Pendidikan Agama Islam & BP",
                        assignedClass = "Kelas 7B"
                    ),
                    User(
                        username = "walikelas",
                        passwordHash = "wali123",
                        fullName = "Ustadzah Siti Aminah, S.Pd.I",
                        nip = "19830825 200801 2 011",
                        role = "Wali Kelas",
                        subjectAssigned = "Bahasa Indonesia",
                        assignedClass = "Kelas 7A"
                    )
                )
            )
        }

        val classes = db.schoolClassDao().getAllClasses().firstOrNull()
        if (classes.isNullOrEmpty()) {
            db.schoolClassDao().insertClasses(
                listOf(
                    SchoolClass(name = "Kelas 7A", gradeLevel = "7", academicYear = "2024/2025", waliKelasName = "Ustadzah Siti Aminah, S.Pd.I"),
                    SchoolClass(name = "Kelas 7B", gradeLevel = "7", academicYear = "2024/2025", waliKelasName = "Drs. Ahmad Fauzi, M.Pd."),
                    SchoolClass(name = "Kelas 8A", gradeLevel = "8", academicYear = "2024/2025", waliKelasName = "Dra. Nurhayati, M.Si"),
                    SchoolClass(name = "Kelas 9A", gradeLevel = "9", academicYear = "2024/2025", waliKelasName = "Budi Santoso, S.Pd.")
                )
            )
        }

        val students = db.studentDao().getAllStudents().firstOrNull()
        if (students.isNullOrEmpty()) {
            db.studentDao().insertStudents(
                listOf(
                    Student(nisn = "0091234501", name = "Ahmad Zaki Mubarak", className = "Kelas 7A", gender = "L", religion = "Islam", parentName = "Mubarak Syah", phone = "081234567890"),
                    Student(nisn = "0091234502", name = "Aisyah Nur Rahmah", className = "Kelas 7A", gender = "P", religion = "Islam", parentName = "Rahmat Hidayat", phone = "081234567891"),
                    Student(nisn = "0091234503", name = "Bagus Pratama Putra", className = "Kelas 7A", gender = "L", religion = "Islam", parentName = "Bambang Sugiharto", phone = "081234567892"),
                    Student(nisn = "0091234504", name = "Citra Kirana Lestari", className = "Kelas 7A", gender = "P", religion = "Islam", parentName = "Iskandar Muda", phone = "081234567893"),
                    Student(nisn = "0091234505", name = "Dafi Al-Faris", className = "Kelas 7A", gender = "L", religion = "Islam", parentName = "Farid Wijaya", phone = "081234567894"),
                    Student(nisn = "0091234506", name = "Fathir Muhammad", className = "Kelas 7A", gender = "L", religion = "Islam", parentName = "Muhammad Ali", phone = "081234567895"),
                    Student(nisn = "0091234507", name = "Hana Qonitah", className = "Kelas 7A", gender = "P", religion = "Islam", parentName = "Kurniawan", phone = "081234567896"),
                    Student(nisn = "0091234508", name = "Irfan Maulana", className = "Kelas 7A", gender = "L", religion = "Islam", parentName = "Maulana Malik", phone = "081234567897"),
                    Student(nisn = "0091234509", name = "Naila Salsabila", className = "Kelas 7A", gender = "P", religion = "Islam", parentName = "Syamsul Bahri", phone = "081234567898"),
                    Student(nisn = "0091234510", name = "Rizky Ramadhan", className = "Kelas 7A", gender = "L", religion = "Islam", parentName = "Hendra Gunawan", phone = "081234567899"),

                    Student(nisn = "0091234601", name = "Aditya Dwi Saputra", className = "Kelas 7B", gender = "L", religion = "Islam", parentName = "Saputra", phone = "081298765431"),
                    Student(nisn = "0091234602", name = "Bella Ananda Putri", className = "Kelas 7B", gender = "P", religion = "Islam", parentName = "Ananda", phone = "081298765432"),
                    Student(nisn = "0091234603", name = "Dimas Anggara", className = "Kelas 7B", gender = "L", religion = "Islam", parentName = "Anggara", phone = "081298765433"),
                    Student(nisn = "0091234604", name = "Fitriani Zahra", className = "Kelas 7B", gender = "P", religion = "Islam", parentName = "Zahra", phone = "081298765434")
                )
            )

            // Seed initial grade samples
            db.gradeDao().insertGrades(
                listOf(
                    GradeRecord(studentId = 1, studentName = "Ahmad Zaki Mubarak", className = "Kelas 7A", subjectName = "Pendidikan Agama Islam & BP", tp1 = 88f, tp2 = 90f, tp3 = 85f, tp4 = 92f, sumatifLm1 = 89f, sumatifLm2 = 91f, sasScore = 90f, finalScore = 89.3f, competencyDescriptor = "Sangat cakap dalam memahami makna dan hikmah Al-Qur'an serta berakhlak mulia."),
                    GradeRecord(studentId = 2, studentName = "Aisyah Nur Rahmah", className = "Kelas 7A", subjectName = "Pendidikan Agama Islam & BP", tp1 = 92f, tp2 = 94f, tp3 = 90f, tp4 = 95f, sumatifLm1 = 93f, sumatifLm2 = 94f, sasScore = 92f, finalScore = 92.9f, competencyDescriptor = "Menunjukkan keteladanan qudwah dalam ibadah dan pemahaman konsep Panca Cinta."),
                    GradeRecord(studentId = 3, studentName = "Bagus Pratama Putra", className = "Kelas 7A", subjectName = "Pendidikan Agama Islam & BP", tp1 = 80f, tp2 = 82f, tp3 = 78f, tp4 = 84f, sumatifLm1 = 81f, sumatifLm2 = 83f, sasScore = 80f, finalScore = 81.1f, competencyDescriptor = "Mampu mencapai Kriteria Ketercapaian Tujuan Pembelajaran dengan baik dan disiplin."),
                    GradeRecord(studentId = 4, studentName = "Citra Kirana Lestari", className = "Kelas 7A", subjectName = "Pendidikan Agama Islam & BP", tp1 = 86f, tp2 = 88f, tp3 = 84f, tp4 = 90f, sumatifLm1 = 87f, sumatifLm2 = 89f, sasScore = 88f, finalScore = 87.4f, competencyDescriptor = "Aktif dan berempati dalam diskusi kelas serta pengamalan ajaran kasih sayang.")
                )
            )

            // Seed initial journal
            db.teacherJournalDao().insertJournal(
                TeacherJournal(
                    date = "2026-08-18",
                    className = "Kelas 7A",
                    subjectName = "Pendidikan Agama Islam & BP",
                    topic = "Hakikat Nilai Panca Cinta dalam Kehidupan Sehari-hari",
                    timeSlot = "07.30 - 09.00 WIB",
                    activitySummary = "Menjelaskan pilar Panca Cinta (Cinta Allah & Rasul, Diri & Sesama, Ilmu, Bangsa, Lingkungan), diskusi kelompok studi kasus empati.",
                    reflectionOrIncidents = "Siswa antusias dan berbagi pengalaman nyata tolong menolong di lingkungan sekolah."
                )
            )

            // Seed initial guidance record
            db.guidanceDao().insertGuidance(
                GuidanceRecord(
                    date = "2026-08-17",
                    studentId = 3,
                    studentName = "Bagus Pratama Putra",
                    className = "Kelas 7A",
                    guidanceCategory = "Akademik",
                    issueDescription = "Perlu penguatan konsentrasi saat pengerjaan tugas mandiri dan manajemen waktu belajar di rumah.",
                    actionTaken = "Diskusi empati dari hati ke hati (KBC approach), penyusunan jadwal belajar bersama orang tua.",
                    resolutionStatus = "Selesai"
                )
            )
        }
    }
}
