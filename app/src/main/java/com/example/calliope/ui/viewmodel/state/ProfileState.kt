package com.example.calliope.ui.viewmodel.state

/**
 * @author Sunny
 * @date 2024/11/22/下午3:25
 */
data class ProfileUiState(
    val personalities: UiState<List<PersonalityUi>> = UiState.Loading,
    val currentPersonality: PersonalityUi? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PersonalityUi(
    val id: Int,
    val name: String,
    val description: String,
    val traits: List<String>,
    val isActive: Boolean = false,
    val extend: Map<String, Any> = emptyMap()
)

data class PersonalityDetailUi(
    val id: Int,
    val name: String,
    val description: String,
    val traits: List<String>,
    val preferences: Map<String, String> = emptyMap(),
    val extend: Map<String, Any> = emptyMap()
)
