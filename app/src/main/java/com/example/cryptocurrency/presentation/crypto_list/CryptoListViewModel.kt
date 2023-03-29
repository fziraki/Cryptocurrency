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
    private val unLikeCryptoUseCase: UnLikeCryptoUseCase,
    private val updateCryptoLikeUseCase: UpdateCryptoLikeUseCase,
    private val updatePinCryptoLikeFieldUseCase: UpdatePinCryptoLikeFieldUseCase,
    private val updateLikeCryptoPinFieldUseCase: UpdateLikeCryptoPinFieldUseCase,
    ): ViewModel() {

    private val _state = mutableStateOf(CryptoListState())
    val state: State<CryptoListState> = _state

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
        _state.value = state.value.copy(
                        cryptosToShow = state.value.cryptosToShow + items,
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
                updateLikeCryptoPinField(event.crypto, event.isPinned)
                updateCryptoPin(event.crypto, event.isPinned)
            }
            is UiEvent.ToggleLike -> {
                if (event.isLiked){
                    likeCrypto(event.crypto)
                }else {
                    unLikeCrypto(event.crypto.id)
                }
                updatePinCryptoLikeField(event.crypto, event.isLiked)
                updateCryptoLike(event.crypto, event.isLiked)
            }
            is UiEvent.ShowSnackbar -> {}
        }
    }

    private fun getCryptos() {
        getCryptoListUseCase(0).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = state.value.copy(isLoading = true,
                        cryptosToShow = result.data ?: emptyList())
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(isLoading = false,
                        cryptosToShow = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(isLoading = false,
                        cryptosToShow = result.data ?: emptyList())
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

    private fun updateCryptoLike(crypto: Crypto, isLiked: Boolean) {
        updateCryptoLikeUseCase(crypto, isLiked).onEach { result ->
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

    private fun updatePinCryptoLikeField(crypto: Crypto, isLiked: Boolean) {
        updatePinCryptoLikeFieldUseCase(crypto, isLiked).onEach { result ->
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

    private fun updateLikeCryptoPinField(crypto: Crypto, isPinned: Boolean) {
        updateLikeCryptoPinFieldUseCase(crypto, isPinned).onEach { result ->
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

    private fun unPinCrypto(cryptoId: Int) {
        unPinCryptoUseCase(cryptoId).onEach { result ->
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

    private fun getLikedCryptos() {
        getLikedCryptoListUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = state.value.copy(liked = result.data?: emptyList())
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