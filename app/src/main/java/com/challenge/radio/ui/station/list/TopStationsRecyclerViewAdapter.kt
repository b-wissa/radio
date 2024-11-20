package com.challenge.radio.ui.station.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.challenge.radio.databinding.TopStationListItemBinding
import com.challenge.radio.station.model.Station

class TopStationsRecyclerViewAdapter : RecyclerView.Adapter<TopStationsRecyclerViewAdapter.ViewHolder>() {
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
    }

    fun setItems(items: List<Station>) {
        asyncListDiffer.submitList(items)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class ViewHolder(
        binding: TopStationListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val nameTextView: TextView = binding.stationName
        val cityTextView: TextView = binding.stationCity
    }
}
