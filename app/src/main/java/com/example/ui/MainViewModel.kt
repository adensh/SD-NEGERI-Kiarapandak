package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ai.CurriculumTemplateEngine
import com.example.data.ai.GeminiAiService
import com.example.data.model.AttendanceRecord
import com.example.data.model.GradeRecord
import com.example.data.model.GuidanceRecord
import com.example.data.model.SavedDocument
import com.example.data.model.SchoolClass
import com.example.data.model.SchoolConfig
import com.example.data.model.Student
import com.example.data.model.TeacherJournal
import com.example.data.model.User
import com.example.data.repository.SchoolRepository
import com.example.ui.components.AlertState
import com.example.ui.components.AlertType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen(val label: String) {
    LOGIN("Login"),
    DASHBOARD("Dashboard"),
    SCHOOL_CONFIG("Konfigurasi Sekolah"),
    USER_MANAGEMENT("Kelola Pengguna"),
    CLASS_MANAGEMENT("Kelola Kelas"),
    STUDENT_MANAGEMENT("Data & Import Siswa"),
    ATTENDANCE("Absensi & Jurnal Harian"),
    GRADES("Input Nilai & Asesmen"),
    TEACHER_JOURNAL("Agenda & Jurnal Guru"),
    WALI_KELAS("Bimbingan & Rekap Wali Kelas"),
    MERDEKA_AI("Perangkat Ajar AI (Kurikulum Merdeka)"),
    KBC_AI("Perangkat Ajar KBC (Panca Cinta)"),
    SAVED_DOCS("Arsip Perangkat Ajar")
}

class MainViewModel(private val repository: SchoolRepository) : ViewModel() {

    private val geminiService = GeminiAiService()

    // Auth & Navigation
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.LOGIN)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // SweetAlert State
    private val _alertState = MutableStateFlow(AlertState())
    val alertState: StateFlow<AlertState> = _alertState.asStateFlow()

    // Active Document Viewer Modal
    private val _activeViewingDoc = MutableStateFlow<SavedDocument?>(null)
    val activeViewingDoc: StateFlow<SavedDocument?> = _activeViewingDoc.asStateFlow()

    // Data streams
    val config: StateFlow<SchoolConfig?> = repository.config
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val users: StateFlow<List<User>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classes: StateFlow<List<SchoolClass>> = repository.allClasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val students: StateFlow<List<Student>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttendance: StateFlow<List<AttendanceRecord>> = repository.allAttendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGrades: StateFlow<List<GradeRecord>> = repository.allGrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allJournals: StateFlow<List<TeacherJournal>> = repository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGuidance: StateFlow<List<GuidanceRecord>> = repository.allGuidance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedDocuments: StateFlow<List<SavedDocument>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username.trim())
            if (user != null && user.passwordHash == pass.trim()) {
                _currentUser.value = user
                _currentScreen.value = AppScreen.DASHBOARD
                showSuccessAlert("Login Berhasil!", "Selamat datang kembali, ${user.fullName} (${user.role}).")
            } else {
                showErrorAlert("Login Gagal", "Username atau Password salah! Periksa kembali data login Anda.")
            }
        }
    }

    fun quickDemoLogin(role: String) {
        viewModelScope.launch {
            val targetUser = when (role) {
                "Admin" -> repository.getUserByUsername("admin")
                "Guru Mapel" -> repository.getUserByUsername("guru")
                "Wali Kelas" -> repository.getUserByUsername("walikelas")
                else -> null
            }
            if (targetUser != null) {
                _currentUser.value = targetUser
                _currentScreen.value = AppScreen.DASHBOARD
                showSuccessAlert("Mode Demo Aktif", "Anda masuk sebagai peran: ${targetUser.role}")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = AppScreen.LOGIN
    }

    // --- Alert Helpers ---
    fun showSuccessAlert(title: String, message: String, onConfirm: () -> Unit = {}) {
        _alertState.value = AlertState(
            isVisible = true,
            type = AlertType.SUCCESS,
            title = title,
            message = message,
            confirmText = "OK",
            onConfirm = {
                hideAlert()
                onConfirm()
            }
        )
    }

    fun showErrorAlert(title: String, message: String) {
        _alertState.value = AlertState(
            isVisible = true,
            type = AlertType.ERROR,
            title = title,
            message = message,
            confirmText = "Mengerti",
            onConfirm = { hideAlert() }
        )
    }

    fun showConfirmAlert(
        title: String,
        message: String,
        confirmText: String = "Ya, Lanjutkan",
        cancelText: String = "Batal",
        onConfirm: () -> Unit
    ) {
        _alertState.value = AlertState(
            isVisible = true,
            type = AlertType.WARNING,
            title = title,
            message = message,
            confirmText = confirmText,
            cancelText = cancelText,
            onConfirm = {
                hideAlert()
                onConfirm()
            },
            onCancel = { hideAlert() }
        )
    }

    fun showLoadingAlert(title: String, message: String) {
        _alertState.value = AlertState(
            isVisible = true,
            type = AlertType.LOADING,
            title = title,
            message = message
        )
    }

    fun hideAlert() {
        _alertState.value = _alertState.value.copy(isVisible = false)
    }

    // --- School Config ---
    fun updateSchoolConfig(newConfig: SchoolConfig) {
        viewModelScope.launch {
            repository.saveConfig(newConfig)
            showSuccessAlert("Konfigurasi Disimpan", "Data identitas sekolah & Kepala Sekolah berhasil diperbarui!")
        }
    }

    // --- User Management ---
    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUser(user)
            showSuccessAlert("Berhasil!", "Data pengguna '${user.fullName}' tersimpan.")
        }
    }

    fun deleteUser(id: Long, name: String) {
        showConfirmAlert(
            title = "Hapus Pengguna?",
            message = "Apakah Anda yakin ingin menghapus akun '$name'?",
            confirmText = "Ya, Hapus"
        ) {
            viewModelScope.launch {
                repository.deleteUser(id)
                showSuccessAlert("Terhapus", "Akun pengguna berhasil dihapus.")
            }
        }
    }

    // --- Class Management ---
    fun saveClass(schoolClass: SchoolClass) {
        viewModelScope.launch {
            repository.saveClass(schoolClass)
            showSuccessAlert("Berhasil!", "Data rombel/kelas '${schoolClass.name}' tersimpan.")
        }
    }

    fun deleteClass(id: Long, className: String) {
        showConfirmAlert(
            title = "Hapus Kelas?",
            message = "Apakah Anda yakin ingin menghapus '$className'?",
            confirmText = "Hapus Kelas"
        ) {
            viewModelScope.launch {
                repository.deleteClass(id)
                showSuccessAlert("Terhapus", "Rombel/Kelas telah dihapus.")
            }
        }
    }

    // --- Student Management ---
    fun saveStudent(student: Student) {
        viewModelScope.launch {
            repository.saveStudent(student)
            showSuccessAlert("Data Siswa Tersimpan", "Data '${student.name}' berhasil disimpan.")
        }
    }

    fun deleteStudent(id: Long, studentName: String) {
        showConfirmAlert(
            title = "Hapus Siswa?",
            message = "Hapus data peserta didik '$studentName'?",
            confirmText = "Hapus"
        ) {
            viewModelScope.launch {
                repository.deleteStudent(id)
                showSuccessAlert("Terhapus", "Data siswa berhasil dihapus.")
            }
        }
    }

    fun importSampleStudents(targetClass: String) {
        viewModelScope.launch {
            val sampleList = listOf(
                Student(nisn = "009${(100000..999999).random()}", name = "Muhammad Farhan Al-Ghifari", className = targetClass, gender = "L", religion = "Islam", parentName = "Drs. Hendra", phone = "081211112222"),
                Student(nisn = "009${(100000..999999).random()}", name = "Nurul Azizah Zahratunnisa", className = targetClass, gender = "P", religion = "Islam", parentName = "H. Supriyadi", phone = "081233334444"),
                Student(nisn = "009${(100000..999999).random()}", name = "Rafi Akbar Maulana", className = targetClass, gender = "L", religion = "Islam", parentName = "Maulana", phone = "081255556666"),
                Student(nisn = "009${(100000..999999).random()}", name = "Salma Khairunnisa", className = targetClass, gender = "P", religion = "Islam", parentName = "Wahyu Hidayat", phone = "081277778888"),
                Student(nisn = "009${(100000..999999).random()}", name = "Zaidan Al-Ayyubi", className = targetClass, gender = "L", religion = "Islam", parentName = "Ayyubi", phone = "081299990000")
            )
            repository.saveStudents(sampleList)
            showSuccessAlert("Import Sukses", "Berhasil menambahkan 5 data siswa baru ke $targetClass.")
        }
    }

    // --- Attendance ---
    fun saveAttendanceBatch(records: List<AttendanceRecord>, journalNote: String) {
        viewModelScope.launch {
            repository.saveAttendanceRecords(records)
            if (records.isNotEmpty() && journalNote.isNotBlank()) {
                val first = records.first()
                repository.saveJournal(
                    TeacherJournal(
                        date = first.date,
                        className = first.className,
                        subjectName = first.subjectName,
                        topic = "Jurnal Mengajar & Presensi Harian",
                        activitySummary = journalNote,
                        reflectionOrIncidents = "Presensi tercatat ${records.size} siswa."
                    )
                )
            }
            showSuccessAlert("Presensi Tersimpan", "Data kehadiran ${records.size} siswa dan jurnal mengajar berhasil dicatat.")
        }
    }

    // --- Grades ---
    fun saveGrade(
        studentId: Long,
        studentName: String,
        className: String,
        subjectName: String,
        tp1: Float,
        tp2: Float,
        tp3: Float,
        tp4: Float,
        sumatifLm1: Float,
        sumatifLm2: Float,
        sasScore: Float
    ) {
        viewModelScope.launch {
            val rataFormatif = (tp1 + tp2 + tp3 + tp4) / 4f
            val rataSumatifLm = (sumatifLm1 + sumatifLm2) / 2f
            // Bobot: Formatif 30%, Sumatif LM 35%, SAS 35%
            val finalScore = (rataFormatif * 0.3f) + (rataSumatifLm * 0.35f) + (sasScore * 0.35f)

            val descriptor = when {
                finalScore >= 90 -> "Menunjukkan penguasaan sangat istimewa (Qudwah) dalam memahami konsep, mengamalkan nilai kasih sayang, dan berakhlak mulia."
                finalScore >= 80 -> "Menunjukkan kecakapan yang baik dalam mencapai seluruh Tujuan Pembelajaran dan berpartisipasi aktif dalam kegiatan kelas."
                finalScore >= 70 -> "Mencapai Kriteria Ketercapaian Tujuan Pembelajaran (KKTP) dengan cukup baik, perlu terus mempertahankan konsistensi belajar."
                else -> "Memerlukan bimbingan dan remedial terarah dalam pemahaman materi pokok dan latihan soal mandiri."
            }

            val record = GradeRecord(
                studentId = studentId,
                studentName = studentName,
                className = className,
                subjectName = subjectName,
                tp1 = tp1,
                tp2 = tp2,
                tp3 = tp3,
                tp4 = tp4,
                sumatifLm1 = sumatifLm1,
                sumatifLm2 = sumatifLm2,
                sasScore = sasScore,
                finalScore = finalScore,
                competencyDescriptor = descriptor
            )
            repository.saveGrade(record)
            showSuccessAlert("Nilai Tersimpan", "Nilai Akhir: ${"%.1f".format(finalScore)} ($descriptor)")
        }
    }

    // --- Journal ---
    fun saveJournal(journal: TeacherJournal) {
        viewModelScope.launch {
            repository.saveJournal(journal)
            showSuccessAlert("Jurnal Tersimpan", "Catatan agenda mengajar tanggal ${journal.date} berhasil disimpan.")
        }
    }

    fun deleteJournal(id: Long) {
        showConfirmAlert(
            title = "Hapus Jurnal?",
            message = "Hapus catatan agenda guru ini?",
            confirmText = "Hapus"
        ) {
            viewModelScope.launch {
                repository.deleteJournal(id)
                showSuccessAlert("Terhapus", "Jurnal mengajar telah dihapus.")
            }
        }
    }

    // --- Guidance (Wali Kelas) ---
    fun saveGuidance(guidance: GuidanceRecord) {
        viewModelScope.launch {
            repository.saveGuidance(guidance)
            showSuccessAlert("Catatan Bimbingan Tersimpan", "Bimbingan untuk '${guidance.studentName}' berhasil dicatat.")
        }
    }

    fun deleteGuidance(id: Long) {
        showConfirmAlert(
            title = "Hapus Catatan Bimbingan?",
            message = "Apakah Anda yakin ingin menghapus catatan bimbingan ini?",
            confirmText = "Hapus"
        ) {
            viewModelScope.launch {
                repository.deleteGuidance(id)
                showSuccessAlert("Terhapus", "Catatan bimbingan telah dihapus.")
            }
        }
    }

    // --- AI Generator ---
    fun generateAiDocument(
        docType: String,
        subject: String,
        gradeLevel: String,
        semester: String,
        topic: String,
        learningPhase: String
    ) {
        val user = _currentUser.value
        val teacherName = user?.fullName ?: "Guru Mata Pelajaran"
        val teacherNip = user?.nip ?: "-"
        val currentConf = config.value ?: SchoolConfig()

        showLoadingAlert(
            title = "AI Generating...",
            message = "Gemini AI sedang menyusun dokumen '$docType' untuk materi '$topic' dengan standar Kurikulum Merdeka & KBC..."
        )

        viewModelScope.launch {
            try {
                val (html, plain) = geminiService.generateDocument(
                    docType = docType,
                    subject = subject,
                    gradeLevel = gradeLevel,
                    semester = semester,
                    topic = topic,
                    learningPhase = learningPhase,
                    teacherName = teacherName,
                    teacherNip = teacherNip,
                    config = currentConf
                )

                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                val doc = SavedDocument(
                    docType = docType,
                    title = "${CurriculumTemplateEngine.getDocTitle(docType)} - $subject ($topic)",
                    subject = subject,
                    gradeLevel = gradeLevel,
                    semester = semester,
                    generatedAt = sdf.format(Date()),
                    htmlContent = html,
                    plainTextContent = plain
                )

                val docId = repository.saveDocument(doc)
                val savedDocWithId = doc.copy(id = docId)

                hideAlert()
                openDocumentViewer(savedDocWithId)
                showSuccessAlert(
                    title = "Dokumen Berhasil Dibuat!",
                    message = "Dokumen '$docType' siap dicetak, diexport ke Word (.doc), atau disalin kode HTML-nya."
                )
            } catch (e: Exception) {
                hideAlert()
                showErrorAlert("Gagal Generate AI", "Terjadi kendala: ${e.message}")
            }
        }
    }

    fun openDocumentViewer(doc: SavedDocument) {
        _activeViewingDoc.value = doc
    }

    fun closeDocumentViewer() {
        _activeViewingDoc.value = null
    }

    fun deleteSavedDocument(id: Long, title: String) {
        showConfirmAlert(
            title = "Hapus Dokumen Arsip?",
            message = "Apakah Anda yakin ingin menghapus '$title' dari arsip?",
            confirmText = "Hapus Arsip"
        ) {
            viewModelScope.launch {
                repository.deleteDocument(id)
                showSuccessAlert("Terhapus", "Dokumen perangkat ajar dihapus dari arsip.")
            }
        }
    }
}

class MainViewModelFactory(private val repository: SchoolRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
