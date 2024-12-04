package com.example.calliope.ui.viewmodel.chat


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calliope.data.repository.UserRepository
import com.example.calliope.data.repository.ChatRepository
import com.example.calliope.ui.viewmodel.state.UiState
import com.example.calliope.ui.viewmodel.state.ContactDetailUi
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
class ContactDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ContactDetailUi>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val contactId: Int = checkNotNull(savedStateHandle["contactId"])
    private val _chatHistoryState = MutableStateFlow<UiState<List<ChatHistoryItemUi>>>(UiState.Loading)
    val chatHistoryState = _chatHistoryState.asStateFlow()

    init {
        loadContactDetails()
        loadChatHistory()
    }

    private fun loadContactDetails() {
        viewModelScope.launch {
            when (val result = userRepository.getUserById(contactId)) {
                is Result.Success -> {
                    val user = result.data
                    _uiState.update {
                        UiState.Success(
                            ContactDetailUi(
                                id = user.id,
                                name = user.name,
                                title = user.extend["title"] as? String ?: "",
                                summary = user.extend["summary"] as? String ?: "",
                                lastActive = user.extend["lastActive"] as? Long,
                                extend = user.extend
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

    private fun loadChatHistory() {
        viewModelScope.launch {
            _chatHistoryState.update { UiState.Loading }
            try {
                chatRepository.getChatRecordsByDialogAndUser(dialogId = -1, userId = contactId)
                    .collect { records ->
                        val historyItems = records.map { record ->
                            ChatHistoryItemUi(
                                id = record.id,
                                message = record.message,
                                timestamp = record.time.time,
                                isCurrentUser = record.extend["isCurrentUser"] as? Boolean ?: false
                            )
                        }
                        _chatHistoryState.update { UiState.Success(historyItems) }
                    }
            } catch (e: Exception) {
                _chatHistoryState.update {
                    UiState.Error(e.message ?: "Failed to load chat history")
                }
            }
        }
    }

    fun updateContact(
        name: String? = null,
        title: String? = null,
        summary: String? = null
    ) {
        viewModelScope.launch {
            val currentContact = (_uiState.value as? UiState.Success)?.data ?: return@launch
            val updatedContact = currentContact.copy(
                name = name ?: currentContact.name,
                title = title ?: currentContact.title,
                summary = summary ?: currentContact.summary
            )

            val userEntity = com.example.calliope.data.entity.UserEntity(
                id = updatedContact.id,
                name = updatedContact.name,
                extend = updatedContact.extend + mapOf(
                    "title" to updatedContact.title,
                    "summary" to updatedContact.summary,
                    "updatedAt" to System.currentTimeMillis()
                )
            )

            when (val result = userRepository.updateUser(userEntity)) {
                is Result.Success -> {
                    loadContactDetails()
                }
                is Result.Error -> {
                    _uiState.update {
                        UiState.Error(result.exception.message ?: "Update failed")
                    }
                }
                else -> {}
            }
        }
    }
}

data class ChatHistoryItemUi(
    val id: Int,
    val message: String,
    val timestamp: Long,
    val isCurrentUser: Boolean
)
