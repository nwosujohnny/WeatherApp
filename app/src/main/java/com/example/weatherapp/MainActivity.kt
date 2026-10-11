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
import kotlin.math.roundToInt

// Shared text colour.
private val Navy = Color(0xFF102C54)

// Opens the app and applies its theme.
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

// Converts Celsius to Fahrenheit when selected.
fun displayTemperature(celsius: Int, useFahrenheit: Boolean): String {
    return if (useFahrenheit) {
        val fahrenheit = (celsius * 9.0 / 5.0 + 32).roundToInt()
        "$fahrenheit°F"
    } else {
        "$celsius°C"
    }
}

// Displays the header, selected screen, and bottom navigation.
@Composable
fun WeatherApp() {
    // Preserves the screen and unit selection after rotation.
    var selectedScreen by rememberSaveable {
        mutableStateOf("Now")
    }

    var useFahrenheit by rememberSaveable {
        mutableStateOf(false)
    }

    val backgroundColour = Color(0xFFFAF8FE)
    val barColour = Color(0xFFDBE2FC)

    // Adds padding to keep content clear of the system bars.
    Scaffold(
        containerColor = barColour
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header with the location and temperature unit button.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(barColour)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "Halifax, Nova Scotia",
                        color = Navy,
                        fontSize = 24.sp
                    )

                    TextButton(
                        onClick = { useFahrenheit = !useFahrenheit }
                    ) {
                        Text(
                            text = if (useFahrenheit) {
                                "Switch to °C"
                            } else {
                                "Switch to °F"
                            },
                            color = Navy
                        )
                    }
                }
            }

            // Gives the selected weather screen the remaining space.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(backgroundColour)
                    .padding(horizontal = 20.dp)
            ) {
                if (selectedScreen == "Now") {
                    CurrentWeatherScreen(useFahrenheit)
                } else {
                    ForecastScreen(useFahrenheit)
                }
            }

            // Bottom navigation with clickable icons.
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

                        // Bold text identifies the selected screen.
                        Text(
                            text = screen,
                            color = Navy,
                            fontSize = 14.sp,
                            fontWeight = if (selectedScreen == screen) {
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

// Shows placeholder current weather in the selected unit.
@Composable
fun CurrentWeatherScreen(useFahrenheit: Boolean) {
    // Allows scrolling when the content exceeds the screen height.
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
            text = displayTemperature(16, useFahrenheit),
            color = Navy,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Feels like: ${displayTemperature(14, useFahrenheit)}",
            color = Navy,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Groups the high, low, and wind details.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "High: ${displayTemperature(18, useFahrenheit)}",
                    color = Navy,
                    fontSize = 18.sp
                )

                Text(
                    text = "Low: ${displayTemperature(10, useFahrenheit)}",
                    color = Navy,
                    fontSize = 18.sp
                )
            }

            Text(
                text = "Wind: SW 18 km/h",
                color = Navy,
                fontSize = 16.sp
            )
        }
    }
}

// Stores one forecast day's data; temperatures are Celsius numbers.
data class ForecastDay(
    val day: String,
    val condition: String,
    val high: Int,
    val low: Int,
    val image: Int,
    val summary: String
)

// Displays three days of placeholder weather.
@Composable
fun ForecastScreen(useFahrenheit: Boolean) {
    // Fixed sample dates, weather values, and drawable icons.
    val forecast = listOf(
        ForecastDay(
            day = "Saturday, Oct 10",
            condition = "Sunny",
            high = 18,
            low = 11,
            image = R.drawable.weather_sunny,
            summary = "Clear skies. Rain chance: 10%. " +
                    "Wind: SW 15 km/h. Humidity: 65%."
        ),
        ForecastDay(
            day = "Sunday, Oct 11",
            condition = "Partly cloudy",
            high = 17,
            low = 10,
            image = R.drawable.weather_partly_cloudy,
            summary = "A mix of sun and clouds. Rain chance: 19%. " +
                    "Wind: SW 15 km/h. Humidity: 15%."
        ),
        ForecastDay(
            day = "Monday, Oct 12",
            condition = "Cloudy",
            high = 15,
            low = 9,
            image = R.drawable.weather_cloudy,
            summary = "Overcast skies. Rain chance: 10%. " +
                    "Wind: SW 15 km/h. Humidity: 65%."
        )
    )

    // Arranges the heading and cards vertically with scrolling.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "3-Day Forecast",
            color = Navy,
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
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = day.image),
                        contentDescription = day.condition,
                        modifier = Modifier.size(56.dp)
                    )

                    // Uses the remaining card width for weather details.
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

                        // Formats both temperatures using the selected unit.
                        Text(
                            text = "High: ${
                                displayTemperature(day.high, useFahrenheit)
                            }   Low: ${
                                displayTemperature(day.low, useFahrenheit)
                            }",
                            fontSize = 14.sp
                        )

                        Text(
                            text = day.summary,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}