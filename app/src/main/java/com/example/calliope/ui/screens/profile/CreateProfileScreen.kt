package com.example.calliope.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calliope.ui.viewmodel.profile.ProfileViewModel

/**
 * @author Sunny
 * @date 2024/11/26/上午10:10
 */
@Composable
fun CreateProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var currentTrait by remember { mutableStateOf("") }
    var traits by remember { mutableStateOf(listOf<String>()) }
    var hasError by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Calliope",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = TextAlign.Center
            )

            // Content Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Back Button
                    Row(
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "返回",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Text(
                        text = "创建新人设",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Error message
                    showError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Title Input
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            hasError = false
                            showError = null
                        },
                        label = { Text("人设名称") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        isError = hasError && title.isEmpty(),
                        singleLine = true
                    )

                    // Description Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            hasError = false
                            showError = null
                        },
                        label = { Text("描述") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        minLines = 3,
                        maxLines = 5,
                        isError = hasError && description.isEmpty()
                    )

                    // Traits Section
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        color = Color(0xFFF8F8F8),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "特征",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Trait Input
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = currentTrait,
                                    onValueChange = { currentTrait = it },
                                    label = { Text("添加特征") },
                                    modifier = Modifier.weight(1f),
                                    enabled = traits.size < 5,
                                    singleLine = true
                                )
                                IconButton(
                                    onClick = {
                                        if (currentTrait.isNotBlank() && traits.size < 5) {
                                            traits = traits + currentTrait
                                            currentTrait = ""
                                            hasError = false
                                            showError = null
                                        }
                                    },
                                    enabled = currentTrait.isNotBlank() && traits.size < 5
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add trait",
                                        tint = if (currentTrait.isNotBlank() && traits.size < 5)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                    )
                                }
                            }

                            if (traits.size >= 5) {
                                Text(
                                    text = "最多添加5个特征",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // Traits List
                            Column(
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                traits.forEach { trait ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = trait,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        IconButton(
                                            onClick = {
                                                traits = traits - trait
                                                hasError = false
                                                showError = null
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove trait",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Create Button
                    Button(
                        onClick = {
                            when {
                                title.isBlank() -> {
                                    hasError = true
                                    showError = "请输入人设名称"
                                }
                                description.isBlank() -> {
                                    hasError = true
                                    showError = "请输入人设描述"
                                }
                                traits.isEmpty() -> {
                                    hasError = true
                                    showError = "请至少添加一个特征"
                                }
                                else -> {
                                    viewModel.createPersonality(title, description, traits)
                                    navController.navigateUp()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "创建",
                            modifier = Modifier.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
