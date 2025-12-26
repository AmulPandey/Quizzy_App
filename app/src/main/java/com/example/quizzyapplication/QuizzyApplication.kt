package com.example.quizzyapplication

import android.app.Application
import com.google.firebase.FirebaseApp

class QuizzyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}