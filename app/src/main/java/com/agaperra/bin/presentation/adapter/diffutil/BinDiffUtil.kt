package com.agaperra.bin.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.agaperra.bin.domain.model.CardItem

class BinDiffUtil : DiffUtil.ItemCallback<CardItem>() {

    override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem) =
        oldItem.number == newItem.number

    override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem) =
        oldItem == newItem

}