package com.nochunsam.makeyourmorning.pages.setting.screen

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(
    rootNavController: NavController
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("환경 설정") }, actions = {
                IconButton(onClick = {
                    rootNavController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기 버튼")
                }
            })
        }
    ) { paddingValue ->
        CustomColumn(paddingValues = paddingValue) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                ) {
                    Text("버전 1.0.0", fontSize = 15.sp)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { rootNavController.navigate("tutorial") }
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                ) {
                    Text("사용 설명", fontSize = 15.sp)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://open.kakao.com/o/skjMZ95h".toUri()
                            )
                            context.startActivity(intent)
                        }
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                ) {
                    Text("문의", fontSize = 15.sp)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }
        }
    }
}