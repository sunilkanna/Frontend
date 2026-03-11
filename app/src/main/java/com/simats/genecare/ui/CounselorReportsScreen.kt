package com.simats.genecare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simats.genecare.ui.theme.GenecareTheme
import com.simats.genecare.data.model.CounselorReportItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselorReportsScreen(
    navController: NavController,
    viewModel: CounselorReportsViewModel = viewModel()
) {
    val reports by viewModel.reports.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Reports", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF673AB7), Color(0xFF512DA8)) // Unified Purple/Deep Purple
                    )
                )
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No reports found", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reports) { report ->
                    CounselorReportCard(report)
                }
            }
        }
    }
}

@Composable
fun CounselorReportCard(report: CounselorReportItem) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3E5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFF9C27B0)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(report.title, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B2A))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Patient: ${report.patientName} • ${report.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            IconButton(onClick = { 
                try {
                    val reportUrl = report.fileUrl
                    if (reportUrl.isNullOrEmpty()) {
                        android.widget.Toast.makeText(context, "No file URL available", android.widget.Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }

                    // Base URL for the API
                    val baseUrl = "http://172.20.10.2/genecare/"
                    
                    var absoluteUrl = when {
                        reportUrl.startsWith("http") -> {
                            reportUrl.replace("localhost", "172.20.10.2")
                                     .replace("127.0.0.1", "172.20.10.2")
                        }
                        reportUrl.contains("view_report.php") -> {
                             baseUrl + reportUrl.substringAfter("genecare/").removePrefix("/")
                        }
                        else -> {
                            // If it's a relative path like "uploads/xyz.pdf" or "xyz.pdf"
                            val fileName = reportUrl.substringAfterLast("/")
                            baseUrl + "view_report.php?file=" + java.net.URLEncoder.encode(fileName, "UTF-8")
                        }
                    }

                    // Append download parameter if not present
                    if (!absoluteUrl.contains("download=1")) {
                        absoluteUrl += if (absoluteUrl.contains("?")) "&download=1" else "?download=1"
                    }

                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(absoluteUrl))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    android.widget.Toast.makeText(context, "Error opening report: ${e.localizedMessage}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(Icons.Default.Download, contentDescription = "Download", tint = Color(0xFF9C27B0))
            }
        }
    }
}

@Preview
@Composable
fun CounselorReportsScreenPreview() {
    GenecareTheme {
        CounselorReportsScreen(rememberNavController())
    }
}
