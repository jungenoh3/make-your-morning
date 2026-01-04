package com.nochunsam.makeyourmorning.pages.tutorial.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.common.compose.CustomTutorialPage
import com.nochunsam.makeyourmorning.utilities.pref.FirstOpenManager
import kotlinx.coroutines.launch
import com.nochunsam.makeyourmorning.R

@Composable
fun FirstPage () {
    CustomTutorialPage(
        text1 = "아침을 만들어봅시다!",
        text2 = null,
        imageId = R.drawable.makeyourmorning_tip1,
        contentDescription = R.string.tip1.toString()
    )
}

@Composable
fun SecondPage() {
    CustomTutorialPage(
        text1 = "타이머를 키고, 휴대폰을 만지지 마세요!!",
        text2 = "알림을 클릭하거나 뒤로 가기를 하면 \n타이머도 꺼져요\n" +
                "타이머가 끝까지 가야만 횟수가 기록돼요",
        imageId = R.drawable.makeyourmorning_tip2,
        contentDescription = R.string.tip2.toString()
    )
}

@Composable
fun ThirdPage () {
    CustomTutorialPage(
        text1 = "앱을 꾹 누르면 바로가기를 할 수 있어요!",
        text2 = "특정 버전 이상의 앱만 가능해요",
        imageId = R.drawable.makeyourmorning_tip3,
        contentDescription = R.string.tip3.toString()
    )
}

@Composable
fun FourthPage() {
    CustomTutorialPage(
        text1 = "잠금화면 바로가기를 설정할 수 있어요!",
        text2 = "특정버전만 가능해요!\n" +
                "잠금화면 및 AOD > 잠금화면 편집 > 모드 및 루틴 추가를 통해 설정할 수 있어요",
        imageId = R.drawable.makeyourmorning_tip4,
        contentDescription = R.string.tip4.toString()
    )
}

@Composable
fun FifthPage (navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val manager = remember { FirstOpenManager(context) }

    val isFirstOpen by manager.isFirstOpen.collectAsState(initial = true)

    CustomColumn (
        paddingValues = PaddingValues(30.dp)
    ) {
        Text(text = "하루를 만든 것만으로도\n우린 충분히 멋져요!", fontSize = 30.sp, textAlign = TextAlign.Center, lineHeight = 35.sp)
        Box(modifier = Modifier.height(20.dp))
        Button(onClick = {
            scope.launch {
                if (isFirstOpen) {
                    manager.setFirstOpen()   // false로 변경
                }
                navController.navigate("main") {
                    popUpTo("main") {inclusive = true}
                }
            }
        }) {
            Text("시작합시다!", fontSize = 25.sp)
        }
    }
}