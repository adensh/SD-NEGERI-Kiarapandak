package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val fullName: String,
    val nip: String = "-",
    val role: String, // "Admin", "Guru Mapel", "Wali Kelas"
    val subjectAssigned: String = "Pendidikan Agama Islam & BP",
    val assignedClass: String = "VII-A"
)

@Entity(tableName = "school_config")
data class SchoolConfig(
    @PrimaryKey val id: Long = 1,
    val governmentName: String = "PEMERINTAH KABUPATEN BOGOR\nDINAS PENDIDIKAN",
    val schoolName: String = "SMP NEGERI 1 SUKAJAYA",
    val schoolAddress: String = "Jl. Raya Sukajaya No. 123, Kec. Sukajaya, Kab. Bogor",
    val signatureCityDate: String = "Sukajaya, 18 Agustus 2026",
    val headmasterName: String = "Dr. H. Muhammad Ilyas, M.Pd.I",
    val headmasterNip: String = "19720515 199803 1 003",
    val academicYear: String = "2024/2025",
    val activeSemester: String = "Semester Ganjil",
    val primaryCurriculum: String = "Kurikulum Merdeka & Kurikulum Berbasis Cinta (KBC)"
)

@Entity(tableName = "school_classes")
data class SchoolClass(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String, // "VII-A", "VII-B", "VIII-A", "IX-A"
    val gradeLevel: String = "7",
    val phase: String = "D",
    val academicYear: String = "2024/2025",
    val waliKelasName: String = "Ustadzah Siti Aminah, S.Pd.I"
)

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nisn: String,
    val name: String,
    val className: String,
    val gender: String, // "L" atau "P"
    val religion: String = "Islam",
    val parentName: String = "-",
    val phone: String = "-"
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val className: String,
    val subjectName: String,
    val studentId: Long,
    val studentName: String,
    val status: String, // "H", "S", "I", "A"
    val notes: String = "",
    val teachingJournal: String = ""
)

@Entity(tableName = "grade_records")
data class GradeRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val className: String,
    val subjectName: String,
    val semester: String = "Semester Ganjil",
    val tp1: Float = 85f,
    val tp2: Float = 88f,
    val tp3: Float = 82f,
    val tp4: Float = 90f,
    val sumatifLm1: Float = 86f,
    val sumatifLm2: Float = 88f,
    val sasScore: Float = 87f,
    val finalScore: Float = 86.5f,
    val competencyDescriptor: String = "Menunjukkan penguasaan yang sangat baik dalam memahami materi dan penerapannya dalam kehidupan sehari-hari."
)

@Entity(tableName = "teacher_journals")
data class TeacherJournal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val className: String,
    val subjectName: String,
    val topic: String,
    val timeSlot: String = "07.30 - 09.00 WIB",
    val activitySummary: String,
    val reflectionOrIncidents: String = "Pembelajaran berlangsung interaktif, siswa aktif berdiskusi."
)

@Entity(tableName = "guidance_records")
data class GuidanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val studentId: Long,
    val studentName: String,
    val className: String,
    val guidanceCategory: String, // "Bimbingan Karakter Panca Cinta", "Bimbingan Akademik", "Bimbingan Sosial"
    val issueDescription: String,
    val actionTaken: String,
    val followUpPlan: String = "Pemantauan berkala",
    val resolutionStatus: String = "Selesai" // "Proses", "Selesai", "Perlu Pemantauan"
)

@Entity(tableName = "saved_documents")
data class SavedDocument(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val docType: String,
    val title: String,
    val subject: String,
    val gradeLevel: String,
    val semester: String,
    val generatedAt: String,
    val htmlContent: String,
    val plainTextContent: String
)
