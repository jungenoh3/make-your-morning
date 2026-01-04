package com.nochunsam.makeyourmorning.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTutorialPage(color: Color = MaterialTheme.colorScheme.background,
                 paddingValues: PaddingValues = PaddingValues(30.dp),
                       text1: String, text2: String?, imageId: Int, contentDescription: String
                 ) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(paddingValues)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text1, fontSize = 30.sp, textAlign = TextAlign.Center, lineHeight = 35.sp)
        Image(
            painter = painterResource(imageId),
            contentDescription = contentDescription
        )
        text2?.let { nonNullText ->
            Text(nonNullText, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}