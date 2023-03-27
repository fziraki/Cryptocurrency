package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.use_case.GetCryptoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val getCryptoListUseCase: GetCryptoListUseCase
): ViewModel() {

    private val _state = mutableStateOf(CryptoListState())
    val state: State<CryptoListState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }

    init {
        getCryptos()
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
}