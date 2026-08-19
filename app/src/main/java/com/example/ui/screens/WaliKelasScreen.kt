package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AttendanceRecord
import com.example.data.model.GradeRecord
import com.example.data.model.GuidanceRecord
import com.example.data.model.Student
import com.example.data.model.User
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.outlinedColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WaliKelasScreen(
    viewModel: MainViewModel,
    currentUser: User?,
    students: List<Student>,
    guidances: List<GuidanceRecord>,
    grades: List<GradeRecord>,
    attendance: List<AttendanceRecord>
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showGuidanceDialog by remember { mutableStateOf(false) }

    val myClass = currentUser?.assignedClass?.ifBlank { "VII-A" } ?: "VII-A"
    val myClassStudents = remember(students, myClass) {
        students.filter { it.className == myClass }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070D1E))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar
            Surface(
                color = Color(0xFF0F172A),
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        modifier = Modifier.testTag("wali_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }

                    Column(modifier = Modifier.padding(start = 6.dp)) {
                        Text(
                            text = "Bimbingan & Rekapitulasi Wali Kelas",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Kelas Binaan: $myClass (${myClassStudents.size} Siswa)",
                            fontSize = 11.5.sp,
                            color = Color(0xFFC026D3)
                        )
                    }
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFF0F172A),
                contentColor = Color(0xFFC026D3)
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Bimbingan & Konseling", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Rekapitulasi Rapor Siswa", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            if (selectedTabIndex == 0) {
                // Guidance Tab Content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (guidances.isEmpty()) {
                        item {
                            EmptyWaliPlaceholder("Belum ada catatan bimbingan konseling. Klik '+' untuk mencatat layanan siswa.")
                        }
                    }

                    items(guidances, key = { it.id }) { g ->
                        GuidanceCard(guidance = g, onDelete = { viewModel.deleteGuidance(g.id) })
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            } else {
                // Rekapitulasi Rapor Siswa Tab Content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(myClassStudents, key = { it.id }) { s ->
                        val studentGrades = grades.filter { it.studentId == s.id }
                        val studentAtt = attendance.filter { it.studentId == s.id }
                        val totalH = studentAtt.count { it.status == "H" }
                        val totalS = studentAtt.count { it.status == "S" }
                        val totalI = studentAtt.count { it.status == "I" }
                        val totalA = studentAtt.count { it.status == "A" }
                        val avgScore = if (studentGrades.isNotEmpty()) studentGrades.map { it.finalScore }.average() else 0.0

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = s.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "NISN: ${s.nisn} • Wali: ${s.parentName.ifBlank { "-" }}",
                                            fontSize = 11.5.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(if (avgScore >= 75) Color(0xFF10B981) else Color(0xFFD97706), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Rata2: ${"%.1f".format(avgScore)}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Presensi: H: $totalH | S: $totalS | I: $totalI | A: $totalA",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF2DD4BF)
                                    )
                                    Text(
                                        text = "${studentGrades.size} Mapel Dinilai",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF38BDF8)
                                    )
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
            }
        }

        if (selectedTabIndex == 0) {
            FloatingActionButton(
                onClick = { showGuidanceDialog = true },
                containerColor = Color(0xFFC026D3),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("add_guidance_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Bimbingan")
            }
        }

        if (showGuidanceDialog) {
            GuidanceInputDialog(
                students = myClassStudents,
                className = myClass,
                onDismiss = { showGuidanceDialog = false },
                onSave = {
                    viewModel.saveGuidance(it)
                    showGuidanceDialog = false
                }
            )
        }
    }
}

@Composable
private fun GuidanceCard(
    guidance: GuidanceRecord,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = guidance.studentName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${guidance.date} • Kategori: ${guidance.guidanceCategory}",
                        fontSize = 11.5.sp,
                        color = Color(0xFFC026D3)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Uraian Masalah/Kebutuhan: ${guidance.issueDescription}", fontSize = 12.5.sp, color = Color(0xFFCBD5E1))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Tindakan Layanan: ${guidance.actionTaken}", fontSize = 12.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rencana Tindak Lanjut: ${guidance.followUpPlan}", fontSize = 12.sp, color = Color(0xFF2DD4BF))
        }
    }
}

@Composable
private fun EmptyWaliPlaceholder(msg: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Belum Ada Catatan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = msg, fontSize = 12.sp, color = Color(0xFF94A3B8), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GuidanceInputDialog(
    students: List<Student>,
    className: String,
    onDismiss: () -> Unit,
    onSave: (GuidanceRecord) -> Unit
) {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    var date by remember { mutableStateOf(sdf.format(Date())) }
    var selectedStudent by remember { mutableStateOf(students.firstOrNull()) }
    var serviceType by remember { mutableStateOf("Bimbingan Karakter Panca Cinta") }
    var caseDescription by remember { mutableStateOf("") }
    var handling by remember { mutableStateOf("") }
    var followUp by remember { mutableStateOf("") }

    val serviceTypes = listOf("Bimbingan Karakter Panca Cinta", "Bimbingan Akademik & Belajar", "Bimbingan Sosial & Pertemanan", "Bimbingan Kehadiran / Presensi")
    var serviceTypeDropdownExpanded by remember { mutableStateOf(false) }
    var studentDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF0F172A),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Catat Bimbingan Wali Kelas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Student Dropdown
                ExposedDropdownMenuBox(
                    expanded = studentDropdownExpanded,
                    onExpandedChange = { studentDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedStudent?.name ?: "Pilih Siswa",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih Peserta Didik") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = outlinedColors()
                    )
                    ExposedDropdownMenu(
                        expanded = studentDropdownExpanded,
                        onDismissRequest = { studentDropdownExpanded = false }
                    ) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s.name) },
                                onClick = {
                                    selectedStudent = s
                                    studentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Service Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = serviceTypeDropdownExpanded,
                    onExpandedChange = { serviceTypeDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = serviceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jenis Layanan Bimbingan") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceTypeDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = outlinedColors()
                    )
                    ExposedDropdownMenu(
                        expanded = serviceTypeDropdownExpanded,
                        onDismissRequest = { serviceTypeDropdownExpanded = false }
                    ) {
                        serviceTypes.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st) },
                                onClick = {
                                    serviceType = st
                                    serviceTypeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = caseDescription,
                    onValueChange = { caseDescription = it },
                    label = { Text("Uraian Kasus / Kebutuhan Siswa") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedColors()
                )

                OutlinedTextField(
                    value = handling,
                    onValueChange = { handling = it },
                    label = { Text("Langkah Penanganan Konseling") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedColors()
                )

                OutlinedTextField(
                    value = followUp,
                    onValueChange = { followUp = it },
                    label = { Text("Rencana Tindak Lanjut") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Batal", color = Color(0xFF94A3B8))
                    }

                    Button(
                        onClick = {
                            selectedStudent?.let { s ->
                                if (caseDescription.isNotBlank()) {
                                    onSave(
                                        GuidanceRecord(
                                            studentId = s.id,
                                            studentName = s.name,
                                            className = className,
                                            date = date,
                                            guidanceCategory = serviceType,
                                            issueDescription = caseDescription.trim(),
                                            actionTaken = handling.trim(),
                                            followUpPlan = followUp.trim(),
                                            resolutionStatus = "Selesai"
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC026D3))
                    ) {
                        Text("Simpan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
