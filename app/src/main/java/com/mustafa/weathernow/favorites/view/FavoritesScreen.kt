package com.mustafa.weathernow.favorites.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mustafa.weathernow.R
import com.mustafa.weathernow.utils.getWeatherIconRes

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun FavoritesScreen() {
    var data = 0
    val context = LocalContext.current

    Box {
        AddToFavoriteFAP(Modifier.align(Alignment.BottomStart))

        if (data > 0) {
            FavoriteLocations()
        } else {
            Image(
                modifier = Modifier.size(100.dp)
                    .align(Alignment.Center),
                alignment = Alignment.Center,
                painter = rememberDrawablePainter(
                    drawable = context.getDrawable(R.drawable.animated_app_icon)
                ),
                contentDescription = stringResource(R.string.weather_state)
            )
        }
    }
}

@Composable
fun FavoriteLocations() {
    LazyColumn() {

    }
}

@Composable
fun AddToFavoriteFAP(modifier: Modifier) {
    val context = LocalContext.current
    val text = stringResource(R.string.add_favorite_location)
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT
            ).show()
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_favorite_location)
        )
    }
}