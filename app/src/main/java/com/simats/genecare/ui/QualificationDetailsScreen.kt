package com.simats.genecare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.genecare.ui.theme.GenecareTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QualificationDetailsScreen(
    navController: NavController,
    viewModel: CounselorViewModel
) {
    val qualificationData by viewModel.qualificationData.collectAsState()
    
    var doctorName by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var yearOfRegistration by remember { mutableStateOf("") }
    var stateMedicalCouncil by remember { mutableStateOf("") }
    
    var yearDropdownExpanded by remember { mutableStateOf(false) }
    var councilDropdownExpanded by remember { mutableStateOf(false) }

    // Generate years from current year back to 1970
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear downTo 1970).map { it.toString() }

    // List of State Medical Councils in India
    val stateMedicalCouncils = listOf(
        "Andhra Pradesh Medical Council",
        "Arunachal Pradesh Medical Council",
        "Assam Medical Council",
        "Bihar Medical Council",
        "Chhattisgarh Medical Council",
        "Delhi Medical Council",
        "Goa Medical Council",
        "Gujarat Medical Council",
        "Haryana Medical Council",
        "Himachal Pradesh Medical Council",
        "Jharkhand Medical Council",
        "Karnataka Medical Council",
        "Kerala Medical Council",
        "Madhya Pradesh Medical Council",
        "Maharashtra Medical Council",
        "Manipur Medical Council",
        "Meghalaya Medical Council",
        "Mizoram Medical Council",
        "Nagaland Medical Council",
        "Odisha Medical Council",
        "Punjab Medical Council",
        "Rajasthan Medical Council",
        "Sikkim Medical Council",
        "Tamil Nadu Medical Council",
        "Telangana Medical Council",
        "Tripura Medical Council",
        "Uttar Pradesh Medical Council",
        "Uttarakhand Medical Council",
        "West Bengal Medical Council"
    )

    val isFormValid = doctorName.isNotBlank() && 
                      registrationNumber.isNotBlank() && 
                      yearOfRegistration.isNotBlank() && 
                      stateMedicalCouncil.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text(
                text = "Qualification Details",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Please provide your medical registration information",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Encryption Info Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFB2EBF2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF00ACC1),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Your documents are encrypted and securely stored",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF00796B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Certificate Uploaded Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Certificate Uploaded",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = qualificationData.certificateFileName.ifEmpty { "medical_registration_cert.pdf" },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit certificate",
                            tint = Color(0xFF26A69A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Doctor Name Field
            LabeledTextField(
                label = "Doctor Name",
                value = doctorName,
                onValueChange = { doctorName = it },
                placeholder = "e.g., Dr. Sarah Johnson",
                isRequired = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Registration Number Field
            LabeledTextField(
                label = "Registration Number",
                value = registrationNumber,
                onValueChange = { registrationNumber = it },
                placeholder = "e.g., MCI-123456",
                isRequired = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Year of Registration Dropdown
            LabeledDropdownField(
                label = "Year of Registration",
                value = yearOfRegistration,
                placeholder = "Select year",
                isRequired = true,
                expanded = yearDropdownExpanded,
                onExpandedChange = { yearDropdownExpanded = it },
                options = years,
                onOptionSelected = { 
                    yearOfRegistration = it
                    yearDropdownExpanded = false
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // State Medical Council Dropdown
            LabeledDropdownField(
                label = "State Medical Council",
                value = stateMedicalCouncil,
                placeholder = "Select council",
                isRequired = true,
                expanded = councilDropdownExpanded,
                onExpandedChange = { councilDropdownExpanded = it },
                options = stateMedicalCouncils,
                onOptionSelected = { 
                    stateMedicalCouncil = it
                    councilDropdownExpanded = false
                }
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Save & Continue Button
            Button(
                onClick = { 
                    // Update ViewModel with qualification details
                    viewModel.updateQualificationDetails(
                        doctorName = doctorName,
                        registrationNumber = registrationNumber,
                        yearOfRegistration = yearOfRegistration,
                        stateMedicalCouncil = stateMedicalCouncil
                    )
                    navController.navigate("preview_confirmation")
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF008080),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Save & Continue",
                    fontSize = 16.sp,
                    color = if (isFormValid) Color.White else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isRequired: Boolean = false
) {
    Column {
        Text(
            text = buildAnnotatedString {
                append(label)
                if (isRequired) {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(" *")
                    }
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.LightGray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF008080),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            ),
            singleLine = true
        )
    }
}

@Composable
fun LabeledDropdownField(
    label: String,
    value: String,
    placeholder: String,
    isRequired: Boolean = false,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(
            text = buildAnnotatedString {
                append(label)
                if (isRequired) {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(" *")
                    }
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Box {
            OutlinedTextField(
                value = value,
                onValueChange = { },
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color.LightGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select",
                        tint = Color.Gray,
                        modifier = Modifier.clickable { onExpandedChange(!expanded) }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(!expanded) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF008080),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                ),
                singleLine = true
            )
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { onOptionSelected(option) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QualificationDetailsScreenPreview() {
    GenecareTheme {
        QualificationDetailsScreen(
            NavController(LocalContext.current),
            CounselorViewModel()
        )
    }
}
