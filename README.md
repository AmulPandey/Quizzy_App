# Quizzy App - Android Application

## Overview
A simple Android educational app with Login → Home → Logout flow, implementing MVVM architecture with Firebase Authentication.

## Features
- 🔐 Firebase Authentication (Email/Password)
- 📊 Student Dashboard with stats
- 🎯 Clean MVVM Architecture
- 🎨 Material Design UI matching Figma designs
- 📱 ViewBinding implementation

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: XML with ViewBinding
- **Authentication**: Firebase Auth
- **Networking**: Retrofit
- **Async**: Coroutines

## Project Structure
```
app/
├── data/
│   ├── model/         # Data models
│   ├── repository/    # Repository pattern
│   └── remote/        # API services
├── ui/
│   ├── login/         # Login screen
│   ├── home/          # Home screen
│   └── base/          # Base classes
├── utils/             # Utility classes
└── viewmodel/         # ViewModels
```

## Setup Instructions

### Firebase Setup
1. Add dependencies.
2. Go to [Firebase Console](https://console.firebase.google.com/)
3. Create a new project or use existing one
4. Add Android app with package name: `com.example.quizzyapp` 
5. Enable Email/Password authentication in Firebase Console

### Installation Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd QuizzyApp
   ```

2. Open project in Android Studio

3. Sync Gradle files

4. Build and run the application

### Test Credentials
Create a test account through Firebase Console or use the app to sign up:
- School ID: SGGP782001
- Student ID: SG211I

## API Endpoint
The app fetches student dashboard data from:
```
https://firebasestorage.googleapis.com/v0/b/user-contacts-ade83.appspot.com/o/student_dashboard.json?alt=media&token=0091b4c2-2ee2-4326-99cd-96d5312b34bd
```

## Architecture

### MVVM Pattern
- **Model**: Data classes and repository
- **View**: Activities/Fragments with XML layouts
- **ViewModel**: Business logic and state management

### Data Flow
```
View → ViewModel → Repository → Remote/Local Data Source
     ← Flow   ← 
```

## Key Features Implementation

### Authentication Flow
1. User enters School ID and Student ID
2. Firebase Auth validates credentials
3. On success, navigate to Home screen
4. Auth state persisted across app restarts

### Home Screen
- Displays personalized greeting
- Shows availability status, quiz attempts, and accuracy
- Daily motivational summary
- Weekly performance overview
- Quiz streak tracker
- Performance by topic

### Logout Flow
- Clear Firebase Auth session
- Navigate back to Login screen
- Clear cached data

## Error Handling
- Network connectivity checks
- Firebase Auth errors with user-friendly messages
- API call failure handling
- Loading states with progress indicators

## Dependencies
Key libraries used:
- Firebase Auth: 22.3.0
- Retrofit: 2.9.0
- Coroutines: 1.7.3
- Material Components: 1.11.0
- Lifecycle/ViewModel: 2.7.0


## License
This project is created as a demonstration app.

## Contact
For issues or questions, please create an issue in the repository.
