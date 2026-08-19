package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.SchoolClass
import com.example.data.model.Student
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.outlinedColors

@Composable
fun StudentManagementScreen(
    viewModel: MainViewModel,
    students: List<Student>,
    classes: List<SchoolClass>
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("Semua") }
    var editingStudent by remember { mutableStateOf<Student?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val filteredStudents = remember(students, searchQuery, selectedClass) {
        students.filter { s ->
            (selectedClass == "Semua" || s.className == selectedClass) &&
            (searchQuery.isBlank() || s.name.contains(searchQuery, ignoreCase = true) || s.nisn.contains(searchQuery))
        }
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                            modifier = Modifier.testTag("students_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "Data Siswa (${filteredStudents.size})",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }

                    // Import Sample Data Button
                    Button(
                        onClick = {
                            val target = if (selectedClass != "Semua") selectedClass else classes.firstOrNull()?.name ?: "VII-A"
                            viewModel.importSampleStudents(target)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("import_sample_students_btn")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Import Contoh", fontSize = 11.5.sp)
                    }
                }
            }

            // Search Bar & Class Filter Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari nama siswa atau NISN...", color = Color(0xFF94A3B8)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF2DD4BF)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_student_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val classTabs = listOf("Semua") + classes.map { it.name }
                    classTabs.forEach { tab ->
                        FilterChip(
                            selected = selectedClass == tab,
                            onClick = { selectedClass = tab },
                            label = { Text(tab, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F766E),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF1E293B),
                                labelColor = Color(0xFF94A3B8)
                            )
                        )
                    }
                }
            }

            // Students List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredStudents, key = { it.id }) { s ->
                    StudentCard(
                        student = s,
                        onEdit = {
                            editingStudent = s
                            showDialog = true
                        },
                        onDelete = {
                            viewModel.deleteStudent(s.id, s.name)
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        FloatingActionButton(
            onClick = {
                editingStudent = null
                showDialog = true
            },
            containerColor = Color(0xFF0F766E),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_student_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah Siswa")
        }

        if (showDialog) {
            StudentFormDialog(
                initialStudent = editingStudent,
                classes = classes,
                onDismiss = { showDialog = false },
                onSave = {
                    viewModel.saveStudent(it)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
private fun StudentCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (student.gender == "L") Color(0xFF0284C7).copy(alpha = 0.2f) else Color(0xFFEC4899).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (student.gender == "L") Color(0xFF38BDF8) else Color(0xFFF472B6),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "NISN: ${student.nisn} • Kelas: ${student.className}",
                    fontSize = 11.5.sp,
                    color = Color(0xFF2DD4BF)
                )
                Text(
                    text = "Wali: ${student.parentName.ifBlank { "-" }} • HP: ${student.phone.ifBlank { "-" }}",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF38BDF8), modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun StudentFormDialog(
    initialStudent: Student?,
    classes: List<SchoolClass>,
    onDismiss: () -> Unit,
    onSave: (Student) -> Unit
) {
    var nisn by remember { mutableStateOf(initialStudent?.nisn ?: "") }
    var name by remember { mutableStateOf(initialStudent?.name ?: "") }
    var className by remember { mutableStateOf(initialStudent?.className ?: (classes.firstOrNull()?.name ?: "VII-A")) }
    var gender by remember { mutableStateOf(initialStudent?.gender ?: "L") }
    var religion by remember { mutableStateOf(initialStudent?.religion ?: "Islam") }
    var parentName by remember { mutableStateOf(initialStudent?.parentName ?: "") }
    var phone by remember { mutableStateOf(initialStudent?.phone ?: "") }

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
                    text = if (initialStudent == null) "Tambah Peserta Didik" else "Edit Data Siswa",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap Siswa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedColors()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nisn,
                        onValueChange = { nisn = it },
                        label = { Text("NISN") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedColors()
                    )
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Kelas") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedColors()
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = { gender = it.take(1).uppercase() },
                        label = { Text("JK (L/P)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedColors()
                    )
                    OutlinedTextField(
                        value = religion,
                        onValueChange = { religion = it },
                        label = { Text("Agama") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = outlinedColors()
                    )
                }

                OutlinedTextField(
                    value = parentName,
                    onValueChange = { parentName = it },
                    label = { Text("Nama Orang Tua / Wali") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedColors()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Nomor HP / WhatsApp") },
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
                            if (name.isNotBlank() && nisn.isNotBlank()) {
                                val s = Student(
                                    id = initialStudent?.id ?: 0L,
                                    nisn = nisn.trim(),
                                    name = name.trim(),
                                    className = className.trim(),
                                    gender = gender.trim(),
                                    religion = religion.trim(),
                                    parentName = parentName.trim(),
                                    phone = phone.trim()
                                )
                                onSave(s)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E))
                    ) {
                        Text("Simpan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
