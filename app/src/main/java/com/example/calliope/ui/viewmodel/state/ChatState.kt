package com.example.calliope.ui.viewmodel.state

/**
 * @author Sunny
 * @date 2024/11/22/下午2:42
 */

data class ChatUiState(
    val dialogs: UiState<List<ChatDialogUi>> = UiState.Loading,
    val currentDialog: ChatDialogUi? = null,
    val messages: UiState<List<ChatMessageUi>> = UiState.Loading,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ChatDialogUi(
    val id: Int,
    val name: String,
    val description: String,
    val lastMessage: String? = null,
    val timestamp: Long? = null,
    val extend: Map<String, Any> = emptyMap()
)

data class ChatMessageUi(
    val id: Int,
    val dialogId: Int,
    val userId: Int,
    val message: String,
    val timestamp: Long,
    val isCurrentUser: Boolean,
    val status: String = "sent", // sent, delivered, read
    val extend: Map<String, Any> = emptyMap()
)

data class ContactDetailUi(
    val id: Int,
    val name: String,
    val title: String,
    val summary: String,
    val lastActive: Long? = null,
    val extend: Map<String, Any> = emptyMap()
)
