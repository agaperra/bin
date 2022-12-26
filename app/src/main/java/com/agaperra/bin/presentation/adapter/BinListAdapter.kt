package com.agaperra.bin.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agaperra.bin.R
import com.agaperra.bin.databinding.BinItemBinding
import com.agaperra.bin.domain.model.BinItem
import com.agaperra.bin.presentation.adapter.diffutil.BinDiffUtil
import com.agaperra.bin.presentation.adapter.listener.BinNumberClickListener

class BinListAdapter(val onBinClickListener: BinNumberClickListener) :
    ListAdapter<BinItem, BinListAdapter.BinListViewHolder>(BinDiffUtil()) {

    inner class BinListViewHolder(private val binding: BinItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(itemPosition: Int) {

            val bin = getItem(itemPosition)

            binding.binN.text = bin.number

            binding.root.setOnClickListener {
                onBinClickListener.onItemClick(item =  bin)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BinListViewHolder(
            BinItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.bin_item,
                    parent,
                    false
                )
            )
        )

    override fun onBindViewHolder(holder: BinListViewHolder, position: Int) =
        holder.bind(itemPosition = position)


}