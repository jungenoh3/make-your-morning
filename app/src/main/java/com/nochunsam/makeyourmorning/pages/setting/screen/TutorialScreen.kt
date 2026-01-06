package com.nochunsam.makeyourmorning.pages.setting.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.nochunsam.makeyourmorning.common.compose.CustomScaffold
import com.nochunsam.makeyourmorning.common.compose.FirstPage
import com.nochunsam.makeyourmorning.common.compose.FifthPageWithoutNavController
import com.nochunsam.makeyourmorning.common.compose.FourthPage
import com.nochunsam.makeyourmorning.common.compose.SecondPage
import com.nochunsam.makeyourmorning.common.compose.ThirdPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(onBack: () -> Boolean) {
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
    CustomScaffold(
        title = "사용 설명",
        onBack = {
            onBack()
        }
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
