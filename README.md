# Posts App - Android Assessment Task

A modern Android application built with Jetpack Compose that displays posts and comments with offline support.

## Features

- 📱 **Two Tabs**: Posts and Favorites
- 🌐 **Network Integration**: Fetches posts from JSONPlaceholder API
- 💾 **Offline Support**: Posts cached locally using Room database
- ⭐ **Favorites**: Save posts to favorites with offline sync
- 💬 **Comments**: View comments for each post
- 🎨 **Modern UI**: Built with Material Design 3 and Jetpack Compose

## Technical Stack

### Architecture
- **MVVM** (Model-View-ViewModel)
- **Clean Architecture** with separation of concerns
- **Repository Pattern** for data management

### Technologies
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Hilt** - Dependency injection
- **Room** - Local database
- **Retrofit** - Network calls
- **Coroutines & Flow** - Asynchronous programming
- **Material Design 3** - UI components

### Libraries
```gradle
- Compose BOM 2024.09.00
- Hilt 2.51.1
- Room 2.6.1
- Retrofit 2.11.0
- OkHttp 4.12.0
- Navigation Compose 2.8.5
```

## Project Structure

```
app/
├── data/
│   ├── local/          # Room database, DAOs, Entities
│   ├── remote/         # Retrofit API service
│   ├── model/          # Data models
│   └── repository/     # Repository implementations
├── domain/
│   └── repository/     # Repository interfaces
├── presentation/
│   ├── posts/          # Posts screen & ViewModel
│   ├── detail/         # Detail screen & ViewModel
│   ├── favorites/      # Favorites screen & ViewModel
│   ├── components/     # Reusable UI components
│   └── navigation/     # Navigation graph
├── di/                 # Dependency injection modules
├── util/               # Utility classes
└── ui/theme/           # Theme, colors, typography
```

## Key Features Implementation

### Offline Support
- Posts are cached in Room database
- App works without internet connection
- Visual indicator when offline
- Cached data displayed automatically

### Favorites Sync
- Favorites marked as "synced" or "pending"
- Automatic sync when internet returns
- Visual sync status indicators

### Network Monitoring
- Real-time network connectivity detection
- User-friendly offline messages
- Graceful error handling

## API Reference

**Base URL**: `https://jsonplaceholder.typicode.com/`

- `GET /posts` - Fetch all posts
- `GET /posts/{id}/comments` - Fetch comments for a post

## Setup & Installation

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or device (Min SDK: 24)

## Requirements Met

✅ Written in Kotlin  
✅ MVVM design pattern  
✅ Dependency injection (Hilt)  
✅ Room for data persistence  
✅ Coroutines for async operations  
✅ Unit tests included  
✅ Two tabs (Posts & Favorites)  
✅ Network integration  
✅ Offline support  
✅ Internet connectivity message  
✅ Detail screen with comments  
✅ Add to favorites functionality  
✅ Offline sync mechanism  

## Testing

Run tests with:
```bash
./gradlew test
```

## Build

Build APK:
```bash
./gradlew assembleDebug
```

## Author

Built as an assessment task demonstrating Android development skills with modern architecture and best practices.

## License

This project is for assessment purposes only.
