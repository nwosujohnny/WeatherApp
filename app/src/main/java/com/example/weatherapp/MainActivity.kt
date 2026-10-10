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


// Shared text color used throughout the app.
private val Navy = Color(0xFF102C54)


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
    // Remembers the selected screen, including after rotation.
    var selectedScreen by rememberSaveable {
        mutableStateOf("Now")
    }

    val backgroundColour = Color(0xFFFAF8FE)
    val barColour = Color(0xFFDBE2FC)

    // Provides the main layout and padding for system bars.
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
                // Displays the screen selected through bottom navigation.
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
                // Creates the two navigation icons and labels.
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
                        // Makes the selected screen's label bold.
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
// Centres the content and allows scrolling on smaller screens.
fun CurrentWeatherScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Loads the weather icon from res/drawable.
        Image(
            painter = painterResource(
                id = R.drawable.weather_partly_cloudy
            ),
            contentDescription = "Partly cloudy",
            modifier = Modifier.size(150.dp)
        )
        // Shows the sample condition, temperature, and feels-like value.
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

        Spacer(modifier = Modifier.height(8.dp))

        // Groups the high, low, and wind details together.
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
                    text = "High: 18°C",
                    color = Navy,
                    fontSize = 18.sp
                )

                Text(
                    text = "Low: 10°C",
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

// Stores the information shown for one forecast day.
data class ForecastDay(
    val day: String,
    val condition: String,
    val temperatures: String,
    val image: Int,
    val summary: String
)

// Displays three days of placeholder forecast data.
@Composable
fun ForecastScreen() {
    // Fixed sample dates and weather values; no live weather service.
    val forecast = listOf(
        ForecastDay(
            "Saturday, Oct 10",
            "Sunny",
            "High: 18°C   Low: 11°C",
            R.drawable.weather_sunny,
            "Clear skies. Rain chance: 10%. Wind: SW 15 km/h. Humidity: 65%."

        ),
        ForecastDay(
            "Sunday, Oct 11",
            "Partly cloudy",
            "High: 17°C   Low: 10°C",
            R.drawable.weather_partly_cloudy,
            "Unclear skies. Rain chance: 19%. Wind: SW 15 km/h. Humidity: 15%."

        ),
        ForecastDay(
            "Monday, Oct 12",
            "Cloudy",
            "High: 15°C   Low: 9°C",
            R.drawable.weather_cloudy,
            "Rain chance: 10%. Wind: SW 15 km/h. Humidity: 65%."
        )
    )
    // Arranges the forecast vertically and enables scrolling.
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
                    // Loads the icon assigned to this forecast day.
                    Image(
                        painter = painterResource(id = day.image),
                        contentDescription = day.condition,
                        modifier = Modifier.size(56.dp)
                    )
                    // Uses the remaining card width for the day's weather details.
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