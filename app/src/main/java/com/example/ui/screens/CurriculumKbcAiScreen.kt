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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.User
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.outlinedColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumKbcAiScreen(
    viewModel: MainViewModel,
    currentUser: User?
) {
    val kbcDocOptions = listOf(
        "MODUL-KBC" to "Modul Ajar KBC (Sintak Terintegrasi Tag PC & PPRA)",
        "LKPD-KBC" to "Lembar Kerja Siswa KBC (Tadabbur & Refleksi Hati)",
        "ACP-KBC" to "Analisis Capaian Pembelajaran KBC (Panca Cinta & PPRA)",
        "TP-KBC" to "Tujuan Pembelajaran Berbasis Cinta (Kaidah Kasih Sayang)",
        "ATP-KBC" to "Alur Tujuan Pembelajaran KBC (3 Tahapan Alur Cinta)",
        "PROTA-KBC" to "Program Tahunan KBC (Pemetaan Tema Panca Cinta)",
        "PROSEM-KBC" to "Program Semester KBC (Distribusi Waktu & Metode)",
        "KKTP-KBC" to "KKTP KBC (Rubrik 4 Level: Tumbuh s/d Qudwah)",
        "RUBRIK-FORMATIF-KBC" to "Rubrik Asesmen Formatif KBC (4 Level Proses)",
        "RUBRIK-SUMATIF-KBC" to "Rubrik Asesmen Sumatif KBC (Bobot Komprehensif 100%)"
    )

    var selectedDocType by remember { mutableStateOf("MODUL-KBC") }
    var subject by remember { mutableStateOf(currentUser?.subjectAssigned ?: "Pendidikan Agama Islam") }
    var gradeLevel by remember { mutableStateOf("7") }
    var phase by remember { mutableStateOf("D") }
    var semester by remember { mutableStateOf("Semester 1 (Ganjil)") }
    var topic by remember { mutableStateOf("Cinta Sesama, Ukhuwah Insaniyah, dan Empati Sosial") }

    var docTypeExpanded by remember { mutableStateOf(false) }

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
                    modifier = Modifier.testTag("kbc_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Generator Kurikulum Berbasis Cinta (KBC)",
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Intro KBC
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB45309)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Integrasi Panca Cinta & 10 Nilai PPRA",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Membangun atmosfer belajar yang penuh kasih sayang, adab, empati, toleransi, dan keteladanan (Qudwah Hasanah).",
                            fontSize = 11.sp,
                            color = Color(0xFFFDE68A),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Input Form Card
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "PILIH JENIS DOKUMEN KBC (10 DOKUMEN):",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B),
                        letterSpacing = 1.sp
                    )

                    // Document Dropdown
                    ExposedDropdownMenuBox(
                        expanded = docTypeExpanded,
                        onExpandedChange = { docTypeExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = kbcDocOptions.firstOrNull { it.first == selectedDocType }?.let { "${it.first} - ${it.second}" } ?: selectedDocType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jenis Dokumen KBC") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = docTypeExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("kbc_doctype_select"),
                            colors = outlinedColors()
                        )
                        ExposedDropdownMenu(
                            expanded = docTypeExpanded,
                            onDismissRequest = { docTypeExpanded = false }
                        ) {
                            kbcDocOptions.forEach { (code, desc) ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(code, fontWeight = FontWeight.Bold)
                                            Text(desc, fontSize = 11.sp, color = Color(0xFF94A3B8))
                                        }
                                    },
                                    onClick = {
                                        selectedDocType = code
                                        docTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Mata Pelajaran") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedColors()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = gradeLevel,
                            onValueChange = { gradeLevel = it },
                            label = { Text("Kelas") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = outlinedColors()
                        )
                        OutlinedTextField(
                            value = phase,
                            onValueChange = { phase = it },
                            label = { Text("Fase") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = outlinedColors()
                        )
                    }

                    OutlinedTextField(
                        value = semester,
                        onValueChange = { semester = it },
                        label = { Text("Semester") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Topik / Fokus Nilai Panca Cinta") },
                        minLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("kbc_topic_input"),
                        colors = outlinedColors()
                    )
                }
            }

            // Quick Topic Preset Inspirations
            Text(
                text = "PILIHAN TEMA PANCA CINTA & PPRA:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            )

            val kbcPresets = listOf(
                "Cinta Sesama, Ukhuwah Insaniyah, dan Empati Sosial",
                "Cinta Allah & Rasul: Menumbuhkan Hati yang Khusyuk dan Ikhlas",
                "Cinta Ilmu: Semangat Menuntut Ilmu untuk Kemaslahatan Umat",
                "Cinta Bangsa & Alam: Harmoni Kebinekaan dan Pelestarian Semesta"
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                kbcPresets.forEach { p ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                            .clickable { topic = p }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(text = "❤️ $p", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button
            Button(
                onClick = {
                    if (subject.isNotBlank() && topic.isNotBlank()) {
                        viewModel.generateAiDocument(
                            docType = selectedDocType,
                            subject = subject.trim(),
                            gradeLevel = gradeLevel.trim(),
                            semester = semester.trim(),
                            topic = topic.trim(),
                            learningPhase = phase.trim().uppercase()
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("generate_kbc_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Dokumen KBC (Gemini AI)", fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
