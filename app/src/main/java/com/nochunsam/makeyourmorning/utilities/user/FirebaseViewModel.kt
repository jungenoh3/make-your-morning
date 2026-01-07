package com.nochunsam.makeyourmorning.utilities.user

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.nochunsam.makeyourmorning.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseViewModel: ViewModel() {
    private val clientID = BuildConfig.WEB_CLIENT_ID
    private val auth = FirebaseAuth.getInstance()

    private val _showLoginButton = MutableStateFlow(isGuestOrNull())
    val showLoginButton = _showLoginButton.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private fun isGuestOrNull(): Boolean {
        val user = auth.currentUser
        return user == null || user.isAnonymous
    }

    fun loginWithEmail(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "이메일과 비밀번호를 입력해주세요."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                onSuccess()
            } catch (e: Exception) {
                // [서버 에러 매핑]
                _errorMessage.value = when (e) {
                    is FirebaseAuthInvalidUserException -> "존재하지 않는 계정입니다."
                    is FirebaseAuthInvalidCredentialsException -> "이메일 형식이 잘못되었거나 비밀번호가 틀렸습니다."
                    else -> "로그인 실패: ${e.localizedMessage}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginInWithGoogle(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Credential Manager 생성
                val credentialManager = CredentialManager.create(context)

                // 2. Google ID 옵션 설정
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(clientID)
                    .setAutoSelectEnabled(false)
                    .build()

                // 3. 요청 생성
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // 4. 실행 (UI 표시 - 여기서 사용자가 계정을 선택할 때까지 대기)
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                // 5. 결과 파싱 및 Firebase 인증
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    // Firebase 로그인
                    auth.signInWithCredential(authCredential).await()

                    // 상태 갱신 및 성공 콜백
                    refreshLoginState()
                    onSuccess()
                } else {
                    _errorMessage.value = "구글 인증 정보를 가져오지 못했습니다."
                }

            } catch (e: GetCredentialCancellationException) {
                // 사용자가 로그인 창을 닫거나 취소한 경우 -> 에러 메시지 띄우지 않음
                Log.d("GoogleLogin", "User cancelled login")
            } catch (e: Exception) {
                _errorMessage.value = "구글 로그인 실패: ${e.localizedMessage}"
                Log.e("GoogleLogin", "Error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUp(email: String, pass: String, name: String, onSuccess: () -> Unit) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "올바른 이메일 형식이 아닙니다."
            return
        }
        if (pass.length < 6) {
            _errorMessage.value = "비밀번호는 최소 6자 이상이어야 합니다."
            return
        }
        if (name.isBlank()) {
            _errorMessage.value = "이름을 입력해주세요."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Firebase 계정 생성 (자동으로 로그인 됨)
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                // name 업데이트
                val profileUpdates = userProfileChangeRequest { displayName = name }
                 result.user?.updateProfile(profileUpdates)?.await()

                auth.signOut()

                onSuccess() // 화면 이동
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is FirebaseAuthWeakPasswordException -> "비밀번호가 너무 취약합니다."
                    is FirebaseAuthInvalidCredentialsException -> "이메일 형식이 올바르지 않습니다."
                    is FirebaseAuthUserCollisionException -> "이미 가입된 이메일 주소입니다."
                    else -> "가입 실패: ${e.localizedMessage}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 에러 메시지 초기화 (화면 이동 시 등)
    fun clearError() {
        _errorMessage.value = null
    }

    fun logout() {
        auth.signOut()
        _showLoginButton.value = true
    }

    fun refreshLoginState() {
        _showLoginButton.value = isGuestOrNull()
    }
}