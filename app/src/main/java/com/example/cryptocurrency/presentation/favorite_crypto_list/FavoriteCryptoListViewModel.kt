package com.example.cryptocurrency.presentation.favorite_crypto_list

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
class FavoriteCryptoListViewModel @Inject constructor(
    private val getPinnedCryptoListUseCase: GetPinnedCryptoListUseCase,
    private val pinCryptoUseCase: PinCryptoUseCase,
    private val unPinCryptoUseCase: UnPinCryptoUseCase,
    private val getLikedCryptoListUseCase: GetLikedCryptoListUseCase,
    private val likeCryptoUseCase: LikeCryptoUseCase,
    private val unLikeCryptoUseCase: UnLikeCryptoUseCase
): ViewModel() {

    private val _state = mutableStateOf(FavoriteCryptoListState())
    val state: State<FavoriteCryptoListState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _pinned = MutableStateFlow<List<Crypto>>(emptyList())
    private val pinned = _pinned.asStateFlow()

    private val _liked = MutableStateFlow<List<Crypto>>(emptyList())
    private val liked = _liked.asStateFlow()


    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class TogglePin(val crypto: Crypto, val isPinned: Boolean) : UiEvent()
        data class ToggleLike(val crypto: Crypto, val isLiked: Boolean) : UiEvent()
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

    init {
        getFavoriteList()
        viewModelScope.launch {
            liked.collect { likedList ->
                likedList.map { crypto ->
                    crypto.isLiked = true
                    crypto.isPinned = pinned.value.any { it.id == crypto.id }
                    crypto
                }.sortedByDescending {
                    it.isPinned
                }.apply {
                    _state.value = state.value.copy(cryptosToShow = this)
                }
            }
        }
    }

    private fun getFavoriteList() {
        getPinnedCryptos()
        getLikedCryptos()
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