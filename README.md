---

## SCI Reflection: Wholeness & Harmony

This project is inspired by the principles of **SCI (Science of Creative Intelligence)**, emphasizing wholeness, harmony, and meaningful connection in technology and design.

**Wholeness** is reflected in the app's unified experience—bringing together travel discovery, profile management, and smart features into a seamless journey for the user. Every component, from UI to background tasks, is designed to work in harmony, supporting a balanced and enjoyable travel experience.

**Meaningful SCI Principle Connection:**
- The architecture encourages clarity and simplicity, making the codebase easy to understand and extend.
- Features like AI-generated profile photos and background syncing are integrated thoughtfully, supporting the user's needs without distraction.
- The app's structure and flow promote a sense of completeness, where every part contributes to the whole.

By reflecting SCI principles, Tour App aims to create not just a functional product, but a harmonious and meaningful experience for travelers and developers alike.

## Tour App (Android, Jetpack Compose)

##Tour App** is a modern traveling application for Android. It allows users to discover and book tours, manage their travel profile, and experience smart features like AI-generated profile photos. The app is built with Jetpack Compose for a beautiful UI, and uses WorkManager for reliable background tasks such as syncing tour data and uploading profile photos.

## Whether you're planning your next adventure or just browsing, Tour App makes travel easy, secure, and fun. Log in to see personalized tours, update your profile, and let AI help you create a unique travel identity!
---

## Features

- Authentication (login/logout) with persisted session via DataStore
- Tour listing and user details from a backend API
- Profile screen with:
	- Current user info (name, email, id)
	- Profile photo display (server-hosted)
	- Generate AI profile photo (client flow + upload to server)
- Background tasks with WorkManager:
	- Periodic sync of tours (scheduled at app start)
	- One-off profile photo upload job (ready to be triggered after image pick)
- Modern UI with Jetpack Compose and Coil3 image loading

## Tech stack

- Kotlin, Jetpack Compose (Material 3)
- Navigation (Navigation3 and navigation-compose)
- WorkManager for background work
- Retrofit + OkHttp (with logging)
- Room (database), DataStore (preferences)
- Coil 3 (image loading)

## Minimum requirements

- Android Studio (Giraffe+ recommended)
- compileSdk = 36, targetSdk = 36, minSdk = 31

## Project structure (high level)

- `app/src/main/java/com/example/survey/SurveyApp.kt` – Application class; schedules periodic WorkManager sync and provides WorkManager configuration
- `app/src/main/java/com/example/survey/ui/screen/ProfileScreen.kt` – Profile UI (delegates to a testable `ProfileScreenContent`)
- `app/src/main/java/com/example/survey/ui/viewmodel/AuthViewModel.kt` – Auth state and session
- `app/src/main/java/com/example/survey/ui/viewmodel/ProfileViewModel.kt` – Profile and AI image flow
- `app/src/main/java/com/example/survey/work/WorkSchedulers.kt` – Helpers to schedule WorkManager jobs
- `app/src/main/java/com/example/survey/work/SyncToursWorker.kt` – Periodic tour sync worker
- `app/src/main/java/com/example/survey/work/UploadProfilePhotoWorker.kt` – One-off profile photo upload worker

## App structure (tree)

```text
survey/
├─ README.md
├─ build.gradle.kts
├─ settings.gradle.kts
├─ gradle.properties
├─ gradlew
├─ app/
│  ├─ build.gradle.kts
│  ├─ google-services.json
│  └─ src/
│     ├─ main/
│     │  ├─ AndroidManifest.xml
│     │  ├─ java/com/example/survey/
│     │  │  ├─ MainActivity.kt
│     │  │  ├─ SurveyApp.kt
│     │  │  ├─ data/
│     │  │  │  ├─ remote/
│     │  │  │  │  ├─ api/TourApiService.kt
│     │  │  │  │  ├─ dto/ …
│     │  │  │  │  ├─ repository/ …
│     │  │  │  ├─ local/ …
│     │  │  │  ├─ domain/ …
│     │  │  ├─ ui/
│     │  │  │  ├─ screen/
│     │  │  │  │  ├─ HomeScreen.kt
│     │  │  │  │  ├─ LoginScreen.kt
│     │  │  │  │  ├─ TourDetailScreen.kt
│     │  │  │  │  ├─ BookmarkScreen.kt
│     │  │  │  │  └─ ProfileScreen.kt
│     │  │  │  ├─ viewmodel/
│     │  │  │  │  ├─ AuthViewModel.kt
│     │  │  │  │  ├─ ProfileViewModel.kt
│     │  │  │  │  └─ TourViewModel.kt
│     │  │  ├─ work/
│     │  │  │  ├─ WorkSchedulers.kt
│     │  │  │  ├─ SyncToursWorker.kt
│     │  │  │  └─ UploadProfilePhotoWorker.kt
│     │  │  └─ util/ …
│     │  └─ res/
│     │     ├─ drawable/ …
│     │     ├─ values/ …
│     │     └─ xml/ …
│     └─ androidTest/
│        └─ java/com/example/survey/ui/screen/
│           └─ ProfileScreenTest.kt
└─ build/
```

## Setup

1) Clone and open in Android Studio
2) Sync Gradle
3) Configure your backend base URL and endpoints in your Retrofit service (see `data/remote/api`)
4) Run on an emulator or device

Permissions (Manifest):
- INTERNET (required)
- CAMERA (if capturing images)
- READ/WRITE_EXTERNAL_STORAGE (legacy) or READ_MEDIA_IMAGES on Android 13+

If using an emulator against a local backend, `10.0.2.2` points to host machine (already used for user photo URLs).

## Background work (WorkManager)

- Periodic tour sync
	- Auto-scheduled on app start in `SurveyApp.onCreate()` via `WorkSchedulers.schedulePeriodicTourSync(this)`
	- Runs under constraints (network connected, battery not low)
	- Logs appear in Logcat with tag `SyncToursWorker`

- One-off profile photo upload
	- Ready to call after user picks an image:
		- `WorkSchedulers.enqueueProfilePhotoUpload(context, imageUri, userId)`
	- Logs appear in Logcat with tag `UploadPhotoWorker`
	- You can observe work state via WorkManager LiveData/Flow to show progress in UI

Note: Periodic work is deferred by the system. For immediate testing, enqueue a one-time work request for `SyncToursWorker` or trigger the photo upload worker.

## Testing

- UI tests (Compose):
	- `app/src/androidTest/java/com/example/survey/ui/screen/ProfileScreenTest.kt`
	- Tests render of title, user info, and actions using the testable `ProfileScreenContent` API.

## How to run

- From Android Studio: select a device/emulator and click Run.
- Optional instrumentation tests:

```bash
./gradlew connectedAndroidTest
```

## Troubleshooting

- Application class not running
	- Ensure `AndroidManifest.xml` has `<application android:name=".SurveyApp" ... />`
	- Reinstall the app after changing the manifest

- Periodic work not firing immediately
	- This is expected; use a one-time work request during development to verify the flow
	- Check Logcat for `SyncToursWorker` and WorkManager (`WM-`) logs

- Images not loading
	- Verify the photo URL and that the emulator can reach your backend (e.g., `http://10.0.2.2:3000/...`)

## Next steps

- Wire the photo picker to call `WorkSchedulers.enqueueProfilePhotoUpload(...)` after selection
- Add progress indicators by observing WorkManager work info in the UI
- Add more UI tests for error/loading branches

---

MIT License © 2025
