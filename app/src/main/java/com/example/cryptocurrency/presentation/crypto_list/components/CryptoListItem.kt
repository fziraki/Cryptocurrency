package com.example.cryptocurrency.presentation.crypto_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cryptocurrency.domain.model.Crypto


@Composable
fun CryptoListItem(
    crypto: Crypto,
    onItemClick: (Crypto) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(crypto) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = "${crypto.name}\n(${crypto.symbol})",
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.weight(1f),
            text = "${crypto.priceInUsdtToShow} $",
            style = MaterialTheme.typography.body1
        )

        Text(
            modifier = Modifier.weight(1f),
            text = crypto.changePercentToShow,
            color = if(crypto.changePercent.toDouble() > 0) Color.Green else Color.Red,
            style = MaterialTheme.typography.body1,
        )


    }
}