package com.example.quizzyapplication.ui.login

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quizzyapplication.databinding.ActivityLoginBinding
import com.example.quizzyapplication.ui.base.BaseActivity
import com.example.quizzyapplication.ui.home.HomeActivity
import com.example.quizzyapplication.utils.NetworkUtils
import com.example.quizzyapplication.utils.hide
import com.example.quizzyapplication.utils.hideKeyboard
import com.example.quizzyapplication.utils.show
import com.example.quizzyapplication.utils.showSnackbar
import com.example.quizzyapplication.utils.showToast
import com.example.quizzyapplication.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()
    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.isUserLoggedIn()) {
            navigateToHome()
        }

        setupKeyboardDetection()
    }

    override fun setupViews() {
        binding.tvSignIn.setOnClickListener {
            hideKeyboard(it)
            performLogin()
        }

        // Automatically scroll when EditText gets focus
        binding.etSchoolId.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.post {
                    val rect = Rect()
                    binding.signInContainer.getHitRect(rect)
                    binding.root.requestChildRectangleOnScreen(binding.signInContainer, rect, false)
                }
            }
        }

        binding.etStudentId.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.post {
                    val rect = Rect()
                    binding.signInContainer.getHitRect(rect)
                    binding.root.requestChildRectangleOnScreen(binding.signInContainer, rect, false)
                }
            }
        }
    }

    private fun setupKeyboardDetection() {
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            // If keyboard is visible (more than 15% of screen)
            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is showing
                scrollToFocusedView()
            }
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun scrollToFocusedView() {
        binding.root.post {
            val focusedView = currentFocus
            if (focusedView != null) {
                val nestedScrollView = binding.root as? NestedScrollView
                val scrollY = binding.signInContainer.top - 100 // 100px padding from top
                nestedScrollView?.smoothScrollTo(0, scrollY)
            }
        }
    }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    when (state) {
                        is LoginViewModel.LoginState.Success -> {
                            showToast("Login successful!")
                            navigateToHome()
                        }
                        is LoginViewModel.LoginState.Error -> {
                            binding.root.showSnackbar(state.message)
                        }
                        is LoginViewModel.LoginState.Idle -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    if (isLoading) {
                        binding.tvSignIn.isEnabled = false
                        binding.progressBar.show()
                    } else {
                        binding.tvSignIn.isEnabled = true
                        binding.progressBar.hide()
                    }
                }
            }
        }
    }

    private fun performLogin() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            binding.root.showSnackbar("No internet connection")
            return
        }

        val schoolId = binding.etSchoolId.text.toString().trim()
        val studentId = binding.etStudentId.text.toString().trim()

        viewModel.login(schoolId, studentId)
    }

    private fun navigateToHome() {
        Intent(this, HomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        globalLayoutListener?.let {
            binding.root.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
    }
}