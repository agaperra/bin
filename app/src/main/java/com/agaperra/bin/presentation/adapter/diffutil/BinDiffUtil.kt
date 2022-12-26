package com.agaperra.bin.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.agaperra.bin.domain.model.BinItem

class BinDiffUtil : DiffUtil.ItemCallback<BinItem>() {

    override fun areItemsTheSame(oldItem: BinItem, newItem: BinItem) =
        oldItem.number == newItem.number

    override fun areContentsTheSame(oldItem: BinItem, newItem: BinItem) =
        oldItem == newItem

}