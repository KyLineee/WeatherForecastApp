package com.example.weatherforecast

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.data.WeatherModel
import com.example.weatherforecast.ui.theme.BlueLight
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

const val API_KEY = ""

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val daysList = remember {
                mutableStateOf(listOf<WeatherModel>())
            }
            val currentDay = remember {
                mutableStateOf(
                    WeatherModel(
                        "",
                        "",
                        "1",
                        "",
                        "",
                        "1",
                        "1",
                        "",
                    )
                )
            }
            val city = remember { mutableStateOf("Penza") }
            getResult(city.value, this, daysList)
            if (daysList.value.isNotEmpty()) currentDay.value = daysList.value[0]
            Image(
                painter = painterResource(id = R.drawable.weather),
                contentDescription = "im1",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                contentScale = ContentScale.FillBounds
            )

            Column {
                MainCard(currentDay, daysList, city, this@MainActivity)
                TabLayout(daysList, currentDay)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCard(
    currentDay: MutableState<WeatherModel>,
    daysList: MutableState<List<WeatherModel>>,
    city: MutableState<String>,
    context: Context
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val list = listOf(
        "Moscow",
        "Penza",
        "Saint-Petersburg",
        "Saratov",
        "Sochi",
        "Ryazan",
        "Nizhniy Novgorod",
        "Vladimir",
        "Volgograd",
        "Tver",
        "Astrakhan",
        "Murmansk"
    )
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(BlueLight),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = currentDay.value.time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https:" + currentDay.value.icon,
                        contentDescription = "im2",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Text(
                    text = currentDay.value.city,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Text(
                    text = if (currentDay.value.currentTemp.isNotEmpty()) currentDay.value.currentTemp else "${currentDay.value.maxTemp}/${currentDay.value.minTemp} C",
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = currentDay.value.condition,
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_searched),
                            contentDescription = "im3",
                            tint = Color.White
                        )
                    }
                    if (showDialog == true) {
                        citySelection(
                            list,
                            city,
                            { showDialog = false },
                            { showDialog = false }
                        )
                    }
                    Text(
                        text = currentDay.value.maxTemp + "/" + currentDay.value.minTemp,
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White
                    )
                    IconButton(
                        onClick = {
                            getResult(city.value, context, daysList)
                        }
                    )
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.cloud_sync),
                            contentDescription = "im3",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun citySelection(
    list: List<String>,
    city: MutableState<String>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .height(300.dp)
                .width(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                citySpinner(list, city)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(text = "Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) { Text("Confirm") }
                }
            }
        }
    }
}

@Composable
fun citySpinner(
    list: List<String>,
    city: MutableState<String>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        items(list) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(top = 3.dp)
                    .clickable {
                        city.value = item
                    },
                colors = CardDefaults.cardColors(BlueLight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = item,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun WeatherHour(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val list = ArrayList<WeatherModel>()
    val hourArray = JSONArray(hours)
    for (i in 0 until hourArray.length()) {
        val item = hourArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c"),
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                ""
            )
        )
    }

    return list
}

fun WeatherDay(response: String): List<WeatherModel> {
    if (response.isEmpty()) return listOf()
    val mainObj = JSONObject(response)
    val list = ArrayList<WeatherModel>()
    val city = mainObj.getJSONObject("location").getString("name")
    val days = mainObj.getJSONObject("forecast").getJSONArray("forecastday")
    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition").getString("text"),
                item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString()
            )
        )
    }

    list[0] = list[0].copy(
        time = mainObj.getJSONObject("current").getString("last_updated"),
        currentTemp = mainObj.getJSONObject("current").getString("temp_c"),
    )

    return list
}

@Composable
fun getList(list: List<WeatherModel>, currentDay: MutableState<WeatherModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            list
        ) { i, item ->
            ListItem(item, currentDay)
        }
    }
}

private fun getResult(city: String, context: Context, daysList: MutableState<List<WeatherModel>>) {
    val url = "https://api.weatherapi.com/v1/forecast.json" +
            "?key=$API_KEY" +
            "&q=$city" +
            "&days=7" +
            "&aqi=no" +
            "&alerts=no"

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            daysList.value = WeatherDay(response)
        },
        { error ->
        }
    )
    queue.add(stringRequest)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>) {
    val pagerState = rememberPagerState { 2 }
    val tabList = listOf("HOURS", "DAYS")
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Color.White
                )
            },
            containerColor = BlueLight
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1.0f),
            verticalAlignment = Alignment.Top
        ) { index ->
            val list = when (index) {
                0 -> WeatherHour(currentDay.value.hours)
                1 -> daysList.value
                else -> daysList.value
            }
            getList(list, currentDay)
        }
    }
}