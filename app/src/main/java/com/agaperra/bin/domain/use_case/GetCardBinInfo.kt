package com.agaperra.bin.domain.use_case

import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.ErrorState
import com.agaperra.bin.domain.repository.BinRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class GetCardBinInfo @Inject constructor(private val binRepository: BinRepository) {

    operator fun invoke(
       number: String?,
    ) = flow {
        emit(AppState.Loading())
        try {
            val response =
                binRepository.mainContent(
                    number = number
                )
            emit(AppState.Success(data = response))
        } catch (exception: HttpException) {
            if (exception.code() != 400)
                emit(AppState.Error(error = ErrorState.NO_INTERNET_CONNECTION))
            Timber.e(exception)
        } catch (exception: Exception) {
            emit(AppState.Error(error = ErrorState.NO_INTERNET_CONNECTION))
            Timber.e(exception)
        }
    }

}