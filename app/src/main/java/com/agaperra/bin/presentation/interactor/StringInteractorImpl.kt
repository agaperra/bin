package com.agaperra.bin.presentation.interactor

import android.content.Context
import com.agaperra.bin.R
import com.agaperra.bin.domain.interactor.StringInteractor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StringInteractor {
    override val enterBin: String = context.getString(R.string.enter_bin)
}