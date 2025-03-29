package com.mustafa.weathernow.map.view

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mustafa.weathernow.R
import com.mustafa.weathernow.map.data.pojo.SearchItem
import com.mustafa.weathernow.map.view_model.MapViewModel
import com.mustafa.weathernow.utils.GeoCoderHelper
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    navController: NavController,
    mapViewModel: MapViewModel,
    sourceScreen: String
) {
    val context = LocalContext.current
    val latitude = mapViewModel.latitude.collectAsStateWithLifecycle()
    val longitude = mapViewModel.longitude.collectAsStateWithLifecycle()

    var currentLocation by rememberSaveable {
        mutableStateOf<GeoPoint?>(
            GeoPoint(
                latitude.value,
                longitude.value
            )
        )
    }

    var isLocationSelected by rememberSaveable { mutableStateOf(false) }

    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.OpenTopo)
                    controller.setZoom(9.5)
                    controller.setCenter(currentLocation)
                    setMultiTouchControls(true)
                    minZoomLevel = 3.5
                    maxZoomLevel = 15.0

                    val marker = Marker(this)
                    marker.position = GeoPoint(latitude.value, longitude.value)
                    overlays.add(marker)

                    val myReceiver: MapEventsReceiver = object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            marker.position = p
                            currentLocation = p
                            isLocationSelected = true
                            return false
                        }

                        override fun longPressHelper(p: GeoPoint?): Boolean {
                            marker.position = p
                            currentLocation = p
                            isLocationSelected = true
                            return false
                        }

                    }

                    val overlayEvent = MapEventsOverlay(context, myReceiver)
                    overlays.add(overlayEvent)

                    overlays.add(overlayEvent)

                }
            },
            update = { view ->
                view.controller.setCenter(currentLocation)
            }
        )

        SearchSection(mapViewModel)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {

            Card {
                Text(
                    text = GeoCoderHelper(context).getCityName(
                        currentLocation?.latitude,
                        currentLocation?.longitude
                    ) ?: "",
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }

            Button(
                onClick = {
                    mapViewModel.saveLocation(currentLocation, sourceScreen)
                    Toast.makeText(
                        context,
                        context.getString(R.string.location_saved), Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = stringResource(R.string.save_location))
            }
        }
    }
}

@Composable
fun SearchSection(mapViewModel: MapViewModel) {
    val searchResults = mapViewModel.searchResults.collectAsStateWithLifecycle(listOf())
    var query by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 145.dp)
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .alpha(0.9f)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                mapViewModel.searchLocation(it)
                query = it
            },
            label = {
                Text(
                    stringResource(R.string.search),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            trailingIcon = {
                IconButton(onClick = {
                    query = ""
                    mapViewModel.searchLocation("")
                    focusManager.clearFocus()
                }) {
                    if(query.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.location),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        )


        if (searchResults.value.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                LazyColumn {
                    items(searchResults.value.size) {
                        SearchRowItem(searchResults.value[it])
                    }
                }
            }
        }
    }
}

@Composable
fun SearchRowItem(item: SearchItem) {//todo add click listener and put this location over map
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = stringResource(R.string.location),

            )
        Text(text = item.displayName)
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium
            )
    )
}