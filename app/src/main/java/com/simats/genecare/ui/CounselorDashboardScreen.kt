package com.simats.genecare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simats.genecare.ui.theme.GenecareTheme
import com.simats.genecare.data.UserSession

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounselorDashboardScreen(
    navController: NavController, 
    onSignOut: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = UserSession.getUser()

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        when (val state = uiState) {
            is DashboardState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF009688))
                }
            }
            is DashboardState.Success -> {
                val stats = state.stats.counselorStats
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Header and Stats Section
                    item {
                        TopSection(onSignOut, navController, user?.fullName ?: "Counselor", stats)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Today's Schedule
                    item {
                        TodaysSchedule(
                            appointments = stats?.todayAppointments ?: emptyList(),
                            onViewAllClick = { /* TODO */ },
                            onStartClick = { navController.navigate("video_call") }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Quick Actions
                    item {
                        QuickActionsSection(navController)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Weekly Summary
                    item {
                        WeeklySummaryCard()
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    
                    // Recent Reviews
                    item {
                        RecentReviewsSection(stats?.recentReviews ?: emptyList())
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
            is DashboardState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${state.message}", color = Color.Red, modifier = Modifier.clickable { viewModel.fetchStats() })
                }
            }
        }
    }
}

@Composable
fun TopSection(onSignOut: () -> Unit, navController: NavController, name: String, stats: com.simats.genecare.data.model.CounselorDashboardStats?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // Adjusted height for 2x2 grid
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF009688), // Teal
                        Color(0xFF00796B)  // Darker Teal
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                 // Back/SignOut Button
                 IconButton(
                    onClick = onSignOut,
                     modifier = Modifier
                         .background(Color.White.copy(alpha = 0.2f), CircleShape)
                         .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Notification Button
                IconButton(
                    onClick = { navController.navigate("notifications") },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .size(40.dp)
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                                .background(Color(0xFFEF5350), CircleShape) // Red dot
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile info
            Text(
                text = "Dr. $name",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 36.sp
                )
            )
            Text(
                text = "Genetic Counselor",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB2DFDB),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Grid (2x2)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Row 1
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(
                        icon = Icons.Outlined.Videocam,
                        label = "Today's Sessions",
                        value = "${stats?.todaysSessions ?: 0}",
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        valueColor = Color(0xFFEC407A),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Outlined.Groups,
                        label = "Total Patients",
                        value = "${stats?.totalPatients ?: 0}",
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        valueColor = Color(0xFFEC407A),
                        modifier = Modifier.weight(1f)
                    )
                }
                // Row 2
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(
                        icon = Icons.Filled.Star,
                        label = "Avg. Rating",
                        value = "${stats?.avgRating ?: 0.0}",
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        valueColor = Color(0xFFEC407A),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Outlined.AttachMoney,
                        label = "This Month",
                        value = stats?.revenueThisMonth ?: "₹0",
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        valueColor = Color(0xFFEC407A),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    containerColor: Color,
    contentColor: Color, 
    valueColor: Color, // New parameter for value text color
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp), // Slightly more rounded
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = contentColor.copy(alpha = 0.9f), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label, 
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp), 
                    color = contentColor.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium, 
                fontWeight = FontWeight.Bold,
                color = valueColor // Use the specific pink/accent color
            )
        }
    }
}


@Composable
fun TodaysSchedule(
    appointments: List<com.simats.genecare.data.model.AppointmentData>,
    onViewAllClick: () -> Unit, 
    onStartClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Schedule",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onViewAllClick) {
                Text("View All", color = Color(0xFF00ACC1), fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (appointments.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Text(text = "No appointments today", color = Color.Gray)
            }
        } else {
            appointments.forEach { appointment ->
                ScheduleItem(
                    name = appointment.patientName ?: "Patient",
                    detail = "Video Consultation",
                    time = appointment.timeSlot,
                    imageInitial = (appointment.patientName ?: "P").take(1),
                    imageColor = Color(0xFFFFCC80),
                    onStartClick = onStartClick
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ScheduleItem(name: String, detail: String, time: String, imageInitial: String, imageColor: Color, onStartClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
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
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(imageColor),
                contentAlignment = Alignment.Center
            ) {
                 // Emoji fallback or Initial
                 Text("👱‍♀️", fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(detail, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Color(0xFF00ACC1), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(time, color = Color(0xFF00ACC1), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00ACC1)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Start", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun QuickActionsSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(
                icon = Icons.Outlined.DateRange, 
                label = "Session\nRequests", 
                bgColor = Color(0xFFE3F2FD), 
                iconColor = Color(0xFF2196F3), 
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("session_requests") }
            )
            QuickActionCard(
                icon = Icons.Outlined.Groups, 
                label = "Patient\nList", 
                bgColor = Color(0xFFE0F2F1), 
                iconColor = Color(0xFF009688), 
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("patient_list") }
            )
            QuickActionCard(Icons.Outlined.ChatBubbleOutline, "Messages", Color(0xFFF3E5F5), Color(0xFF9C27B0), Modifier.weight(1f), onClick = { navController.navigate("counselor_messages") })
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(Icons.Outlined.PieChart, "Reports", Color(0xFFE8EAF6), Color(0xFF3F51B5), Modifier.weight(1f), onClick = { navController.navigate("counselor_reports") })
            @Suppress("DEPRECATION")
            QuickActionCard(Icons.Outlined.TrendingUp, "Performance", Color(0xFFF1F8E9), Color(0xFF4CAF50), Modifier.weight(1f), onClick = { navController.navigate("counselor_performance") })
            QuickActionCard(Icons.Outlined.Settings, "Settings", Color(0xFFFAFAFA), Color(0xFF757575), Modifier.weight(1f), onClick = { navController.navigate("counselor_settings") })
        }
    }
}

@Composable
fun QuickActionCard(
    icon: ImageVector, 
    label: String, 
    bgColor: Color, 
    iconColor: Color, 
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun WeeklySummaryCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // Light Blue background
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                @Suppress("DEPRECATION")
                Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = Color(0xFF1976D2))
                Spacer(modifier = Modifier.width(8.dp))
                Text("This Week Summary", fontWeight = FontWeight.Bold, color = Color(0xFF0D1B2A))
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            SummaryRow("Sessions Completed", "24")
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow("New Patients", "8")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Revenue", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                Text("₹18,400", fontWeight = FontWeight.Bold, color = Color(0xFF00C853)) // Green for money
            }
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow("Avg. Session Time", "52 min")
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B2A))
    }
}

@Composable
fun RecentReviewsSection(reviews: List<com.simats.genecare.data.model.ReviewData>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB300))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recent Reviews", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (reviews.isEmpty()) {
            Text("No reviews yet.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        } else {
            reviews.forEach { review ->
                ReviewCard(
                    rating = review.rating,
                    daysAgo = review.daysAgo,
                    review = "\"${review.review}\"",
                    author = "- ${review.author}"
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ReviewCard(rating: Int, daysAgo: String, review: String, author: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    repeat(rating) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    }
                }
                Text(daysAgo, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(author, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounselorDashboardScreenPreview() {
    GenecareTheme {
        CounselorDashboardScreen(rememberNavController())
    }
}
