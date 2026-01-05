package com.nochunsam.makeyourmorning.pages.setting.screen

import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.common.compose.FirstPage
import com.nochunsam.makeyourmorning.common.compose.FifthPageWithoutNavController
import com.nochunsam.makeyourmorning.common.compose.FourthPage
import com.nochunsam.makeyourmorning.common.compose.SecondPage
import com.nochunsam.makeyourmorning.common.compose.ThirdPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tutorial(onBack: () -> Boolean) {
    val pages: List<@Composable () -> Unit> = listOf(
        { FirstPage() },
        { SecondPage() },
        { ThirdPage() },
        { FourthPage() },
        { FifthPageWithoutNavController()  }
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(title = { Text("환경 설정") }, actions = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기 버튼"
                    )
                }
            })
        }
    ) { innerPadding ->
        CustomColumn (
            paddingValues = innerPadding
        ) {
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { idx ->
                pages[idx]()
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
