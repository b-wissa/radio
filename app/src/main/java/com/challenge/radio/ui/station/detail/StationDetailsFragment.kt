package com.challenge.radio.ui.station.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.challenge.radio.R
import com.challenge.radio.databinding.FragmentStationDetailsBinding
import com.challenge.radio.station.model.Station
import com.challenge.radio.ui.station.detail.StationDetailsViewModel.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StationDetailsFragment : Fragment() {
    private val viewModel: StationDetailsViewModel by viewModels()
    private lateinit var stationId: String
    private lateinit var binding: FragmentStationDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stationId = arguments?.let { getStationId(it) } ?: error("Station Id is not provided")
        subscribeToViewState()
        loadStationDetails(stationId)
    }

    private fun subscribeToViewState() {
        viewModel.viewState
            .flowWithLifecycle(lifecycle)
            .onEach { viewState ->
                displayState(viewState)
            }.launchIn(lifecycleScope)
    }

    private fun loadStationDetails(id: String) {
        lifecycleScope.launch { viewModel.loadStationDetails(id = id) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStationDetailsBinding.inflate(inflater)
        return binding.root
    }

    private fun displayState(viewState: ViewState) {
        with(binding) {
            when (viewState) {
                ViewState.Error -> {
                    errorConstraintLayout.visibility = View.VISIBLE
                    loadingProgressIndicator.visibility = View.GONE
                    detailsConstraintLayout.visibility = View.GONE
                }

                ViewState.Loading -> {
                    errorConstraintLayout.visibility = View.GONE
                    loadingProgressIndicator.visibility = View.VISIBLE
                    detailsConstraintLayout.visibility = View.GONE
                }

                is ViewState.Loaded -> {
                    errorConstraintLayout.visibility = View.GONE
                    loadingProgressIndicator.visibility = View.GONE
                    detailsConstraintLayout.visibility = View.VISIBLE
                    populateDetails(station = viewState.station)
                }
            }
        }
    }

    private fun populateDetails(station: Station) {
        with(binding) {
            name.text = station.name
            location.text =
                getString(R.string.station_detail_location, station.city, station.country)

            if (station.genres.isNotEmpty()) {
                genreTitle.visibility = View.VISIBLE
                genre.text = station.genres.joinToString(separator = ",", prefix = " ")
            } else {
                genreTitle.visibility = View.GONE
            }

            if (station.topics.isNotEmpty()) {
                topicsTitle.visibility = View.VISIBLE
                topics.text = station.topics.joinToString(separator = ",", prefix = " ")
            } else {
                topicsTitle.visibility = View.GONE
            }

            Glide
                .with(logo.context)
                .load(station.logo)
                .placeholder(R.drawable.ic_radio_solid)
                .into(logo)

            description.text = station.description
        }
    }

    companion object {
        private const val STATION_ID_KEY = "station_id"
        const val TAG = "stations_details_fragment"

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
