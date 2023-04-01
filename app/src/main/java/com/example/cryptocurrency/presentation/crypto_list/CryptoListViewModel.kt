package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.DefaultPaginator
import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.use_case.GetCryptoListByPageUseCase
import com.example.cryptocurrency.domain.use_case.GetCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.like.GetLikedCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.like.LikeCryptoUseCase
import com.example.cryptocurrency.domain.use_case.like.UnLikeCryptoUseCase
import com.example.cryptocurrency.domain.use_case.pin.GetPinnedCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.pin.PinCryptoUseCase
import com.example.cryptocurrency.domain.use_case.pin.UnPinCryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val getCryptoListUseCase: GetCryptoListUseCase,
    private val getCryptoListByPageUseCase: GetCryptoListByPageUseCase,
    private val pinCryptoUseCase: PinCryptoUseCase,
    private val unPinCryptoUseCase: UnPinCryptoUseCase,
    private val getLikedCryptoListUseCase: GetLikedCryptoListUseCase,
    private val likeCryptoUseCase: LikeCryptoUseCase,
    private val unLikeCryptoUseCase: UnLikeCryptoUseCase,
    private val getPinnedCryptoListUseCase: GetPinnedCryptoListUseCase
    ): ViewModel() {

    private val _state = mutableStateOf(CryptoListState())
    val state: State<CryptoListState> = _state

    private val _likedState = MutableStateFlow<List<Crypto>>(emptyList())
    val likedState = _likedState.asStateFlow()

    private val _pinnedState = MutableStateFlow<List<Crypto>>(emptyList())

    private val _cryptosState = MutableStateFlow<List<Crypto>>(emptyList())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val paginator = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = {
            _state.value = state.value.copy(isPagingLoading = it)
        },
        onRequest = { nextPage ->
            getCryptoListByPageUseCase(nextPage).last()
        },
        getNextKey = {
            state.value.page + 1
        },
        onError = {

        }
    ) { items, newKey ->

        _cryptosState.value = state.value.cryptosToShow + items
        _state.value = state.value.copy(page = newKey, endReached = items.isEmpty())
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class TogglePin(val crypto: Crypto, val isPinned: Boolean) : UiEvent()
        data class ToggleLike(val crypto: Crypto, val isLiked: Boolean) : UiEvent()
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            while(true) {
                getCryptos()
                delay(20_000)
            }
        }
        getLikedCryptos()
        getPinnedCryptos()
        _isRefreshing.value = false

        combineCryptosLikedPinned()
        combineLikedPinnedToGetLiked()
    }

    private fun combineLikedPinnedToGetLiked() {
        viewModelScope.launch {
            combine(
                _likedState,
                _pinnedState
            ){ likes, pins ->
                likes.map { likedCrypto ->
                    likedCrypto.isPinned = pins.any { it.id == likedCrypto.id }
                    likedCrypto.isLiked = true
                    likedCrypto
                }.sortedByDescending { it.isPinned }
            }.collect { likes ->
                _likedState.value = likes
            }
        }
    }

    private fun combineCryptosLikedPinned() {
        viewModelScope.launch {
            combine(
                _cryptosState,
                _likedState
            ) { cryptos, likes ->
                cryptos.map {crypto ->
                    crypto.isLiked = likes.any { it.id == crypto.id }
                    crypto
                }
            }.combine(_pinnedState){ cryptos, pins ->
                cryptos.map {crypto ->
                    crypto.isPinned = pins.any { it.id == crypto.id }
                    crypto
                }.sortedByDescending { it.isPinned }
            }.collect {
                _state.value = state.value.copy(cryptosToShow = it)
            }
        }
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
        getCryptoListUseCase(0).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _cryptosState.value = result.data?: emptyList()
                    _state.value = state.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    _cryptosState.value = result.data?: emptyList()
                    _state.value = state.value.copy(isLoading = false)
                }
                is Resource.Error -> {
                    _cryptosState.value = result.data?: emptyList()
                    _state.value = state.value.copy(isLoading = false)
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    private fun pinCrypto(crypto: Crypto) {
        pinCryptoUseCase(crypto).onEach { result ->
            when(result){
                is Resource.Success -> {
                    getPinnedCryptos()
                }
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun unPinCrypto(cryptoId: Int) {
        unPinCryptoUseCase(cryptoId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    getPinnedCryptos()
                }
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getLikedCryptos() {
        getLikedCryptoListUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _likedState.value = result.data?: emptyList()
                }
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getPinnedCryptos() {
        getPinnedCryptoListUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _pinnedState.value = result.data?: emptyList()
                }
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun likeCrypto(crypto: Crypto) {
        likeCryptoUseCase(crypto).onEach { result ->
            when(result){
                is Resource.Success -> {
                    getLikedCryptos()
                }
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun unLikeCrypto(cryptoId: Int) {
        unLikeCryptoUseCase(cryptoId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    getLikedCryptos()
                }
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

}