package com.example.myapplication.UserApp

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


import kotlinx.coroutines.delay





sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}



interface AuthService {
    suspend fun login(email: String, password: String): ApiResponse<UserResponse>
    suspend fun logout(userId: Long): ApiResponse<Boolean>
    suspend fun getUserProfile(userId: Long): ApiResponse<UserResponse>
}


data class UserResponse(
    val userId: Long,
    val email: String,
    val fullName: String,
    val roleId: Long,
    val isActive: Boolean,
    val token: String
)


class MockAuthService : AuthService {
    private val mockUsers = listOf(
        UserResponse(
            userId = 1,
            email = "admin",
            fullName = "Admin User",
            roleId = 1,
            isActive = true,
            token = "admin-token-123"
        ),
        UserResponse(
            userId = 2,
            email = "user@test.com",
            fullName = "Normal User",
            roleId = 2,
            isActive = true,
            token = "user-token-456"
        ),
        UserResponse(
            userId = 3,
            email = "guest@test.com",
            fullName = "Guest User",
            roleId = 3,
            isActive = true,
            token = "guest-token-789"
        )
    )

    override suspend fun login(email: String, password: String): ApiResponse<UserResponse> {
        // Simulate network delay
        delay(1000)

        // Simple validation - in real API would check password hash
        return when {
            email.isEmpty() || password.isEmpty() -> {
                ApiResponse.Error("Email and password are required")
            }
            password == "123456" -> { // Simple password for testing
                val user = mockUsers.find { it.email == email }
                if (user != null) {
                    ApiResponse.Success(user)
                } else {
                    ApiResponse.Error("User not found")
                }
            }
            else -> {
                ApiResponse.Error("Invalid credentials")
            }
        }
    }

    override suspend fun logout(userId: Long): ApiResponse<Boolean> {
        delay(500) // Simulate network delay
        return ApiResponse.Success(true)
    }

    override suspend fun getUserProfile(userId: Long): ApiResponse<UserResponse> {
        delay(500) // Simulate network delay
        val user = mockUsers.find { it.userId == userId }
        return if (user != null) {
            ApiResponse.Success(user)
        } else {
            ApiResponse.Error("User not found")
        }
    }
}

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: UserResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}


class UserSessionViewModel(application: Context) : AndroidViewModel(application as Application) {
    private val authService = MockAuthService()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState

    private var currentUser: UserResponse? = null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            when (val response = authService.login(email, password)) {
                is ApiResponse.Success -> {
                    currentUser = response.data
                    _uiState.value = LoginUiState.Success(response.data)
                }
                is ApiResponse.Error -> {
                    _uiState.value = LoginUiState.Error(response.message)
                }
                is ApiResponse.Loading -> {
                    _uiState.value = LoginUiState.Loading
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            currentUser?.let { user ->
                when (authService.logout(user.userId)) {
                    is ApiResponse.Success -> {
                        currentUser = null
                        _uiState.value = LoginUiState.Initial
                    }
                    is ApiResponse.Error -> {
                        // Aún así cerramos la sesión localmente
                        currentUser = null
                        _uiState.value = LoginUiState.Initial
                    }
                    is ApiResponse.Loading -> {
                        _uiState.value = LoginUiState.Loading
                    }
                }
            } ?: run {
                // Si no hay usuario actual, simplemente regresamos al estado inicial
                _uiState.value = LoginUiState.Initial
            }
        }
    }
}
