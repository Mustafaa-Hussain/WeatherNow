package com.mustafa.weathernow.settings.view

import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafa.weathernow.R

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    )
    {
        LocationFinder()
        Language()
        MeasurementsUnits()
    }
}


@Composable
fun LocationFinder() {
    val radioOptions = listOf(stringResource(R.string.gps), stringResource(R.string.map))
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
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
                            onClick = { onOptionSelected(text) },
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


@Composable
fun Language() {
    val radioOptions =
        listOf(
            stringResource(R.string.english),
            stringResource(R.string.arabic),
            stringResource(R.string.default_lang)
        )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            radioOptions.forEach { text ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
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

@Composable
fun MeasurementsUnits() {
    val radioOptions = listOf("Default system", "Imperial system", "Metric system")
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
                text = "Measurements Units",
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
                            onClick = { onOptionSelected(text) },
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
                            text = "Temperature Unit: ${
                                optionTempUnitsInfo[
                                    radioOptions.indexOf(text)
                                ]
                            }"
                        )
                        Text(
                            text = "Wind speed Unit: ${
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