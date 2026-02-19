package com.simats.genecare.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.genecare.ui.theme.GenecareTheme

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiskAssessmentScreen(
    navController: NavController,
    viewModel: RiskAssessmentViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Store answers in a map
    val answers = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is OnboardingState.Success -> {
                Toast.makeText(context, (uiState as OnboardingState.Success).message, Toast.LENGTH_SHORT).show()
                navController.navigate("dashboard") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }
            is OnboardingState.Error -> {
                Toast.makeText(context, (uiState as OnboardingState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    // Data model for questions
    data class QuestionData(
        val text: String,
        val options: List<String>
    )

    // Questions based on user request/screenshots
    val questions = listOf(
        QuestionData(
            text = "Have you or any family member been diagnosed with a genetic condition?",
            options = listOf("Yes", "No", "Not sure")
        ),
        QuestionData(
            text = "Do you have any concerns about inherited health conditions?",
            options = listOf("Yes, very concerned", "Somewhat concerned", "Not concerned")
        ),
        QuestionData(
            text = "What is your primary reason for seeking genetic counseling?",
            options = listOf("Family history", "Personal diagnosis", "Preventive care", "Other")
        )
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    
    val currentQuestion = questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1).toFloat() / questions.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Risk Assessment",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 48.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                            selectedOption = null // Ideally revert to previous selection if stored
                        } else {
                            navController.popBackStack() 
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Progress Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "${((progress) * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF00ACC1),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF00ACC1),
                    trackColor = Color(0xFFE0F7FA),
                    strokeCap = StrokeCap.Round,
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Question
                Text(
                    text = currentQuestion.text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00363A)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Options
                currentQuestion.options.forEach { option ->
                    OutlinedButton(
                        onClick = { selectedOption = option },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            width = if (selectedOption == option) 2.dp else 1.dp,
                            color = if (selectedOption == option) Color(0xFF00ACC1) else Color(0xFFEEEEEE)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedOption == option) Color(0xFFE0F7FA) else Color.White,
                            contentColor = if (selectedOption == option) Color(0xFF006064) else Color.Black
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Fixed Footer
            Surface(
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = {
                            selectedOption?.let { answers[currentQuestion.text] = it }
                            
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                selectedOption = null
                            } else {
                                viewModel.saveAssessment(answers)
                            }
                        },
                        enabled = selectedOption != null && uiState !is OnboardingState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00ACC1),
                            disabledContainerColor = Color(0xFFB0BEC5), // Darker gray for better visibility
                            disabledContentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (uiState is OnboardingState.Loading) "Saving..." 
                                       else if (currentQuestionIndex < questions.size - 1) "Next Question" 
                                       else "Complete Assessment",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (uiState is OnboardingState.Loading) {
                                Spacer(modifier = Modifier.width(8.dp))
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RiskAssessmentScreenPreview() {
    GenecareTheme {
        RiskAssessmentScreen(NavController(androidx.compose.ui.platform.LocalContext.current))
    }
}
