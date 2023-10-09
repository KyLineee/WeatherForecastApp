package com.example.weatherforecast

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.ui.theme.BlueLight
import kotlinx.coroutines.launch
import org.json.JSONObject

const val API_KEY = ""

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this
            Image(
                painter = painterResource(id = R.drawable.weather),
                contentDescription = "im1",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                contentScale = ContentScale.FillBounds
            )
            Column {
                MainCard(context = context)
                TabLayout()
            }
        }
    }
}

@Composable
fun MainCard(context: Context) {
    val state = remember {
        mutableStateOf("Unknown")
    }
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
                        text = "7 Okt 2023 21:00",
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https://cdn.weatherapi.com/weather/64x64/night/113.png",
                        contentDescription = "im2",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Text(
                    text = "Penza",
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Text(
                    modifier = Modifier.clickable {
                        getResult("Penza", state, context)
                    },
                    text = "${state.value} C",
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = "Sunny",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_searched),
                            contentDescription = "im3",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "5C/-3C",
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White
                    )
                    IconButton(onClick = {

                    }) {
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

private fun getResult(city: String, state: MutableState<String>, context: Context) {
    val url = "https://api.weatherapi.com/v1/current.json" +
            "?key=$API_KEY&" +
            "q=$city" +
            "&aqi=no"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            state.value = obj.getJSONObject("current").getString("temp_c")
        },
        { error ->
        }
    )
    queue.add(stringRequest)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout() {
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
            indicator = {tabPositions ->
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
            modifier = Modifier.weight(1.0f)
        ) {index ->

        }

    }
}