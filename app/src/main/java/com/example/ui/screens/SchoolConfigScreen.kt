package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchoolConfig
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.outlinedColors

@Composable
fun SchoolConfigScreen(
    viewModel: MainViewModel,
    config: SchoolConfig?
) {
    var governmentName by remember(config) { mutableStateOf(config?.governmentName ?: "PEMERINTAH KABUPATEN BOGOR\nDINAS PENDIDIKAN") }
    var schoolName by remember(config) { mutableStateOf(config?.schoolName ?: "SMP NEGERI 1 SUKAJAYA") }
    var schoolAddress by remember(config) { mutableStateOf(config?.schoolAddress ?: "Jl. Raya Sukajaya No. 12, Sukajaya, Kab. Bogor, Jawa Barat") }
    var signatureCityDate by remember(config) { mutableStateOf(config?.signatureCityDate ?: "Sukajaya, 15 Juli 2024") }
    var headmasterName by remember(config) { mutableStateOf(config?.headmasterName ?: "Drs. H. Ahmad Fauzi, M.Pd.") }
    var headmasterNip by remember(config) { mutableStateOf(config?.headmasterNip ?: "197204151998021003") }
    var academicYear by remember(config) { mutableStateOf(config?.academicYear ?: "2024/2025") }
    var semester by remember(config) { mutableStateOf(config?.activeSemester ?: "Semester 1 (Ganjil)") }

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
                    modifier = Modifier.testTag("config_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Konfigurasi Sekolah & TTD",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }

        // Body Scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                        text = "IDENTITAS RESMI KOP SURAT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2DD4BF),
                        letterSpacing = 1.sp
                    )

                    OutlinedTextField(
                        value = governmentName,
                        onValueChange = { governmentName = it },
                        label = { Text("Instansi Pemerintah / Yayasan") },
                        minLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_gov_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = schoolName,
                        onValueChange = { schoolName = it },
                        label = { Text("Nama Lengkap Satuan Pendidikan") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_school_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = schoolAddress,
                        onValueChange = { schoolAddress = it },
                        label = { Text("Alamat Sekolah & Kontak") },
                        minLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_address_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )
                }
            }

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
                        text = "LEGALITAS TANDA TANGAN & PIMPINAN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        letterSpacing = 1.sp
                    )

                    OutlinedTextField(
                        value = headmasterName,
                        onValueChange = { headmasterName = it },
                        label = { Text("Nama Lengkap Kepala Sekolah + Gelar") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_kepsek_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = headmasterNip,
                        onValueChange = { headmasterNip = it },
                        label = { Text("NIP Kepala Sekolah") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_kepsek_nip_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = signatureCityDate,
                        onValueChange = { signatureCityDate = it },
                        label = { Text("Titik Mangsa / Kota & Tanggal TTD") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_city_date_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )
                }
            }

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
                        text = "PERIODE AKADEMIK AKTIF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B),
                        letterSpacing = 1.sp
                    )

                    OutlinedTextField(
                        value = academicYear,
                        onValueChange = { academicYear = it },
                        label = { Text("Tahun Pelajaran") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_year_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = semester,
                        onValueChange = { semester = it },
                        label = { Text("Semester Berjalan") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_semester_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = outlinedColors()
                    )
                }
            }

            Button(
                onClick = {
                    val updated = (config ?: SchoolConfig()).copy(
                        governmentName = governmentName.trim(),
                        schoolName = schoolName.trim(),
                        schoolAddress = schoolAddress.trim(),
                        signatureCityDate = signatureCityDate.trim(),
                        headmasterName = headmasterName.trim(),
                        headmasterNip = headmasterNip.trim(),
                        academicYear = academicYear.trim(),
                        activeSemester = semester.trim()
                    )
                    viewModel.updateSchoolConfig(updated)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_config_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E))
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text("Simpan Konfigurasi Sekolah", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

