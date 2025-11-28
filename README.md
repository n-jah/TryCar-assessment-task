# Posts App - Android Assessment

A modern Android application built with Jetpack Compose that displays posts and comments with full offline support and favorites management.

## 📱 Demo

### 🎬 App in Action

<p align="center">
  <img src="screenshots/online.gif" width="280" alt="Online Mode" />
  <img src="screenshots/offline.gif" width="280" alt="Offline Mode" />
</p>

### 📸 Screenshots

#### Online Mode
<p align="center">
  <img src="screenshots/online posts.jpg" width="250" alt="Posts List" />
  <img src="screenshots/online post details.jpg" width="250" alt="Post Details" />
  <img src="screenshots/online post datails and comments.jpg" width="250" alt="Comments" />
</p>

#### Offline Mode
<p align="center">
  <img src="screenshots/loading on no internet.jpg" width="250" alt="Loading Offline" />
  <img src="screenshots/offline post details.jpg" width="250" alt="Offline Details" />
  <img src="screenshots/favorites screen.jpg" width="250" alt="Favorites" />
</p>

### 🎥 Full Demo Video
[Watch Full Demo](screenshots/showUp.mp4)

## 📥 Download

Get the latest APK from the [Releases](../../releases) page.

## ✨ Features

- **Two Tabs Navigation**: Posts and Favorites with bottom navigation
- **Posts List**: Displays posts fetched from JSONPlaceholder API
- **Offline Mode**: Full offline support with local caching
- **Post Details**: View post content with comments
- **Favorites**: Save posts to favorites with offline sync
- **Network Status**: Visual indicators for connectivity status
- **Modern UI**: Material Design 3 with smooth animations

## 🏗️ Architecture

### MVVM + Clean Architecture
```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│  (UI, ViewModels, Compose Screens)  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Domain Layer               │
│     (Repository Interfaces)         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│           Data Layer                │
│  (Room, Retrofit, Repositories)     │
└─────────────────────────────────────┘
```

### Key Design Patterns
- **MVVM**: Separation of UI and business logic
- **Repository Pattern**: Single source of truth for data
- **Dependency Injection**: Hilt for managing dependencies
- **Offline-First**: Local cache with network sync

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room |
| Networking | Retrofit + OkHttp |
| Async | Coroutines + Flow |
| Navigation | Navigation Compose |
| Testing | JUnit |

## 📦 Dependencies

```kotlin
// Core
- Kotlin 2.0.21
- Compose BOM 2024.09.00
- Material Design 3

// Architecture
- Hilt 2.51.1
- Lifecycle ViewModel Compose 2.8.7
- Navigation Compose 2.8.5

// Data
- Room 2.6.1
- Retrofit 2.11.0
- OkHttp 4.12.0

// Async
- Coroutines 1.8.0
```

## 📂 Project Structure

```
app/src/main/java/com/example/trycar_assessment_task/
│
├── data/
│   ├── local/
│   │   ├── dao/              # Room DAOs
│   │   ├── database/         # Database configuration
│   │   └── entity/           # Room entities
│   ├── remote/
│   │   └── api/              # Retrofit API service
│   ├── model/                # Data models (Post, Comment)
│   └── repository/           # Repository implementations
│
├── domain/
│   └── repository/           # Repository interfaces
│
├── presentation/
│   ├── posts/                # Posts screen + ViewModel
│   ├── detail/               # Detail screen + ViewModel
│   ├── favorites/            # Favorites screen + ViewModel
│   ├── components/           # Reusable UI components
│   └── navigation/           # Navigation graph
│
├── di/                       # Hilt modules
├── Network/                  # Network connectivity observer
├── util/                     # Utility classes (Resource)
└── ui/theme/                 # Theme, colors, shapes
```

## ✨ Key Features Implementation

### 1. Offline Support
- **Room Database**: Local caching of all posts
- **Automatic Fallback**: Shows cached data when offline
- **Network Indicator**: Visual message when no internet
- **Seamless Experience**: App fully functional offline

### 2. Favorites Management
- **Local Storage**: Favorites saved in Room database
- **Sync Status**: Visual indicators (Synced/Pending)
- **Offline Sync**: Automatic sync when connection restored
- **Real-time Updates**: Flow-based reactive updates

### 3. Network Monitoring
- **ConnectivityManager**: Real-time network status
- **Flow-based**: Reactive connectivity updates
- **User Feedback**: Clear messages about connection status

### 4. Modern UI
- **Jetpack Compose**: Declarative UI
- **Material Design 3**: Modern design system
- **Smooth Navigation**: Bottom tabs + detail navigation
- **Responsive**: Adapts to different screen sizes

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 11 or higher
- Android SDK 24+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/posts-app.git
cd posts-app
```

2. **Open in Android Studio**
   - File → Open → Select project folder

3. **Sync Gradle**
   - Wait for Gradle sync to complete

4. **Run the app**
   - Click Run button or press Shift+F10
   - Select emulator or connected device

### Build APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (signed)
./gradlew assembleRelease
```

**APK Location**: `app/build/outputs/apk/debug/app-debug.apk`

### Create Release

1. **Build Release APK**
```bash
./gradlew assembleRelease
```

2. **Create GitHub Release**
   - Go to repository → Releases → Create new release
   - Tag version (e.g., v1.0.0)
   - Upload APK file
   - Add release notes

### Add Screenshots

1. **Take Screenshots**
   - Posts screen
   - Detail screen with comments
   - Favorites screen
   - Offline mode indicator

2. **Record GIF**
   - Use Android Studio's screen recorder
   - Or use ADB: `adb shell screenrecord /sdcard/demo.mp4`
   - Convert to GIF using online tools

3. **Add to Repository**
```bash
mkdir screenshots
# Add your images: posts_screen.png, detail_screen.png, favorites_screen.png, app_demo.gif
git add screenshots/
git commit -m "Add screenshots and demo"
git push
```

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Test Coverage
- **8 Unit Tests** covering:
  - Resource state management
  - Repository pattern
  - Network observer
  - Data flow

### View Test Results
```bash
# Open HTML report
start app/build/reports/tests/testDebugUnitTest/index.html
```

## 📋 Requirements Checklist

### Technical Requirements
- ✅ Written in Kotlin
- ✅ Unit tests included (8 tests)
- ✅ MVVM design pattern
- ✅ Dependency injection (Hilt)
- ✅ Room for data persistence
- ✅ Coroutines for async operations
- ⚪ UI tests (optional - not implemented)

### Functional Requirements

#### Posts Tab
- ✅ Display list of posts from network
- ✅ Posts available offline (cached)
- ✅ Internet status message
- ✅ Click post → Detail screen
- ✅ Detail shows comments
- ✅ Add to favorites button

#### Favorites Tab
- ✅ List all favorited posts
- ✅ Offline sync mechanism
- ✅ Visual sync status

#### Design
- ✅ Modern design (Material Design 3)
- ✅ Two tabs navigation
- ✅ Smooth user experience

## 🌐 API Reference

**Base URL**: `https://jsonplaceholder.typicode.com/`

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/posts` | GET | Fetch all posts |
| `/posts/{id}/comments` | GET | Fetch comments for a post |

## 🎨 Design Decisions

### Why Jetpack Compose?
- Modern, declarative UI
- Less boilerplate code
- Better performance
- Easier to maintain

### Why Clean Architecture?
- Separation of concerns
- Testable code
- Scalable structure
- Easy to modify

### Why Offline-First?
- Better user experience
- Works without internet
- Faster app performance
- Reduced network calls

## 📝 Notes

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14)
- **Compile SDK**: 36

## 👨‍💻 Author

Ahmed - Android Developer

Built as an assessment task demonstrating:
- Modern Android development
- Clean architecture principles
- Best practices and patterns
- Professional code quality

## 📄 License

This project is for assessment purposes only.

---

**Built with ❤️ using Kotlin & Jetpack Compose**
