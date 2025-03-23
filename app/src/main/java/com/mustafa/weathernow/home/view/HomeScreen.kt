package com.mustafa.weathernow.home.view


import android.content.res.Configuration
import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mustafa.weathernow.R
import com.mustafa.weathernow.main_screen.view.MainScreen
import org.intellij.lang.annotations.JdkConstants

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var location = rememberSaveable { mutableStateOf("Location name") }
    var day = rememberSaveable { mutableStateOf("Day, dd:mm:yyyy") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            Text(text = location.value, fontSize = 24.sp)
        }
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
                        text = day.value,
                        fontSize = 18.sp
                    )
                    Row(
                        modifier = Modifier.padding(top = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Text(
                                text = "29",
                                fontSize = 128.sp
                            )
                            Text(
                                text = stringResource(R.string.degrees_c),
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
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = rememberDrawablePainter(
                            drawable = context.getDrawable(
                                R.drawable.sunny
                            )
                        ),
                        contentDescription = stringResource(R.string.weather_state)
                    )
                    Text(text = "Sunny", fontSize = 18.sp)
                }
            }

            Column(
                Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(R.string.feels_like))
                    Text(text = "29")
                    Text(text = stringResource(R.string.degrees_c))
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
                    Text(text = "6:17")
                    Icon(
                        painter = painterResource(R.drawable.sunset_icon),
                        contentDescription = stringResource(R.string.sunset)
                    )
                    Text(text = stringResource(R.string.sunset))
                    Text(text = "5:59")
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.humidity), color = Color.White)
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.hum_icon),
                        contentDescription = stringResource(R.string.humidity),
                        tint = Color.White
                    )
                    Text(text = "10Km/h", color = Color.White)
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.wind_speed), color = Color.White)
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.windy_icon),
                        contentDescription = stringResource(R.string.wind_speed),
                        tint = Color.White
                    )
                    Text(text = "10Km/h", color = Color.White)
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.pressure), color = Color.White)
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.pressure_icon),
                        contentDescription = stringResource(R.string.pressure),
                        tint = Color.White
                    )
                    Text(text = "10Km/h", color = Color.White)
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.clouds), color = Color.White)
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.clouds_icon),
                        contentDescription = stringResource(R.string.clouds),
                        tint = Color.White
                    )
                    Text(text = "10Km/h", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreen()
}