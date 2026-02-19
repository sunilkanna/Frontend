package com.simats.genecare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simats.genecare.ui.navgraph.NavGraph
import com.simats.genecare.ui.theme.GenecareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.simats.genecare.data.UserSession.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            val startDestination = if (com.simats.genecare.data.UserSession.isLoggedIn()) {
                when (com.simats.genecare.data.UserSession.getUserType()) {
                    "Patient" -> "dashboard"
                    "Doctor/Counselor" -> "counselor_pending_dashboard"
                    "Admin" -> "admin_dashboard"
                    else -> "dashboard" // Default fallback
                }
            } else {
                "splash"
            }
            
            GenecareTheme {
                NavGraph(startDestination = startDestination)
            }
        }
    }
}
