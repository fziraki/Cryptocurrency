package com.example.cryptocurrency.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.example.cryptocurrency.common.pagerTabIndicatorOffset
import com.example.cryptocurrency.common.theme.CryptocurrencyAppTheme
import com.example.cryptocurrency.presentation.crypto_list.CryptoListScreen
import com.example.cryptocurrency.presentation.favorite_crypto_list.FavoriteCryptoListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptocurrencyAppTheme {
                TabLayout()
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout() {

    val pagerState = rememberPagerState(initialPage = 0)
    Column(
        modifier = Modifier.background(Color.Black)
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        "Cryptos",
        "Favorites",
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.DarkGray,
        contentColor = Color.LightGray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.LightGray
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) Color.LightGray else Color.Gray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState) {
    HorizontalPager(pageCount = 2, state = pagerState) { page ->
        when (page) {
            0 -> CryptoListScreen()
            1 -> FavoriteCryptoListScreen()
        }
    }
}