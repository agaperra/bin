package com.agaperra.bin.presentation.adapter.listener

import com.agaperra.bin.domain.model.BinItem

interface BinNumberClickListener {
    fun onItemClick(item: BinItem)
}