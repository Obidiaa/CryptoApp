package com.obidia.cryptoapp.crypto.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.obidia.crypto.core.presentation.util.getDrawableIdForCoin
import com.obidia.cryptoapp.core.presentation.util.DisplayableNumber
import com.obidia.cryptoapp.core.presentation.util.toDisplayableNumber
import com.obidia.cryptoapp.crypto.presentation.CryptoItemUi
import com.obidia.cryptoapp.ui.theme.CryptoAppTheme
import com.obidia.cryptoapp.ui.theme.RobotoMono

@Composable
fun CryptoListItem(
    data: CryptoItemUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = RoundedCornerShape(16.dp)
                )
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(data.iconRes),
                    contentDescription = "",
                    modifier = Modifier
                        .size(84.dp)
                        .padding(8.dp),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(84.dp)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = data.symbol,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = RobotoMono,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Text(
                    text = data.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = RobotoMono,
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(84.dp)
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "$ ${data.priceUsd.formatted}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = RobotoMono
                    )
                )

                ChangePrice(data = data.changePercent24Hr)
            }
        }
    }
}

@Composable
fun ChangePrice(
    modifier: Modifier = Modifier,
    data: DisplayableNumber,
) {
    val contentColor =
        if (data.value >= 0) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onError
    val backgroundColor =
        if (data.value >= 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error

    Row(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = if (data.value > 0)
                Icons.Default.KeyboardArrowUp
            else Icons.Default.KeyboardArrowDown,
            contentDescription = "",
            tint = contentColor
        )

        Text(
            modifier = Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterVertically),
            text = "${data.formatted} %",
            style = MaterialTheme.typography.labelMedium.copy(
                textAlign = TextAlign.Center,
                color = contentColor,
                fontFamily = RobotoMono
            )
        )
    }
}

@Composable
@PreviewLightDark
fun PreviewCryptoItemList() {
    CryptoAppTheme {
        CryptoListItem(
            data = dataPreview,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {}
    }
}

internal val dataPreview = CryptoItemUi(
    name = "Bitcoin",
    symbol = "BTC",
    id = "1",
    rank = 1,
    marketCapUsd = 12382829.21.toDisplayableNumber(),
    priceUsd = 392032.32.toDisplayableNumber(),
    changePercent24Hr = 0.1.toDisplayableNumber(),
    iconRes = getDrawableIdForCoin("BTC")
)