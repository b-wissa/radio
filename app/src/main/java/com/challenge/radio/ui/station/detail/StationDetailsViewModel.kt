package com.challenge.radio.ui.station.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.radio.station.model.Station
import com.challenge.radio.station.usecase.GetStationDetailsByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class StationDetailsViewModel
    @Inject
    constructor(
        private val getStationDetailsByIdUseCase: GetStationDetailsByIdUseCase,
    ) : ViewModel() {
        private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
        val viewState: StateFlow<ViewState> = _viewState

        fun loadStationDetails(id: String) {
            _viewState.value = ViewState.Loading
            viewModelScope.launch {
                _viewState.value =
                    getStationDetailsByIdUseCase(stationId = id).fold(
                        onSuccess = { station -> ViewState.Loaded(station) },
                        onFailure = { ViewState.Error },
                    )
            }
        }

        sealed interface ViewState {
            data object Loading : ViewState

            data object Error : ViewState

            data class Loaded(
                val station: Station,
            ) : ViewState
        }
    }
