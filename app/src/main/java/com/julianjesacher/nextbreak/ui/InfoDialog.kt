package com.julianjesacher.nextbreak.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.ui.components.OpenUrlButton
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

@Composable
fun InfoDialog(viewModel: MainViewModel, appVersionText: String) {
    Dialog(onDismissRequest = {
        viewModel.setInfoDialog(false)
    }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 560.dp)

        ) {
            InfoDialogContent(viewModel, appVersionText)
        }
    }
}

@Composable
fun InfoDialogContent(viewModel: MainViewModel, appVersionLabel: String) {
    Box(
        modifier = Modifier
            .padding(0.dp)
    ) {
        IconButton(
            onClick = {
                viewModel.setInfoDialog(false)
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(30.dp)
            )
        }
    }

    Column(
        Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About this app",
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Created by Julian Jesacher",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = appVersionLabel,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.tertiaryContainer
        )
        Spacer(modifier = Modifier.height(20.dp))
        OpenUrlButton("Send feedback", Icons.Default.Feedback, AppConstants.FEEDBACK_URL)
        OpenUrlButton("View source code", Icons.Default.Code, AppConstants.SOURCE_CODE_URL)
    }
}

@Preview(showBackground = true)
@Composable
fun InfoWindowPreview() {
    NextBreakTheme {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            //InfoDialogContent(MainViewModel())
        }

    }
}