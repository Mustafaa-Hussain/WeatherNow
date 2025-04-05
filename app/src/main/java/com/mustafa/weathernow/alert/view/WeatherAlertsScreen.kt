package com.mustafa.weathernow.alert.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mustafa.weathernow.R
import com.mustafa.weathernow.alert.recever.AlarmReceiver
import com.mustafa.weathernow.alert.view_model.WeatherAlertsViewModel
import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.utils.AlarmBroadcastReceiverConstants.ALARM_ACTION
import com.mustafa.weathernow.utils.AlarmBroadcastReceiverConstants.ALARM_ID
import com.mustafa.weathernow.utils.AlarmBroadcastReceiverConstants.LATITUDE
import com.mustafa.weathernow.utils.AlarmBroadcastReceiverConstants.LONGITUDE
import com.mustafa.weathernow.utils.GeoCoderHelper
import com.mustafa.weathernow.utils.NavigationRoute
import com.mustafa.weathernow.utils.dateFormater
import com.mustafa.weathernow.utils.dateTimeFormater
import com.mustafa.weathernow.utils.formatAsTimeInMillis
import com.mustafa.weathernow.utils.timeFormater
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WeatherAlertsScreen(
    navController: NavController,
    weatherAlertsViewModel: WeatherAlertsViewModel
) {
    val context = LocalContext.current
    val alerts = weatherAlertsViewModel.alerts.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val alertsDeletionState = weatherAlertsViewModel.deletionStatus.collectAsStateWithLifecycle()
    val alertsUndoState = weatherAlertsViewModel.undoStatus.collectAsStateWithLifecycle()

    var removedItem by remember { mutableStateOf<AlertLocation?>(null) }

    LaunchedEffect(alertsDeletionState.value) {
        //un-schedule alarm
    }


    Box(Modifier.fillMaxSize()) {
        if (alerts.value.isNotEmpty()) {
            Alerts(
                alerts.value, {
                    navController.navigate(
                        NavigationRoute.WeatherPreviewScreen(
                            it.longitude,
                            it.latitude
                        )
                    )
                }) { location ->
                removedItem = location
                weatherAlertsViewModel.deleteAlert(location)

                coroutineScope.launch {
                    val actionResult = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_deleted),
                        actionLabel = context.getString(R.string.undo),
                        duration = SnackbarDuration.Short
                    )

                    if (actionResult == SnackbarResult.ActionPerformed && removedItem != null) {
                        weatherAlertsViewModel.undoAlert(removedItem)
                        removedItem = null
                    }

                    if (removedItem != null) {
                        //cancel alarm
                        val alarmIntent = Intent(
                            context,
                            AlarmReceiver::class.java
                        ).apply {
                            action = ALARM_ACTION
                            putExtra(ALARM_ID, removedItem?.id?.toInt())
                            putExtra(LONGITUDE, removedItem?.longitude)
                            putExtra(LATITUDE, removedItem?.latitude)
                        }
                        val pendingIntent =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
                                PendingIntent.getBroadcast(
                                    context,
                                    removedItem?.id?.toInt() ?: -1,
                                    alarmIntent,
                                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
                                )
                            } else { // Below Android 12
                                PendingIntent.getBroadcast(
                                    context,
                                    removedItem?.id?.toInt() ?: -1,
                                    alarmIntent,
                                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                            }

                        if (context.getAlarmManager() != null) {
                            context.getAlarmManager()?.cancel(pendingIntent);
                            Toast.makeText(
                                context,
                                context.getString(R.string.alarm_cancelled)
                                        + " ${removedItem?.id?.toInt() ?: -1}",
                                Toast.LENGTH_SHORT
                            ).show();
                        }
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
                    drawable = context.getDrawable(R.drawable.animated_alarm)
                ),
                contentDescription = stringResource(R.string.weather_state)
            )
        }

        Column(
            Modifier.align(Alignment.BottomEnd)
        ) {
            AddToAlertsFAB(
                navController,
                weatherAlertsViewModel,
                Modifier.align(Alignment.End)
            )

            SnackbarHost(hostState = snackbarHostState)
        }

    }
}


@Composable
fun Alerts(
    alert: List<AlertLocation>,
    onItemClick: (AlertLocation) -> Unit,
    onDelete: (AlertLocation) -> Unit
) {
    LazyColumn {
        items(items = alert, key = { it.id }) { alert ->
            AlertItem(
                alert,
                onItemClick,
                onDelete
            )
        }
    }
}

@Composable
fun AlertItem(
    alert: AlertLocation,
    onItemClick: (AlertLocation) -> Unit,
    onDelete: (AlertLocation) -> Unit
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(swipeToDismissBoxState.currentValue) {
        if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            coroutineScope.launch {
                swipeToDismissBoxState.reset()
                onDelete(alert)
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
                    onClick = { onItemClick(alert) }
                ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (alert.alertType == "notification") {
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.Default.Notifications,
                        contentDescription = stringResource(R.string.notification)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_alarm),
                        contentDescription = stringResource(R.string.alert),
                        modifier = Modifier.weight(1f)
                    )
                }
                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    Text(
                        text = GeoCoderHelper(LocalContext.current)
                            .getCityName(
                                alert.latitude,
                                alert.longitude
                            ) ?: "",
                    )
                    Text(text = (alert.startTime/1000).dateTimeFormater())
                }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToAlertsFAB(
    navController: NavController,
    weatherAlertsViewModel: WeatherAlertsViewModel,
    modifier: Modifier = Modifier
) {
    val alertsSaveState = weatherAlertsViewModel.saveStatus.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    weatherAlertsViewModel.getTempLocation()
    val tempAlertLocation = weatherAlertsViewModel.tempLocation.collectAsStateWithLifecycle()
    val lastAddedAlertId = weatherAlertsViewModel.lastAddedAlertId.collectAsStateWithLifecycle()
    val lastAddedAlert = rememberSaveable { mutableStateOf<AlertLocation?>(null) }

    LaunchedEffect(alertsSaveState.value) {
        if (lastAddedAlert.value != null) {
            if (alertsSaveState.value) {
                scheduleAlert(
                    context,
                    lastAddedAlertId = lastAddedAlertId.value,
                    alert = lastAddedAlert.value,
                )
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_to_save_location),
                    Toast.LENGTH_SHORT
                ).show()
            }
            lastAddedAlert.value = null
            weatherAlertsViewModel.resetState()

            showBottomSheet = false
            weatherAlertsViewModel.resetTempLocation()
        }
    }

    FloatingActionButton(
        modifier = modifier.padding(16.dp),
        onClick = { showBottomSheet = true },
        content = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.add_to_alerts)
            )
        }
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                weatherAlertsViewModel.resetTempLocation()
            },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                navController,
                tempAlertLocation.value
            ) {

                //save alert location
                weatherAlertsViewModel.saveAlert(it)
                lastAddedAlert.value = it
            }
        }
    }
}

fun scheduleAlert(context: Context, lastAddedAlertId: Int, alert: AlertLocation?) {
    if (alert == null) return

    val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
        action = ALARM_ACTION
        putExtra(ALARM_ID, lastAddedAlertId)
        putExtra(LONGITUDE, alert.longitude)
        putExtra(LATITUDE, alert.latitude)
    }
    val pendingIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            PendingIntent.getBroadcast(
                context,
                lastAddedAlertId,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else { // Below Android 12
            PendingIntent.getBroadcast(
                context,
                lastAddedAlertId,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

    // pass calender trigger alarm time
    context.setExactAlarm(alert.startTime * 1000, pendingIntent)
}

fun Context.setExactAlarm(
    triggerAtMillis: Long,
    operation: PendingIntent?,
    type: Int = AlarmManager.RTC_WAKEUP,
) {
    val currentTime = Calendar.getInstance().timeInMillis
    if (triggerAtMillis <= currentTime) {
        Toast.makeText(
            this,
            getString(R.string.it_is_not_possible_to_set_alarm_in_the_past),
            Toast.LENGTH_SHORT
        ).show()
        return
    }

    if (operation == null) {
        Log.i("```TAG```", "operation is null")
        return
    }

    val manager = getAlarmManager()
    manager?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || it.canScheduleExactAlarms()) {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                it,
                type,
                triggerAtMillis,
                operation
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    navController: NavController,
    tempAlertLocation: Pair<Double, Double>,
    onSaveAlert: (AlertLocation) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val context = LocalContext.current
        var startTime = rememberSaveable { mutableStateOf(Triple(0, 0, false)) }
        var startDate = rememberSaveable { mutableLongStateOf(0L) }

        val alertOptions = listOf(
            "notification",
            "alert"
        )
        val radioOptions = listOf(
            stringResource(R.string.notification),
            stringResource(R.string.alert)
        )
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }


        LocationPickerSection(navController, tempAlertLocation)
        Spacer(Modifier.height(8.dp))
        TimePickerSection(startTime)
        DatePickerSection(startDate)
        Spacer(Modifier.height(8.dp))
        AlertTypeSection(radioOptions, selectedOption, onOptionSelected)
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (isValidInputData(
                        context,
                        tempAlertLocation,
                        startTime,
                        startDate
                    )
                ) {
                    onSaveAlert(
                        AlertLocation(
                            latitude = tempAlertLocation.first,
                            longitude = tempAlertLocation.second,
                            startTime = startTime.value.formatAsTimeInMillis(startDate.longValue),
                            alertType = alertOptions[radioOptions.indexOf(selectedOption)],
                        )
                    )
                }

            },
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save))
        }

        Spacer(Modifier.height(16.dp))
    }
}

private fun isValidInputData(
    context: Context,
    tempAlertLocation: Pair<Double, Double>,
    startTime: MutableState<Triple<Int, Int, Boolean>>,
    startDate: MutableLongState,
): Boolean {
    var isAllDataValid = true
    if (tempAlertLocation.first == 0.0 || tempAlertLocation.second == 0.0) {
        Toast.makeText(
            context,
            context.getString(R.string.please_select_location),
            Toast.LENGTH_SHORT
        ).show()
        isAllDataValid = false
    }
    if (startTime.value.first == 0 && startTime.value.second == 0) {
        Toast.makeText(
            context,
            context.getString(R.string.please_select_start_time),
            Toast.LENGTH_SHORT
        ).show()
        isAllDataValid = false
    }

    if (startTime.value.formatAsTimeInMillis(startDate.longValue) < Calendar.getInstance().timeInMillis) {
        Toast.makeText(
            context,
            context.getString(R.string.start_time_must_be_in_the_future),
            Toast.LENGTH_SHORT
        ).show()
        isAllDataValid = false
    }
    return isAllDataValid
}

@Composable
fun AlertTypeSection(
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Text(
        stringResource(R.string.alert_type),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    ),
                verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.onBackground,
                        unselectedColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSection(startDate: MutableState<Long>) {

    val currentTime = Calendar.getInstance()
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentTime.timeInMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= currentTime.timeInMillis
            }
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    //show date picker
                    showDatePicker = true
                }
            )
            .padding(16.dp)
    ) {
        Icon(
            modifier = Modifier.padding(end = 8.dp),
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = stringResource(R.string.start_date)
        )
        Text(
            text = "${stringResource(R.string.start_date)} : ",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = startDate.value.let {
                if (it > 0) {
                    it.dateFormater()
                } else stringResource(R.string.start_date)
            }
        )
    }
    if (showDatePicker) {
        StartDatePicker(datePickerState) { timeMillis ->
            startDate.value = timeMillis
            showDatePicker = false
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSection(startTime: MutableState<Triple<Int, Int, Boolean>>) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var isTimeSelected by rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    //show time picker
                    showTimePicker = true
                }
            )
            .padding(16.dp)
    ) {
        Icon(
            modifier = Modifier.padding(end = 8.dp),
            painter = painterResource(R.drawable.ic_time),
            contentDescription = stringResource(R.string.initial_time)
        )
        Text(
            text = "${stringResource(R.string.start_time)} : ",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = if (isTimeSelected) startTime.value.formatAsTimeInMillis(0).timeFormater()
            else stringResource(R.string.start_time)
        )
    }
    if (showTimePicker) {
        StartTimePicker(timePickerState) { hour, minute, isAfternoon ->
            startTime.value = Triple(hour, minute, isAfternoon)
            showTimePicker = false
        }
        isTimeSelected = true
    }
}

@Composable
fun LocationPickerSection(
    navController: NavController,
    tempAlertLocation: Pair<Double, Double>
) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    navController.navigate(
                        NavigationRoute
                            .MapLocationFinderScreen(
                                NavigationRoute.WEATHER_ALERT_SCREEN
                            )
                    )
                }
            )
            .padding(16.dp)
    ) {
        Icon(
            modifier = Modifier.padding(end = 8.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = stringResource(R.string.location)
        )

        Text(
            text = "${stringResource(R.string.location)} : ",
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = GeoCoderHelper(context)
                .getCityName(tempAlertLocation.first, tempAlertLocation.second).let {
                    if (it != null && it.isNotEmpty()) it else stringResource(R.string.select_location)
                }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDatePicker(datePickerState: DatePickerState, onConfirm: (Long) -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DatePicker(
                state = datePickerState,
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    onConfirm(
                        datePickerState.selectedDateMillis ?: 0
                    )
                }) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartTimePicker(
    timePickerState: TimePickerState,
    onConfirm: (Int, Int, Boolean) -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TimePicker(
                state = timePickerState,
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    onConfirm(
                        timePickerState.hour,
                        timePickerState.minute,
                        timePickerState.isAfternoon
                    )
                }) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }
}

fun Context.getAlarmManager() =
    getSystemService(Context.ALARM_SERVICE) as? AlarmManager