package com.example.quizzyapplication.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.quizzyapplication.data.model.DashboardResponse
import com.example.quizzyapplication.data.model.Result
import com.example.quizzyapplication.data.repository.DashboardRepository
import com.example.quizzyapplication.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    private val repository = DashboardRepository()

    private val _dashboardData = MutableStateFlow<Result<DashboardResponse>>(Result.Loading)
    val dashboardData: StateFlow<Result<DashboardResponse>> = _dashboardData.asStateFlow()

    private var isLoadingData = false

    fun loadDashboardData() {
        // Prevent multiple simultaneous requests
        if (isLoadingData) return

        isLoadingData = true
        _dashboardData.value = Result.Loading

        viewModelScope.launch {
            try {
                val result = repository.getDashboardData()
                _dashboardData.value = result
            } catch (e: Exception) {
                _dashboardData.value = Result.Error(e)
            } finally {
                isLoadingData = false
            }
        }
    }

    override fun logout() {
        super.logout()
        _dashboardData.value = Result.Loading
        isLoadingData = false
    }
}