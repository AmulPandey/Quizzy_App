package com.example.quizzyapplication.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quizzyapplication.R
import com.example.quizzyapplication.data.model.DashboardResponse
import com.example.quizzyapplication.data.model.QuizStreakItem
import com.example.quizzyapplication.data.model.Result
import com.example.quizzyapplication.databinding.ActivityHomeBinding
import com.example.quizzyapplication.ui.login.LoginActivity
import com.example.quizzyapplication.ui.base.BaseActivity
import com.example.quizzyapplication.utils.NetworkUtils
import com.example.quizzyapplication.utils.hide
import com.example.quizzyapplication.utils.invisible
import com.example.quizzyapplication.utils.show
import com.example.quizzyapplication.utils.showSnackbar
import com.example.quizzyapplication.utils.showToast
import com.example.quizzyapplication.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (NetworkUtils.isNetworkAvailable(this)) {
            viewModel.loadDashboardData()
        } else {
            binding.root.showSnackbar("No internet connection")
        }
    }

    override fun setupViews() {
        binding.ivNotification.setOnClickListener {
            showToast("Notifications clicked")
        }

        binding.btnLogout.setOnClickListener {
            performLogout()
        }

    }

    override fun observeData() {
        // Observe dashboard data
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dashboardData.collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.show()
                            binding.scrollView.invisible()
                        }
                        is Result.Success -> {
                            binding.progressBar.hide()
                            binding.scrollView.show()
                            populateData(result.data)
                        }
                        is Result.Error -> {
                            binding.progressBar.hide()
                            binding.scrollView.show()
                            binding.root.showSnackbar(
                                result.exception.message ?: "Failed to load dashboard data"
                            )
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuthenticated.collect { isAuthenticated ->
                    if (!isAuthenticated) {
                        navigateToLogin()
                    }
                }
            }
        }
    }

    private fun populateData(data: DashboardResponse) {
        with(binding) {
            // Student Header
            tvGreeting.text = "Hello ${data.student.name}!"
            tvClass.text = data.student.`class`

            // Availability
            tvAvailabilityStatus.text = data.student.availability.status
            tvAvailabilityLabel.text = when (data.student.availability.status.lowercase()) {
                "present" -> "Present Today"
                else -> "Absent"
            }

            // Quiz Attempts
            tvQuizAttempts.text = data.student.quiz.attempts.toString()
            tvQuizLabel.text = "Quiz\n${data.student.quiz.attempts} Attempts"

            // Accuracy
            tvAccuracy.text = data.student.accuracy.current

            // Today's Summary
            tvSummaryStatus.text = data.todaySummary.mood
            tvSummaryMessage.text = "\"${data.todaySummary.description}\""

            // Weekly Overview - Quiz Streak
            updateQuizStreak(data.weeklyOverview.quizStreak)

            // Overall Accuracy
            tvAccuracyPercentage.text = data.weeklyOverview.overallAccuracy.label
            progressAccuracy.progress = data.weeklyOverview.overallAccuracy.percentage

            // Performance by Topic (first topic)
            if (data.weeklyOverview.performanceByTopic.isNotEmpty()) {
                val topic = data.weeklyOverview.performanceByTopic[0]
                tvTopicPerformance.text = when (topic.trend) {
                    "up" -> "${topic.topic} ↑ Improving"
                    "down" -> "${topic.topic} ↓ Needs Attention"
                    else -> topic.topic
                }
            }
        }
    }

    private fun updateQuizStreak(streakList: List<QuizStreakItem>) {
        val streakViews = listOf(
            binding.ivDay1, binding.ivDay2, binding.ivDay3,
            binding.ivDay4, binding.ivDay5, binding.ivDay6, binding.ivDay7
        )

        val dayLabels = listOf(
            binding.tvDay1, binding.tvDay2, binding.tvDay3,
            binding.tvDay4, binding.tvDay5, binding.tvDay6, binding.tvDay7
        )

        streakList.forEachIndexed { index, streakItem ->
            if (index < streakViews.size) {
                dayLabels[index].text = streakItem.day.uppercase()

                if (streakItem.status == "done") {
                    streakViews[index].setImageResource(R.drawable.check)
                    dayLabels[index].hide()
                } else {
                    streakViews[index].setImageResource(R.drawable.ic_circle_outline)
                }
            }
        }
    }

    private fun performLogout() {
        viewModel.logout()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}