package com.example.calliope.ui.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calliope.data.repository.PersonalityRepository
import com.example.calliope.ui.viewmodel.state.PersonalityUi
import com.example.calliope.ui.viewmodel.state.ProfileUiState
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
class ProfileViewModel @Inject constructor(
    private val personalityRepository: PersonalityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPersonalities()
    }

    fun loadPersonalities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                personalityRepository.getAllPersonalities().collect { personalities ->
                    val personalitiesUi = personalities.map { personality ->
                        PersonalityUi(
                            id = personality.id,
                            name = personality.name,
                            description = personality.extend["description"] as? String ?: "",
                            traits = (personality.extend["traits"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                            isActive = personality.extend["isActive"] as? Boolean ?: false,
                            extend = personality.extend
                        )
                    }
                    _uiState.update {
                        it.copy(
                            personalities = UiState.Success(personalitiesUi),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        personalities = UiState.Error(e.message ?: "Unknown error"),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun createPersonality(
        name: String,
        description: String,
        traits: List<String>
    ) {
        viewModelScope.launch {
            val personality = com.example.calliope.data.entity.PersonalityEntity(
                name = name,
                extend = mapOf(
                    "description" to description,
                    "traits" to traits,
                    "isActive" to false,
                    "createdAt" to System.currentTimeMillis()
                )
            )

            when (val result = personalityRepository.insertPersonality(personality)) {
                is Result.Success -> {
                    loadPersonalities()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(error = result.exception.message)
                    }
                }
                else -> {}
            }
        }
    }

    fun deletePersonality(personalityId: Int) {
        viewModelScope.launch {
            val currentPersonalities = (_uiState.value.personalities as? UiState.Success)?.data
            val personalityToDelete = currentPersonalities?.find { it.id == personalityId }

            personalityToDelete?.let {
                val entity = com.example.calliope.data.entity.PersonalityEntity(
                    id = it.id,
                    name = it.name,
                    extend = it.extend
                )

                when (val result = personalityRepository.deletePersonality(entity)) {
                    is Result.Success -> {
                        loadPersonalities()
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(error = result.exception.message)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun selectPersonality(personalityId: Int) {
        viewModelScope.launch {
            val currentPersonalities = (_uiState.value.personalities as? UiState.Success)?.data
            val selectedPersonality = currentPersonalities?.find { it.id == personalityId }
            _uiState.update {
                it.copy(currentPersonality = selectedPersonality)
            }
        }
    }
}
