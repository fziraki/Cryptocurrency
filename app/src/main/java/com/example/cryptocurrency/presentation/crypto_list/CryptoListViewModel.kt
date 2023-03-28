package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val getCryptoListUseCase: GetCryptoListUseCase,
    private val getPinnedCryptoListUseCase: GetPinnedCryptoListUseCase,
    private val pinCryptoUseCase: PinCryptoUseCase,
    private val unPinCryptoUseCase: UnPinCryptoUseCase,
    private val getLikedCryptoListUseCase: GetLikedCryptoListUseCase,
    private val likeCryptoUseCase: LikeCryptoUseCase,
    private val unLikeCryptoUseCase: UnLikeCryptoUseCase
): ViewModel() {

    private val _state = mutableStateOf(CryptoListState())
    val state: State<CryptoListState> = _state

    private val _pinned = MutableStateFlow<List<Crypto>>(emptyList())
    private val pinned = _pinned.asStateFlow()

    private val _liked = MutableStateFlow<List<Crypto>>(emptyList())
    private val liked = _liked.asStateFlow()

    private val _cryptos = MutableStateFlow<List<Crypto>>(emptyList())
    private val cryptos = _cryptos.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class TogglePin(val crypto: Crypto, val isPinned: Boolean) : UiEvent()
        data class ToggleLike(val crypto: Crypto, val isLiked: Boolean) : UiEvent()
    }

    init {
        refresh()
        viewModelScope.launch {
            cryptos.collect { cryptoList ->
                cryptoList.map { crypto ->
                    crypto.isPinned = pinned.value.any { it.id == crypto.id }
                    crypto.isLiked = liked.value.any { it.id == crypto.id }
                    crypto
                }.sortedByDescending {
                    it.isPinned
                }.apply {
                    _state.value = state.value.copy(cryptosToShow = this)
                }
            }
        }
    }

    fun refresh() {
        getPinnedCryptos()
        getLikedCryptos()
        getCryptos()
        _isRefreshing.value = false
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
            is UiEvent.ToggleLike -> {
                if (event.isLiked){
                    likeCrypto(event.crypto)
                }else {
                    unLikeCrypto(event.crypto.id)
                }
            }
            is UiEvent.ShowSnackbar -> {}
        }
    }

    private fun getCryptos() {
        viewModelScope.launch {
            getCryptoListUseCase().onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _cryptos.value = result.data?: emptyList()
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _cryptos.value = result.data?: emptyList()
                        _state.value = state.value.copy(isLoading = false)
                    }
                    is Resource.Error -> {
                        _cryptos.value = result.data?: emptyList()
                        _state.value = state.value.copy(isLoading = false)
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
                    is Resource.Success -> {
                        _pinned.value = result.data?: emptyList()
                    }
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

    private fun pinCrypto(crypto: Crypto) {
        viewModelScope.launch {
            pinCryptoUseCase(crypto).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

    private fun unPinCrypto(cryptoId: Int) {
        viewModelScope.launch {
            unPinCryptoUseCase(cryptoId).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

    private fun getLikedCryptos() {
        viewModelScope.launch {
            getLikedCryptoListUseCase().onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _liked.value = result.data?: emptyList()
                    }
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

    private fun likeCrypto(crypto: Crypto) {
        viewModelScope.launch {
            likeCryptoUseCase(crypto).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

    private fun unLikeCrypto(cryptoId: Int) {
        viewModelScope.launch {
            unLikeCryptoUseCase(cryptoId).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        result.message?.let {
                            _eventFlow.emit(UiEvent.ShowSnackbar(it))
                        }
                    }
                    else -> {}
                }
            }.launchIn(this)
        }
    }

}