package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme

private val Navy = Color(0xFF102C54)
private val Yellow = Color(0xFFFFD166)

// Opens the app.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherAppTheme {
                WeatherApp()
            }
        }
    }
}

// Displays the header, weather screen, and bottom navigation.
@Composable
fun WeatherApp() {
    var selectedScreen by rememberSaveable {
        mutableStateOf("Now")
    }

    val backgroundColour = Color(0xFFFAF8FE)
    val barColour = Color(0xFFDBE2FC)

    Scaffold(
        containerColor = barColour
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Full-width rectangular header.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(barColour)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Halifax, Nova Scotia",
                    color = Navy,
                    fontSize = 24.sp
                )
            }

            // Weather content fills the area between the bars.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(backgroundColour)
                    .padding(horizontal = 20.dp)
            ) {
                if (selectedScreen == "Now") {
                    CurrentWeatherScreen()
                } else {
                    ForecastScreen()
                }
            }

            // Full-width rectangular bottom navigation.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(barColour)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Now", "Daily").forEach { screen ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Clicking the icon changes the screen.
                        IconButton(
                            onClick = { selectedScreen = screen }
                        ) {
                            Image(
                                painter = painterResource(
                                    id = if (screen == "Now") {
                                        R.drawable.ic_now
                                    } else {
                                        R.drawable.ic_daily
                                    }
                                ),
                                contentDescription = "Show $screen weather",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Text(
                            text = screen,
                            color = Navy,
                            fontSize = 14.sp,
                            fontWeight =
                                if (selectedScreen == screen) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                        )
                    }
                }
            }
        }
    }
}

// Shows placeholder current weather.
@Composable
fun CurrentWeatherScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.weather_partly_cloudy
            ),
            contentDescription = "Partly cloudy",
            modifier = Modifier.size(150.dp)
        )

        Text(
            text = "Partly cloudy",
            color = Navy,
            fontSize = 24.sp
        )

        Text(
            text = "16°C",
            color = Navy,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Feels like: 14°C",
            color = Navy,
            fontSize = 20.sp
        )

        Text(
            text = "Wind: SW 18 km/h",
            color = Navy,
            fontSize = 18.sp
        )

        Text(
            text = "High: 18°C   Low: 10°C",
            color = Navy,
            fontSize = 16.sp
        )
    }
}

// Stores the information shown for one forecast day.
data class ForecastDay(
    val day: String,
    val condition: String,
    val temperatures: String,
    val image: Int
)

// Displays three days of placeholder forecast data.
@Composable
fun ForecastScreen() {
    val forecast = listOf(
        ForecastDay(
            "Saturday",
            "Sunny",
            "High: 18°C   Low: 11°C",
            R.drawable.weather_sunny
        ),
        ForecastDay(
            "Sunday",
            "Partly cloudy",
            "High: 17°C   Low: 10°C",
            R.drawable.weather_partly_cloudy
        ),
        ForecastDay(
            "Monday",
            "Cloudy",
            "High: 15°C   Low: 9°C",
            R.drawable.weather_cloudy
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "3-Day Forecast",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Creates one card for each forecast day.
        forecast.forEach { day ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Navy
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = day.image),
                        contentDescription = day.condition,
                        modifier = Modifier.size(56.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = day.day,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = day.condition)
                        Text(
                            text = day.temperatures,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}