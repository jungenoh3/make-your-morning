package com.nochunsam.makeyourmorning.pages.setting.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun LoginOptionScreen (
    onNavigateToEmailLogin: () -> Unit,
    onBack: () -> Unit,
    onGoogleLoginClick: () -> Unit // 구글 로그인은 별도 설정 필요
) {
    CustomScaffold(
        title = "로그인 옵션",
        onBack = onBack
    ) {

            // 구글 로그인 버튼
            Button(
                onClick = onGoogleLoginClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Google로 계속하기", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

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
fun EmailLoginScreen (
    viewModel: FirebaseViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    CustomScaffold(
        title = "로그인",
        onBack = {
            viewModel.clearError()
            onBack()
        }
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearError() // 사용자가 수정하기 시작하면 에러 지움
            },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null // 에러가 있으면 빨간색 처리
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        // 에러 메시지 표시 영역
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.loginWithEmail(email, password, onLoginSuccess) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White)
            else Text("로그인")
        }

        Spacer(modifier = Modifier.height(12.dp))

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
fun SignupScreen (
    viewModel: FirebaseViewModel,
    onSignupSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val isPasswordMatch = password == confirmPassword || confirmPassword.isEmpty()

    CustomScaffold(
        title = "회원가입",
        onBack = {
            viewModel.clearError() // 뒤로 갈 때 에러 지우기
            onBack()
        }
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearError() // 입력 수정 시 에러 초기화
            },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage?.contains("이메일") == true // 에러 메시지에 '이메일'이 포함되면 빨간줄
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = { Text("비밀번호 (6자 이상)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage?.contains("비밀번호") == true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 비밀번호 확인 필드
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("비밀번호 확인") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = !isPasswordMatch, // 불일치 시 에러 표시
            supportingText = {
                if (!isPasswordMatch) {
                    Text("비밀번호가 일치하지 않습니다.", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // 전체적인 에러 메시지 (Firebase 에러 등)
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isPasswordMatch) return@Button
                viewModel.signUp(email, password, name, onSignupSuccess)
            },
            enabled = !isLoading && isPasswordMatch && name.isNotEmpty() && email.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if(isLoading) CircularProgressIndicator(color = Color.White)
            else Text("회원가입")
        }

        TextButton(onClick = {
            viewModel.clearError()
            onBack()
        }) { Text("취소") }
    }
}