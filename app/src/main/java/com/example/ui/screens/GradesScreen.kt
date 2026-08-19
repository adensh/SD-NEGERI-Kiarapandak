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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grade
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GradeRecord
import com.example.data.model.SchoolClass
import com.example.data.model.Student
import com.example.data.model.User
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.outlinedColors

@Composable
fun GradesScreen(
    viewModel: MainViewModel,
    currentUser: User?,
    classes: List<SchoolClass>,
    students: List<Student>,
    grades: List<GradeRecord>
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingGrade by remember { mutableStateOf<GradeRecord?>(null) }

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
                        modifier = Modifier.testTag("grades_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Input Nilai & Asesmen (${grades.size})",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }

            // Grades List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (grades.isEmpty()) {
                    item {
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
                                Icon(
                                    Icons.Default.Grade,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Belum Ada Data Nilai",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Ketuk tombol '+' di pojok kanan bawah untuk input Formatif TP, Sumatif LM, dan SAS.",
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF94A3B8),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                items(grades, key = { it.id }) { record ->
                    GradeItemCard(
                        record = record,
                        onEdit = {
                            editingGrade = record
                            showDialog = true
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        FloatingActionButton(
            onClick = {
                editingGrade = null
                showDialog = true
            },
            containerColor = Color(0xFF7C3AED),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_grade_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Input Nilai")
        }

        if (showDialog) {
            GradeInputDialog(
                initialGrade = editingGrade,
                students = students,
                classes = classes,
                defaultSubject = currentUser?.subjectAssigned ?: "Pendidikan Agama Islam",
                onDismiss = { showDialog = false },
                onSave = { stId, stName, cName, sName, tp1, tp2, tp3, tp4, sum1, sum2, sas ->
                    viewModel.saveGrade(stId, stName, cName, sName, tp1, tp2, tp3, tp4, sum1, sum2, sas)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
private fun GradeItemCard(
    record: GradeRecord,
    onEdit: () -> Unit
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
                        text = record.studentName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Kelas ${record.className} • ${record.subjectName}",
                        fontSize = 12.sp,
                        color = Color(0xFF2DD4BF)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFF7C3AED), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "NA: ${"%.1f".format(record.finalScore)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scores Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreMiniTag("TP1", record.tp1)
                ScoreMiniTag("TP2", record.tp2)
                ScoreMiniTag("TP3", record.tp3)
                ScoreMiniTag("TP4", record.tp4)
                ScoreMiniTag("LM1", record.sumatifLm1)
                ScoreMiniTag("LM2", record.sumatifLm2)
                ScoreMiniTag("SAS", record.sasScore)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Deskripsi: ${record.competencyDescriptor}",
                fontSize = 11.5.sp,
                color = Color(0xFFCBD5E1),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ScoreMiniTag(label: String, score: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 9.sp, color = Color(0xFF94A3B8))
        Text(
            text = "${score.toInt()}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (score >= 75) Color(0xFF10B981) else Color(0xFFEF4444)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GradeInputDialog(
    initialGrade: GradeRecord?,
    students: List<Student>,
    classes: List<SchoolClass>,
    defaultSubject: String,
    onDismiss: () -> Unit,
    onSave: (Long, String, String, String, Float, Float, Float, Float, Float, Float, Float) -> Unit
) {
    var selectedStudent by remember { mutableStateOf(students.firstOrNull { it.id == initialGrade?.studentId } ?: students.firstOrNull()) }
    var subjectName by remember { mutableStateOf(initialGrade?.subjectName ?: defaultSubject) }

    var tp1 by remember { mutableStateOf(initialGrade?.tp1?.toInt()?.toString() ?: "85") }
    var tp2 by remember { mutableStateOf(initialGrade?.tp2?.toInt()?.toString() ?: "88") }
    var tp3 by remember { mutableStateOf(initialGrade?.tp3?.toInt()?.toString() ?: "80") }
    var tp4 by remember { mutableStateOf(initialGrade?.tp4?.toInt()?.toString() ?: "90") }
    var sumatif1 by remember { mutableStateOf(initialGrade?.sumatifLm1?.toInt()?.toString() ?: "85") }
    var sumatif2 by remember { mutableStateOf(initialGrade?.sumatifLm2?.toInt()?.toString() ?: "87") }
    var sas by remember { mutableStateOf(initialGrade?.sasScore?.toInt()?.toString() ?: "88") }

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
                    text = "Input Nilai Asesmen Kurikulum Merdeka",
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
                        value = selectedStudent?.let { "${it.name} (${it.className})" } ?: "Pilih Siswa",
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
                                text = { Text("${s.name} (${s.className})") },
                                onClick = {
                                    selectedStudent = s
                                    studentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text("Mata Pelajaran") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedColors()
                )

                Text(
                    text = "NILAI FORMATIF (TP 1 - TP 4):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2DD4BF)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ScoreInput("TP 1", tp1, Modifier.weight(1f)) { tp1 = it }
                    ScoreInput("TP 2", tp2, Modifier.weight(1f)) { tp2 = it }
                    ScoreInput("TP 3", tp3, Modifier.weight(1f)) { tp3 = it }
                    ScoreInput("TP 4", tp4, Modifier.weight(1f)) { tp4 = it }
                }

                Text(
                    text = "NILAI SUMATIF LM & SAS:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ScoreInput("Sumatif 1", sumatif1, Modifier.weight(1f)) { sumatif1 = it }
                    ScoreInput("Sumatif 2", sumatif2, Modifier.weight(1f)) { sumatif2 = it }
                    ScoreInput("SAS / PAS", sas, Modifier.weight(1f)) { sas = it }
                }

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
                                onSave(
                                    s.id,
                                    s.name,
                                    s.className,
                                    subjectName.trim(),
                                    tp1.toFloatOrNull() ?: 0f,
                                    tp2.toFloatOrNull() ?: 0f,
                                    tp3.toFloatOrNull() ?: 0f,
                                    tp4.toFloatOrNull() ?: 0f,
                                    sumatif1.toFloatOrNull() ?: 0f,
                                    sumatif2.toFloatOrNull() ?: 0f,
                                    sas.toFloatOrNull() ?: 0f
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                    ) {
                        Text("Simpan Nilai", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreInput(
    label: String,
    value: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 10.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier,
        colors = outlinedColors()
    )
}
