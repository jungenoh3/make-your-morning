package com.nochunsam.makeyourmorning.pages.setting.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nochunsam.makeyourmorning.common.compose.CustomScaffold
import com.nochunsam.makeyourmorning.utilities.user.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginOption(
    onNavigateToEmailLogin: () -> Unit,
    onBack: () -> Unit,
    onGoogleLoginClick: () -> Unit // 구글 로그인은 별도 설정 필요
) {
    CustomScaffold(
        title = "로그인 옵션",
        onBack = onBack
    ) {

            // 구글 로그인 버튼
            //        Button(
            //            onClick = onGoogleLoginClick,
            //            modifier = Modifier.fillMaxWidth(),
            //            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            //        ) {
            //            Text("Google로 계속하기", color = Color.Black)
            //        }
            //
            //        Spacer(modifier = Modifier.height(12.dp))

            // 이메일 로그인 버튼
            Button(
                onClick = onNavigateToEmailLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("이메일로 로그인")
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailLogin(
    viewModel: FirebaseViewModel, // 같은 그래프 내라면 공유 가능
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // 에러 발생 시 토스트 메시지 등 처리
    if (errorMessage != null) {
        Text(text = errorMessage!!, color = Color.Red)
    }

    CustomScaffold(
        title = "로그인",
        onBack = onBack
    ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 로그인 버튼
            Button(
                onClick = { viewModel.loginWithEmail(email, password, onLoginSuccess) },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White)
                else Text("로그인")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 회원가입 이동 버튼
            TextButton(
                onClick = onNavigateToSignup,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("계정이 없으신가요? 회원가입")
            }
        }
}

@Composable
fun Signup(
    viewModel: FirebaseViewModel,
    onSignupSuccess: () -> Unit, // 로그인 화면으로 이동
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

    CustomScaffold(
        title = "회원가입",
        onBack = onBack
    ) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("이름") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("이메일") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("비밀번호") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("비밀번호 확인") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (password == confirmPassword) {
                    viewModel.signUp(email, password, name, onSignupSuccess)
                } else {
                    // 비밀번호 불일치 처리 (Toast 등)
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입")
        }
        TextButton(onClick = onBack) { Text("취소") }
    }
}