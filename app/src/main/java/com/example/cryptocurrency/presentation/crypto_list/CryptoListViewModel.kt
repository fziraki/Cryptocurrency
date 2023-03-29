package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.DefaultPaginator
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
    private val getCryptoListByPageUseCase: GetCryptoListByPageUseCase,
    private val pinCryptoUseCase: PinCryptoUseCase,
    private val unPinCryptoUseCase: UnPinCryptoUseCase,
    private val updateCryptoPinUseCase: UpdateCryptoPinUseCase,
    private val getLikedCryptoListUseCase: GetLikedCryptoListUseCase,
    private val likeCryptoUseCase: LikeCryptoUseCase,
    private val unLikeCryptoUseCase: UnLikeCryptoUseCase
): ViewModel() {

    private val _state = mutableStateOf(CryptoListState())
    val state: State<CryptoListState> = _state

    private val _liked = MutableStateFlow<List<Crypto>>(emptyList())
    private val liked = _liked.asStateFlow()

    private val _cryptos = MutableStateFlow<List<Crypto>>(emptyList())
    private val cryptos = _cryptos.asStateFlow()

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
        _cryptos.value = state.value.cryptosToShow + items
        _state.value = state.value.copy(
                        page = newKey,
                        endReached = items.isEmpty()
                    )
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class TogglePin(val crypto: Crypto, val isPinned: Boolean) : UiEvent()
        data class ToggleLike(val crypto: Crypto, val isLiked: Boolean) : UiEvent()
    }

    init {
        viewModelScope.launch {
            cryptos.collect { cryptoList ->
                cryptoList.map { crypto ->
                    crypto.isLiked = liked.value.any { it.id == crypto.id }
                    crypto
                }.apply {
                    _state.value = state.value.copy(cryptosToShow = this)
                }
            }
        }
        refresh()
    }

    fun refresh() {
        getCryptos()
        getLikedCryptos()
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
                updateCryptoPin(event.crypto, event.isPinned)
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
                    _cryptos.value = result.data ?: emptyList()
                    _state.value = state.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    _cryptos.value = result.data ?: emptyList()
                    _state.value = state.value.copy(isLoading = false)
                }
                is Resource.Error -> {
                    _cryptos.value = result.data ?: emptyList()
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

    private fun updateCryptoPin(crypto: Crypto, isPinned: Boolean) {
        updateCryptoPinUseCase(crypto, isPinned).onEach { result ->
            when(result){
                is Resource.Error -> {
                    result.message?.let {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it))
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun pinCrypto(crypto: Crypto) {
        pinCryptoUseCase(crypto).onEach { result ->
            when(result){
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
                    _liked.value = result.data?: emptyList()
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