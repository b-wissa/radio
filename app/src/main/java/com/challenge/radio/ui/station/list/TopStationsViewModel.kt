package com.challenge.radio.ui.station.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.radio.station.model.Station
import com.challenge.radio.station.usecase.GetTopStationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopStationsViewModel
    @Inject
    constructor(
        private val getTopStationsUseCase: GetTopStationsUseCase,
    ) : ViewModel() {
        private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
        val viewState: StateFlow<ViewState> = _viewState

        init {
            loadStations()
        }

        fun onRetryClicked() {
            _viewState.value = ViewState.Loading
            loadStations()
        }

        private fun loadStations() {
            viewModelScope.launch {
                _viewState.value =
                    getTopStationsUseCase().fold(
                        onSuccess = {
                            ViewState.Loaded(stations = it)
                        },
                        onFailure = {
                            ViewState.Error
                        },
                    )
            }
        }

        sealed interface ViewState {
            data object Loading : ViewState

            data object Error : ViewState

            data class Loaded(
                val stations: List<Station>,
            ) : ViewState
        }
    }
