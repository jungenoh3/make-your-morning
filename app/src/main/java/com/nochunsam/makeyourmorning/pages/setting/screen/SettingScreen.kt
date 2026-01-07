package com.nochunsam.makeyourmorning.pages.setting.screen

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import androidx.core.net.toUri
import com.nochunsam.makeyourmorning.utilities.user.FirebaseViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.nochunsam.makeyourmorning.utilities.database.DatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    firebaseViewModel: FirebaseViewModel,
    databaseViewModel: DatabaseViewModel,
    onNavigateToTutorial: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onBack: () -> Boolean
) {
    val context = LocalContext.current
    val showLoginButton by firebaseViewModel.showLoginButton.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showTruncateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firebaseViewModel.refreshLoginState()
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false }, // 다이얼로그 바깥 클릭 시 닫기
            title = { Text(text = "로그아웃") },
            text = { Text(text = "정말 로그아웃 하시겠습니까?\n로그아웃을 하면 정보가 백업되지 않습니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 4. 확인 클릭 시 실제 로그아웃 수행 및 다이얼로그 닫기
                        firebaseViewModel.logout()
                        showLogoutDialog = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false } // 취소 클릭 시 다이얼로그만 닫기
                ) {
                    Text("취소")
                }
            }
        )
    }

    if (showTruncateDialog) {
        AlertDialog(
            onDismissRequest = { showTruncateDialog = false },
            title = { Text(text = "데이터 전체 삭제") },
            text = { Text(text = "정말 모든 데이터를 삭제하시겠습니까?\n이 행동은 되돌릴 수 없습니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        databaseViewModel.clearAllData()
                        showTruncateDialog = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTruncateDialog = false } // 취소 클릭 시 다이얼로그만 닫기
                ) {
                    Text("취소")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("환경 설정") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
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
                        .padding(vertical = 20.dp, horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "사용자 아이콘")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text( if (showLoginButton) "비회원" else firebaseViewModel.getUserName(),
                        fontSize = 20.sp)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (showLoginButton) {
                                onNavigateToLogin()
                            } else {
                                showLogoutDialog = true
                            }
                        }
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                ) {
                    Text( if (showLoginButton) "로그인" else "로그아웃",
                        fontSize = 15.sp)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
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
                        .clickable { onNavigateToTutorial() }
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showTruncateDialog = true
                        }
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                ) {
                    Text("데이터 전체 삭제", fontSize = 15.sp, color = Color.Red)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }
        }
    }
}