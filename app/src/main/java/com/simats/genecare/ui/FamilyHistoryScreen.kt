package com.simats.genecare.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.simats.genecare.ui.theme.GenecareTheme
import java.util.UUID
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

data class FamilyMember(
    val id: String = UUID.randomUUID().toString(),
    val relation: String,
    val role: String,
    val conditions: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyHistoryScreen(
    navController: NavController,
    viewModel: FamilyHistoryViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is OnboardingState.Success -> {
                Toast.makeText(context, (uiState as OnboardingState.Success).message, Toast.LENGTH_SHORT).show()
                navController.navigate("risk_assessment")
            }
            is OnboardingState.Error -> {
                Toast.makeText(context, (uiState as OnboardingState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    // State for the list of family members
    val familyMembers = remember {
        mutableStateListOf(
            FamilyMember(relation = "Father", role = "Parent", conditions = "Heart Disease, Diabetes"),
            FamilyMember(relation = "Mother", role = "Parent", conditions = "Breast Cancer"),
            FamilyMember(relation = "Sister", role = "Sibling", conditions = "None reported")
        )
    }

    // State for the dialog
    var showDialog by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<FamilyMember?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Family History",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 48.dp) // Balance the back button
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Info Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE0F7FA)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = Color(0xFF00ACC1),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Family health history helps identify genetic risks and patterns that may affect your health.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF006064)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Family Members List
            familyMembers.forEach { member ->
                FamilyMemberItem(
                    member = member,
                    onEdit = {
                        selectedMember = member
                        showDialog = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Add Family Member Button
            OutlinedButton(
                onClick = {
                    selectedMember = null // Reset for new member
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = BorderStroke(1.dp, Color(0xFF00ACC1)),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF00ACC1)
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Family Member", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveFamilyHistory(familyMembers) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState !is OnboardingState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00838F)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                if (uiState is OnboardingState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Save & Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showDialog) {
            AddEditFamilyMemberDialog(
                member = selectedMember,
                onDismiss = { showDialog = false },
                onSave = { newMember ->
                    if (selectedMember != null) {
                        // Edit existing
                        val index = familyMembers.indexOfFirst { it.id == selectedMember!!.id }
                        if (index != -1) {
                            familyMembers[index] = newMember
                        }
                    } else {
                        // Add new
                        familyMembers.add(newMember)
                    }
                    showDialog = false
                },
                onDelete = { memberToDelete ->
                    familyMembers.removeIf { it.id == memberToDelete.id }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun FamilyMemberItem(
    member: FamilyMember,
    onEdit: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = member.relation,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = member.role,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                TextButton(onClick = onEdit) {
                    Text(
                        text = "Edit",
                        color = Color(0xFF00ACC1),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = Color(0xFFEEEEEE)
            )
            
            Text(
                text = "Conditions",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = member.conditions,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF455A64)
            )
        }
    }
}

@Composable
fun AddEditFamilyMemberDialog(
    member: FamilyMember?,
    onDismiss: () -> Unit,
    onSave: (FamilyMember) -> Unit,
    onDelete: (FamilyMember) -> Unit
) {
    var relation by remember { mutableStateOf(member?.relation ?: "") }
    var role by remember { mutableStateOf(member?.role ?: "") }
    var conditions by remember { mutableStateOf(member?.conditions ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (member == null) "Add Family Member" else "Edit Family Member",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = relation,
                    onValueChange = { relation = it },
                    label = { Text("Relation (e.g., Father)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role (e.g., Parent)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = conditions,
                    onValueChange = { conditions = it },
                    label = { Text("Conditions (comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val newMember = member?.copy(
                                relation = relation,
                                role = role,
                                conditions = conditions
                            ) ?: FamilyMember(
                                relation = relation,
                                role = role,
                                conditions = conditions
                            )
                            onSave(newMember)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00838F))
                    ) {
                        Text("Save")
                    }
                }
                
                if (member != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { onDelete(member) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Delete Member")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FamilyHistoryScreenPreview() {
    GenecareTheme {
        FamilyHistoryScreen(NavController(androidx.compose.ui.platform.LocalContext.current))
    }
}
