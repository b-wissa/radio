package com.challenge.radio.ui.station.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.radio.databinding.FragmentTopStationsBinding
import com.challenge.radio.ui.station.list.TopStationsViewModel.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TopStationsFragment : Fragment() {
    private val viewModel: TopStationsViewModel by viewModels()
    private var interactionListener: InteractionsListener? = null
    private val stationsAdapter =
        TopStationsRecyclerViewAdapter { station ->
            interactionListener?.onStationClicked(id = station.id)
        }
    private lateinit var binding: FragmentTopStationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState
            .flowWithLifecycle(lifecycle)
            .onEach {
                displayState(it)
            }.launchIn(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTopStationsBinding.inflate(inflater)
        with(binding.stationsRecyclerView) {
            layoutManager = LinearLayoutManager(container?.context)
            adapter = stationsAdapter
        }
        binding.retryButton.setOnClickListener {
            viewModel.onRetryClicked()
        }
        return binding.root
    }

    private fun displayState(state: ViewState) {
        with(binding) {
            when (state) {
                ViewState.Error -> {
                    loadingProgressIndicator.visibility = View.GONE
                    errorConstraintLayout.visibility = View.VISIBLE
                    stationsAdapter.setItems(emptyList())
                }

                is ViewState.Loaded -> {
                    loadingProgressIndicator.visibility = View.GONE
                    errorConstraintLayout.visibility = View.GONE
                    stationsAdapter.setItems(state.stations)
                }

                ViewState.Loading -> {
                    loadingProgressIndicator.visibility = View.VISIBLE
                    errorConstraintLayout.visibility = View.GONE
                    stationsAdapter.setItems(emptyList())
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is InteractionsListener) {
            error("Host $context must implement ${InteractionsListener::class.simpleName}")
        } else {
            interactionListener = context
        }
    }

    interface InteractionsListener {
        fun onStationClicked(id: String)
    }

    companion object {
        fun newInstance() = TopStationsFragment()

        const val TAG = "top_stations_fragment"
    }
}
