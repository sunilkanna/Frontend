package com.simats.genecare.ui.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simats.genecare.ui.AdminDashboardScreen
import com.simats.genecare.ui.BookingViewModel
import com.simats.genecare.ui.NotificationViewModel
import com.simats.genecare.ui.ConsentAndPrivacyScreen
import com.simats.genecare.ui.CounselorDashboardScreen
import com.simats.genecare.ui.CounselorVerificationScreen
import com.simats.genecare.ui.CreateAccountScreen
import com.simats.genecare.ui.CreateNewPasswordScreen
import com.simats.genecare.ui.EnterVerificationCodeScreen
import com.simats.genecare.ui.ForgotPasswordScreen
import com.simats.genecare.ui.PasswordResetSuccessfulScreen
import com.simats.genecare.ui.SplashScreen
import com.simats.genecare.ui.WelcomeScreen
import com.simats.genecare.ui.ProfileSetupScreen
import com.simats.genecare.ui.MedicalHistoryScreen
import com.simats.genecare.ui.FamilyHistoryScreen
import com.simats.genecare.ui.RiskAssessmentScreen
import com.simats.genecare.ui.DashboardScreen
import com.simats.genecare.ui.BookSessionScreen
import com.simats.genecare.ui.AppointmentDetailsScreen
import com.simats.genecare.ui.CounselorQualificationScreen
import com.simats.genecare.ui.QualificationDetailsScreen
import com.simats.genecare.ui.PreviewConfirmationScreen
import com.simats.genecare.ui.CounselorViewModel
import com.simats.genecare.ui.VerificationStatusScreen
import com.simats.genecare.ui.CounselorPendingDashboardScreen
import com.simats.genecare.ui.SignInScreen
import com.simats.genecare.ui.CounselorVerificationDetailScreen
import com.simats.genecare.ui.NotificationScreen
import com.simats.genecare.ui.SessionRequestsScreen
import com.simats.genecare.ui.VideoCallScreen
import com.simats.genecare.ui.SessionBillScreen
import com.simats.genecare.ui.PaymentSuccessScreen
import com.simats.genecare.ui.SessionFeedbackScreen


@Composable
fun NavGraph(startDestination: String = "splash") {
    val navController = rememberNavController()
    val bookingViewModel: BookingViewModel = viewModel()
    val counselorViewModel: CounselorViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        composable("create_account") {
            CreateAccountScreen(navController = navController)
        }
        composable("sign_in") {
            SignInScreen(navController = navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController = navController)
        }
        composable("enter_verification_code/{email}") { backStackEntry ->
            EnterVerificationCodeScreen(navController = navController, email = backStackEntry.arguments?.getString("email") ?: "")
        }
        composable("create_new_password/{email}") { backStackEntry ->
            CreateNewPasswordScreen(navController = navController, email = backStackEntry.arguments?.getString("email") ?: "")
        }
        composable("password_reset_successful") {
            PasswordResetSuccessfulScreen(navController = navController)
        }
        composable("consent_and_privacy") {
            ConsentAndPrivacyScreen(navController = navController)
        }
        composable("counselor_qualification") {
            CounselorQualificationScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("qualification_details") {
            QualificationDetailsScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("preview_confirmation") {
            PreviewConfirmationScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("verification_status") {
            VerificationStatusScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("counselor_pending_dashboard") {
            CounselorPendingDashboardScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("profile_setup") {
            ProfileSetupScreen(navController = navController)
        }
        composable("medical_history") {
            MedicalHistoryScreen(navController = navController)
        }
        composable("family_history") {
            FamilyHistoryScreen(navController = navController)
        }
        composable("risk_assessment") {
            RiskAssessmentScreen(navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
        composable("book_session") {
            BookSessionScreen(navController = navController, viewModel = bookingViewModel)
        }
        composable("appointment_details") {
            AppointmentDetailsScreen(navController = navController, viewModel = bookingViewModel)
        }
        composable("admin_dashboard") {
            AdminDashboardScreen(
                navController = navController,
                onLogout = {
                    navController.navigate("sign_in") {
                        popUpTo("admin_dashboard") { inclusive = true }
                    }
                }
            )
        }
        composable("counselor_verification_detail") {
            CounselorVerificationDetailScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("counselor_verification") {
            CounselorVerificationScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("notifications") {
            NotificationScreen(navController = navController, viewModel = notificationViewModel)
        }
        composable("counselor_dashboard") {
            com.simats.genecare.ui.CounselorDashboardScreen(
                navController = navController,
                onSignOut = {
                    navController.navigate("sign_in") {
                        popUpTo("counselor_dashboard") { inclusive = true }
                    }
                }
            )
        }
        composable("session_requests") {
            SessionRequestsScreen(navController = navController, viewModel = counselorViewModel)
        }
        composable("video_call") {
            VideoCallScreen(navController = navController)
        }
        composable("session_bill") {
            SessionBillScreen(navController = navController)
        }
        composable("payment_success") {
            PaymentSuccessScreen(navController = navController)
        }
        composable("session_feedback") {
            SessionFeedbackScreen(navController = navController)
        }
        composable("user_management") {
            com.simats.genecare.ui.UserManagementScreen(navController = navController)
        }
        composable("system_settings") {
            com.simats.genecare.ui.SystemSettingsScreen(navController = navController)
        }
        composable("chat/{otherUserId}/{otherUserName}") { backStackEntry ->
            val otherUserId = backStackEntry.arguments?.getString("otherUserId")?.toIntOrNull() ?: 0
            val otherUserName = backStackEntry.arguments?.getString("otherUserName") ?: "User"
            com.simats.genecare.ui.ChatScreen(
                navController = navController,
                otherUserId = otherUserId,
                otherUserName = otherUserName
            )
        }
        composable("my_results") {
            com.simats.genecare.ui.MyResultsScreen(navController = navController)
        }
        composable("analytics") {
            com.simats.genecare.ui.AnalyticsScreen(navController = navController)
        }
        composable("reports_and_logs") {
            com.simats.genecare.ui.ReportsAndLogsScreen(navController = navController)
        }
        composable("patient_list") {
            com.simats.genecare.ui.PatientListScreen(navController = navController)
        }
        composable("patient_details/{patientId}") { backStackEntry ->
            com.simats.genecare.ui.PatientDetailsScreen(navController = navController, patientId = backStackEntry.arguments?.getString("patientId") ?: "")
        }
        composable("counselor_messages") {
            com.simats.genecare.ui.CounselorMessagesScreen(navController = navController)
        }
        composable("counselor_reports") {
            com.simats.genecare.ui.CounselorReportsScreen(navController = navController)
        }
        composable("counselor_performance") {
            com.simats.genecare.ui.CounselorPerformanceScreen(navController = navController)
        }
        composable("counselor_settings") {
            com.simats.genecare.ui.CounselorSettingsScreen(navController = navController)
        }
    }
}
