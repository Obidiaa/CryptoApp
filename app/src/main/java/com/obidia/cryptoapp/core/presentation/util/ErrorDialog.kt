package com.obidia.cryptoapp.core.presentation.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.obidia.cryptoapp.ui.theme.CryptoAppTheme
import com.obidia.cryptoapp.ui.theme.RobotoMono

@Composable
fun ErrorDialog(
    errorDataState: ErrorDataState? = null,
    txtButton: String = "Try",
    onClick: () -> Unit,
) {

    Dialog(
        onDismissRequest = {
            onClick.invoke()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(16.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) errorDataState?.imageDark
                        ?: 0 else errorDataState?.imageLight ?: 0
                ),
                contentDescription = ""
            )

            Text(
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = RobotoMono,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                text = stringResource(errorDataState?.message ?: 0)
            )

            Button(
                onClick = { onClick.invoke() }
            ) {
                Text(text = txtButton)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@PreviewLightDark
@Composable
fun ErrorDialogPreview() {
    CryptoAppTheme {
        ErrorDialog(onClick = {})
    }
}