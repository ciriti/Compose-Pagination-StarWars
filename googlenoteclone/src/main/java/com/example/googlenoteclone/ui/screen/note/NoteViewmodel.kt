package com.example.googlenoteclone.ui.screen.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlenoteclone.data.repository.ConnectivityRepository
import kotlinx.coroutines.launch

class NotesViewModel(
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    val networkStatus = connectivityRepository.isConnected

    init {
        viewModelScope.launch {

        }
    }
}
