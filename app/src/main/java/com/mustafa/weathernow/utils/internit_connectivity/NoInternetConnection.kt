package com.mustafa.weathernow.utils.internit_connectivity

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mustafa.weathernow.R

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun NoInternetConnectivity() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        Image(
            painter = rememberDrawablePainter(
                context.getDrawable(R.drawable.no_internet_conniction)
            ),
            contentDescription = stringResource(R.string.no_internet_connectivity)
        )
        Spacer(Modifier.height(8.dp))
        Text(text = stringResource(R.string.no_internet_connectivity))
    }
}