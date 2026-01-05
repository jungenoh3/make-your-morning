package com.nochunsam.makeyourmorning.pages.intro.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.common.compose.FirstPage
import com.nochunsam.makeyourmorning.common.compose.FourthPage
import com.nochunsam.makeyourmorning.common.compose.SecondPage
import com.nochunsam.makeyourmorning.common.compose.ThirdPage

@Composable
fun Intro(navController: NavHostController) {
    val pages: List<@Composable () -> Unit> = listOf(
        { FirstPage() },
        { SecondPage() },
        { ThirdPage() },
        { FourthPage() },
        // { FifthPage(navController)  }
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    CustomColumn {
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