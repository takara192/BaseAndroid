package com.winnix.base.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.winnix.base.R
import com.winnix.base.domain.model.DataError
import com.winnix.base.domain.model.SyncResult
import com.winnix.base.domain.repository.SampleRepository
import com.winnix.base.ui.core.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SampleRepository,
) : ViewModel() {
    private val isRefreshing = MutableStateFlow(false)

    private val _events = Channel<MainEvent>(Channel.BUFFERED)
    val events: Flow<MainEvent> = _events.receiveAsFlow()

    val uiState: StateFlow<MainUiState> = combine(repository.observeItems(), isRefreshing) { items, refreshing ->
        MainUiState(items = items, isRefreshing = refreshing)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState(isRefreshing = true))

    init {
        refresh()
    }

    fun refresh() {
        if (isRefreshing.value) return
        isRefreshing.value = true
        viewModelScope.launch {
            val result = repository.refresh()
            isRefreshing.value = false
            if (result is SyncResult.Failure) {
                _events.send(MainEvent.ShowError(StringResource.Resource(result.error.messageRes)))
            }
        }
    }

    private val DataError.messageRes: Int
        get() = when (this) {
            DataError.Network -> R.string.error_network
            DataError.Server -> R.string.error_server
            DataError.Client, DataError.Parse, DataError.Unknown -> R.string.error_unknown
        }
}
