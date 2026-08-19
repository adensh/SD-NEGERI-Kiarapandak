package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.SchoolClass
import com.example.data.model.Student
import com.example.data.model.User
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.outlinedColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AttendanceScreen(
    viewModel: MainViewModel,
    currentUser: User?,
    classes: List<SchoolClass>,
    students: List<Student>
) {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    val todayStr = remember { sdf.format(Date()) }

    var selectedClass by remember(classes) { mutableStateOf(classes.firstOrNull()?.name ?: "VII-A") }
    var subjectName by remember { mutableStateOf(currentUser?.subjectAssigned ?: "Pendidikan Agama Islam") }
    var journalNote by remember { mutableStateOf("") }

    val classStudents = remember(students, selectedClass) {
        students.filter { it.className == selectedClass }
    }

    // Attendance map: studentId -> status ("H", "S", "I", "A")
    val statusMap = remember(classStudents) {
        mutableStateMapOf<Long, String>().apply {
            classStudents.forEach { put(it.id, "H") }
        }
    }

    val totalHadir = statusMap.values.count { it == "H" }
    val totalSakit = statusMap.values.count { it == "S" }
    val totalIzin = statusMap.values.count { it == "I" }
    val totalAlpa = statusMap.values.count { it == "A" }
    val percentHadir = if (classStudents.isNotEmpty()) (totalHadir * 100) / classStudents.size else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070D1E))
    ) {
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
                    modifier = Modifier.testTag("attendance_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Presensi & Jurnal Harian",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info & Class Picker
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "TANGGAL: $todayStr",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2DD4BF),
                            letterSpacing = 1.sp
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = selectedClass,
                                onValueChange = { selectedClass = it },
                                label = { Text("Kelas") },
                                modifier = Modifier.weight(1f),
                                colors = outlinedColors()
                            )
                            OutlinedTextField(
                                value = subjectName,
                                onValueChange = { subjectName = it },
                                label = { Text("Mata Pelajaran") },
                                modifier = Modifier.weight(1.5f),
                                colors = outlinedColors()
                            )
                        }

                        // Summary Indicators
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusTag("H: $totalHadir", Color(0xFF10B981))
                                StatusTag("S: $totalSakit", Color(0xFF3B82F6))
                                StatusTag("I: $totalIzin", Color(0xFFF59E0B))
                                StatusTag("A: $totalAlpa", Color(0xFFEF4444))
                            }

                            Text(
                                text = "Kehadiran: $percentHadir%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (percentHadir >= 80) Color(0xFF10B981) else Color(0xFFEF4444)
                            )
                        }

                        // Quick action: Set Semua Hadir
                        OutlinedButton(
                            onClick = {
                                classStudents.forEach { statusMap[it.id] = "H" }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2DD4BF))
                        ) {
                            Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Setel Semua Siswa Hadir", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Student Attendance Rows
            item {
                Text(
                    text = "DAFTAR PRESENSI SISWA (${classStudents.size} Siswa)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )
            }

            items(classStudents, key = { it.id }) { s ->
                val currentStatus = statusMap[s.id] ?: "H"
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = s.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "NISN: ${s.nisn}",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("H", "S", "I", "A").forEach { code ->
                                val isSelected = currentStatus == code
                                val (bg, textCol) = when (code) {
                                    "H" -> (if (isSelected) Color(0xFF10B981) else Color(0xFF1E293B)) to Color.White
                                    "S" -> (if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B)) to Color.White
                                    "I" -> (if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B)) to Color.White
                                    else -> (if (isSelected) Color(0xFFEF4444) else Color(0xFF1E293B)) to Color.White
                                }

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(bg)
                                        .clickable { statusMap[s.id] = code },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = code,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textCol
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Journal Note section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "CATATAN JURNAL MENGAJAR HARI INI",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            letterSpacing = 1.sp
                        )

                        OutlinedTextField(
                            value = journalNote,
                            onValueChange = { journalNote = it },
                            placeholder = { Text("Tuliskan materi yang dibahas, aktivitas siswa, atau catatan khusus pembelajaran hari ini...") },
                            minLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("attendance_journal_note_input"),
                            colors = outlinedColors()
                        )
                    }
                }
            }

            // Save Batch Button
            item {
                Button(
                    onClick = {
                        val records = classStudents.map { s ->
                            AttendanceRecord(
                                studentId = s.id,
                                studentName = s.name,
                                className = selectedClass,
                                date = todayStr,
                                subjectName = subjectName,
                                status = statusMap[s.id] ?: "H"
                            )
                        }
                        viewModel.saveAttendanceBatch(records, journalNote)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_attendance_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E))
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Simpan Presensi & Jurnal Kelas", fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun StatusTag(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = label, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
