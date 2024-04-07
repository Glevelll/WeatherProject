package com.example.jetpackproject

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerScreen() {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState {2}
    val focusManager = LocalFocusManager.current

    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.jci),
                contentDescription = "Image",
                modifier = Modifier.size(70.dp)
            )
        }

        TabRow(selectedTabIndex = pagerState.currentPage, modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                modifier = Modifier.height(40.dp),
                onClick = {
                    focusManager.clearFocus()
                    coroutineScope.launch { pagerState.scrollToPage(0) }
                }
            ) {
                Text(text = "Вход")
            }
            Tab(
                selected = pagerState.currentPage == 1,
                modifier = Modifier.height(40.dp),
                onClick = {
                    focusManager.clearFocus()
                    coroutineScope.launch { pagerState.scrollToPage(1) }
                }
            ) {
                Text(text = "Регистрация")
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> LoginPage()
                1 -> RegisterPage()
            }
        }

        TabRowDefaults.Indicator(color = Color.Blue, height = 2.dp)
    }
}