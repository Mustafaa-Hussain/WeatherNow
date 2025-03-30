package com.mustafa.weathernow.settings.view

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mustafa.weathernow.R
import com.mustafa.weathernow.settings.view_model.SettingsViewModel
import com.mustafa.weathernow.utils.LocationFinder
import com.mustafa.weathernow.utils.NavigationRoute
import com.mustafa.weathernow.utils.NavigationRoute.MapSources

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
    askForLocationPermission: () -> Unit
) {
    val location = viewModel.locationFinder.collectAsStateWithLifecycle()
    val language = viewModel.language.collectAsStateWithLifecycle()
    val measurementSystem = viewModel.measurementSystem.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.getSettings() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    )
    {
        LocationFinder(navController, viewModel, location.value, askForLocationPermission)
        Language(viewModel, language.value)
        MeasurementsUnits(viewModel, measurementSystem.value)
    }
}


@Composable
fun LocationFinder(
    navController: NavController,
    viewModel: SettingsViewModel,
    location: String,
    askForLocationPermission: () -> Unit
) {
    val context = LocalContext.current
    val settingOptions = listOf("GPS", "Map")
    val radioOptions = listOf(stringResource(R.string.gps), stringResource(R.string.map))
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(location) {
        if (location.isNotEmpty())
            onOptionSelected(radioOptions[settingOptions.indexOf(location)])
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.location)
            )
            Text(
                text = stringResource(R.string.location_finder),
                Modifier.padding(8.dp),
                fontSize = 24.sp
            )
        }
        Spacer(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            radioOptions.forEach { text ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                viewModel.saveLocationFinder(
                                    settingOptions[radioOptions.indexOf(text)]
                                )
                                onOptionSelected(text)
                                if (text == radioOptions[radioOptions.lastIndex]) {
                                    //navigate to map screen
                                    navController.navigate(
                                        NavigationRoute.MapLocationFinderScreen(MapSources.SETTING_SCREEN)
                                    )
                                } else {
                                    askForLocationPermission
                                }
                            },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = text)
                }
            }
        }

    }
}

@Composable
fun Language(viewModel: SettingsViewModel, language: String) {
    val context = LocalContext.current

    val settingOptions = listOf("en", "ar", "device_lang")
    val radioOptions =
        listOf(
            stringResource(R.string.english),
            stringResource(R.string.arabic),
            stringResource(R.string.device_lang)
        )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }


    LaunchedEffect(language) {
        if (language.isNotEmpty()) {
            onOptionSelected(radioOptions[settingOptions.indexOf(language)])
            if (language != settingOptions.last()) {
                updateAppLocale(context, language)
            } else {
                resetAppLocale(context)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(R.drawable.language_icon),
                contentDescription = stringResource(R.string.location)
            )
            Text(
                text = stringResource(R.string.language),
                Modifier.padding(8.dp),
                fontSize = 24.sp
            )
        }

        Spacer(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            radioOptions.forEach { text ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                viewModel.saveAppLanguage(
                                    settingOptions[radioOptions.indexOf(text)]
                                )
                                onOptionSelected(text)
                            },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    Text(text = text)
                }
            }
        }
    }
}

private fun updateAppLocale(context: Context, language: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(language)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(
                language
            )
        )
    }
}


private fun resetAppLocale(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.getEmptyLocaleList()
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.getEmptyLocaleList()
        )
    }
}

@Composable
fun MeasurementsUnits(viewModel: SettingsViewModel, measurementSystem: String) {
    val settingOptions = listOf("default", "imperial", "metric")

    val radioOptions = listOf(
        stringResource(R.string._default),
        stringResource(R.string.imperial),
        stringResource(R.string.metric)
    )
    val optionTempUnitsInfo = listOf(
        stringResource(R.string.degree_k),
        stringResource(R.string.degree_f),
        stringResource(R.string.degree_c)
    )
    val optionWindSpeedUnitsInfo = listOf(
        stringResource(R.string.meter_sec),
        stringResource(R.string.mile_hour),
        stringResource(R.string.meter_sec)
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }


    LaunchedEffect(measurementSystem) {
        if (measurementSystem.isNotEmpty())
            onOptionSelected(radioOptions[settingOptions.indexOf(measurementSystem)])
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
    ) {
        Row {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(R.drawable.pressure_icon),
                contentDescription = stringResource(R.string.location)
            )
            Text(
                text = stringResource(R.string.measurements_units),
                Modifier.padding(8.dp),
                fontSize = 24.sp
            )
        }

        Spacer(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            radioOptions.forEach { text ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                viewModel.saveMeasurementSystem(
                                    settingOptions[radioOptions.indexOf(text)]
                                )
                                onOptionSelected(text)
                            },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        selected = (text == selectedOption),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .width(1.dp)
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                    Column {
                        Text(
                            text = text,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                        Text(
                            text = "${stringResource(R.string.temperature_unit)}: ${
                                optionTempUnitsInfo[
                                    radioOptions.indexOf(text)
                                ]
                            }"
                        )
                        Text(
                            text = "${stringResource(R.string.wind_speed_unit)}: ${
                                optionWindSpeedUnitsInfo[
                                    radioOptions.indexOf(text)
                                ]
                            }"
                        )
                    }
                }
            }
        }
    }
}