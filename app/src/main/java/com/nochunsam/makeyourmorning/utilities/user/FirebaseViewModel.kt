package com.nochunsam.makeyourmorning.utilities.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseViewModel: ViewModel() {
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
                _errorMessage.value = "로그인 실패: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUp(email: String, pass: String, name: String, onSuccess: () -> Unit) {
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
                _errorMessage.value = "가입 실패: ${e.message}"
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