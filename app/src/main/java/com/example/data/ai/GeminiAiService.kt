package com.example.data.ai

import com.example.BuildConfig
import com.example.data.model.SchoolConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateDocument(
        docType: String,
        subject: String,
        gradeLevel: String,
        semester: String,
        topic: String,
        learningPhase: String,
        teacherName: String,
        teacherNip: String,
        config: SchoolConfig
    ): Pair<String, String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val prompt = buildPrompt(
            docType = docType,
            subject = subject,
            gradeLevel = gradeLevel,
            semester = semester,
            topic = topic,
            learningPhase = learningPhase,
            teacherName = teacherName,
            teacherNip = teacherNip,
            config = config
        )

        var aiText: String? = null
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                aiText = callGeminiApi(apiKey, prompt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // If AI call succeeded and returned substantive text, wrap it with standard Kop and Signature table.
        // Otherwise, construct comprehensive document using our high-standard Curriculum Template Engine.
        val htmlDoc = if (!aiText.isNullOrBlank() && aiText.length > 100) {
            wrapAiContentWithDocumentStandard(
                docType = docType,
                subject = subject,
                gradeLevel = gradeLevel,
                semester = semester,
                contentHtml = formatAiOutputToHtml(aiText),
                teacherName = teacherName,
                teacherNip = teacherNip,
                config = config
            )
        } else {
            CurriculumTemplateEngine.generateCompleteDocument(
                docType = docType,
                subject = subject,
                gradeLevel = gradeLevel,
                semester = semester,
                topic = topic,
                learningPhase = learningPhase,
                teacherName = teacherName,
                teacherNip = teacherNip,
                config = config
            )
        }

        val plainText = stripHtml(htmlDoc)
        Pair(htmlDoc, plainText)
    }

    private fun callGeminiApi(apiKey: String, prompt: String): String? {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val jsonBody = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    }
                    put("parts", parts)
                })
            }
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.6)
            })
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody(jsonMediaType))
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            return null
        }

        val responseString = response.body?.string() ?: return null
        val responseJson = JSONObject(responseString)
        val candidates = responseJson.optJSONArray("candidates") ?: return null
        if (candidates.length() == 0) return null

        val firstCand = candidates.getJSONObject(0)
        val content = firstCand.optJSONObject("content") ?: return null
        val parts = content.optJSONArray("parts") ?: return null
        if (parts.length() == 0) return null

        return parts.getJSONObject(0).optString("text")
    }

    private fun buildPrompt(
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
        return """
            Anda adalah Konsultan Ahli Kurikulum Merdeka dan Kurikulum Berbasis Cinta (KBC) dari Kementerian Pendidikan dan Kementerian Agama.
            Buatkan dokumen administrasi pembelajaran lengkap dan terperinci untuk:
            - Jenis Dokumen: $docType
            - Satuan Pendidikan: ${config.schoolName}
            - Mata Pelajaran: $subject
            - Fase/Kelas: Fase $learningPhase / Kelas $gradeLevel
            - Semester/Tahun: $semester / ${config.academicYear}
            - Materi/Topik: $topic
            - Guru Pengampu: $teacherName (NIP: $teacherNip)
            - Kepala Sekolah: ${config.headmasterName} (NIP: ${config.headmasterNip})

            Panduan Khusus per jenis dokumen:
            - Jika ADM-CP / ACP-KBC: Muat Rasional, Tujuan, Karakteristik & Elemen, CP Fase, Penjabaran KKO, Pemetaan 8 Dimensi Profil Lulusan / Panca Cinta.
            - Jika ADM-TP / TP-KBC: Muat Kode TP, Rumusan TP (KKO Bloom C2-C5 + ABCD), Aspek Kompetensi, Alokasi JP, dan Integrasi Cinta/PPRA.
            - Jika ADM-ATP / ATP-KBC: Muat Diagram Alur Visual Kode TP dan Tabel 8 Kolom (Kode, Elemen, TP, Materi, Bloom, Dimensi, JP, Semester).
            - Jika ADM-PROTA / PROTA-KBC: Muat Tabel Perhitungan Minggu Efektif (Kalender - Tidak Efektif) x JP dan Distribusi TP + Jam Cadangan.
            - Jika ADM-PROSEM / PROSEM-KBC: Matriks Distribusi JP per Minggu dengan keterangan model pembelajaran.
            - Jika ADM-KKTP / KKTP-KBC: Dasar Hukum Permendikbudristek No. 21/2022, 4 Level Capaian (Mulai Berkembang, Layak, Cakap, Mahir/Qudwah), Rubrik 9 Kolom.
            - Jika Modul Ajar: Pembelajaran Berbasis Deep Learning (Mindful, Meaningful, Joyful Learning) / Tag KBC (PC: ... | PPRA: ...), Pertemuan 1-3 (Awal, Inti, Penutup), Asesmen, LKPD, Refleksi.
            - Jika LKPD KBC: Petunjuk santun, sintak model pembelajaran dengan emoji visual, tempat jawaban, dan Refleksi Personal KBC.
            - Jika Asesmen Sumatif: Kisi-kisi, 10 Soal Pilihan Ganda HOTS + Kunci, 5 Soal Uraian + Rubrik Penskoran.

            Format output: Tuliskan dalam format HTML terstruktur bersih (gunakan <h3>, <h4>, <p>, <ul>, <ol>, <li>, <table>, <tr>, <th>, <td>, <strong>). Jangan sertakan tag <html>, <head>, atau <body>, cukup isi kontennya saja.
        """.trimIndent()
    }

    private fun formatAiOutputToHtml(raw: String): String {
        var text = raw.trim()
        if (text.startsWith("```html")) {
            text = text.removePrefix("```html").removeSuffix("```").trim()
        } else if (text.startsWith("```")) {
            text = text.removePrefix("```").removeSuffix("```").trim()
        }
        return text
    }

    private fun wrapAiContentWithDocumentStandard(
        docType: String,
        subject: String,
        gradeLevel: String,
        semester: String,
        contentHtml: String,
        teacherName: String,
        teacherNip: String,
        config: SchoolConfig
    ): String {
        val kop = CurriculumTemplateEngine.generateKopHtml(config, docType, subject, gradeLevel, semester)
        val signature = CurriculumTemplateEngine.generateSignatureHtml(config, teacherName, teacherNip)
        return """
            $kop
            <div class="document-body">
                $contentHtml
            </div>
            $signature
        """.trimIndent()
    }

    private fun stripHtml(html: String): String {
        return html.replace(Regex("<[^>]*>"), " ")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
