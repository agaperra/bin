package com.agaperra.bin.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agaperra.bin.data.db.toDomain
import com.agaperra.bin.data.remote.dto.BinResponse
import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.ErrorState
import com.agaperra.bin.domain.interactor.StringInteractor
import com.agaperra.bin.domain.model.BinItem
import com.agaperra.bin.domain.use_case.Cases
import com.agaperra.bin.domain.use_case.GetBinHistoryList
import com.agaperra.bin.domain.use_case.GetCardBinInfo
import com.agaperra.bin.domain.use_case.InsertBinItem
import com.agaperra.bin.utils.network.ConnectionState
import com.agaperra.bin.utils.network.NetworkStatusListener
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
    private val networkStatusListener: NetworkStatusListener,
    private val stringInteractor: StringInteractor,
    cases: Cases
) : ViewModel() {

    private val allCases = cases

    private val _mainContent = MutableStateFlow<AppState<BinResponse>>(AppState.Loading())
    var mainContent = _mainContent.asStateFlow()

    private val _mainContentLoading = MutableStateFlow(true)
    var mainContentLoading = _mainContentLoading.asStateFlow()

    private val _readAllBin = MutableStateFlow<List<BinItem>>(listOf())
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
        liveDataEnter.value = stringInteractor.enterBin
        liveDataHistory.value = stringInteractor.historyTitle
    }


    fun getContent(number: String?) {
        networkStatusListener.networkStatus.onEach { status ->
            when (status) {
                ConnectionState.Available -> {
                    getBinInfo(number = number)
                }
                ConnectionState.Unavailable -> {
                    if (_mainContent.value.data != null) _mainContent.value =
                        AppState.Error(error = ErrorState.NO_INTERNET_CONNECTION)
                }
            }
        }.launchIn(viewModelScope)

    }

    private fun getBinInfo(number: String?) {
        allCases.getCardBinInfo(number = number).onEach { result ->
            when (result) {
                is AppState.Success -> {
                    _mainContent.value = result
                    _mainContentLoading.value = false
                }
                is AppState.Loading -> {
                    _mainContentLoading.value = true
                }
                is AppState.Error -> {
                    _mainContentLoading.value = false
                    _mainContent.value = result
                    Timber.e(result.message?.name)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun insertBin(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allCases.insertBinItem(BinItem(number = number))
        }
    }

}

