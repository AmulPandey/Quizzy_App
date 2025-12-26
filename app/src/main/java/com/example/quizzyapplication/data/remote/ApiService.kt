package com.example.quizzyapplication.data.remote

import com.example.quizzyapplication.data.model.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("b/user-contacts-ade83.appspot.com/o/student_dashboard.json?alt=media&token=0091b4c2-2ee2-4326-99cd-96d5312b34bd")
    suspend fun getDashboardData(): Response<DashboardResponse>
}