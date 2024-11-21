package com.challenge.radio.ui.station.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class StationDetailsFragment : Fragment() {
    private val viewModel: StationDetailsViewModel by viewModels()
    private lateinit var stationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stationId = arguments?.let { getStationId(it) } ?: error("Station Id is not provided")
        subscribeToViewState()
        loadStationDetails(stationId)
    }

    private fun subscribeToViewState() {
        viewModel.viewState
            .flowWithLifecycle(lifecycle)
            .onEach { viewState -> }
            .launchIn(lifecycleScope)
    }

    private fun loadStationDetails(id: String) {
        lifecycleScope.launch { viewModel.loadStationDetails(id = stationId) }
    }

    companion object {
        private const val STATION_ID_KEY = "station_id"

        fun newInstance(stationId: String): StationDetailsFragment {
            val fragment =
                StationDetailsFragment().apply {
                    arguments =
                        Bundle().apply {
                            putString(STATION_ID_KEY, stationId)
                        }
                }
            return fragment
        }

        private fun getStationId(bundle: Bundle): String? = bundle.getString(STATION_ID_KEY)
    }
}
