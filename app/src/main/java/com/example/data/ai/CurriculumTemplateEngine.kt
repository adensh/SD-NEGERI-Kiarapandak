package com.example.data.ai

import com.example.data.model.SchoolConfig

object CurriculumTemplateEngine {

    fun generateKopHtml(
        config: SchoolConfig,
        docType: String,
        subject: String,
        gradeLevel: String,
        semester: String
    ): String {
        val govLines = config.governmentName.lines().joinToString("<br>") { it.trim() }
        val docTitle = getDocTitle(docType)
        return """
            <div style="text-align: center; border-bottom: 3px double #000; padding-bottom: 12px; margin-bottom: 20px; font-family: 'Times New Roman', Times, serif;">
                <div style="font-size: 15pt; font-weight: bold; text-transform: uppercase; line-height: 1.3;">$govLines</div>
                <div style="font-size: 17pt; font-weight: bold; text-transform: uppercase; color: #0B3C5D; margin-top: 4px;">${config.schoolName}</div>
                <div style="font-size: 10.5pt; font-style: italic; margin-top: 2px;">${config.schoolAddress}</div>
            </div>
            <div style="text-align: center; margin-bottom: 24px;">
                <h2 style="font-size: 14pt; font-weight: bold; margin: 0; text-transform: uppercase; text-decoration: underline;">$docTitle</h2>
                <div style="font-size: 11pt; font-weight: bold; margin-top: 4px;">MATA PELAJARAN: ${subject.uppercase()}</div>
                <div style="font-size: 10.5pt; color: #444; margin-top: 2px;">KELAS $gradeLevel • $semester • TAHUN AJARAN ${config.academicYear}</div>
            </div>
        """.trimIndent()
    }

    fun generateSignatureHtml(
        config: SchoolConfig,
        teacherName: String,
        teacherNip: String
    ): String {
        return """
            <div style="margin-top: 40px; page-break-inside: avoid;">
                <table class="signature-table" style="width: 100%; border: none !important; border-collapse: collapse; font-family: inherit;">
                    <tbody>
                        <tr style="border: none !important;">
                            <td style="width: 50%; vertical-align: top; text-align: left; border: none !important; padding: 0 10px;">
                                <p style="margin: 0; font-size: 11pt;">Mengetahui,</p>
                                <p style="margin: 0; font-weight: bold; font-size: 11pt;">Kepala Sekolah</p>
                                <div style="height: 75px;"></div>
                                <p style="margin: 0; font-weight: bold; font-size: 11pt; text-decoration: underline;">${config.headmasterName}</p>
                                <p style="margin: 0; font-size: 10pt; color: #333;">NIP. ${config.headmasterNip}</p>
                            </td>
                            <td style="width: 50%; vertical-align: top; text-align: left; border: none !important; padding: 0 10px;">
                                <p style="margin: 0; font-size: 11pt;">${config.signatureCityDate}</p>
                                <p style="margin: 0; font-weight: bold; font-size: 11pt;">Guru Mata Pelajaran</p>
                                <div style="height: 75px;"></div>
                                <p style="margin: 0; font-weight: bold; font-size: 11pt; text-decoration: underline;">$teacherName</p>
                                <p style="margin: 0; font-size: 10pt; color: #333;">NIP. ${if (teacherNip.isBlank()) "-" else teacherNip}</p>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        """.trimIndent()
    }

    fun getDocTitle(docType: String): String {
        return when (docType) {
            "ADM-CP" -> "ANALISIS CAPAIAN PEMBELAJARAN (ADM-CP)"
            "ADM-TP" -> "TUJUAN PEMBELAJARAN (ADM-TP)"
            "ADM-ATP" -> "ALUR TUJUAN PEMBELAJARAN (ADM-ATP)"
            "ADM-PROTA" -> "PROGRAM TAHUNAN (ADM-PROTA)"
            "ADM-PROSEM" -> "PROGRAM SEMESTER (ADM-PROSEM)"
            "ADM-KKTP" -> "KRITERIA KETERCAPAIAN TUJUAN PEMBELAJARAN (ADM-KKTP)"
            "MODUL-AJAR-AI" -> "MODUL AJAR DEEP LEARNING (MIND-MEANING-JOY)"
            "ASESMEN-SUMATIF-AI" -> "INSTRUMEN & KISI-KISI ASESMEN SUMATIF"
            "ACP-KBC" -> "ANALISIS CAPAIAN PEMBELAJARAN KBC (ACP-KBC)"
            "TP-KBC" -> "TUJUAN PEMBELAJARAN BERBASIS CINTA (TP-KBC)"
            "ATP-KBC" -> "ALUR TUJUAN PEMBELAJARAN KBC (ATP-KBC)"
            "PROTA-KBC" -> "PROGRAM TAHUNAN KBC (PROTA-KBC)"
            "PROSEM-KBC" -> "PROGRAM SEMESTER KBC (PROSEM-KBC)"
            "KKTP-KBC" -> "KKTP BERBASIS KASIH SAYANG & QUDWAH (KKTP-KBC)"
            "MODUL-KBC" -> "MODUL AJAR KURIKULUM BERBASIS CINTA (KBC)"
            "LKPD-KBC" -> "LEMBAR KERJA PESERTA DIDIK KBC (LKPD-KBC)"
            "RUBRIK-FORMATIF-KBC" -> "RUBRIK ASESMEN FORMATIF PROSES KBC"
            "RUBRIK-SUMATIF-KBC" -> "RUBRIK ASESMEN SUMATIF KOMPREHENSIF KBC"
            else -> "PERANGKAT PEMBELAJARAN $docType"
        }
    }

    fun generateCompleteDocument(
        docType: String,
        subject: String,
        gradeLevel: String,
        semester: String,
        topic: String,
        learningPhase: String,
        teacherName: String,
        teacherNip: String,
        config: SchoolConfig
    ): String {
        val kop = generateKopHtml(config, docType, subject, gradeLevel, semester)
        val body = when (docType) {
            "ADM-CP" -> generateAdmCpHtml(subject, gradeLevel, learningPhase, topic)
            "ADM-TP" -> generateAdmTpHtml(subject, gradeLevel, learningPhase, topic)
            "ADM-ATP" -> generateAdmAtpHtml(subject, gradeLevel, learningPhase, topic, semester)
            "ADM-PROTA" -> generateAdmProtaHtml(subject, gradeLevel, topic)
            "ADM-PROSEM" -> generateAdmProsemHtml(subject, gradeLevel, semester, topic)
            "ADM-KKTP" -> generateAdmKktpHtml(subject, gradeLevel, topic)
            "MODUL-AJAR-AI" -> generateModulAjarDeepLearningHtml(subject, gradeLevel, semester, topic, learningPhase, config)
            "ASESMEN-SUMATIF-AI" -> generateAsesmenSumatifHtml(subject, gradeLevel, topic)
            "ACP-KBC" -> generateAcpKbcHtml(subject, gradeLevel, learningPhase, topic)
            "TP-KBC" -> generateTpKbcHtml(subject, gradeLevel, learningPhase, topic)
            "ATP-KBC" -> generateAtpKbcHtml(subject, gradeLevel, topic, semester)
            "PROTA-KBC" -> generateProtaKbcHtml(subject, gradeLevel, topic)
            "PROSEM-KBC" -> generateProsemKbcHtml(subject, gradeLevel, semester, topic)
            "KKTP-KBC" -> generateKktpKbcHtml(subject, gradeLevel, topic)
            "MODUL-KBC" -> generateModulKbcHtml(subject, gradeLevel, semester, topic, learningPhase, config)
            "LKPD-KBC" -> generateLkpdKbcHtml(subject, gradeLevel, topic)
            "RUBRIK-FORMATIF-KBC" -> generateRubrikFormatifKbcHtml(subject, topic)
            "RUBRIK-SUMATIF-KBC" -> generateRubrikSumatifKbcHtml(subject, topic)
            else -> generateGenericDocHtml(docType, subject, topic)
        }
        val sig = generateSignatureHtml(config, teacherName, teacherNip)

        return """
            $kop
            <div class="document-content" style="font-size: 11pt; line-height: 1.6; color: #111; font-family: 'Times New Roman', Times, serif;">
                $body
            </div>
            $sig
        """.trimIndent()
    }

    private fun generateAdmCpHtml(subject: String, grade: String, phase: String, topic: String): String {
        return """
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;" border="1" cellpadding="6">
                <tr style="background-color: #f2f2f2;">
                    <td style="width: 25%; font-weight: bold;">Fase / Kelas</td>
                    <td>Fase $phase / Kelas $grade</td>
                </tr>
                <tr>
                    <td style="font-weight: bold;">Mata Pelajaran</td>
                    <td>$subject</td>
                </tr>
                <tr style="background-color: #f2f2f2;">
                    <td style="font-weight: bold;">Elemen Pembelajaran</td>
                    <td>Al-Qur'an & Hadis, Akidah, Akhlak, Fikih, Sejarah Peradaban Islam</td>
                </tr>
                <tr>
                    <td style="font-weight: bold;">Fokus Pembelajaran / Topik</td>
                    <td>$topic</td>
                </tr>
            </table>

            <h3 style="font-size: 12pt; border-bottom: 2px solid #000; padding-bottom: 4px;">I. RASIONAL MATA PELAJARAN</h3>
            <p style="text-align: justify;">Pendidikan $subject merupakan wahana esensial dalam membangun pondasi spiritual, moral, dan intelektual peserta didik. Mata pelajaran ini membimbing peserta didik untuk mengembangkan pemahaman yang mendalam, sikap moderat, serta cinta terhadap ilmu dan sesama, sekaligus membentuk Profil Pelajar Pancasila yang beriman, bertakwa kepada Tuhan YME, dan berakhlak mulia.</p>

            <h3 style="font-size: 12pt; border-bottom: 2px solid #000; padding-bottom: 4px; margin-top: 20px;">II. TUJUAN MATA PELAJARAN</h3>
            <ol style="margin-left: 20px; padding-left: 0;">
                <li>Meningkatkan keimanan dan ketakwaan melalui pemahaman ayat kauniyah dan qauliyah.</li>
                <li>Menerapkan nilai-nilai universal kasih sayang dan toleransi dalam kehidupan bermasyarakat.</li>
                <li>Mengembangkan nalar kritis dan literasi komprehensif dalam memecahkan masalah kontekstual.</li>
            </ol>

            <h3 style="font-size: 12pt; border-bottom: 2px solid #000; padding-bottom: 4px; margin-top: 20px;">III. CAPAIAN PEMBELAJARAN FASE $phase</h3>
            <div style="background: #fdfdfd; border-left: 4px solid #0B3C5D; padding: 10px 15px; margin: 12px 0;">
                <p style="margin: 0; text-align: justify; font-style: italic;">"Pada akhir Fase $phase, peserta didik mampu memahami, menganalisis, serta menginternalisasikan hakikat $topic, serta mampu menyajikan gagasan solutif dan reflektif dalam bingkai akhlak karimah dan kebinekaan global."</p>
            </div>

            <h3 style="font-size: 12pt; border-bottom: 2px solid #000; padding-bottom: 4px; margin-top: 20px;">IV. PENJABARAN KKO & PEMETAAN 8 DIMENSI PROFIL LULUSAN</h3>
            <table style="width: 100%; border-collapse: collapse; margin-top: 10px;" border="1" cellpadding="6">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th style="width: 25%;">Elemen</th>
                        <th style="width: 35%;">Capaian Per Elemen & KKO</th>
                        <th style="width: 40%;">Pemetaan Dimensi Profil Lulusan</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Pemahaman Konsep</strong></td>
                        <td>Memahami (C2) dan Menganalisis (C4) $topic</td>
                        <td>Beriman & Bertakwa, Bernalar Kritis, Cinta Ilmu</td>
                    </tr>
                    <tr>
                        <td><strong>Keterampilan Proses</strong></td>
                        <td>Menerapkan (C3) dan Mengevaluasi (C5) penerapan dalam aksi nyata</td>
                        <td>Mandiri, Gotong Royong, Cinta Sesama & Lingkungan</td>
                    </tr>
                    <tr>
                        <td><strong>Refleksi & Aksi</strong></td>
                        <td>Mengkreasi (C6) gagasan cinta kasih dan toleransi sosial</td>
                        <td>Kreatif, Berkebinekaan Global, Cinta Bangsa</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateAdmTpHtml(subject: String, grade: String, phase: String, topic: String): String {
        return """
            <div style="background: #f0f7fa; border: 1px solid #c2e0ec; padding: 10px 14px; margin-bottom: 18px; border-radius: 4px;">
                <strong>Panduan Format Kode TP:</strong> [Kode Elemen].[Tingkat Kelas].[Nomor Urut TP] — Dirumuskan dengan kaidah ABCD (Audience, Behavior, Condition, Degree) dan KKO Taksonomi Bloom (C2-C5).
            </div>

            <h3 style="font-size: 12pt; font-weight: bold; margin-bottom: 10px;">DAFTAR TUJUAN PEMBELAJARAN (TP) — KELAS $grade</h3>
            <table style="width: 100%; border-collapse: collapse;" border="1" cellpadding="6">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th style="width: 12%;">Kode TP</th>
                        <th style="width: 20%;">Elemen</th>
                        <th style="width: 48%;">Rumusan Tujuan Pembelajaran (ABCD + KKO)</th>
                        <th style="width: 10%;">Level Bloom</th>
                        <th style="width: 10%;">Alokasi JP</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.1</td>
                        <td>Al-Qur'an & Hadis</td>
                        <td>Peserta didik (A) dapat <strong>mengidentifikasi & menjelaskan (B)</strong> makna pokok terkait $topic melalui telaah literatur (C) dengan tepat dan santun (D).</td>
                        <td style="text-align: center;">C2</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.2</td>
                        <td>Akidah & Akhlak</td>
                        <td>Peserta didik (A) dapat <strong>menganalisis (B)</strong> hikmah dan implikasi nilai kasih sayang dari $topic melalui diskusi kelompok (C) secara kritis dan berempati (D).</td>
                        <td style="text-align: center;">C4</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.3</td>
                        <td>Fikih & Ibadah</td>
                        <td>Peserta didik (A) dapat <strong>menerapkan & mendemonstrasikan (B)</strong> tata cara pelaksanaan amalan $topic dalam simulasi kelas (C) dengan tertib dan khusyuk (D).</td>
                        <td style="text-align: center;">C3</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.4</td>
                        <td>Sejarah & Cinta Kasih</td>
                        <td>Peserta didik (A) dapat <strong>mengevaluasi & merefleksikan (B)</strong> keteladanan tokoh dalam materi $topic melalui penulisan esai reflektif (C) dengan orisinal dan inspiratif (D).</td>
                        <td style="text-align: center;">C5</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr style="background-color: #eaeaea; font-weight: bold;">
                        <td colspan="4" style="text-align: right; padding-right: 15px;">TOTAL ALOKASI JP SEMESTER INI</td>
                        <td style="text-align: center;">24 JP</td>
                    </tr>
                </tfoot>
            </table>
        """.trimIndent()
    }

    private fun generateAdmAtpHtml(subject: String, grade: String, phase: String, topic: String, semester: String): String {
        return """
            <div style="text-align: center; margin-bottom: 16px;">
                <span style="border: 2px solid #0B3C5D; padding: 6px 16px; font-weight: bold; background: #e8f4f8; border-radius: 20px;">
                    DIAGRAM ALUR TUJUAN PEMBELAJARAN (ATP) — FASE $phase / KELAS $grade
                </span>
            </div>

            <div style="background: #fafafa; border: 1px dashed #777; padding: 12px; margin-bottom: 20px; text-align: center;">
                <span style="background: #2B7A78; color: white; padding: 4px 10px; border-radius: 4px; font-weight: bold;">TP.${grade}.1.1 (Fondasi C2)</span>
                &nbsp; ➔ &nbsp;
                <span style="background: #17252A; color: white; padding: 4px 10px; border-radius: 4px; font-weight: bold;">TP.${grade}.1.2 (Analisis C4)</span>
                &nbsp; ➔ &nbsp;
                <span style="background: #3AAFA9; color: white; padding: 4px 10px; border-radius: 4px; font-weight: bold;">TP.${grade}.1.3 (Aplikasi C3)</span>
                &nbsp; ➔ &nbsp;
                <span style="background: #D96B27; color: white; padding: 4px 10px; border-radius: 4px; font-weight: bold;">TP.${grade}.1.4 (Refleksi C5)</span>
            </div>

            <table style="width: 100%; border-collapse: collapse; font-size: 10pt;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th>Kode</th>
                        <th>Elemen</th>
                        <th>Tujuan Pembelajaran (TP)</th>
                        <th>Materi Pokok</th>
                        <th>Bloom</th>
                        <th>Dimensi Profil</th>
                        <th>JP</th>
                        <th>Sem</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.1</td>
                        <td>Al-Qur'an Hadis</td>
                        <td>Mengidentifikasi & memahami hakikat pokok $topic</td>
                        <td>Konsep Dasar & Dalil</td>
                        <td style="text-align: center;">C2</td>
                        <td>Beriman, Cinta Ilmu</td>
                        <td style="text-align: center;">6</td>
                        <td style="text-align: center;">1</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.2</td>
                        <td>Akidah Akhlak</td>
                        <td>Menganalisis nilai kasih sayang dan hikmah dari $topic</td>
                        <td>Nilai Kasih & Toleransi</td>
                        <td style="text-align: center;">C4</td>
                        <td>Kritis, Gotong Royong</td>
                        <td style="text-align: center;">6</td>
                        <td style="text-align: center;">1</td>
                    </tr>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.3</td>
                        <td>Fikih</td>
                        <td>Menerapkan prinsip dan adab amalan $topic dalam kehidupan</td>
                        <td>Aplikasi Amaliyah</td>
                        <td style="text-align: center;">C3</td>
                        <td>Mandiri, Berakhlak</td>
                        <td style="text-align: center;">6</td>
                        <td style="text-align: center;">1</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1.4</td>
                        <td>Sejarah Peradaban</td>
                        <td>Mengevaluasi keteladanan tokoh dan menghasilkan refleksi karya</td>
                        <td>Keteladanan Qudwah</td>
                        <td style="text-align: center;">C5</td>
                        <td>Kreatif, Kebinekaan</td>
                        <td style="text-align: center;">6</td>
                        <td style="text-align: center;">1</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateAdmProtaHtml(subject: String, grade: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold;">A. KALENDER DISTRIBUSI MINGGU EFEKTIF</h3>
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th>No</th>
                        <th>Semester</th>
                        <th>Jumlah Minggu Kalender</th>
                        <th>Minggu Tidak Efektif</th>
                        <th>Minggu Efektif</th>
                        <th>JP / Minggu</th>
                        <th>Total Jam Efektif</th>
                    </tr>
                </thead>
                <tbody>
                    <tr style="text-align: center;">
                        <td>1</td>
                        <td style="text-align: left;">Semester 1 (Ganjil)</td>
                        <td>26</td>
                        <td>8</td>
                        <td><strong>18</strong></td>
                        <td>3 JP</td>
                        <td><strong>54 JP</strong></td>
                    </tr>
                    <tr style="text-align: center; background-color: #f9f9f9;">
                        <td>2</td>
                        <td style="text-align: left;">Semester 2 (Genap)</td>
                        <td>26</td>
                        <td>10</td>
                        <td><strong>16</strong></td>
                        <td>3 JP</td>
                        <td><strong>48 JP</strong></td>
                    </tr>
                    <tr style="font-weight: bold; background-color: #eaeaea; text-align: center;">
                        <td colspan="4" style="text-align: right; padding-right: 15px;">TOTAL PROGRAM TAHUNAN</td>
                        <td>34 Minggu</td>
                        <td>3 JP</td>
                        <td>102 JP</td>
                    </tr>
                </tbody>
            </table>

            <h3 style="font-size: 11.5pt; font-weight: bold;">B. MATRIKS DISTRIBUSI ALOKASI WAKTU TAHUNAN</h3>
            <table style="width: 100%; border-collapse: collapse;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th style="width: 8%;">Sem</th>
                        <th style="width: 15%;">Kode TP</th>
                        <th style="width: 52%;">Materi Pokok / Lingkup Pembelajaran</th>
                        <th style="width: 12%;">Alokasi JP</th>
                        <th style="width: 13%;">Keterangan</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td rowspan="5" style="text-align: center; font-weight: bold; vertical-align: middle;">1 (Ganjil)</td>
                        <td>TP.${grade}.1.1</td>
                        <td>Hakikat Dasar & Nilai Utama $topic</td>
                        <td style="text-align: center;">12 JP</td>
                        <td style="text-align: center;">Tuntas</td>
                    </tr>
                    <tr>
                        <td>TP.${grade}.1.2</td>
                        <td>Menganalisis Makna Kasih Sayang dan Toleransi</td>
                        <td style="text-align: center;">12 JP</td>
                        <td style="text-align: center;">Tuntas</td>
                    </tr>
                    <tr>
                        <td>TP.${grade}.1.3</td>
                        <td>Praktik dan Aplikasi Amaliyah Kontekstual</td>
                        <td style="text-align: center;">12 JP</td>
                        <td style="text-align: center;">Tuntas</td>
                    </tr>
                    <tr>
                        <td>TP.${grade}.1.4</td>
                        <td>Refleksi Keteladanan & Proyek Sosial</td>
                        <td style="text-align: center;">12 JP</td>
                        <td style="text-align: center;">Tuntas</td>
                    </tr>
                    <tr style="background-color: #fff9e6; font-style: italic;">
                        <td>-</td>
                        <td><strong>JAM CADANGAN / ASESMEN SUMATIF AKHIR SEMESTER</strong></td>
                        <td style="text-align: center;">6 JP</td>
                        <td style="text-align: center;">Fleksibel</td>
                    </tr>
                    <tr style="background-color: #e6f3ff; font-weight: bold;">
                        <td colspan="3" style="text-align: right; padding-right: 15px;">SUBTOTAL SEMESTER 1</td>
                        <td style="text-align: center;">54 JP</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateAdmProsemHtml(subject: String, grade: String, semester: String, topic: String): String {
        return """
            <div style="margin-bottom: 12px; font-size: 10pt;">
                <strong>LEGENDA KODE WARNA PROSEM:</strong>
                <span style="background: #90CAF9; padding: 2px 8px; margin: 0 4px; border: 1px solid #1976D2;">Biru: JP Pembelajaran</span>
                <span style="background: #FFCDD2; padding: 2px 8px; margin: 0 4px; border: 1px solid #D32F2F;">Merah: Libur Sekolah</span>
                <span style="background: #FFF59D; padding: 2px 8px; margin: 0 4px; border: 1px solid #FBC02D;">Kuning: STS/PTS</span>
                <span style="background: #C8E6C9; padding: 2px 8px; margin: 0 4px; border: 1px solid #388E3C;">Hijau: SAS/PAS</span>
                <span style="background: #E0E0E0; padding: 2px 8px; margin: 0 4px; border: 1px solid #757575;">Abu: Cadangan / Remedial</span>
            </div>

            <table style="width: 100%; border-collapse: collapse; font-size: 9pt;" border="1" cellpadding="4">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th rowspan="2">No</th>
                        <th rowspan="2">Tujuan Pembelajaran (TP) / Materi</th>
                        <th rowspan="2">JP</th>
                        <th colspan="4">Juli</th>
                        <th colspan="4">Agustus</th>
                        <th colspan="4">September</th>
                        <th colspan="4">Oktober</th>
                        <th colspan="4">November</th>
                        <th colspan="4">Desember</th>
                        <th rowspan="2">Model Pembelajaran</th>
                    </tr>
                    <tr style="background-color: #1D5F8A; color: white; text-align: center;">
                        <th>1</th><th>2</th><th>3</th><th>4</th>
                        <th>1</th><th>2</th><th>3</th><th>4</th>
                        <th>1</th><th>2</th><th>3</th><th>4</th>
                        <th>1</th><th>2</th><th>3</th><th>4</th>
                        <th>1</th><th>2</th><th>3</th><th>4</th>
                        <th>1</th><th>2</th><th>3</th><th>4</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center;">1</td>
                        <td>TP 1: Pemahaman Konsep & Dalil $topic</td>
                        <td style="text-align: center;">12</td>
                        <td style="background: #FFCDD2;">L</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td>Problem Based Learning</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">2</td>
                        <td>TP 2: Analisis Nilai Kasih Sayang & Empati</td>
                        <td style="text-align: center;">12</td>
                        <td></td><td></td><td></td><td></td>
                        <td></td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td>Inquiry / Deep Learning</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">3</td>
                        <td>STS (Sumatif Tengah Semester)</td>
                        <td style="text-align: center;">3</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td></td>
                        <td style="background: #FFF59D; text-align: center; font-weight: bold;">STS</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td>CBT / Tertulis</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">4</td>
                        <td>TP 3: Praktik Amaliyah & Proyek Nyata</td>
                        <td style="text-align: center;">12</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td style="background: #90CAF9; text-align: center;">3</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td>Project-Based Learning</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">5</td>
                        <td>SAS (Sumatif Akhir Semester) & Remedial</td>
                        <td style="text-align: center;">6</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                        <td></td><td></td><td></td><td></td>
                        <td style="background: #C8E6C9; text-align: center; font-weight: bold;">SAS</td>
                        <td style="background: #E0E0E0; text-align: center;">Rem</td>
                        <td style="background: #FFCDD2;">L</td>
                        <td style="background: #FFCDD2;">L</td>
                        <td>Evaluasi Mandiri</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateAdmKktpHtml(subject: String, grade: String, topic: String): String {
        return """
            <div style="background: #fdfdfd; border-left: 4px solid #0B3C5D; padding: 10px 14px; margin-bottom: 18px;">
                <p style="margin: 0; font-size: 10pt;"><strong>Dasar Hukum:</strong> Permendikbudristek No. 21 Tahun 2022 tentang Standar Penilaian pada Pendidikan Anak Usia Dini, Jenjang Pendidikan Dasar, dan Jenjang Pendidikan Menengah. Penentuan kriteria ketercapaian menggunakan pendekatan Deskripsi Kriteria dan Rubrik Skala Bertingkat 4 Kategori.</p>
            </div>

            <h3 style="font-size: 11.5pt; font-weight: bold; margin-bottom: 10px;">RUBRIK 9 KOLOM KRITERIA KETERCAPAIAN TUJUAN PEMBELAJARAN (KKTP)</h3>
            <table style="width: 100%; border-collapse: collapse; font-size: 9.5pt;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th rowspan="2" style="width: 6%;">Kode TP</th>
                        <th rowspan="2" style="width: 24%;">Tujuan Pembelajaran & Indikator (IKTP)</th>
                        <th colspan="4">Interval Kategori Ketercapaian</th>
                        <th rowspan="2" style="width: 10%;">Tindak Lanjut Remedial</th>
                        <th rowspan="2" style="width: 10%;">Tindak Lanjut Pengayaan</th>
                        <th rowspan="2" style="width: 8%;">Kesimpulan</th>
                    </tr>
                    <tr style="background-color: #1D5F8A; color: white; text-align: center;">
                        <th style="width: 10%;">Baru Berkembang (0-65)</th>
                        <th style="width: 11%;">Layak ✓ (66-75) [KKTP]</th>
                        <th style="width: 11%;">Cakap (76-85)</th>
                        <th style="width: 10%;">Mahir (86-100)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.1</td>
                        <td><strong>IKTP 1.1:</strong> Mampu menjelaskan konsep dan dalil $topic dengan tepat.</td>
                        <td>Belum mampu menjelaskan konsep dasar.</td>
                        <td style="background: #e8f5e9;"><strong>Mampu menjelaskan pokok konsep dasar.</strong></td>
                        <td>Mampu menjelaskan dan memberi contoh konkret.</td>
                        <td>Mampu menganalisis serta mengaitkan antar konsep secara kritis.</td>
                        <td>Bimbingan tutor sebaya dan review materi.</td>
                        <td>Eksplorasi literatur lanjutan dan studi kasus.</td>
                        <td style="text-align: center; color: green; font-weight: bold;">Tercapai (Layak)</td>
                    </tr>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.${grade}.2</td>
                        <td><strong>IKTP 1.2:</strong> Menunjukkan empati dan aksi nyata dalam $topic.</td>
                        <td>Belum menunjukkan kepedulian sikap.</td>
                        <td style="background: #e8f5e9;"><strong>Menunjukkan sikap empati dengan bimbingan.</strong></td>
                        <td>Menunjukkan sikap kepedulian secara mandiri.</td>
                        <td>Menjadi teladan (qudwah hasanah) bagi teman sekelas.</td>
                        <td>Konseling dan pendampingan personal.</td>
                        <td>Menjadi duta empati kelas.</td>
                        <td style="text-align: center; color: green; font-weight: bold;">Tercapai (Cakap)</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateModulAjarDeepLearningHtml(
        subject: String,
        grade: String,
        semester: String,
        topic: String,
        phase: String,
        config: SchoolConfig
    ): String {
        return """
            <div style="background: #f3f9fc; border: 2px solid #0B3C5D; border-radius: 6px; padding: 12px 18px; margin-bottom: 20px;">
                <h3 style="margin: 0; color: #0B3C5D; font-size: 12pt;">PENDEKATAN PEMBELAJARAN DEEP LEARNING (MIND-MEANING-JOY)</h3>
                <p style="margin: 4px 0 0 0; font-size: 10pt;">1. <strong>Mindful Learning:</strong> Menghadirkan kesadaran penuh, fokus, dan keterlibatan batin.<br>
                2. <strong>Meaningful Learning:</strong> Menghubungkan materi dengan realitas kehidupan dan kebermaknaan hidup.<br>
                3. <strong>Joyful Learning:</strong> Menciptakan suasana belajar yang menyenangkan, penuh apresiasi, dan tanpa rasa takut.</p>
            </div>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px;">I. INFORMASI UMUM</h3>
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 15px;" border="1" cellpadding="5">
                <tr><td style="width: 30%; font-weight: bold;">Nama Penyusun</td><td>Guru Mata Pelajaran</td></tr>
                <tr><td style="font-weight: bold;">Satuan Pendidikan</td><td>${config.schoolName}</td></tr>
                <tr><td style="font-weight: bold;">Fase / Kelas / Semester</td><td>Fase $phase / Kelas $grade / $semester</td></tr>
                <tr><td style="font-weight: bold;">Alokasi Waktu</td><td>3 JP x 40 Menit (1 Pertemuan)</td></tr>
                <tr><td style="font-weight: bold;">Profil Pelajar Pancasila</td><td>Beriman, Bertakwa kepada Tuhan YME & Berakhlak Mulia, Gotong Royong, Bernalar Kritis, Kreatif</td></tr>
                <tr><td style="font-weight: bold;">Sarana & Prasarana</td><td>LCD Proyektor, Lembar Refleksi Emosional, Modul Interaktif, Video Inspiratif</td></tr>
                <tr><td style="font-weight: bold;">Target Peserta Didik</td><td>Peserta didik reguler / tipikal (Heterogen)</td></tr>
                <tr><td style="font-weight: bold;">Model Pembelajaran</td><td>Problem Based Learning (PBL) terintegrasi Deep Learning</td></tr>
            </table>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px; margin-top: 20px;">II. KOMPONEN INTI</h3>
            <p><strong>A. Tujuan Pembelajaran (TP):</strong><br>
            Melalui model PBL dengan pendekatan Deep Learning, peserta didik mampu menganalisis substansi $topic, menemukan nilai luhur kasih sayang, serta mendemonstrasikan tindakan empati nyata dalam interaksi sehari-hari dengan penuh kesadaran dan kegembiraan.</p>

            <p><strong>B. Pemahaman Bermakna (Meaningful):</strong><br>
            Pemahaman terhadap $topic bukan sekadar hafalan pengetahuan, melainkan panduan hidup untuk merawat hubungan harmonis dengan Tuhan, diri sendiri, sesama manusia, dan alam semesta.</p>

            <p><strong>C. Pertanyaan Pemantik (Mindful):</strong><br>
            <em>"Bagaimana perasaanmu ketika seseorang memperlakukanmu dengan penuh cinta dan kelembutan? Bagaimana $topic dapat mengubah caramu memandang orang lain?"</em></p>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px; margin-top: 20px;">III. KEGIATAN PEMBELAJARAN (RINCIAN SINTAK)</h3>
            
            <div style="background: #fafafa; border: 1px solid #ddd; padding: 10px 14px; margin-bottom: 12px;">
                <h4 style="margin: 0 0 6px 0; color: #0B3C5D;">1. Kegiatan Pendahuluan (15 Menit) — [Mindful State]</h4>
                <ul style="margin: 0; padding-left: 20px;">
                    <li>Guru menyapa dengan senyum ramah, doa bersama, dan presensi berbasis emosi (Check-in perasaan bahagia/antusias).</li>
                    <li>Latihan bernapas sadar (Mindful Breathing) 2 menit untuk menenangkan pikiran dan memfokuskan atensi.</li>
                    <li>Apersepsi mengaitkan pengalaman nyata peserta didik dengan materi $topic.</li>
                </ul>
            </div>

            <div style="background: #fafafa; border: 1px solid #ddd; padding: 10px 14px; margin-bottom: 12px;">
                <h4 style="margin: 0 0 6px 0; color: #0B3C5D;">2. Kegiatan Inti (90 Menit) — [Meaningful Exploration & Joyful Collab]</h4>
                <ul style="margin: 0; padding-left: 20px;">
                    <li><strong>Orientasi Masalah:</strong> Menayangkan cuplikan video studi kasus nyata tentang implementasi $topic dalam kehidupan.</li>
                    <li><strong>Organisasi Belajar:</strong> Peserta didik membentuk kelompok lingkaran kasih (Love Circles) beranggotakan 4-5 orang.</li>
                    <li><strong>Penyelidikan Bermakna:</strong> Kelompok menganalisis masalah pada LKPD, menggali dalil, dan mendiskusikan solusi penuh empati.</li>
                    <li><strong>Presentasi Menyenangkan (Joyful):</strong> Setiap kelompok menyajikan hasil temuan dengan gaya kreatif (infografis mini / roleplay singkat).</li>
                    <li><strong>Apresiasi:</strong> Guru dan teman memberikan tepuk apresiasi penuh kehangatan (Love Clap).</li>
                </ul>
            </div>

            <div style="background: #fafafa; border: 1px solid #ddd; padding: 10px 14px; margin-bottom: 12px;">
                <h4 style="margin: 0 0 6px 0; color: #0B3C5D;">3. Kegiatan Penutup (15 Menit) — [Reflective Closure]</h4>
                <ul style="margin: 0; padding-left: 20px;">
                    <li>Peserta didik menuliskan 1 kalimat mutiara / komitmen cinta pada lembar Refleksi Diri.</li>
                    <li>Guru memberikan umpan balik konstruktif dan motivasi spiritual.</li>
                    <li>Doa penutup majelis dan salam hangat.</li>
                </ul>
            </div>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px; margin-top: 20px;">IV. ASESMEN & LEMBAR REFLEKSI</h3>
            <p>1. <strong>Asesmen Awal:</strong> Pertanyaan lisan diagnostik non-kognitif (kesiapan belajar & minat).<br>
            2. <strong>Asesmen Formatif:</strong> Observasi keaktifan diskusi kelompok dan rubrik unjuk kerja LKPD.<br>
            3. <strong>Asesmen Sumatif:</strong> Tes tertulis pemahaman konsep & studi kasus $topic.</p>
        """.trimIndent()
    }

    private fun generateAsesmenSumatifHtml(subject: String, grade: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px;">A. KISI-KISI ASESMEN SUMATIF LINGKUP MATERI</h3>
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 9.5pt;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #0B3C5D; color: white; text-align: center;">
                        <th style="width: 6%;">No</th>
                        <th style="width: 25%;">Capaian Pembelajaran (CP)</th>
                        <th style="width: 25%;">Materi / Topik</th>
                        <th style="width: 24%;">Indikator Soal</th>
                        <th style="width: 10%;">Level Kognitif</th>
                        <th style="width: 10%;">Bentuk Soal</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center;">1</td>
                        <td>Memahami hakikat dan penerapan $topic</td>
                        <td>Konsep & Dalil Pokok</td>
                        <td>Disajikan narasi, siswa dapat menganalisis pesan utama dalil.</td>
                        <td style="text-align: center;">L2 (C3)</td>
                        <td style="text-align: center;">Pilihan Ganda</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">2</td>
                        <td>Menganalisis dampak sosial nilai kasih sayang</td>
                        <td>Studi Kasus Kontekstual</td>
                        <td>Disajikan kasus perselisihan, siswa dapat merumuskan solusi damai berbasis empati.</td>
                        <td style="text-align: center;">L3 (C4/C5)</td>
                        <td style="text-align: center;">Uraian HOTS</td>
                    </tr>
                </tbody>
            </table>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px;">B. BANK SOAL PILIHAN GANDA (HOTS)</h3>
            <ol style="margin-left: 20px; padding-left: 0; line-height: 1.8;">
                <li>Dalam ajaran nilai luhur $topic, kasih sayang kepada sesama diwujudkan melalui sikap...<br>
                    A. Menuntut orang lain memahami perasaan kita<br>
                    <strong>B. Memberikan empati dan bantuan tanpa memandang perbedaan (Kunci: B)</strong><br>
                    C. Membantu hanya kepada orang yang berbuat baik kepada kita<br>
                    D. Mengabaikan kesalahan teman demi menjaga perdamaian semu
                </li>
                <li style="margin-top: 10px;">Perhatikan pernyataan berikut:<br>
                    (1) Menjaga lisan dari perkataan yang menyakiti hati<br>
                    (2) Membantu teman saat ulangan harian berlangsung<br>
                    (3) Memaafkan kesalahan orang lain sebelum diminta<br>
                    (4) Merawat kebersihan fasilitas umum di sekolah<br>
                    Penerapan hakikat $topic yang tepat ditunjukkan oleh nomor...<br>
                    A. (1), (2), dan (3)<br>
                    <strong>B. (1), (3), dan (4) (Kunci: B)</strong><br>
                    C. (2), (3), dan (4)<br>
                    D. (1), (2), dan (4)
                </li>
            </ol>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px; margin-top: 20px;">C. SOAL URAIAN BERBASIS STUDI KASUS & RUBRIK SKOR</h3>
            <p><strong>Soal Uraian:</strong><br>
            <em>"Di kelasmu terdapat teman yang sering menyendiri dan tampak murung karena kesulitan mengikuti pelajaran. Bagaimana langkah konkret yang dapat kamu lakukan berdasarkan pemahaman materi $topic dan nilai kasih sayang?"</em></p>
            
            <table style="width: 100%; border-collapse: collapse; font-size: 9.5pt;" border="1" cellpadding="5">
                <tr style="background-color: #f2f2f2; font-weight: bold;">
                    <td style="width: 70%;">Kriteria Jawaban</td>
                    <td style="width: 30%; text-align: center;">Skor Maksimal</td>
                </tr>
                <tr>
                    <td>Menyebutkan pendekatan empati personal tanpa menghakimi dan menawarkan belajar bersama dengan sabar.</td>
                    <td style="text-align: center;">10</td>
                </tr>
                <tr>
                    <td>Menghubungkan tindakan tersebut dengan dalil/nilai spiritual kasih sayang $topic.</td>
                    <td style="text-align: center;">10</td>
                </tr>
                <tr style="font-weight: bold; background-color: #eaeaea;">
                    <td>TOTAL SKOR URAIAN</td>
                    <td style="text-align: center;">20</td>
                </tr>
            </table>
        """.trimIndent()
    }

    // --- KBC & PPRA SPECIFIC DOCUMENTS ---

    private fun generateAcpKbcHtml(subject: String, grade: String, phase: String, topic: String): String {
        return """
            <div style="background: #eef9f6; border-left: 5px solid #2B7A78; padding: 12px 16px; margin-bottom: 20px;">
                <h3 style="margin: 0; color: #2B7A78; font-size: 12pt;">INTEGRASI PANCA CINTA (KBC) & 10 NILAI PPRA</h3>
                <p style="margin: 4px 0 0 0; font-size: 10pt;">
                <strong>Panca Cinta:</strong> 1. Cinta Allah & Rasul, 2. Cinta Diri & Sesama, 3. Cinta Ilmu Pengetahuan, 4. Cinta Bangsa & Negara, 5. Cinta Alam & Lingkungan.<br>
                <strong>Nilai PPRA:</strong> Berkeadaban (Ta'addub), Keteladanan (Qudwah), Kewarganegaraan & Kebangsaan (Muwatanah), Mengambil Jalan Tengah (Tawassut), Berimbang (Tawazun), Lurus & Tegas (I'tidal), Kesetaraan (Musawah), Musyawarah (Syura), Toleransi (Tasamuh), Dinamis & Inovatif (Tatawwur wa Ibtikar).
                </p>
            </div>

            <h3 style="font-size: 12pt; border-bottom: 2px solid #2B7A78; padding-bottom: 4px;">I. RASIONALISASI ACP TERINTEGRASI PANCA CINTA</h3>
            <p style="text-align: justify;">Pembelajaran $subject dalam bingkai Kurikulum Berbasis Cinta (KBC) memposisikan cinta sebagai energi penggerak seluruh aktivitas pendidikan. Pembelajaran tidak hanya mentransfer kognisi mengenai $topic, namun menumbuhkan rasa takzim kepada Sang Pencipta dan kasih sayang mendalam kepada sesama makhluk, selaras dengan prinsip Rahmatan Lil 'Alamin.</p>

            <h3 style="font-size: 12pt; border-bottom: 2px solid #2B7A78; padding-bottom: 4px; margin-top: 20px;">II. MATRIKS PEMETAAN CAPAIAN PEMBELAJARAN KBC</h3>
            <table style="width: 100%; border-collapse: collapse; font-size: 10pt;" border="1" cellpadding="6">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th style="width: 25%;">Pilar Panca Cinta</th>
                        <th style="width: 35%;">Capaian Integrasi $topic</th>
                        <th style="width: 40%;">Nilai Karakter PPRA</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>1. Cinta Allah & Rasul</strong></td>
                        <td>Memahami $topic sebagai manifestasi syukur dan ketaatan ibadah.</td>
                        <td>Ta'addub (Berkeadaban), Qudwah (Keteladanan)</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td><strong>2. Cinta Diri & Sesama</strong></td>
                        <td>Menginternalisasi adab empati, memaafkan, dan saling menolong.</td>
                        <td>Tasamuh (Toleransi), Musawah (Kesetaraan)</td>
                    </tr>
                    <tr>
                        <td><strong>3. Cinta Ilmu Pengetahuan</strong></td>
                        <td>Memiliki rasa ingin tahu tinggi dan nalar kritis terhadap substansi $topic.</td>
                        <td>Tatawwur wa Ibtikar (Dinamis & Inovatif)</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td><strong>4. Cinta Bangsa & Negara</strong></td>
                        <td>Menjunjung persatuan, kedamaian, dan keberagaman bangsa.</td>
                        <td>Muwatanah (Kebangsaan), Syura (Musyawarah)</td>
                    </tr>
                    <tr>
                        <td><strong>5. Cinta Alam & Lingkungan</strong></td>
                        <td>Menjaga kelestarian dan keharmonisan lingkungan sekitar.</td>
                        <td>Tawazun (Berimbang), I'tidal (Tegas & Lurus)</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateTpKbcHtml(subject: String, grade: String, phase: String, topic: String): String {
        return """
            <h3 style="font-size: 12pt; font-weight: bold; margin-bottom: 10px;">DAFTAR TUJUAN PEMBELAJARAN BERBASIS CINTA (TP-KBC)</h3>
            <table style="width: 100%; border-collapse: collapse;" border="1" cellpadding="6">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th style="width: 12%;">Kode TP</th>
                        <th style="width: 48%;">Rumusan TP Berbasis Kasih Sayang (ABCD + KBC)</th>
                        <th style="width: 25%;">Integrasi Panca Cinta</th>
                        <th style="width: 15%;">Nilai PPRA</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.KBC.${grade}.1</td>
                        <td>Peserta didik mampu <strong>menjelaskan dengan kelembutan hati</strong> esensi dan dalil $topic melalui telaah mendalam untuk memperkokoh iman.</td>
                        <td>Cinta Allah & Rasul, Cinta Ilmu</td>
                        <td style="text-align: center;">Ta'addub & Qudwah</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td style="text-align: center; font-weight: bold;">TP.KBC.${grade}.2</td>
                        <td>Peserta didik mampu <strong>menganalisis & merefleksikan</strong> peran kasih sayang dalam menyelesaikan problematika $topic di lingkungan sosial.</td>
                        <td>Cinta Diri & Sesama</td>
                        <td style="text-align: center;">Tasamuh & Musawah</td>
                    </tr>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">TP.KBC.${grade}.3</td>
                        <td>Peserta didik mampu <strong>merancang aksi peduli nyata</strong> yang mencerminkan keteladanan akhlak mulia dari pemahaman $topic.</td>
                        <td>Cinta Bangsa & Lingkungan</td>
                        <td style="text-align: center;">Tawazun & Muwatanah</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateAtpKbcHtml(subject: String, grade: String, topic: String, semester: String): String {
        return """
            <div style="background: #eef9f6; border: 1px dashed #2B7A78; padding: 12px; margin-bottom: 20px; text-align: center;">
                <span style="font-weight: bold; color: #2B7A78;">ALUR PEMBELAJARAN KBC:</span><br>
                <span>🌱 <strong>Tahap 1: Pengenalan Cinta (Ta'aruf al-Hubb)</strong></span> ➔ 
                <span>🌸 <strong>Tahap 2: Penghayatan Empati (Tafakkur ar-Rahmah)</strong></span> ➔ 
                <span>🌳 <strong>Tahap 3: Aksi Keteladanan Nyata (Qudwah Hasanah)</strong></span>
            </div>

            <table style="width: 100%; border-collapse: collapse; font-size: 10pt;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th>Kode</th>
                        <th>Alur Pembelajaran KBC</th>
                        <th>Materi Pokok</th>
                        <th>Panca Cinta</th>
                        <th>PPRA</th>
                        <th>JP</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">ATP.KBC.1</td>
                        <td>Mengenal dan mencintai kebenaran dalil $topic</td>
                        <td>Fondasi Spiritualitas</td>
                        <td>Cinta Allah & Rasul</td>
                        <td>Ta'addub</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                    <tr style="background-color: #f9f9f9;">
                        <td style="text-align: center; font-weight: bold;">ATP.KBC.2</td>
                        <td>Menumbuhkan empati dan persaudaraan melalui dialog kasih</td>
                        <td>Harmoni Sosial & Toleransi</td>
                        <td>Cinta Sesama</td>
                        <td>Tasamuh</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                    <tr>
                        <td style="text-align: center; font-weight: bold;">ATP.KBC.3</td>
                        <td>Melakukan aksi peduli dan keteladanan lingkungan</td>
                        <td>Aksi Nyata & Proyek Kasih</td>
                        <td>Cinta Lingkungan</td>
                        <td>Qudwah & Tawazun</td>
                        <td style="text-align: center;">6 JP</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateProtaKbcHtml(subject: String, grade: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold;">PROGRAM TAHUNAN KURIKULUM BERBASIS CINTA (PROTA-KBC)</h3>
            <table style="width: 100%; border-collapse: collapse; margin-top: 10px;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th>Semester</th>
                        <th>Tema Panca Cinta</th>
                        <th>Tujuan Pembelajaran & Ruang Lingkup</th>
                        <th>Alokasi JP</th>
                        <th>Nilai PPRA</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td rowspan="3" style="text-align: center; font-weight: bold;">1 (Ganjil)</td>
                        <td>Cinta Allah, Rasul & Diri</td>
                        <td>Membangun kesadaran spiritual dan akhlak terpuji dalam $topic</td>
                        <td style="text-align: center;">18 JP</td>
                        <td style="text-align: center;">Ta'addub & Qudwah</td>
                    </tr>
                    <tr>
                        <td>Cinta Sesama & Ilmu</td>
                        <td>Mempererat ukhuwah insaniyah dan semangat belajar bermakna</td>
                        <td style="text-align: center;">18 JP</td>
                        <td style="text-align: center;">Tasamuh & Tatawwur</td>
                    </tr>
                    <tr>
                        <td>Cinta Bangsa & Alam</td>
                        <td>Menjaga keharmonisan sosial dan pelestarian alam sekitar</td>
                        <td style="text-align: center;">18 JP</td>
                        <td style="text-align: center;">Muwatanah & Tawazun</td>
                    </tr>
                    <tr style="background-color: #e6f7f4; font-weight: bold;">
                        <td colspan="3" style="text-align: right; padding-right: 15px;">TOTAL ALOKASI WAKTU KBC SEMESTER 1</td>
                        <td style="text-align: center;">54 JP</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateProsemKbcHtml(subject: String, grade: String, semester: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold;">PROGRAM SEMESTER KURIKULUM BERBASIS CINTA (PROSEM-KBC)</h3>
            <table style="width: 100%; border-collapse: collapse; font-size: 9pt; margin-top: 10px;" border="1" cellpadding="4">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th>No</th>
                        <th>Fokus Panca Cinta & Materi $topic</th>
                        <th>JP</th>
                        <th>Bulan 1</th>
                        <th>Bulan 2</th>
                        <th>Bulan 3</th>
                        <th>Bulan 4</th>
                        <th>Bulan 5</th>
                        <th>Bulan 6</th>
                        <th>Metode KBC</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center;">1</td>
                        <td>Panca Cinta 1: Mahabbah Ilahiyah & Konsep Dasar</td>
                        <td style="text-align: center;">12</td>
                        <td style="background: #a7f3d0; text-align: center;">6</td>
                        <td style="background: #a7f3d0; text-align: center;">6</td>
                        <td></td><td></td><td></td><td></td>
                        <td>Tadabbur & Refleksi Hati</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">2</td>
                        <td>Panca Cinta 2: Ukhuwah & Empati Sosial</td>
                        <td style="text-align: center;">12</td>
                        <td></td><td></td>
                        <td style="background: #a7f3d0; text-align: center;">6</td>
                        <td style="background: #a7f3d0; text-align: center;">6</td>
                        <td></td><td></td>
                        <td>Circle of Love & Roleplay</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">3</td>
                        <td>Panca Cinta 3: Aksi Kasih Lingkungan & Penilaian Qudwah</td>
                        <td style="text-align: center;">12</td>
                        <td></td><td></td><td></td><td></td>
                        <td style="background: #a7f3d0; text-align: center;">6</td>
                        <td style="background: #a7f3d0; text-align: center;">6</td>
                        <td>Proyek Aksi Nyata KBC</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateKktpKbcHtml(subject: String, grade: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold;">KRITERIA KETERCAPAIAN BERBASIS KASIH SAYANG & QUDWAH (KKTP-KBC)</h3>
            <p>Pengukuran ketercapaian siswa mengintegrasikan ranah kognitif, kepekaan afektif, dan aksi keteladanan (Qudwah):</p>
            
            <table style="width: 100%; border-collapse: collapse; font-size: 9.5pt;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th style="width: 25%;">Aspek KBC</th>
                        <th style="width: 18%;">Level 1: Mulai Tumbuh</th>
                        <th style="width: 18%;">Level 2: Berkembang (KKTP)</th>
                        <th style="width: 19%;">Level 3: Cakap Mandiri</th>
                        <th style="width: 20%;">Level 4: Qudwah Hasanah (Teladan)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Kelembutan Lisan & Adab</strong></td>
                        <td>Kadang masih menggunakan kata kasar jika marah.</td>
                        <td>Mampu menjaga lisan dengan pengingatan guru.</td>
                        <td>Konsisten bertutur kata santun dan menghargai teman.</td>
                        <td>Menjadi penyejuk suasana dan pendamai saat ada perselisihan.</td>
                    </tr>
                    <tr>
                        <td><strong>Pemahaman Nilai $topic</strong></td>
                        <td>Hanya menghafal teks definisi.</td>
                        <td>Memahami maksud dasar dan hikmah ajaran.</td>
                        <td>Mampu menguraikan kaitan materi dengan kasih sayang.</td>
                        <td>Mampu menginspirasi orang lain untuk berbuat kebajikan.</td>
                    </tr>
                    <tr>
                        <td><strong>Aksi Empati Nyata</strong></td>
                        <td>Pasif terhadap kesulitan teman.</td>
                        <td>Mau membantu jika diminta secara khusus.</td>
                        <td>Inisiatif membantu tanpa diminta secara tulus.</td>
                        <td>Memimpin gerakan kepedulian sosial di kelas dan sekolah.</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateModulKbcHtml(
        subject: String,
        grade: String,
        semester: String,
        topic: String,
        phase: String,
        config: SchoolConfig
    ): String {
        return """
            <div style="background: #eef9f6; border: 2px solid #2B7A78; border-radius: 6px; padding: 12px 18px; margin-bottom: 20px;">
                <h3 style="margin: 0; color: #2B7A78; font-size: 12pt;">MODUL AJAR KURIKULUM BERBASIS CINTA (KBC)</h3>
                <p style="margin: 4px 0 0 0; font-size: 10pt;">
                <strong>Pilar Utama:</strong> Cinta Allah & Rasul, Cinta Sesama, Cinta Ilmu, Cinta Bangsa, Cinta Lingkungan.<br>
                <strong>Tag Pembelajaran:</strong> Setiap sintak ditandai dengan kode <code>(PC: [Pilar Cinta] | PPRA: [Nilai PPRA])</code>.
                </p>
            </div>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px;">I. IDENTITAS MODUL KBC</h3>
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 15px;" border="1" cellpadding="5">
                <tr><td style="width: 30%; font-weight: bold;">Satuan Pendidikan</td><td>${config.schoolName}</td></tr>
                <tr><td style="font-weight: bold;">Mata Pelajaran & Fase</td><td>$subject / Fase $phase (Kelas $grade)</td></tr>
                <tr><td style="font-weight: bold;">Topik Utama</td><td>$topic</td></tr>
                <tr><td style="font-weight: bold;">Fokus Panca Cinta</td><td>Pilar 2: Cinta Diri & Sesama & Pilar 3: Cinta Ilmu Pengetahuan</td></tr>
                <tr><td style="font-weight: bold;">Fokus PPRA</td><td>Tasamuh (Toleransi), Musawah (Kesetaraan), Qudwah (Keteladanan)</td></tr>
            </table>

            <h3 style="font-size: 11.5pt; font-weight: bold; border-bottom: 1px solid #000; padding-bottom: 3px; margin-top: 20px;">II. SINTAK KEGIATAN PEMBELAJARAN KBC</h3>
            
            <div style="background: #fafafa; border: 1px solid #ddd; padding: 10px 14px; margin-bottom: 12px;">
                <h4 style="margin: 0 0 6px 0; color: #2B7A78;">A. Tahap Pembuka: Sentuhan Kasih Sayang (15 Menit)</h4>
                <ul style="margin: 0; padding-left: 20px;">
                    <li>Guru menyapa dengan senyum penuh kehangatan, mendoakan keberkahan seluruh siswa. <code>(PC: Cinta Allah & Rasul | PPRA: Ta'addub)</code></li>
                    <li>Saling bertukar kabar baik dan apresiasi singkat antar teman sebangku. <code>(PC: Cinta Sesama | PPRA: Musawah)</code></li>
                    <li>Apersepsi dengan kisah inspiratif tentang pentingnya kelembutan dan kasih sayang dalam $topic.</li>
                </ul>
            </div>

            <div style="background: #fafafa; border: 1px solid #ddd; padding: 10px 14px; margin-bottom: 12px;">
                <h4 style="margin: 0 0 6px 0; color: #2B7A78;">B. Tahap Inti: Eksplorasi Panca Cinta (90 Menit)</h4>
                <ul style="margin: 0; padding-left: 20px;">
                    <li><strong>Eksplorasi Makna:</strong> Siswa membaca teks materi $topic dengan niat mencari rida Allah dan menambah bekal kebaikan. <code>(PC: Cinta Ilmu | PPRA: Tatawwur)</code></li>
                    <li><strong>Lingkaran Kasih (Love Circles):</strong> Diskusi kelompok merumuskan solusi atas masalah sosial dengan musyawarah santun tanpa memotong pembicaraan orang lain. <code>(PC: Cinta Sesama | PPRA: Syura & Tasamuh)</code></li>
                    <li><strong>Presentasi Damai:</strong> Menyajikan karya dengan bahasa yang santun, kelompok lain menyimak dengan penuh perhatian dan memberikan masukan membangun. <code>(PC: Cinta Bangsa | PPRA: Qudwah)</code></li>
                </ul>
            </div>

            <div style="background: #fafafa; border: 1px solid #ddd; padding: 10px 14px; margin-bottom: 12px;">
                <h4 style="margin: 0 0 6px 0; color: #2B7A78;">C. Tahap Penutup: Refleksi Batin KBC (15 Menit)</h4>
                <ul style="margin: 0; padding-left: 20px;">
                    <li>Siswa menuliskan satu komitmen kebaikan hati yang akan dilakukan hari ini untuk orang tua atau teman. <code>(PC: Cinta Diri & Sesama | PPRA: Qudwah)</code></li>
                    <li>Doa bersama memohon hati yang bersih dan penuh cinta kasih. <code>(PC: Cinta Allah | PPRA: Ta'addub)</code></li>
                </ul>
            </div>
        """.trimIndent()
    }

    private fun generateLkpdKbcHtml(subject: String, grade: String, topic: String): String {
        return """
            <div style="background: #fdfaf3; border: 2px solid #D97706; padding: 12px 18px; border-radius: 6px; margin-bottom: 20px;">
                <h3 style="margin: 0; color: #B45309; font-size: 12pt; text-align: center;">LEMBAR KERJA PESERTA DIDIK (LKPD) BERBASIS CINTA</h3>
                <p style="margin: 4px 0 0 0; text-align: center; font-size: 10pt; font-style: italic;">
                "Belajarlah dengan hati yang gembira, saling membantu, dan senantiasa menghargai perbedaan pendapat sahabatmu."
                </p>
            </div>

            <table style="width: 100%; border-collapse: collapse; margin-bottom: 15px;" border="1" cellpadding="5">
                <tr><td style="width: 25%; font-weight: bold;">Kelompok / Kelas</td><td>........................................................... / Kelas $grade</td></tr>
                <tr><td style="font-weight: bold;">Anggota Kelompok</td><td>1. ..................................................... 2. .....................................................<br>3. ..................................................... 4. .....................................................</td></tr>
                <tr><td style="font-weight: bold;">Materi / Topik</td><td>$topic</td></tr>
            </table>

            <h4 style="color: #2B7A78; margin-top: 15px;">🌟 TAHAP 1: TADABBUR MASALAH (EMPATI)</h4>
            <p>Bacalah studi kasus berikut dengan seksama bersama rekan sekelompokmu:</p>
            <div style="background: #f4f4f4; padding: 10px; border-left: 4px solid #B45309; font-style: italic; margin-bottom: 10px;">
                "Seorang sahabatmu tidak membawa bekal makanan dan merasa malu untuk berkumpul saat jam istirahat. Di sisi lain, ia kesulitan memahami materi $topic yang diajarkan hari ini."
            </div>

            <h4 style="color: #2B7A78; margin-top: 15px;">✍️ RUANG JAWABAN & SOLUSI KASIH SAYANG:</h4>
            <p>1. Apa yang dirasakan oleh sahabatmu tersebut? Tuliskan analisis empatimu:<br>
            ____________________________________________________________________________________________________<br>
            ____________________________________________________________________________________________________</p>

            <p>2. Berdasarkan nilai $topic dan prinsip Panca Cinta, langkah nyata apa yang kelompokmu akan lakukan?<br>
            ____________________________________________________________________________________________________<br>
            ____________________________________________________________________________________________________</p>

            <div style="background: #eef9f6; border: 1px solid #2B7A78; padding: 12px; margin-top: 20px; border-radius: 4px;">
                <h4 style="margin: 0 0 6px 0; color: #2B7A78;">❤️ REFLEKSI PERSONAL KBC (DIISI MANDIRI OLEH SISWA)</h4>
                <p style="margin: 0; font-size: 10pt;"><em>"Hal paling berharga yang saya pelajari hari ini tentang arti kasih sayang adalah:"</em><br>
                ........................................................................................................................................................................................<br>
                ........................................................................................................................................................................................</p>
            </div>
        """.trimIndent()
    }

    private fun generateRubrikFormatifKbcHtml(subject: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold;">RUBRIK PENILAIAN FORMATIF PROSES KBC (4 LEVEL)</h3>
            <table style="width: 100%; border-collapse: collapse; font-size: 9.5pt; margin-top: 10px;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th style="width: 25%;">Dimensi & Indikator KBC</th>
                        <th style="width: 18%;">1: Perlu Bimbingan</th>
                        <th style="width: 18%;">2: Cukup / Layak</th>
                        <th style="width: 19%;">3: Baik / Cakap</th>
                        <th style="width: 20%;">4: Sangat Baik (Qudwah)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Keterlibatan Emosional & Adab (Ta'addub)</strong></td>
                        <td>Tampak enggan atau pasif dalam proses belajar.</td>
                        <td>Mengikuti pembelajaran dengan tertib namun pasif.</td>
                        <td>Aktif dan menunjukkan minat belajar yang tulus.</td>
                        <td>Sangat bersemangat, antusias, dan menyemangati teman lain.</td>
                    </tr>
                    <tr>
                        <td><strong>Kolaborasi Penuh Kasih (Musawah & Syura)</strong></td>
                        <td>Mendominasi atau menolak bekerja sama.</td>
                        <td>Bekerja sama hanya jika diminta ketua kelompok.</td>
                        <td>Bekerja sama dengan baik dan menghargai pendapat rekan.</td>
                        <td>Menjadi perekat kelompok, mengapresiasi dan merangkul semua teman.</td>
                    </tr>
                    <tr>
                        <td><strong>Kedalaman Analisis $topic</strong></td>
                        <td>Belum mampu menangkap esensi materi.</td>
                        <td>Memahami fakta dasar materi $topic.</td>
                        <td>Menganalisis hubungan materi dengan nilai cinta.</td>
                        <td>Mampu mensintesis dan mengajarkan kembali dengan cara santun.</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateRubrikSumatifKbcHtml(subject: String, topic: String): String {
        return """
            <h3 style="font-size: 11.5pt; font-weight: bold;">RUBRIK PENILAIAN SUMATIF KOMPREHENSIF KBC (BOBOT 100%)</h3>
            <table style="width: 100%; border-collapse: collapse; font-size: 9.5pt; margin-top: 10px;" border="1" cellpadding="5">
                <thead>
                    <tr style="background-color: #2B7A78; color: white; text-align: center;">
                        <th style="width: 25%;">Komponen Penilaian</th>
                        <th style="width: 10%;">Bobot</th>
                        <th style="width: 25%;">Kriteria Skor Rendah (0-69)</th>
                        <th style="width: 20%;">Kriteria Skor Sedang (70-84)</th>
                        <th style="width: 20%;">Kriteria Skor Maksimal (85-100)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>1. Penguasaan Konseptual $topic</strong></td>
                        <td style="text-align: center; font-weight: bold;">30%</td>
                        <td>Hanya menguasai sebagian kecil konsep dasar.</td>
                        <td>Menguasai konsep dasar dan dalil pokok secara tepat.</td>
                        <td>Menguasai konsep secara komprehensif dan mampu menganalisis secara kritis.</td>
                    </tr>
                    <tr>
                        <td><strong>2. Penghayatan Panca Cinta & Nilai PPRA</strong></td>
                        <td style="text-align: center; font-weight: bold;">35%</td>
                        <td>Belum mampu menghubungkan materi dengan nilai moral kasih sayang.</td>
                        <td>Mampu menjelaskan hubungan nilai kasih sayang dengan kehidupan.</td>
                        <td>Menunjukkan internalisasi nilai cinta yang mendalam dan berkarakter mulia.</td>
                    </tr>
                    <tr>
                        <td><strong>3. Portofolio Proyek Aksi Kasih / Qudwah</strong></td>
                        <td style="text-align: center; font-weight: bold;">35%</td>
                        <td>Laporan proyek tidak lengkap atau tidak dilaksanakan.</td>
                        <td>Proyek terlaksana sesuai petunjuk teknis minimum.</td>
                        <td>Proyek berdampak positif nyata bagi lingkungan dan menjadi inspirasi keteladanan.</td>
                    </tr>
                    <tr style="font-weight: bold; background-color: #e6f7f4; text-align: center;">
                        <td style="text-align: right; padding-right: 15px;">TOTAL BOBOT NILAI SUMATIF</td>
                        <td>100%</td>
                        <td colspan="3" style="text-align: left; padding-left: 15px;">Kategori: 4 = Sangat Berkembang & Qudwah Hasanah (≥ 90)</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()
    }

    private fun generateGenericDocHtml(docType: String, subject: String, topic: String): String {
        return """
            <h3 style="font-size: 12pt; border-bottom: 2px solid #000; padding-bottom: 4px;">DOKUMEN ADMINISTRASI PEMBELAJARAN</h3>
            <p><strong>Mata Pelajaran:</strong> $subject</p>
            <p><strong>Topik / Materi:</strong> $topic</p>
            <p>Dokumen ini disusun sesuai dengan kaidah Kurikulum Merdeka dan Kurikulum Berbasis Cinta (KBC) untuk menjamin terselenggaranya pembelajaran yang bermakna, menyenangkan, dan berakhlak mulia.</p>
        """.trimIndent()
    }
}
