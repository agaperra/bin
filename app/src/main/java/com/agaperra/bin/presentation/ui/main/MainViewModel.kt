package com.agaperra.bin.presentation.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agaperra.bin.R
import com.agaperra.bin.data.db.ConverterEntityToDomain.toDomain
import com.agaperra.bin.data.remote.dto.CarsAnswerResponse
import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.ErrorState
import com.agaperra.bin.domain.interactor.StringInteractor
import com.agaperra.bin.domain.model.CardItem
import com.agaperra.bin.domain.use_case.Cases
import com.agaperra.bin.utils.network.InternetReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val iReceiver: InternetReceiver,
    private val stringInteractor: StringInteractor,
    cases: Cases
) : ViewModel() {

    private val allCases = cases

    private val _mainContent = MutableStateFlow<AppState<CarsAnswerResponse>>(AppState.Loading())
    var mainContent = _mainContent.asStateFlow()

    private val _mainContentLoading = MutableStateFlow(true)
    var mainContentLoading = _mainContentLoading.asStateFlow()

    private val _readAllBin = MutableStateFlow<List<CardItem>>(listOf())
    var readAllBin = _readAllBin.asStateFlow()

    val liveDataEnter = MutableLiveData<String>()
    val liveDataHistory = MutableLiveData<String>()

    init {
        setText()
        readData()
    }

    private fun readData() = allCases.getBinHistoryList().onEach { resource ->
        _readAllBin.value = resource.map { it.toDomain() }
    }.launchIn(viewModelScope)


    private fun setText() {
        liveDataEnter.value = stringInteractor.enterBinNumber
        liveDataHistory.value = stringInteractor.historyTitle
    }


    fun getContent(context: Context, number: String?, noInternet: () -> Unit,  onError: (ErrorState) -> Unit, onSuccess: ()->Unit) {
        if (iReceiver.checkState(context)) {
            getBinInfo(number = number, onError = onError, onSuccess)
        } else {
            if (_mainContent.value.data != null) _mainContent.value =
                AppState.Error(error = ErrorState.NO_INTERNET_CONNECTION)
            noInternet()
        }

    }

    private fun getBinInfo(number: String?, onError: (ErrorState) -> Unit, onSuccess: ()->Unit) {
        allCases.getCardBinInfo(number = number).onEach { result ->
            when (result) {
                is AppState.Success -> {
                    _mainContent.value = result
                    _mainContentLoading.value = false
                    onSuccess()
                }

                is AppState.Loading -> {
                    _mainContentLoading.value = true
                }

                is AppState.Error -> {
                    _mainContentLoading.value = false
                    _mainContent.value = result
                    Timber.e(result.message?.name)
                    when (result.error) {
                        ErrorState.BAD_REQUEST -> {
                            (ErrorState.BAD_REQUEST)
                        }

                        ErrorState.NO_BIN_LOADED -> {
                            onError(ErrorState.NO_BIN_LOADED)
                        }

                        ErrorState.NO_INTERNET_CONNECTION -> {
                            onError(ErrorState.NO_INTERNET_CONNECTION)
                        }
                        ErrorState.TOO_MANY_REQUESTS->{
                            onError(ErrorState.TOO_MANY_REQUESTS)
                        }
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun insertBin(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allCases.insertBinItem(CardItem(number = number))
        }
    }

}

