package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.use_case.GetCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.GetPinnedCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.PinCryptoUseCase
import com.example.cryptocurrency.domain.use_case.UnPinCryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val getCryptoListUseCase: GetCryptoListUseCase,
    private val getPinnedCryptoListUseCase: GetPinnedCryptoListUseCase,
    private val pinCryptoUseCase: PinCryptoUseCase,
    private val unPinCryptoUseCase: UnPinCryptoUseCase
): ViewModel() {

    private val _state = mutableStateOf(CryptoListState())
    val state: State<CryptoListState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class TogglePin(val crypto: Crypto, val isPinned: Boolean) : UiEvent()
    }

    init {
        getPinnedCryptos()
        getCryptos()
    }

    fun onUiEvent(event: UiEvent){
        when(event){
            is UiEvent.TogglePin -> {
                if (event.isPinned){
                    pinCrypto(event.crypto)
                }else {
                    unPinCrypto(event.crypto.id)
                }
            }
            else -> {}
        }
    }

    private fun getCryptos() {
        viewModelScope.launch {
            getCryptoListUseCase().onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true,
                            cryptos = result.data?: emptyList()
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false,
                            cryptos = result.data?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false,
                            cryptos = result.data?: emptyList()
                        )
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getPinnedCryptos() {
        viewModelScope.launch {
            getPinnedCryptoListUseCase().onEach { result ->
                when(result){
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false,
                            pinnedCryptos = result.data?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun pinCrypto(crypto: Crypto) {
        viewModelScope.launch {
            pinCryptoUseCase(crypto).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun unPinCrypto(cryptoId: Int) {
        viewModelScope.launch {
            unPinCryptoUseCase(cryptoId).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoading = false)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                }
            }.launchIn(this)
        }
    }

}