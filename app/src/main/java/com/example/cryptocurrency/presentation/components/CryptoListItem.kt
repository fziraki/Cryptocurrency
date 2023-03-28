package com.example.cryptocurrency.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cryptocurrency.R
import com.example.cryptocurrency.domain.model.Crypto


@Composable
fun CryptoListItem(
    crypto: Crypto,
    onItemPinClick: (Crypto, Boolean) -> Unit,
    onItemLikeClick: (Crypto, Boolean) -> Unit
) {

    val isCryptoPinned = rememberSaveable { mutableStateOf(crypto.isPinned) }

    Row(
        modifier = Modifier
            .background(if (isCryptoPinned.value) Color.Gray.copy(alpha = 0.1f) else Color.Transparent)
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier.weight(1.5f),
            text = "${crypto.name}\n(${crypto.symbol})",
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.weight(1.5f),
            text = "${crypto.priceInUsdtToShow} $",
            style = MaterialTheme.typography.body1
        )

        Text(
            modifier = Modifier.weight(1f),
            text = crypto.changePercentToShow,
            color = if(crypto.changePercent.toDouble() > 0) Color.Green else Color.Red,
            style = MaterialTheme.typography.body1,
        )


        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {
                    isCryptoPinned.value = !isCryptoPinned.value
                    onItemPinClick(crypto, isCryptoPinned.value)
                }
        ) {
            Icon(
                painter = if (isCryptoPinned.value){
                    painterResource(id = R.drawable.ic_pin_filled)
                } else{
                    painterResource(id = R.drawable.ic_pin_border)
                },
                tint = Color.LightGray,
                contentDescription = "pin"
            )
        }


        val isCryptoLiked = rememberSaveable { mutableStateOf(crypto.isLiked) }

        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {
                isCryptoLiked.value = !isCryptoLiked.value
                onItemLikeClick(crypto, isCryptoLiked.value)
            }
        ) {
            Icon(
                painter = if (isCryptoLiked.value){
                    painterResource(id = R.drawable.ic_star_filled)
                } else{
                    painterResource(id = R.drawable.ic_star_border)
                },
                tint = Color.LightGray,
                contentDescription = "like"
            )
        }

    }
}