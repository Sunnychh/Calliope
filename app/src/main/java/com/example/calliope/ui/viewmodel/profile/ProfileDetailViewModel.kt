package com.example.calliope.ui.viewmodel.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calliope.data.repository.PersonalityRepository
import com.example.calliope.ui.viewmodel.state.PersonalityDetailUi
import com.example.calliope.ui.viewmodel.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.calliope.utils.Result

/**
 * @author Sunny
 * @date 2024/11/22/下午3:27
 */
@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val personalityRepository: PersonalityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val personalityId: Int = checkNotNull(savedStateHandle["profileId"])

    private val _uiState = MutableStateFlow<UiState<PersonalityDetailUi>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPersonalityDetails()
    }

    private fun loadPersonalityDetails() {
        viewModelScope.launch {
            when (val result = personalityRepository.getPersonalityById(personalityId)) {
                is Result.Success -> {
                    val personality = result.data
                    _uiState.update {
                        UiState.Success(
                            PersonalityDetailUi(
                                id = personality.id,
                                name = personality.name,
                                description = personality.extend["description"] as? String ?: "",
                                traits = (personality.extend["traits"] as? List<*>)?.mapNotNull { it as? String }
                                    ?: emptyList(),
                                preferences = (personality.extend["preferences"] as? Map<*, *>)?.mapNotNull {
                                    (it.key as? String)?.to(it.value as? String ?: "")
                                }?.toMap() ?: emptyMap(),
                                extend = personality.extend
                            )
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        UiState.Error(result.exception.message ?: "Unknown error")
                    }
                }
                else -> {}
            }
        }
    }

    fun updatePersonality(personality: PersonalityDetailUi) {
        viewModelScope.launch {
            val updatedEntity = com.example.calliope.data.entity.PersonalityEntity(
                id = personality.id,
                name = personality.name,
                extend = personality.extend + mapOf(
                    "description" to personality.description,
                    "traits" to personality.traits,
                    "preferences" to personality.preferences,
                    "updatedAt" to System.currentTimeMillis()
                )
            )

            when (val result = personalityRepository.updatePersonality(updatedEntity)) {
                is Result.Success -> {
                    loadPersonalityDetails()
                }
                is Result.Error -> {
                    _uiState.update {
                        UiState.Error(result.exception.message ?: "Unknown error")
                    }
                }
                else -> {}
            }
        }
    }

    fun addTrait(trait: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is UiState.Success) {
                val updatedTraits = currentState.data.traits + trait
                updatePersonality(currentState.data.copy(traits = updatedTraits))
            }
        }
    }

    fun removeTrait(trait: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is UiState.Success) {
                val updatedTraits = currentState.data.traits - trait
                updatePersonality(currentState.data.copy(traits = updatedTraits))
            }
        }
    }

    fun updatePreference(key: String, value: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is UiState.Success) {
                val updatedPreferences = currentState.data.preferences + (key to value)
                updatePersonality(currentState.data.copy(preferences = updatedPreferences))
            }
        }
    }
}
