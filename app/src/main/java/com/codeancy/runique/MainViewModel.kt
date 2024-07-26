package com.codeancy.runique

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeancy.core.domain.SessionStorage
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {

            state = state.copy(isCheckingAuth = true)

            val authInfo = sessionStorage.get()

            state = state.copy(
                isCheckingAuth = false,
                isLoggedIn = authInfo != null
            )

        }
    }

    fun setAnalyticsDialogVisibilityStatus(status:Boolean){
        state = state.copy(
            showAnalyticsInstallDialog = status
        )
    }

}