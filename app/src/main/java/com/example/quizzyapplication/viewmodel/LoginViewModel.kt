package com.example.quizzyapplication.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.quizzyapplication.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : BaseViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(schoolId: String, studentId: String) {
        if (schoolId.isBlank() || studentId.isBlank()) {
            _loginState.value = LoginState.Error("Please enter both School ID and Student ID")
            return
        }

        _isLoading.value = true
        _loginState.value = LoginState.Idle // Reset any previous state

        viewModelScope.launch {
            try {
                // Create email and password from IDs
                val email = "${schoolId}_${studentId}@quizzy.app".lowercase()
                val password = "$schoolId$studentId"

                auth.signInWithEmailAndPassword(email, password).await()

                _loginState.value = LoginState.Success

            } catch (e: FirebaseAuthInvalidUserException) {
                _loginState.value = LoginState.Error("Invalid School ID or Student ID. Account not found.")

            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _loginState.value = LoginState.Error("Invalid credentials. Please check your School ID and Student ID.")

            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed. Please try again.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}