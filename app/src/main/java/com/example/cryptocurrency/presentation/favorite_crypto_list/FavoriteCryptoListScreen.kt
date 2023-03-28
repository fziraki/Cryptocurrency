package com.example.cryptocurrency.presentation.favorite_crypto_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptocurrency.presentation.components.CryptoListItem
import kotlinx.coroutines.launch

@Composable
fun FavoriteCryptoListScreen(
    viewModel: FavoriteCryptoListViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val eventFlow = viewModel.eventFlow

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState
    ) {

        LaunchedEffect(true){
            eventFlow.collect { uiEvent ->
                when(uiEvent) {
                    is FavoriteCryptoListViewModel.UiEvent.ShowSnackbar -> {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = uiEvent.message
                            )
                        }
                    }
                    else -> {}
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            if(state.isLoading) {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(4.dp)
                )
            }

            if (state.cryptosToShow.isNotEmpty()){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( top = 20.dp, start = 20.dp, end = 20.dp, bottom = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier.weight(1.5f),
                        text = "currency",
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray
                    )

                    Text(
                        modifier = Modifier.weight(1.5f),
                        text = "last price",
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = "changes",
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray
                    )

                    IconButton(modifier = Modifier.weight(1f), onClick = {}){}
                    IconButton(modifier = Modifier.weight(1f), onClick = {}){}

                }
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.cryptosToShow) { crypto ->
                    CryptoListItem(
                        crypto = crypto,
                        onItemPinClick = { toggledCrypto, isPinned ->
                            viewModel.onUiEvent(FavoriteCryptoListViewModel.UiEvent.TogglePin(toggledCrypto, isPinned))
                        },
                        onItemLikeClick = { toggledCrypto, isLiked ->
                            viewModel.onUiEvent(FavoriteCryptoListViewModel.UiEvent.ToggleLike(toggledCrypto, isLiked))
                        }
                    )
                }
            }

        }

    }

}
