package com.simats.genecare.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simats.genecare.ui.theme.GenecareTheme


@Composable
fun VideoCallScreen(navController: NavController, appointmentId: Int = 1) { // Default ID for demo if not passed
    val viewModel: VideoCallViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val appointmentDetails by viewModel.appointmentDetails.collectAsState()
    val callStatus by viewModel.callStatus.collectAsState()

    LaunchedEffect(appointmentId) {
        viewModel.fetchAppointmentDetails(appointmentId)
    }

    var isMicOn by remember { mutableStateOf(true) }
    var isCameraOn by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)) // Dark Blue Background
    ) {
        // "You" PIP (Picture in Picture)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 24.dp)
                .size(width = 100.dp, height = 140.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(16.dp))
                .background(Color(0xFF1B2D42)), // Slightly lighter dark blue
            contentAlignment = Alignment.Center
        ) {
             androidx.compose.material3.Text("You", color = Color.White, fontWeight = FontWeight.Medium)
        }

        // Center Content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 60.dp), // Lift up slightly
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00ACC1)), // Cyan/Teal
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = appointmentDetails?.counselorName?.take(2)?.uppercase() ?: "DR",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Name
            androidx.compose.material3.Text(
                text = appointmentDetails?.counselorName ?: "Doctor",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                color = Color(0xFFECEFF1).copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status
            androidx.compose.material3.Text(
                text = callStatus,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = if (callStatus == "Connected") Color(0xFF90A4AE) else Color.Red,
                fontWeight = FontWeight.Normal
            )
        }

        // Bottom Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mic Button
            IconButton(
                onClick = { isMicOn = !isMicOn },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF263238), CircleShape)
            ) {
                Icon(
                    imageVector = if (isMicOn) Icons.Filled.Mic else Icons.Filled.MicOff,
                    contentDescription = "Mic",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // Camera Button
            IconButton(
                onClick = { isCameraOn = !isCameraOn },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF263238), CircleShape)
            ) {
                Icon(
                    imageVector = if (isCameraOn) Icons.Filled.Videocam else Icons.Filled.VideocamOff,
                    contentDescription = "Camera",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // End Call Button
            IconButton(
                onClick = { 
                    viewModel.endCall(appointmentId) {
                        navController.navigate("session_bill/$appointmentId") 
                    }
                }, 
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFD32F2F), CircleShape) // Red
            ) {
                Icon(
                    imageVector = Icons.Filled.CallEnd,
                    contentDescription = "End Call",
                    tint = Color.White
                )
            }
        }
    }
}
