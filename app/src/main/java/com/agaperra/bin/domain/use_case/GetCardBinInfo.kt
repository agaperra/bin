package com.agaperra.bin.domain.use_case

import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.ErrorState
import com.agaperra.bin.domain.repository.CardsRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class GetCardBinInfo @Inject constructor(private val binRepository: CardsRepository) {

    operator fun invoke(
       number: String?,
    ) = flow {
        emit(AppState.Loading())
        try {
            val response =
                binRepository.getInfoByCardNumber(
                    number = number
                )
            emit(AppState.Success(data = response))
        } catch (exception: HttpException) {
            exception.printStackTrace()
            when (exception.code()) {
                400 -> {
                    emit(AppState.Error(error = ErrorState.BAD_REQUEST))
                }
                429 -> {
                    emit(AppState.Error(error = ErrorState.TOO_MANY_REQUESTS))
                }
                else -> {
                    emit(AppState.Error(error = ErrorState.NO_INTERNET_CONNECTION))
                }
            }
            Timber.e(exception)
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(AppState.Error(error = ErrorState.NO_BIN_LOADED))
            Timber.e(exception)
        }
    }

}