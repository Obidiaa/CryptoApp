package com.obidia.cryptoapp.crypto.presentation.cryptodetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.obidia.crypto.core.presentation.util.getDrawableIdForCoin
import com.obidia.cryptoapp.R
import com.obidia.cryptoapp.core.presentation.util.CoinDetailScreenRoute
import com.obidia.cryptoapp.core.presentation.util.Route
import com.obidia.cryptoapp.core.presentation.util.toDisplayableNumber
import com.obidia.cryptoapp.crypto.presentation.cryptodetail.components.LineChart
import com.obidia.cryptoapp.crypto.presentation.cryptodetail.model.ChartStyle
import com.obidia.cryptoapp.ui.theme.CryptoAppTheme
import com.obidia.cryptoapp.ui.theme.RobotoMono
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun NavGraphBuilder.coinDetailScreen(navigate: (Route) -> Unit) {
    composable<CoinDetailScreenRoute> {
        val idCoin = it.toRoute<CoinDetailScreenRoute>().id
        val viewModel = koinViewModel<CryptoDetailViewModel>()

        LoadData(idCoin = idCoin, viewModel = viewModel)

        CoinDetailScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            id = idCoin,
            action = viewModel::action
        )
    }
}

@Composable
fun LoadData(
    idCoin: String,
    viewModel: CryptoDetailViewModel,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getDetailCoin(idCoin)
        viewModel.getHistoryCoin(idCoin = idCoin, interval = "1d")
    }
}

@Composable
fun CoinDetailScreen(
    state: CryptoDetailState,
    modifier: Modifier = Modifier,
    id: String,
    action: (CryptoDetailEvent) -> Unit
) {
    if (state.isCoinDetailLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (state.coinDetailUi == null) return

        val coin = state.coinDetailUi
        val isPositive = coin.changeLast24Hr.value > 0.0

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            Spacer(
                modifier = Modifier.height(
                    WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                )
            )

            Box(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = state.coinDetailUi.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontFamily = RobotoMono
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHighest,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(4.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = coin.iconRes
                        ),
                        contentDescription = coin.name,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Text(
                    text = coin.symbol,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.coinDetailUi.price.formatted} $",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier
                        .background(
                            color = if (isPositive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = state.coinDetailUi.changeLast24Hr.formatted,
                        color = if (isPositive) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = if (isPositive) ImageVector.vectorResource(R.drawable.ic_trending_up) else ImageVector.vectorResource(
                            R.drawable.ic_trending_down
                        ),
                        contentDescription = "",
                        tint = if (isPositive) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onError
                    )
                }
            }

            val listKindInterval = listOf("1d", "1w", "1m", "1y", "5y")
            val itemSelected = remember {
                mutableStateOf("1d")
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 16.dp)
            ) {
                items(
                    listKindInterval
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                itemSelected.value = it
                                action(CryptoDetailEvent.OnClickItem(idCoin = id, interval = it))
                            }
                            .background(
                                if (itemSelected.value == it) MaterialTheme.colorScheme.primary else Color.Transparent,
                                RoundedCornerShape(4.dp)
                            )
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (itemSelected.value == it) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }

            var selectedDataPoint by remember {
                mutableStateOf<DataPoint?>(null)
            }

            if (state.isCoinHistoryLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LineChart(
                    dataPoints = state.listDataPoint,
                    style = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLinesThicknessPx = 5f,
                        axisLinesThicknessPx = 5f,
                        labelFontSize = 14.sp,
                        verticalPadding = 8.dp,
                        horizontalPadding = 16.dp,
                        chartLineColorShadow = Color.Transparent
                    ),
                    visibleDataPointsIndices = state.listDataPoint.indices,
                    unit = "$",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f),
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = {
                        selectedDataPoint = it
                    },
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCoinDetail() {
    val coinHistoryRandomized = remember {
        (1..20).map {
            DataPoint(
                x = ZonedDateTime.now().plusHours(it.toLong()).hour.toFloat(),
                y = (Random.nextFloat() * 1000.0).toFloat(),
                xLabel = DateTimeFormatter
                    .ofPattern("ha M/d")
                    .format(ZonedDateTime.now().plusHours(it.toLong()))
            )
        }
    }

    CryptoAppTheme {
        CoinDetailScreen(
            state = CryptoDetailState(
                coinDetailUi = CryptoDetailUi(
                    "Bitcoin",
                    "BTC",
                    marketCap = 1203020.2f.toDouble().toDisplayableNumber(),
                    price = 1203020.2f.toDouble().toDisplayableNumber(),
                    changeLast24Hr = 1203020.2f.toDouble().toDisplayableNumber(),
                    iconRes = getDrawableIdForCoin("BTC")
                ), listDataPoint = coinHistoryRandomized
            ),
            id = "",
            action = {}
        )
    }
}