package com.challenge.radio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.challenge.radio.ui.station.detail.StationDetailsFragment
import com.challenge.radio.ui.station.list.TopStationsFragment
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    TopStationsFragment.InteractionsListener {
    @Inject
    internal lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setSystemBarPaddings()
        addTopStationsFragment()
    }

    private fun setSystemBarPaddings() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addTopStationsFragment() {
        val fragment = TopStationsFragment.newInstance()
        supportFragmentManager.commit {
            add(R.id.container, fragment, TopStationsFragment.TAG)
        }
    }

    override fun onStationClicked(id: String) {
        val fragment = StationDetailsFragment.newInstance(stationId = id)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add(R.id.container, fragment, StationDetailsFragment.TAG)
        }
    }
}
