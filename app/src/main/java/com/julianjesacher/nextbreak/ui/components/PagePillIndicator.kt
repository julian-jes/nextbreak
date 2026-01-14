package com.julianjesacher.nextbreak.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme

@Composable
fun PagePillIndicator(isSelected: Boolean, onClick: () -> Unit) {

    val width by animateDpAsState(
        targetValue = if (isSelected) 24.dp else 8.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "width"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = width, height = 8.dp)
                .clip(CircleShape)
                .background(
                    if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
        )
    }
}

@Preview(showBackground = false)
@Composable
fun PagePillIndicatorPreview() {
    NextBreakTheme {
        PagePillIndicator(false) { }
    }
}