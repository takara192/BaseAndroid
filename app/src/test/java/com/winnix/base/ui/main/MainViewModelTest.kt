package com.winnix.base.ui.main

import app.cash.turbine.test
import com.winnix.base.MainDispatcherRule
import com.winnix.base.R
import com.winnix.base.domain.model.DataError
import com.winnix.base.domain.model.SampleItem
import com.winnix.base.domain.model.SyncResult
import com.winnix.base.domain.repository.SampleRepository
import com.winnix.base.ui.core.StringResource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val item = SampleItem(id = "1", title = "Title", description = "Description")

    @Test
    fun `refresh stores items and stops refreshing`() = runTest {
        val repository = FakeSampleRepository()
        val viewModel = MainViewModel(repository)

        viewModel.uiState.test {
            assertEquals(MainUiState(items = emptyList(), isRefreshing = true), awaitItem())

            repository.items.value = listOf(item)
            assertEquals(MainUiState(items = listOf(item), isRefreshing = true), awaitItem())

            repository.result.complete(SyncResult.Success)
            assertEquals(MainUiState(items = listOf(item), isRefreshing = false), awaitItem())
        }
    }

    @Test
    fun `network failure emits an error event`() = runTest {
        val repository = FakeSampleRepository()
        val viewModel = MainViewModel(repository)

        viewModel.events.test {
            repository.result.complete(SyncResult.Failure(DataError.Network))
            assertEquals(MainEvent.ShowError(StringResource.Resource(R.string.error_network)), awaitItem())
        }
    }

    private class FakeSampleRepository : SampleRepository {
        val items = MutableStateFlow<List<SampleItem>>(emptyList())
        val result = CompletableDeferred<SyncResult>()

        override fun observeItems(): Flow<List<SampleItem>> = items

        override suspend fun refresh(): SyncResult = result.await()
    }
}
