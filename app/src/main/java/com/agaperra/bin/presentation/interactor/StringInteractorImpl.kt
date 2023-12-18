package com.agaperra.bin.presentation.interactor

import android.content.Context
import com.agaperra.bin.R
import com.agaperra.bin.domain.interactor.StringInteractor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StringInteractor {
    override val enterBinNumber: String = context.getString(R.string.enter_bin)
    override val historyTitle: String = context.getString(R.string.history_title)
}