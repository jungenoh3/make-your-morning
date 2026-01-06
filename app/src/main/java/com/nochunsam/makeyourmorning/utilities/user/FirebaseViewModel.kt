package com.nochunsam.makeyourmorning.utilities.user

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirebaseViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _showLoginButton = MutableStateFlow(isGuestOrNull())
    val showLoginButton = _showLoginButton.asStateFlow()

    private fun isGuestOrNull(): Boolean {
        val user = auth.currentUser
        return user == null || user.isAnonymous
    }

    fun logout() {
        auth.signOut()
        _showLoginButton.value = true
    }

    fun refreshLoginState() {
        _showLoginButton.value = isGuestOrNull()
    }
}