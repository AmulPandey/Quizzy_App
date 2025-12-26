package com.example.quizzyapplication.data.repository

import com.example.quizzyapplication.data.model.DashboardResponse
import com.example.quizzyapplication.data.model.Result
import com.example.quizzyapplication.data.remote.ApiService
import com.example.quizzyapplication.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class DashboardRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {
    suspend fun getDashboardData(): Result<DashboardResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDashboardData()

                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    Result.Error(Exception("Failed to load dashboard: $errorMsg"))
                }
            } catch (e: IOException) {
                Result.Error(Exception("Network error. Please check your internet connection.", e))
            } catch (e: Exception) {
                Result.Error(Exception("Something went wrong. Please try again.", e))
            }
        }
    }
}