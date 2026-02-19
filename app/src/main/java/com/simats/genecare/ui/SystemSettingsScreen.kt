package com.simats.genecare.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simats.genecare.ui.theme.GenecareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemSettingsScreen(navController: NavController) {
    var maintenanceMode by remember { mutableStateOf(false) }
    var emailNotifications by remember { mutableStateOf(true) }
    var pushNotifications by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }
    var twoFactorAuth by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // General Settings Section
            SettingsSectionHeader("General")
            SettingSwitchItem(
                title = "Maintenance Mode",
                subtitle = "Only admins can access the platform",
                icon = Icons.Default.PowerSettingsNew,
                checked = maintenanceMode,
                onCheckedChange = { maintenanceMode = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Notifications Section
            SettingsSectionHeader("Notifications")
            SettingSwitchItem(
                title = "Email Alerts",
                subtitle = "Receive system alerts via email",
                icon = Icons.Default.Notifications,
                checked = emailNotifications,
                onCheckedChange = { emailNotifications = it }
            )
            SettingSwitchItem(
                title = "Push Notifications",
                subtitle = "Receive updates on mobile devices",
                icon = Icons.Default.Notifications,
                checked = pushNotifications,
                onCheckedChange = { pushNotifications = it }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Appearance Section
            SettingsSectionHeader("Appearance")
            SettingSwitchItem(
                title = "Dark Mode",
                subtitle = "Enable dark theme for admin panel",
                icon = Icons.Default.Palette,
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Security Section
            SettingsSectionHeader("Security")
            SettingSwitchItem(
                title = "Two-Factor Authentication",
                subtitle = "Require 2FA for all admin logins",
                icon = Icons.Default.Security,
                checked = twoFactorAuth,
                onCheckedChange = { twoFactorAuth = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Version Info
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF1976D2))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("GeneCare Platform", fontWeight = FontWeight.Bold)
                        Text("Version 1.0.0 (Build 20260209)", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingSwitchItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SystemSettingsScreenPreview() {
    GenecareTheme {
        SystemSettingsScreen(rememberNavController())
    }
}
