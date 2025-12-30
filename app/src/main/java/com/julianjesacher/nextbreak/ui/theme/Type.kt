package com.julianjesacher.nextbreak.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.julianjesacher.nextbreak.R

val Roboto: FontFamily = FontFamily(
    Font(R.font.roboto_thin, weight = FontWeight.W100),
    Font(R.font.roboto_extra_light, weight = FontWeight.W200),
    Font(R.font.roboto_light, weight = FontWeight.W300),
    Font(R.font.roboto_regular, weight = FontWeight.W400),
    Font(R.font.roboto_medium, weight = FontWeight.W500),
    Font(R.font.roboto_semi_bold, weight = FontWeight.W600),
    Font(R.font.roboto_bold, weight = FontWeight.W700),
    Font(R.font.roboto_extra_bold, weight = FontWeight.W800),
    Font(R.font.roboto_black, weight = FontWeight.W900),

    Font(R.font.roboto_thin_italic, weight = FontWeight.W100, FontStyle.Italic),
    Font(R.font.roboto_extra_light_italic, weight = FontWeight.W200, FontStyle.Italic),
    Font(R.font.roboto_light_italic, weight = FontWeight.W300, FontStyle.Italic),
    Font(R.font.roboto_italic, weight = FontWeight.W400, FontStyle.Italic),
    Font(R.font.roboto_medium_italic, weight = FontWeight.W500, FontStyle.Italic),
    Font(R.font.roboto_semi_bold_italic, weight = FontWeight.W600, FontStyle.Italic),
    Font(R.font.roboto_bold_italic, weight = FontWeight.W700, FontStyle.Italic),
    Font(R.font.roboto_extra_bold_italic, weight = FontWeight.W800, FontStyle.Italic),
    Font(R.font.roboto_black_italic, weight = FontWeight.W900, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400
    ),
    titleLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W700
    ),
    titleMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W600
    ),
    labelLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500
    )
)