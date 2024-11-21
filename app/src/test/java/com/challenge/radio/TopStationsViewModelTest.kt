package com.challenge.radio

import app.cash.turbine.test
import com.challenge.radio.station.model.Station
import com.challenge.radio.station.usecase.GetTopStationsUseCase
import com.challenge.radio.ui.station.list.TopStationsViewModel
import com.challenge.radio.ui.station.list.TopStationsViewModel.ViewState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TopStationsViewModelTest {
    @get:Rule
    val coroutineRule = TestCoroutineRule()
    private val getTopStationsUseCase: GetTopStationsUseCase = mockk()

    private lateinit var viewModel: TopStationsViewModel

    private val expectedStations =
        listOf(
            Station(
                id = "CNN",
                name = "CNN",
                city = "California",
                country = null,
                genres = listOf("Politics", "Entertainment"),
                topics = listOf(),
                description = null,
                logo = "image_url",
            ),
            Station(
                id = "fx_news",
                name = "Fox News",
                city = "Florida",
                country = "USA",
                genres = listOf("Politics", "Entertainment", "Sports"),
                topics = listOf(),
                description = null,
                logo = "image_url_2",
            ),
        )

    @Test
    fun `loads top stations by default`() =
        runTest {
            coEvery { getTopStationsUseCase() } returns Result.success(expectedStations)
            initViewModel()
            viewModel.viewState.test {
                val actualLoading = awaitItem()
                assertEquals(ViewState.Loading, actualLoading)
                val actualLoadedStations = awaitItem()
                assertEquals(
                    ViewState.Loaded(expectedStations),
                    actualLoadedStations,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `on failed to load stations show error state`() =
        runTest {
            coEvery { getTopStationsUseCase() } returns Result.failure(Throwable("some error"))
            initViewModel()
            viewModel.viewState.test {
                skipItems(1)
                val actualError = awaitItem()
                assertEquals(ViewState.Error, actualError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `on retry called loads stations again`() =
        runTest {
            coEvery { getTopStationsUseCase() } returnsMany (
                listOf(
                    Result.failure(Throwable("some error")),
                    Result.success(expectedStations),
                )
            )
            initViewModel()
            viewModel.viewState.test {
                skipItems(2)
                viewModel.onRetryClicked()
                val actualLoading = awaitItem()
                assertEquals(ViewState.Loading, actualLoading)
                val actualLoadedStations = awaitItem()
                assertEquals(
                    ViewState.Loaded(expectedStations),
                    actualLoadedStations,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun initViewModel() {
        viewModel =
            TopStationsViewModel(
                getTopStationsUseCase = getTopStationsUseCase,
            )
    }
}
