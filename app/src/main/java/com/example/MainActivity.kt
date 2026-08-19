package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.db.AppDatabase
import com.example.data.repository.SchoolRepository
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.components.DocumentViewerModal
import com.example.ui.components.SweetAlertModal
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.ClassManagementScreen
import com.example.ui.screens.CurriculumKbcAiScreen
import com.example.ui.screens.CurriculumMerdekaAiScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GradesScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SavedDocumentsScreen
import com.example.ui.screens.SchoolConfigScreen
import com.example.ui.screens.StudentManagementScreen
import com.example.ui.screens.TeacherJournalScreen
import com.example.ui.screens.UserManagementScreen
import com.example.ui.screens.WaliKelasScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val repo = SchoolRepository(db)
        MainViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainAppContent(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val alertState by viewModel.alertState.collectAsState()
    val activeViewingDoc by viewModel.activeViewingDoc.collectAsState()

    val config by viewModel.config.collectAsState()
    val users by viewModel.users.collectAsState()
    val classes by viewModel.classes.collectAsState()
    val students by viewModel.students.collectAsState()
    val attendance by viewModel.allAttendance.collectAsState()
    val grades by viewModel.allGrades.collectAsState()
    val journals by viewModel.allJournals.collectAsState()
    val guidance by viewModel.allGuidance.collectAsState()
    val savedDocuments by viewModel.savedDocuments.collectAsState()

    // Screen Router
    when (currentScreen) {
        AppScreen.LOGIN -> LoginScreen(
            viewModel = viewModel,
            config = config
        )
        AppScreen.DASHBOARD -> DashboardScreen(
            viewModel = viewModel,
            currentUser = currentUser,
            config = config,
            classes = classes,
            students = students,
            docCount = savedDocuments.size,
            journalCount = journals.size
        )
        AppScreen.SCHOOL_CONFIG -> SchoolConfigScreen(
            viewModel = viewModel,
            config = config
        )
        AppScreen.USER_MANAGEMENT -> UserManagementScreen(
            viewModel = viewModel,
            users = users
        )
        AppScreen.CLASS_MANAGEMENT -> ClassManagementScreen(
            viewModel = viewModel,
            classes = classes
        )
        AppScreen.STUDENT_MANAGEMENT -> StudentManagementScreen(
            viewModel = viewModel,
            students = students,
            classes = classes
        )
        AppScreen.ATTENDANCE -> AttendanceScreen(
            viewModel = viewModel,
            currentUser = currentUser,
            classes = classes,
            students = students
        )
        AppScreen.GRADES -> GradesScreen(
            viewModel = viewModel,
            currentUser = currentUser,
            classes = classes,
            students = students,
            grades = grades
        )
        AppScreen.TEACHER_JOURNAL -> TeacherJournalScreen(
            viewModel = viewModel,
            currentUser = currentUser,
            journals = journals
        )
        AppScreen.WALI_KELAS -> WaliKelasScreen(
            viewModel = viewModel,
            currentUser = currentUser,
            students = students,
            guidances = guidance,
            grades = grades,
            attendance = attendance
        )
        AppScreen.MERDEKA_AI -> CurriculumMerdekaAiScreen(
            viewModel = viewModel,
            currentUser = currentUser
        )
        AppScreen.KBC_AI -> CurriculumKbcAiScreen(
            viewModel = viewModel,
            currentUser = currentUser
        )
        AppScreen.SAVED_DOCS -> SavedDocumentsScreen(
            viewModel = viewModel,
            documents = savedDocuments
        )
    }

    // Global SweetAlert Modal
    SweetAlertModal(
        state = alertState,
        onDismissRequest = { viewModel.hideAlert() }
    )

    // Global Full-Screen Document Viewer Modal (Print / Word / HTML)
    activeViewingDoc?.let { doc ->
        DocumentViewerModal(
            title = doc.title,
            htmlContent = doc.htmlContent,
            onDismiss = { viewModel.closeDocumentViewer() }
        )
    }
}
