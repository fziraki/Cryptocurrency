package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptocurrency.presentation.components.CryptoListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CryptoListScreen(
    viewModel: CryptoListViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
){

    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refresh() })

    val state = viewModel.state.value
    val eventFlow = viewModel.eventFlow

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        scaffoldState = scaffoldState
    ) {

        LaunchedEffect(true){
            eventFlow.collect { uiEvent ->
                when(uiEvent) {
                    is CryptoListViewModel.UiEvent.ShowSnackbar -> {
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

        Box(modifier = Modifier.fillMaxWidth()) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            if(state.isLoading) {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(4.dp)
                    .padding(top = 2.dp)
                )
            }

            if (state.cryptosToShow.isNotEmpty()){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier.weight(1.5f),
                        text = "currency",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )

                    Text(
                        modifier = Modifier.weight(1.5f),
                        text = "last price",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )

                    Text(
                        modifier = Modifier.weight(1.5f),
                        text = "changes",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )

                    Button(
                        modifier = Modifier.weight(2f),
                        onClick = onButtonClick
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "favorite list",
                            style = MaterialTheme.typography.body2,
                            color = Color.DarkGray
                        )
                    }

                }
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = state.cryptosToShow, key = { it.id }, contentType = { it }){ crypto ->
                    val index = state.cryptosToShow.indexOf(crypto)
                    if (index >= state.cryptosToShow.size - 1 && !state.endReached && !state.isPagingLoading) {
                        viewModel.loadNextItems()
                    }
                    CryptoListItem(
                        crypto = crypto,
                        onItemPinClick = { toggledCrypto, isPinned ->
                            viewModel.onUiEvent(CryptoListViewModel.UiEvent.TogglePin(toggledCrypto, isPinned))
                        },
                        onItemLikeClick = { toggledCrypto, isLiked ->
                            viewModel.onUiEvent(CryptoListViewModel.UiEvent.ToggleLike(toggledCrypto, isLiked))
                        }
                    )
                }
                item {
                    if (state.isPagingLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }

    }

}
