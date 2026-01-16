package com.julianjesacher.nextbreak.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme

@Composable
fun BaseScreen(
    onInfoClick: () -> Unit,
    onRetryClick: () -> Unit,
    onUpdateClick: () -> Unit,
    showUpdateButton: Boolean,
    refreshButtonText: String?,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(end = 10.dp, start = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(showUpdateButton) {
                TextButton(onClick = onUpdateClick) {
                    Text(
                        text = "Update",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 21.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.SystemUpdate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if(refreshButtonText != null) {
                TextButton(onClick = onRetryClick) {
                    Text(
                        text = refreshButtonText,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 21.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            IconButton(onClick = onInfoClick) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
        Text(
            text = "Next Break",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 55.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .statusBarsPadding()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 120.dp),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    NextBreakTheme {
        BaseScreen(
            onInfoClick = {},
            onRetryClick = {},
            onUpdateClick = {},
            showUpdateButton = true,
            refreshButtonText = "No Internet"
        ) { }
    }
}