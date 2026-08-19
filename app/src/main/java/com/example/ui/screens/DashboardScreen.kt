package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchoolClass
import com.example.data.model.SchoolConfig
import com.example.data.model.Student
import com.example.data.model.User
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.Amber400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Indigo800
import com.example.ui.theme.Orange500
import com.example.ui.theme.Pink500
import com.example.ui.theme.Purple600
import com.example.ui.theme.Rose600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    currentUser: User?,
    config: SchoolConfig?,
    classes: List<SchoolClass>,
    students: List<Student>,
    docCount: Int,
    journalCount: Int
) {
    val role = currentUser?.role ?: "Guru Mapel"
    val isAdmin = role == "Admin"
    val isWaliKelas = role == "Wali Kelas" || isAdmin

    val schoolName = config?.schoolName ?: "SMP NEGERI 1 SUKAJAYA"
    val academicYear = config?.academicYear ?: "2024/2025"
    val semester = config?.activeSemester ?: "Semester Ganjil"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
    ) {
        // Top Sleek Header with Indigo Gradient and rounded bottom
        Surface(
            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
            color = Color.Transparent,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Indigo700, Indigo800)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    // Header Top Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser?.fullName?.take(1)?.uppercase() ?: "A",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "SISTEM ADMINISTRASI",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.75f),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = schoolName,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Logout / Switch button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable { viewModel.logout() }
                                .padding(8.dp)
                                .testTag("logout_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Keluar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Greeting
                    Column {
                        Text(
                            text = "Halo, ${currentUser?.fullName ?: "Bapak/Ibu Guru"}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RoleBadge(role = role)
                            Text(
                                text = "$semester • T.A. $academicYear",
                                fontSize = 12.sp,
                                color = Color(0xFFE0E7FF)
                            )
                        }
                    }
                }
            }
        }

        // Scrollable Body Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Grid (2x2 Sleek Cards)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 1: Presensi / Siswa
                SleekStatCard(
                    title = "PRESENSI HARI INI",
                    value = "94%",
                    subValue = "${students.size} Siswa Terdaftar",
                    icon = Icons.Default.HowToReg,
                    iconTint = Indigo600,
                    iconBg = Color(0xFFEEF2FF),
                    modifier = Modifier.weight(1f)
                )

                // Stat 2: Dokumen AI
                SleekStatCard(
                    title = "DOKUMEN AI",
                    value = "$docCount",
                    subValue = "Tersimpan di Arsip",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = Emerald500,
                    iconBg = Color(0xFFECFDF5),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 3: Rombel Kelas
                SleekStatCard(
                    title = "ROMBEL KELAS",
                    value = "${classes.size}",
                    subValue = "Fase D (SMP)",
                    icon = Icons.Default.Class,
                    iconTint = Amber400,
                    iconBg = Color(0xFFFFFBEB),
                    modifier = Modifier.weight(1f)
                )

                // Stat 4: Jurnal Guru
                SleekStatCard(
                    title = "JURNAL GURU",
                    value = "$journalCount",
                    subValue = "Catatan Mengajar",
                    icon = Icons.Default.Assignment,
                    iconTint = Purple600,
                    iconBg = Color(0xFFFAF5FF),
                    modifier = Modifier.weight(1f)
                )
            }

            // Section: Modul Unggulan
            Text(
                text = "MODUL UNGGULAN",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            // Modul 1: Kurikulum Merdeka AI
            SleekModuleButton(
                title = "Kurikulum Merdeka AI",
                subtitle = "Generate Prota, Prosem, KKTP & Modul Ajar",
                gradientBrush = Brush.linearGradient(listOf(Indigo500, Purple600)),
                icon = Icons.Default.AutoAwesome,
                testTag = "menu_merdeka_ai",
                onClick = { viewModel.navigateTo(AppScreen.MERDEKA_AI) }
            )

            // Modul 2: Kurikulum Berbasis Cinta (KBC)
            SleekModuleButton(
                title = "Kurikulum Berbasis Cinta (KBC)",
                subtitle = "Integrasi Panca Cinta, PPRA & Modul Sintak PC",
                gradientBrush = Brush.linearGradient(listOf(Pink500, Rose600)),
                icon = Icons.Default.Favorite,
                testTag = "menu_kbc_ai",
                onClick = { viewModel.navigateTo(AppScreen.KBC_AI) }
            )

            // Modul 3: Absensi & Jurnal Kelas
            SleekModuleButton(
                title = "Absensi & Jurnal Kelas",
                subtitle = "Input Harian & Monitoring Kehadiran Siswa",
                gradientBrush = Brush.linearGradient(listOf(Amber400, Orange500)),
                icon = Icons.Default.HowToReg,
                testTag = "menu_attendance",
                onClick = { viewModel.navigateTo(AppScreen.ATTENDANCE) }
            )

            // Modul 4: Nilai & Asesmen Sumatif
            SleekModuleButton(
                title = "Nilai & Asesmen Sumatif",
                subtitle = "Pengolahan Nilai Formatif, Sumatif & SAS",
                gradientBrush = Brush.linearGradient(listOf(Purple600, Color(0xFF6366F1))),
                icon = Icons.Default.Grade,
                testTag = "menu_grades",
                onClick = { viewModel.navigateTo(AppScreen.GRADES) }
            )

            // Modul 5: Arsip & Cetak Dokumen
            SleekModuleButton(
                title = "Arsip & Cetak Dokumen AI",
                subtitle = "Pratinjau, Ekspor Word & Cetak PDF Resmi",
                gradientBrush = Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF0284C7))),
                icon = Icons.Default.FolderShared,
                testTag = "menu_saved_docs",
                onClick = { viewModel.navigateTo(AppScreen.SAVED_DOCS) }
            )

            // Modul 6: Wali Kelas (Wali & Admin)
            if (isWaliKelas) {
                SleekModuleButton(
                    title = "Bimbingan & Rekap Wali Kelas",
                    subtitle = "Konseling Karakter Panca Cinta & Rekapitulasi Rapor",
                    gradientBrush = Brush.linearGradient(listOf(Color(0xFFC026D3), Color(0xFFDB2777))),
                    icon = Icons.Default.SupervisorAccount,
                    testTag = "menu_wali_kelas",
                    onClick = { viewModel.navigateTo(AppScreen.WALI_KELAS) }
                )
            }

            // Admin Section Header
            if (isAdmin) {
                Text(
                    text = "PENGATURAN ADMINISTRATOR",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )

                SleekModuleButton(
                    title = "Kelola Siswa & Rombel",
                    subtitle = "Import Data Siswa NISN & Kelola Kelas",
                    gradientBrush = Brush.linearGradient(listOf(Color(0xFF4F46E5), Color(0xFF3730A3))),
                    icon = Icons.Default.Groups,
                    testTag = "menu_students",
                    onClick = { viewModel.navigateTo(AppScreen.STUDENT_MANAGEMENT) }
                )

                SleekModuleButton(
                    title = "Kelola Pengguna (RBAC)",
                    subtitle = "Hak Akses Akun Guru, Admin & Wali Kelas",
                    gradientBrush = Brush.linearGradient(listOf(Color(0xFFBE185D), Color(0xFF881337))),
                    icon = Icons.Default.ManageAccounts,
                    testTag = "menu_users",
                    onClick = { viewModel.navigateTo(AppScreen.USER_MANAGEMENT) }
                )

                SleekModuleButton(
                    title = "Konfigurasi Sekolah",
                    subtitle = "Identitas Kop Surat, Kepala Sekolah & T.A.",
                    gradientBrush = Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF0F172A))),
                    icon = Icons.Default.Settings,
                    testTag = "menu_config",
                    onClick = { viewModel.navigateTo(AppScreen.SCHOOL_CONFIG) }
                )
            }

            // Sleek AI Status Card
            SleekAiStatusCard()

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Sleek Bottom Navigation Bar
        SleekBottomNavBar(
            currentScreen = AppScreen.DASHBOARD,
            onNavigate = { viewModel.navigateTo(it) },
            isAdmin = isAdmin,
            isWali = isWaliKelas
        )
    }
}

@Composable
private fun SleekStatCard(
    title: String,
    value: String,
    subValue: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        modifier = modifier
            .border(1.dp, Slate800, RoundedCornerShape(18.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate400,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = value,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = iconTint
                    )
                    Text(
                        text = subValue,
                        fontSize = 10.sp,
                        color = Slate400,
                        maxLines = 1
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SleekModuleButton(
    title: String,
    subtitle: String,
    gradientBrush: Brush,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Slate800, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(gradientBrush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = Slate400,
                    lineHeight = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Slate500,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SleekAiStatusCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF3730A3).copy(alpha = 0.5f), RoundedCornerShape(18.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AI ENGINE AKTIF",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFA5B4FC),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Gemini AI & Template KBC siap men-generate 16 jenis dokumen administrasi resmi.",
                    fontSize = 11.5.sp,
                    color = Color(0xFFE0E7FF),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF312E81)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFA5B4FC),
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(rotation)
                )
            }
        }
    }
}

@Composable
fun SleekBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    isAdmin: Boolean,
    isWali: Boolean
) {
    Surface(
        color = Slate900,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Slate800, RoundedCornerShape(0.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SleekNavTabItem(
                label = "Home",
                icon = Icons.Default.Home,
                selected = currentScreen == AppScreen.DASHBOARD,
                onClick = { onNavigate(AppScreen.DASHBOARD) }
            )

            SleekNavTabItem(
                label = "Siswa",
                icon = Icons.Default.Groups,
                selected = currentScreen == AppScreen.STUDENT_MANAGEMENT || currentScreen == AppScreen.ATTENDANCE,
                onClick = { onNavigate(if (isAdmin) AppScreen.STUDENT_MANAGEMENT else AppScreen.ATTENDANCE) }
            )

            SleekNavTabItem(
                label = "AI Tools",
                icon = Icons.Default.AutoAwesome,
                selected = currentScreen == AppScreen.MERDEKA_AI || currentScreen == AppScreen.KBC_AI || currentScreen == AppScreen.SAVED_DOCS,
                onClick = { onNavigate(AppScreen.KBC_AI) }
            )

            SleekNavTabItem(
                label = "Sistem",
                icon = Icons.Default.Settings,
                selected = currentScreen == AppScreen.SCHOOL_CONFIG || currentScreen == AppScreen.USER_MANAGEMENT,
                onClick = { onNavigate(if (isAdmin) AppScreen.SCHOOL_CONFIG else AppScreen.SAVED_DOCS) }
            )
        }
    }
}

@Composable
private fun SleekNavTabItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Indigo500 else Slate400,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Indigo500 else Slate400
        )
    }
}

@Composable
fun RoleBadge(role: String) {
    val (bgColor, textColor) = when (role) {
        "Admin" -> Color(0xFF78350F) to Color(0xFFFDE68A)
        "Wali Kelas" -> Color(0xFF581C87) to Color(0xFFE9D5FF)
        else -> Color(0xFF1E1B4B) to Color(0xFFA5B4FC)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = role,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
