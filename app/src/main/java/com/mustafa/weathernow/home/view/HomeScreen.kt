package com.mustafa.weathernow.home.view


import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mustafa.weathernow.BuildConfig
import com.mustafa.weathernow.R
import com.mustafa.weathernow.data.ResponseState
import com.mustafa.weathernow.data.pojos.Current
import com.mustafa.weathernow.data.pojos.DailyItem
import com.mustafa.weathernow.data.pojos.HourlyItem
import com.mustafa.weathernow.data.pojos.OneResponse
import com.mustafa.weathernow.home.view_model.HomeViewModel
import com.mustafa.weathernow.utils.WeatherImage
import com.mustafa.weathernow.utils.dateTimeFormater
import com.mustafa.weathernow.utils.dayFormater
import com.mustafa.weathernow.utils.format
import com.mustafa.weathernow.utils.timeFormater

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    isLocationPermissionGranted: Boolean,
    location: Location?
) {
    val weatherData = viewModel.weatherResponse.collectAsStateWithLifecycle()
    val isRefreshing = rememberSaveable { mutableStateOf(true) }
    val getData = rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(location, getData.value) {
        if (location != null && isLocationPermissionGranted) {
            viewModel.getWeatherData(
                location.longitude,
                location.latitude,
                BuildConfig.API_KEY,
                units = "metric"
            )
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            getData.value = !getData.value
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (weatherData.value) {
                is ResponseState.Failure -> {
                    val errorMsg = (weatherData.value as ResponseState.Failure).errorMessage
                    ShowErrorMessage(errorMsg)
                    isRefreshing.value = false
                }

                is ResponseState.Loading -> LoadingData()

                is ResponseState.Success -> {
                    val weatherData = (weatherData.value as ResponseState.Success).response
                    WeatherData(weatherData)
                    isRefreshing.value = false
                }
            }
        }
    }
}

@Composable
fun ShowErrorMessage(errorMsg: String) {
    Text(text = errorMsg, fontSize = 24.sp)
}

@Composable
fun LoadingData(modifier: Modifier = Modifier) {
//    CircularProgressIndicator()
}

@Composable
fun WeatherData(weatherData: OneResponse) {
    val city = Geocoder(LocalContext.current).getFromLocation(
        weatherData.lat ?: 0.0,
        weatherData.lon ?: 0.0,
        1
    )?.first()?.subAdminArea

    LocationData(city ?: "")

    TodayWeatherData(
        weatherData.current,
        context = LocalContext.current
    )

    Spacer(
        Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(2.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    )

    TodayHourlyData(weatherData.hourly)

    Spacer(
        Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(2.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    )

    NextDaysData(weatherData.daily)
}


@Composable
fun LocationData(location: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = stringResource(R.string.location)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = location, fontSize = 24.sp)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TodayWeatherData(currentData: Current?, context: Context) {
    if (currentData != null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.75f)
                .padding(all = 24.dp)
                .shadow(shape = MaterialTheme.shapes.medium, elevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = currentData.dt.dateTimeFormater(),
                        fontSize = 18.sp
                    )
                    Row(
                        modifier = Modifier.padding(top = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Text(
                                text =
                                    currentData.temp?.toInt().format(),
                                fontSize = 128.sp
                            )
                            Text(
                                text = stringResource(R.string.degrees_c),//todo get this from saved settings
                                fontSize = 24.sp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    GlideImage(
                        modifier = Modifier.size(100.dp),
                        model = WeatherImage.getWeatherImage(
                            currentData.weather?.first()?.icon ?: ""
                        ),
                        contentDescription = stringResource(R.string.weather_state)
                    )
//                    Image(
//                        modifier = Modifier.size(100.dp),
//                        painter = rememberDrawablePainter(
//                            drawable = context.getDrawable(
//                                R.drawable.sunny
//                            )
//                        ),
//                        contentDescription = stringResource(R.string.weather_state)
//                    )
                    Text(text = currentData.weather?.first()?.description ?: "", fontSize = 18.sp)
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(R.string.feels_like))
                    Text(
                        text = currentData.feelsLike?.toInt().format()
                    )
                    Text(text = stringResource(R.string.degrees_c))//todo get unit from setting file
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.sunrise_icon),
                        contentDescription = stringResource(R.string.sunrise)
                    )
                    Text(text = stringResource(R.string.sunrise))
                    Text(text = currentData.sunrise.timeFormater())
                    Icon(
                        painter = painterResource(R.drawable.sunset_icon),
                        contentDescription = stringResource(R.string.sunset)
                    )
                    Text(text = stringResource(R.string.sunset))
                    Text(text = currentData.sunset.timeFormater())
                }
            }
        }
        TodayExtraInfo(currentData)
    }
}

@Composable
fun TodayExtraInfo(currentData: Current) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TodayExtraInfoItem(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.hum_icon),
            title = stringResource(R.string.humidity),
            value = "${currentData.humidity.format()} %"
        )
        TodayExtraInfoItem(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.windy_icon),
            title = stringResource(R.string.wind_speed),
            value = "${currentData.windSpeed.format()} " +
                    stringResource(R.string.km_h)//todo get this type from setting file
        )
        TodayExtraInfoItem(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.pressure_icon),
            title = stringResource(R.string.pressure),
            value = "${currentData.pressure.format()} " + stringResource(R.string.hpa)
        )
        TodayExtraInfoItem(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.clouds_icon),
            title = stringResource(R.string.clouds),
            value = "${currentData.clouds.format()} %"
        )
    }
}

@Composable
fun TodayExtraInfoItem(modifier: Modifier, painter: Painter, title: String, value: String) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = Color.White)
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painter,
                contentDescription = title,
                tint = Color.White
            )
            Text(text = value, color = Color.White)
        }
    }
}


@Composable
fun TodayHourlyData(hourlyData: List<HourlyItem?>?) {
    if (!hourlyData.isNullOrEmpty()) {
        Text(
            text = stringResource(R.string.next_24_h_temperatures),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        LazyRow(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            items(hourlyData.size) {
                HourlyRowItem(hourlyData[it])
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyRowItem(hourlyItem: HourlyItem?) {
    if (hourlyItem != null) {
        Card(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .width(100.dp)
                .height(140.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = hourlyItem.dt.timeFormater())
                GlideImage(
                    modifier = Modifier.size(50.dp),
                    model = WeatherImage.getWeatherImage(hourlyItem.weather?.first()?.icon ?: ""),
                    contentDescription = hourlyItem.weather?.first()?.description
                )
                Text(
                    text = "${
                        hourlyItem.temp?.toInt().format()
                    } " + stringResource(R.string.degrees_c),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = hourlyItem.weather?.first()?.description ?: "",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun NextDaysData(dailyData: List<DailyItem?>?) {
    if (!dailyData.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dailyData.forEachIndexed { index, it ->
                if (index != 0)
                    DailyColumItem(it)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyColumItem(dayData: DailyItem?) {
    val isOpened = rememberSaveable { mutableStateOf(false) }
    val rotateState = animateFloatAsState(
        targetValue = if (isOpened.value) 180f else 0f
    )

    if (dayData != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                isOpened.value = !isOpened.value
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = dayData.dt.dayFormater(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Bold
                )


                GlideImage(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f),
                    model = WeatherImage.getWeatherImage(dayData.weather?.first()?.icon ?: ""),
                    contentDescription = dayData.weather?.first()?.description
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = "${dayData.temp?.max?.toInt().format()} / ${
                        dayData.temp?.min?.toInt().format()
                    }" + stringResource(
                        R.string.degrees_c
                    )
                )
                IconButton(
                    onClick = { isOpened.value = !isOpened.value },
                    modifier = Modifier.rotate(rotateState.value)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }
            if (isOpened.value) {
                NextDayExtraData(dayData)
            }
        }
    }
}

@Composable

fun NextDayExtraData(dayData: DailyItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.sunrise_icon),
                contentDescription = stringResource(R.string.sunrise)
            )
            Text(text = stringResource(R.string.sunrise))
            Text(text = dayData.sunrise.timeFormater())

            Spacer(
                Modifier
                    .padding(4.dp)
                    .width(1.dp)
                    .height(20.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
            )

            Icon(
                painter = painterResource(R.drawable.sunset_icon),
                contentDescription = stringResource(R.string.sunset)
            )
            Text(text = stringResource(R.string.sunset))
            Text(text = dayData.sunset.timeFormater())
        }

        Spacer(
            Modifier
                .padding(horizontal = 36.dp, vertical = 4.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.medium
                )
        )


        Row(
            Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.hum_icon),
                contentDescription = stringResource(R.string.humidity),
                tint = Color.White
            )
            Text(text = "${dayData.humidity.format()} %")

            Spacer(
                Modifier
                    .padding(4.dp)
                    .width(1.dp)
                    .height(20.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
            )

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.windy_icon),
                contentDescription = stringResource(R.string.wind_speed),
                tint = Color.White
            )
            Text(text = "${dayData.windSpeed.format()} " + stringResource(R.string.km_h))

            Spacer(
                Modifier
                    .padding(4.dp)
                    .width(1.dp)
                    .height(20.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
            )

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.pressure_icon),
                contentDescription = stringResource(R.string.pressure),
                tint = Color.White
            )
            Text(text = "${dayData.pressure.format()} " + stringResource(R.string.hpa))

            Spacer(
                Modifier
                    .padding(4.dp)
                    .width(1.dp)
                    .height(20.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
            )

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.clouds_icon),
                contentDescription = stringResource(R.string.clouds),
                tint = Color.White
            )
            Text(text = "${dayData.clouds.format()} %")

        }
    }
}