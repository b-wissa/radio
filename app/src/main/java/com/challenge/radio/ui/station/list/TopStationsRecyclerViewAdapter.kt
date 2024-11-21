package com.challenge.radio.ui.station.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.radio.R
import com.challenge.radio.databinding.TopStationListItemBinding
import com.challenge.radio.station.model.Station

class TopStationsRecyclerViewAdapter(
    private val onStationClicked: (station: Station) -> Unit,
) : RecyclerView.Adapter<TopStationsRecyclerViewAdapter.ViewHolder>() {
    private val diffUtil =
        object : DiffUtil.ItemCallback<Station>() {
            override fun areItemsTheSame(
                oldItem: Station,
                newItem: Station,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Station,
                newItem: Station,
            ): Boolean = oldItem == newItem
        }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder =
        ViewHolder(
            TopStationListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = asyncListDiffer.currentList[position]
        holder.cityTextView.text = item.city
        holder.nameTextView.text = item.name
        if (item.genres.isNotEmpty()) {
            holder.genreTextView.visibility = View.VISIBLE
            holder.genreTitleTextView.visibility = View.VISIBLE
            holder.genreTextView.text = item.genres.joinToString(separator = ",", prefix = " ")
        } else {
            holder.genreTextView.visibility = View.GONE
            holder.genreTitleTextView.visibility = View.GONE
        }
        Glide
            .with(holder.logoImageView.context)
            .load(item.logo)
            .placeholder(R.drawable.ic_radio_solid)
            .into(holder.logoImageView)
        holder.containerCaredView.setOnClickListener {
            onStationClicked(item)
        }
    }

    fun setItems(items: List<Station>) {
        asyncListDiffer.submitList(items)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class ViewHolder(
        binding: TopStationListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val logoImageView: ImageView = binding.logo
        val nameTextView: TextView = binding.name
        val cityTextView: TextView = binding.city
        val genreTextView: TextView = binding.genre
        val genreTitleTextView: TextView = binding.genreTitle
        val containerCaredView: CardView = binding.stationItemContainer
    }
}
