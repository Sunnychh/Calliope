package com.example.calliope.ui.viewmodel.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calliope.data.repository.ChatRepository
import com.example.calliope.data.repository.DialogRepository
import com.example.calliope.ui.viewmodel.state.ChatUiState
import com.example.calliope.ui.viewmodel.state.ChatDialogUi
import com.example.calliope.ui.viewmodel.state.ChatMessageUi
import com.example.calliope.ui.viewmodel.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import com.example.calliope.utils.Result

/**
 * @author Sunny
 * @date 2024/11/22/下午3:27
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dialogRepository: DialogRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDialogs()
    }

    private fun loadDialogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                dialogRepository.getAllDialogs().collect { dialogs ->
                    val chatDialogsUi = dialogs.map { dialog ->
                        ChatDialogUi(
                            id = dialog.id,
                            name = dialog.name,
                            description = dialog.extend["description"] as? String ?: "",
                            lastMessage = dialog.extend["lastMessage"] as? String,
                            timestamp = dialog.extend["lastMessageTime"] as? Long,
                            extend = dialog.extend
                        )
                    }
                    _uiState.update {
                        it.copy(
                            dialogs = UiState.Success(chatDialogsUi),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        dialogs = UiState.Error(e.message ?: "Unknown error"),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun createDialog(name: String, description: String) {
        viewModelScope.launch {
            val dialog = com.example.calliope.data.entity.DialogEntity(
                name = name,
                extend = mapOf(
                    "description" to description,
                    "createdAt" to System.currentTimeMillis()
                )
            )

            when (val result = dialogRepository.createDialog(dialog)) {
                is Result.Success -> {
                    loadDialogs()
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

    fun loadMessages(dialogId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(messages = UiState.Loading) }
            try {
                chatRepository.getChatRecordsByDialog(dialogId).collect { records ->
                    val messagesUi = records.map { record ->
                        ChatMessageUi(
                            id = record.id,
                            dialogId = record.dialogId,
                            userId = record.userId,
                            message = record.message,
                            timestamp = record.time.time,
                            isCurrentUser = record.extend["isCurrentUser"] as? Boolean ?: false,
                            status = record.extend["status"] as? String ?: "sent",
                            extend = record.extend
                        )
                    }
                    _uiState.update {
                        it.copy(messages = UiState.Success(messagesUi))
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(messages = UiState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun sendMessage(dialogId: Int, message: String, userId: Int) {
        viewModelScope.launch {
            val chatRecord = com.example.calliope.data.entity.ChatRecordEntity(
                dialogId = dialogId,
                userId = userId,
                message = message,
                time = Date(),
                extend = mapOf(
                    "isCurrentUser" to true,
                    "status" to "sent",
                    "timestamp" to System.currentTimeMillis()
                )
            )

            when (val result = chatRepository.insertChatRecord(chatRecord)) {
                is Result.Success -> {
                    // Update dialog's last message
                    updateDialogLastMessage(dialogId, message)
                    loadMessages(dialogId)
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

    private suspend fun updateDialogLastMessage(dialogId: Int, message: String) {
        val dialog = dialogRepository.getDialogById(dialogId)
        if (dialog is Result.Success) {
            val updatedDialog = dialog.data.copy(
                extend = dialog.data.extend + mapOf(
                    "lastMessage" to message,
                    "lastMessageTime" to System.currentTimeMillis()
                )
            )
            dialogRepository.updateDialog(updatedDialog)
        }
    }

    fun selectDialog(dialogId: Int) {
        viewModelScope.launch {
            when (val state = _uiState.value.dialogs) {
                is UiState.Success -> {
                    val selectedDialog = state.data.find { it.id == dialogId }
                    _uiState.update {
                        it.copy(currentDialog = selectedDialog)
                    }
                    loadMessages(dialogId)
                }
                else -> {}
            }
        }
    }

    fun deleteDialog(dialogId: Int) {
        viewModelScope.launch {
            when (val dialog = dialogRepository.getDialogById(dialogId)) {
                is Result.Success -> {
                    when (val result = dialogRepository.deleteDialog(dialog.data)) {
                        is Result.Success -> {
                            loadDialogs()
                            _uiState.update {
                                it.copy(currentDialog = null)
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(error = result.exception.message)
                            }
                        }
                        else -> {}
                    }
                }
                else -> {}
            }
        }
    }
}
