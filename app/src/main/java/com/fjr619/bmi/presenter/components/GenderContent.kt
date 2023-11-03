package com.fjr619.bmi.presenter.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fjr619.bmi.R

@Composable
fun GenderContent(modifier: Modifier = Modifier) {

    var rotation by rememberSaveable {
        mutableIntStateOf(45)
    }

    val (lastRotation, setLastRotation) = remember { mutableStateOf(0) } // this keeps last rotation
    var newRotation = lastRotation // newRotation will be updated in proper way
    val modLast =
        if (lastRotation > 0) lastRotation % 360 else 360 - (-lastRotation % 360) // last rotation converted to range [-359; 359]
    if (modLast != rotation) // if modLast isn't equal rotation retrieved as function argument it means that newRotation has to be updated
    {
        val backward =
            if (rotation > modLast) modLast + 360 - rotation else modLast - rotation // distance in degrees between modLast and rotation going backward
        val forward =
            if (rotation > modLast) rotation - modLast else 360 - modLast + rotation // distance in degrees between modLast and rotation going forward

        // update newRotation so it will change rotation in the shortest way
        newRotation = if (backward < forward) {
            // backward rotation is shorter
            lastRotation - backward
        } else {
            // forward rotation is shorter (or they are equal)
            lastRotation + forward
        }

        setLastRotation(newRotation)
    }

    val angle: Float by animateFloatAsState(
        targetValue = newRotation.toFloat(),
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing
        ), label = ""
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = rotation, label = ""
        ) { rotation ->
            val id = when (rotation) {
                45 -> R.drawable.man_svgrepo_com
                315 -> R.drawable.woman_svgrepo_com
                else -> R.drawable.other_people_svgrepo_com
            }

            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = id), contentDescription = ""
            )
        }


        Box(
            modifier = Modifier
                .width(130.dp)
                .height(120.dp),
            contentAlignment = Alignment.Center

        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .align(
                        Alignment.BottomCenter
                    ),
            ) {

                GenderIcon(
                    iconRotate = 315f,
                    idRes = R.drawable.woman_svgrepo_com,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                ) {
                    rotation = 315
                }

                GenderIcon(
                    iconRotate = 0f,
                    idRes = R.drawable.other_people_svgrepo_com,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                ) {
                    rotation = 0
                }

                GenderIcon(
                    iconRotate = 45f,
                    idRes = R.drawable.man_svgrepo_com,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                ) {
                    rotation = 45
                }

                Image(
                    modifier = Modifier
                        .size(65.dp)
                        .align(Alignment.Center)
                        .rotate(angle),
                    painter = painterResource(id = R.drawable.svg),
                    contentDescription = ""
                )
            }
        }
    }
}