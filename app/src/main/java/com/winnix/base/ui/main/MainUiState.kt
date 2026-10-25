package com.winnix.base.ui.main

import com.winnix.base.domain.model.SampleItem
import com.winnix.base.ui.core.StringResource

data class MainUiState(
    val items: List<SampleItem> = emptyList(),
    val isRefreshing: Boolean = false,
) {
    val isEmpty: Boolean get() = items.isEmpty() && !isRefreshing
}

sealed interface MainEvent {
    data class ShowError(val message: StringResource) : MainEvent
}
