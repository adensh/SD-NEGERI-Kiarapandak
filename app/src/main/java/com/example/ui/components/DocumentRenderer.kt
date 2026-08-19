package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

@Composable
fun DocumentViewerModal(
    title: String,
    htmlContent: String,
    onSaveToDb: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Wrap with complete standard CSS for print & word rendering
    val fullHtml = remember(htmlContent) {
        wrapWithFullHtmlTemplate(title, htmlContent)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .testTag("document_viewer_modal"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF0F172A),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "Format Siap Cetak A4 / Export Word (.doc)",
                            fontSize = 11.sp,
                            color = Color(0xFF38BDF8)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_doc_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Bar (Cetak / PDF, Export Word, Salin HTML, Simpan DB)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cetak / PDF Button
                    Button(
                        onClick = { printHtmlDocument(context, title, fullHtml) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("print_doc_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cetak / PDF", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Download Word (.doc) Button
                    Button(
                        onClick = { exportToWordDoc(context, title, fullHtml) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("export_word_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Word (.doc)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Salin HTML Button
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Dokumen HTML", fullHtml)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Kode HTML disalin ke Clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("copy_html_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFCBD5E1))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Salin HTML", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                    }

                    if (onSaveToDb != null) {
                        Button(
                            onClick = onSaveToDb,
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("save_to_db_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Document Paper Container (White Sheet inside Dark Shell)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = false
                                settings.useWideViewPort = true
                                settings.loadWithOverviewMode = true
                                settings.textZoom = 100
                                loadDataWithBaseURL(null, fullHtml, "text/html", "UTF-8", null)
                            }
                        },
                        update = { webView ->
                            webView.loadDataWithBaseURL(null, fullHtml, "text/html", "UTF-8", null)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

fun wrapWithFullHtmlTemplate(title: String, bodyHtml: String): String {
    return """
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>$title</title>
            <style>
                @page {
                    size: A4 portrait;
                    margin: 20mm 15mm 20mm 15mm;
                }
                body {
                    font-family: 'Times New Roman', Times, serif;
                    font-size: 11pt;
                    line-height: 1.5;
                    color: #000000;
                    background-color: #ffffff;
                    margin: 0;
                    padding: 15px;
                }
                h1, h2, h3, h4 {
                    color: #000;
                    margin-top: 14px;
                    margin-bottom: 6px;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 10px;
                    margin-bottom: 15px;
                    font-size: 10pt;
                }
                th, td {
                    border: 1px solid #333333;
                    padding: 6px 8px;
                    vertical-align: top;
                }
                th {
                    background-color: #f2f2f2;
                    font-weight: bold;
                    text-align: center;
                }
                table.signature-table {
                    border: none !important;
                    margin-top: 35px;
                    page-break-inside: avoid;
                }
                table.signature-table td {
                    border: none !important;
                    padding: 0 10px;
                }
                .text-center { text-align: center; }
                .text-right { text-align: right; }
                .font-bold { font-weight: bold; }
                .italic { font-style: italic; }
            </style>
        </head>
        <body>
            $bodyHtml
        </body>
        </html>
    """.trimIndent()
}

fun printHtmlDocument(context: Context, jobName: String, htmlContent: String) {
    try {
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                val printAttributes = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(PrintAttributes.Resolution("id1", "print", 300, 300))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build()
                printManager?.print(jobName, printAdapter, printAttributes)
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal membuka layanan Cetak: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

fun exportToWordDoc(context: Context, title: String, htmlContent: String) {
    try {
        val sanitizedTitle = title.replace(Regex("[^a-zA-Z0-9.-]"), "_")
        val fileName = "$sanitizedTitle.doc"
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)

        // Microsoft Word can natively open HTML content saved with .doc extension with appropriate MIME header
        val wordHeader = """
            <html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:w='urn:schemas-microsoft-com:office:word' xmlns='http://www.w3.org/TR/REC-html40'>
            <head><meta charset='utf-8'><title>$title</title></head>
            <body>
        """.trimIndent()
        val wordFooter = "</body></html>"
        val fullWordContent = "$wordHeader\n$htmlContent\n$wordFooter"

        FileOutputStream(file).use { out ->
            out.write(fullWordContent.toByteArray(Charsets.UTF_8))
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/msword"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Simpan atau Buka File Word ($fileName)"))
    } catch (e: Exception) {
        // Fallback: copy formatted content or show toast
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Dokumen Word", htmlContent)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Teks dokumen disalin (Bisa di-paste ke Microsoft Word/Docs): ${e.message}", Toast.LENGTH_LONG).show()
    }
}
