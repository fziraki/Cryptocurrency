package com.example.cryptocurrency.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptocurrency.R
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.presentation.crypto_list.CryptoListViewModel

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.favorite_list), fontSize = 32.sp)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    onItemClick: (Crypto) -> Unit,
    viewModel: CryptoListViewModel = hiltViewModel()
) {

    val likedState = viewModel.likedState.collectAsStateWithLifecycle()

    LazyColumn(modifier) {
        items(likedState.value, key = {it.id}) {likedCrypto ->
            key(likedCrypto.isLiked) {
                key(likedCrypto.isPinned) {
                    CryptoListItem(
                        modifier = Modifier.animateItemPlacement(tween(durationMillis = 500)),
                        crypto = likedCrypto,
                        onItemPinClick = { toggledCrypto, isPinned ->
                            viewModel.onUiEvent(CryptoListViewModel.UiEvent.TogglePin(toggledCrypto, isPinned))
                        },
                        onItemLikeClick = { toggledCrypto, isLiked ->
                            viewModel.onUiEvent(CryptoListViewModel.UiEvent.ToggleLike(toggledCrypto, isLiked))
                        }
                    )
                }
            }
        }
    }
}