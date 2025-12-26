package com.example.quizzyapplication.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DashboardResponse(
    @SerializedName("student")
    val student: Student,

    @SerializedName("todaySummary")
    val todaySummary: TodaySummary,

    @SerializedName("weeklyOverview")
    val weeklyOverview: WeeklyOverview
)

@Keep
data class Student(
    @SerializedName("name")
    val name: String,

    @SerializedName("class")
    val `class`: String,

    @SerializedName("availability")
    val availability: Availability,

    @SerializedName("quiz")
    val quiz: Quiz,

    @SerializedName("accuracy")
    val accuracy: Accuracy
)

@Keep
data class Availability(
    @SerializedName("status")
    val status: String
)

@Keep
data class Quiz(
    @SerializedName("attempts")
    val attempts: Int
)

@Keep
data class Accuracy(
    @SerializedName("current")
    val current: String
)

@Keep
data class TodaySummary(
    @SerializedName("mood")
    val mood: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("recommendedVideo")
    val recommendedVideo: RecommendedVideo,

    @SerializedName("characterImage")
    val characterImage: String
)

@Keep
data class RecommendedVideo(
    @SerializedName("title")
    val title: String,

    @SerializedName("actionText")
    val actionText: String
)

@Keep
data class WeeklyOverview(
    @SerializedName("quizStreak")
    val quizStreak: List<QuizStreakItem>,

    @SerializedName("overallAccuracy")
    val overallAccuracy: OverallAccuracy,

    @SerializedName("performanceByTopic")
    val performanceByTopic: List<PerformanceByTopic>
)

@Keep
data class QuizStreakItem(
    @SerializedName("day")
    val day: String,

    @SerializedName("status")
    val status: String
)

@Keep
data class OverallAccuracy(
    @SerializedName("percentage")
    val percentage: Int,

    @SerializedName("label")
    val label: String
)

@Keep
data class PerformanceByTopic(
    @SerializedName("topic")
    val topic: String,

    @SerializedName("trend")
    val trend: String
)