package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme

private val Navy = Color(0xFF102C54)
private val Blue = Color(0xFF246A9C)
private val Yellow = Color(0xFFFFD166)

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

// Holds the selected screen and displays the app layout.
@Composable
fun WeatherApp() {
    var selectedScreen by rememberSaveable {
        mutableStateOf("Current")
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Navy, Blue))
                )
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "WeatherApp",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "HALIFAX, NOVA SCOTIA",
                color = Color(0xFFD5E8F5),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons switch between the two screens.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("Current", "Forecast").forEach { screen ->
                    Button(
                        onClick = { selectedScreen = screen },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedScreen == screen) {
                                Yellow
                            } else {
                                Color(0xFF234B73)
                            },
                            contentColor = if (selectedScreen == screen) {
                                Navy
                            } else {
                                Color.White
                            }
                        )
                    ) {
                        Text(screen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Each screen receives the remaining available height.
            Box(modifier = Modifier.weight(1f)) {
                if (selectedScreen == "Current") {
                    CurrentWeatherScreen()
                } else {
                    ForecastScreen()
                }
            }
        }
    }
}

// Home screen: all weather values are placeholders.
@Composable
fun CurrentWeatherScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Friday, October 9",
            color = Color.White,
            fontSize = 18.sp
        )

        WeatherIllustration(
            cloudy = true,
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = "16°C",
            color = Color.White,
            fontSize = 68.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Partly cloudy",
            color = Color.White,
            fontSize = 22.sp
        )

        Text(
            text = "Feels like 14°C  •  High 18° / Low 10°",
            color = Color(0xFFD5E8F5),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        WeatherDetail(label = "Humidity", value = "72%")
        WeatherDetail(label = "Wind", value = "SW 18 km/h")
        WeatherDetail(label = "Sunrise", value = "7:20 AM")
        WeatherDetail(label = "Sunset", value = "6:40 PM")

        Text(
            text = "Sample weather data • For demonstration",
            color = Color(0xFFD5E8F5),
            fontSize = 12.sp
        )
    }
}

// Reusable card for a weather detail.
@Composable
fun WeatherDetail(label: String, value: String) {
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
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label)
            Text(
                text = value,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// One object represents one day's placeholder forecast.
data class DailyForecast(
    val day: String,
    val condition: String,
    val high: Int,
    val low: Int,
    val cloudy: Boolean
)

private val sampleForecast = listOf(
    DailyForecast("Saturday", "Sunny", 18, 11, false),
    DailyForecast("Sunday", "Partly cloudy", 17, 10, true),
    DailyForecast("Monday", "Cloudy", 15, 9, true),
    DailyForecast("Tuesday", "Sunny", 16, 8, false),
    DailyForecast("Wednesday", "Partly cloudy", 14, 7, true),
    DailyForecast("Thursday", "Sunny", 15, 9, false),
    DailyForecast("Friday", "Cloudy", 13, 6, true)
)

// Forecast screen: LazyColumn provides vertical scrolling.
@Composable
fun ForecastScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "7-day forecast",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Text(
                text = "Plan your week in Halifax",
                color = Color(0xFFD5E8F5)
            )
        }

        items(sampleForecast, key = { it.day }) { forecast ->
            ForecastCard(forecast)
        }

        item {
            Text(
                text = "Sample forecast • For demonstration",
                color = Color(0xFFD5E8F5),
                fontSize = 12.sp
            )
        }
    }
}

// Reusable forecast card.
@Composable
fun ForecastCard(forecast: DailyForecast) {
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherIllustration(
                cloudy = forecast.cloudy,
                modifier = Modifier.size(48.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = forecast.day,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Text(
                    text = forecast.condition,
                    fontSize = 13.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${forecast.high}°",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Text(
                    text = "${forecast.low}°",
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Draws placeholder weather imagery without external image files.
@Composable
fun WeatherIllustration(
    cloudy: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.semantics {
            contentDescription = if (cloudy) {
                "Cloudy weather illustration"
            } else {
                "Sunny weather illustration"
            }
        }
    ) {
        val width = size.width
        val height = size.height

        drawCircle(
            color = Yellow,
            radius = width * 0.27f,
            center = Offset(width * 0.45f, height * 0.42f)
        )

        if (cloudy) {
            val cloudColor = Color(0xFFDCE6F0)

            drawCircle(
                color = cloudColor,
                radius = width * 0.20f,
                center = Offset(width * 0.40f, height * 0.62f)
            )

            drawCircle(
                color = cloudColor,
                radius = width * 0.25f,
                center = Offset(width * 0.61f, height * 0.55f)
            )

            drawCircle(
                color = cloudColor,
                radius = width * 0.17f,
                center = Offset(width * 0.79f, height * 0.65f)
            )

            drawRect(
                color = cloudColor,
                topLeft = Offset(width * 0.40f, height * 0.62f),
                size = Size(width * 0.39f, height * 0.17f)
            )
        }
    }
}

// Optional: previews the app inside Android Studio.
@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    WeatherAppTheme {
        WeatherApp()
    }
}