package com.example.cryptocurrency.presentation.crypto_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cryptocurrency.presentation.crypto_list.components.CryptoListItem
import kotlinx.coroutines.launch

@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoListViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val eventFlow = viewModel.eventFlow

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState
    ) {

        LaunchedEffect(true){
            eventFlow.collect { uiEvent ->
                when(uiEvent) {
                    is CryptoListViewModel.UiEvent.ShowSnackbar -> {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = uiEvent.message
                            )
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = "currency",
                    style = MaterialTheme.typography.body2,
                    color = Color.LightGray
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = "last price",
                    style = MaterialTheme.typography.body2,
                    color = Color.LightGray
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = "changes",
                    style = MaterialTheme.typography.body2,
                    color = Color.LightGray
                )


            }

            if(state.isLoading) {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(4.dp)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.cryptos) { crypto ->
                    CryptoListItem(
                        crypto = crypto,
                        onItemClick = {
                        }
                    )
                }
            }

        }



    }




}