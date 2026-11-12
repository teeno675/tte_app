# TTE App – Photo & Video Publisher

Android app that lets you **take or select photos and videos** and **publish** them to Firebase Storage.

## Features
- Pick photos/videos from gallery (Photo Picker)
- Take photos with CameraX
- Record short videos with CameraX
- Upload to Firebase Storage with progress
- Simple Jetpack Compose UI

## Requirements
- Android Studio (latest stable recommended)
- Android device or emulator (API 26+)
- A Firebase project

## Setup Instructions

### 1. Open the project
1. Clone this repository
2. Open the folder in **Android Studio**
3. Let Gradle sync finish

### 2. Connect Firebase
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project (or use existing)
3. Add an **Android app** with package name: `com.teeno.tteapp`
4. Download `google-services.json`
5. Place it in the `app/` folder (replace the placeholder)
6. In Firebase Console → **Storage** → Create bucket (start in test mode for development)
7. Enable **Authentication** (Anonymous or Email/Google) if you want user-specific uploads later

### 3. Update Security Rules (Firebase Storage)
For testing you can use:
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;   // ⚠️ Change this for production!
    }
  }
}
```

### 4. Run the app
- Connect a real device (recommended for camera) or use emulator
- Click **Run**

## Project Structure
```
app/
├── src/main/java/com/teeno/tteapp/
│   ├── MainActivity.kt
│   ├── TteApplication.kt
│   ├── data/
│   │   └── MediaUploadRepository.kt
│   └── ui/
│       ├── screens/
│       │   ├── HomeScreen.kt
│       │   └── PublishScreen.kt
│       └── theme/
└── ...
```

## Next Steps You Can Add
- User authentication (Firebase Auth)
- Save published media metadata to Firestore
- Public feed of published items
- Video compression before upload
- Multiple media selection
- Captions and hashtags

## Package Name
`com.teeno.tteapp`

---
Created for the TTE photo/video publishing project.
