package com.mustafa.weathernow.favorites.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mustafa.weathernow.R
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import com.mustafa.weathernow.favorites.view_model.FavoriteViewModel
import com.mustafa.weathernow.utils.GeoCoderHelper
import com.mustafa.weathernow.utils.NavigationRoute
import kotlinx.coroutines.launch

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun FavoritesScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel
) {
    val context = LocalContext.current
    val favoriteLocations = favoriteViewModel.favoriteLocations.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var removedItem by remember { mutableStateOf<FavoriteLocation?>(null) }

    Box(Modifier.fillMaxSize()) {
        if (favoriteLocations.value.isNotEmpty()) {
            FavoriteLocations(favoriteLocations.value, {
                navController.navigate(
                    NavigationRoute.WeatherPreviewScreen(
                        it.longitude,
                        it.latitude
                    )
                )
            }) { location ->
                removedItem = location
                favoriteViewModel.deleteFavoriteLocations(location)

                coroutineScope.launch {
                    val actionResult = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_deleted),
                        actionLabel = context.getString(R.string.undo),
                        duration = SnackbarDuration.Short
                    )

                    if (actionResult == SnackbarResult.ActionPerformed && removedItem != null) {
                        favoriteViewModel.undoFavoriteLocation(removedItem)
                        removedItem = null
                    }
                }
            }
        } else {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                alignment = Alignment.Center,
                painter = rememberDrawablePainter(
                    drawable = context.getDrawable(R.drawable.animated_location)
                ),
                contentDescription = stringResource(R.string.weather_state)
            )
        }

        Column(
            Modifier.align(Alignment.BottomEnd)
        ) {
            AddToFavoriteFAP(
                Modifier.align(Alignment.End)
            ) {
                navController.navigate(
                    NavigationRoute.MapLocationFinderScreen(NavigationRoute.FAVORITE_SCREEN)
                )
            }

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

@Composable
fun FavoriteLocations(
    favoriteLocations: List<FavoriteLocation>,
    onItemClick: (FavoriteLocation) -> Unit,
    onDelete: (FavoriteLocation) -> Unit
) {
    LazyColumn {
        items(items = favoriteLocations, key = { it.id }) { location ->
            FavoriteLocationItem(location, onItemClick, onDelete)
        }
    }
}

@Composable
fun FavoriteLocationItem(
    favoriteLocation: FavoriteLocation,
    onItemClick: (FavoriteLocation) -> Unit,
    onDelete: (FavoriteLocation) -> Unit
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(swipeToDismissBoxState.currentValue) {
        if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            coroutineScope.launch {
                swipeToDismissBoxState.reset()
                onDelete(favoriteLocation)
            }
        }
    }

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                DeleteBackground()
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp)
                .clickable(
                    onClick = { onItemClick(favoriteLocation) }
                ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = stringResource(R.string.location)
                )
                Text(
                    text = GeoCoderHelper(LocalContext.current)
                        .getCityName(
                            favoriteLocation.latitude,
                            favoriteLocation.longitude
                        ) ?: "",
                    modifier = Modifier.weight(4f),
                )
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.error, shape = MaterialTheme.shapes.large),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 24.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun AddToFavoriteFAP(modifier: Modifier = Modifier, onAddToFavorite: () -> Unit) {
    FloatingActionButton(
        modifier = modifier.padding(16.dp),
        onClick = onAddToFavorite
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_favorite_location)
        )
    }
}