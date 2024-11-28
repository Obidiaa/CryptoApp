package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.obidia.cryptoapp.core.presentation.util.CryptoDetailScreenRoute
import com.obidia.cryptoapp.core.presentation.util.CryptoListScreenRoute
import com.obidia.cryptoapp.core.presentation.util.Route
import com.obidia.cryptoapp.crypto.presentation.cryptolist.components.CryptoListItem
import com.obidia.cryptoapp.crypto.presentation.cryptolist.components.dataPreview
import com.obidia.cryptoapp.ui.theme.CryptoAppTheme
import com.obidia.cryptoapp.ui.theme.RobotoMono
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.cryptoListScreenRoute(navigate: (Route) -> Unit) {
    composable<CryptoListScreenRoute> {
        val viewModel = koinViewModel<CryptoListViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        CoinListScreen(
            uiState = state,
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceBright),
            navigate
        )
    }
}

@Composable
fun CoinListScreen(
    uiState: CryptoListState,
    modifier: Modifier = Modifier,
    navigate: (Route) -> Unit
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillParentMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier.height(
                            WindowInsets.statusBars
                                .asPaddingValues()
                                .calculateTopPadding()
                        )
                    )

                    Box(
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Cryptocurrency".uppercase(),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = RobotoMono,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        )
                    }
                }
            }
            items(items = uiState.cryptoList) {
                CryptoListItem(data = it, modifier = Modifier.fillMaxWidth()) {
                    navigate(CryptoDetailScreenRoute(it.id))
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.systemBars.asPaddingValues(LocalDensity.current)
                            .calculateBottomPadding()
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCoinListScreen() {
    CryptoAppTheme {
        CoinListScreen(
            uiState = CryptoListState(
                cryptoList = (1..100).map {
                    dataPreview.copy(id = it.toString())
                }
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) { }
    }
}