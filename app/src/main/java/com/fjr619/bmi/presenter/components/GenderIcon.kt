package com.fjr619.bmi.presenter.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GenderIcon(
    modifier: Modifier = Modifier,
    @DrawableRes idRes: Int,
    iconRotate: Float = 0f,
    onClickChange: () -> Unit) {
    IconButton(
        onClick = onClickChange, modifier = modifier
            .rotate(iconRotate)
//            .align(Alignment.Center)
            .offset(x = 0.dp, y = -60.dp)

    ) {
        Image(
            painter = painterResource(id = idRes), contentDescription = "",
            modifier = Modifier.size(48.dp)
        )
    }
}