# Device Info Sharing

An Android application that shares device information (weather, temperature, location, battery percentage) between two devices in real-time using Firebase.

## Features

- **Real-time Device Sync**: Share device data instantly between two Android devices
- **Location Tracking**: Share precise GPS location coordinates
- **Weather Integration**: Display current weather and temperature from OpenWeatherMap API
- **Battery Monitoring**: Track device battery percentage in real-time
- **Device Management**: Name and identify devices uniquely
- **Push Notifications**: Receive instant notifications for data updates
- **Offline Support**: Local caching of device information
- **Network Status**: Monitor connection status
- **Modern Architecture**: MVVM + Clean Architecture with Hilt DI

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Backend**: Firebase Realtime Database
- **APIs**: OpenWeatherMap API
- **Async**: Kotlin Coroutines
- **Testing**: JUnit, Mockito, AndroidX Test

## Project Structure

`
app/src/main/kotlin/com/example/deviceinfosharing/
├── data/
│   ├── api/        # External API clients (Weather API)
│   ├── local/      # Local database
│   ├── remote/     # Firebase database
│   └── repository/ # Data repositories
├── domain/
│   └── models/     # Domain models
├── di/
│   └── modules/    # Hilt dependency injection
├── presentation/
│   ├── viewmodel/  # MVVM ViewModels
│   ├── ui/         # Jetpack Compose screens
│   └── components/ # Reusable UI components
└── MainActivity.kt
`

## Architecture Overview

### Data Layer
- **Repositories**: Manage data from Firebase and local storage
- **Remote Data Source**: Firebase Realtime Database integration
- **Local Data Source**: Room database for offline support
- **API Client**: OpenWeatherMap API integration

### Domain Layer
- Clean business logic models
- Repository interfaces
- Use cases for business operations

### Presentation Layer
- **ViewModels**: State management with MVVM pattern
- **Composable Functions**: Jetpack Compose UI
- **Events**: User interaction handling

## Setup Instructions

### Prerequisites
- Android Studio Flamingo or later
- Android SDK 24+
- Kotlin 1.9+

### Firebase Configuration
1. Create a Firebase project at [firebase.google.com](https://firebase.google.com)
2. Add Android app to your Firebase project
3. Download google-services.json
4. Place it in pp/ directory

### API Keys
1. Get OpenWeatherMap API key from [openweathermap.org](https://openweathermap.org)
2. Add to gradle.properties:
   `
   WEATHER_API_KEY=your_api_key_here
   `

### Build & Run
`ash
./gradlew build
./gradlew installDebug
`

## Features in Detail

### Device Management
- Auto-generate unique device IDs
- Custom device naming
- Device discovery and pairing

### Real-time Sync
- Firebase Realtime Database for instant updates
- Optimized data synchronization
- Conflict resolution

### Location Services
- Fine and coarse location permissions
- Continuous location tracking
- Location updates every 10 seconds

### Weather Information
- Current weather conditions
- Temperature in Celsius
- Automatic weather updates
- Weather for both devices

### Battery Monitoring
- Real-time battery percentage
- Battery status indicators
- Low battery warnings
- Battery history tracking

## Testing

Run tests with:
`ash
./gradlew test
`

## Performance Optimization

- Optimized Firebase queries
- Efficient coroutine management
- Image caching
- Battery-aware background tasks
- Network-aware data sync

## Security Considerations

- Firebase security rules configured
- Location data only shared between paired devices
- No sensitive data stored locally
- API keys managed through gradle.properties

## Permissions

- ACCESS_FINE_LOCATION: Precise GPS location
- ACCESS_COARSE_LOCATION: Approximate location
- INTERNET: Network communication
- ACCESS_NETWORK_STATE: Network status
- RECEIVE_BOOT_COMPLETED: Background services

## Future Enhancements

- [ ] Multiple device support (>2 devices)
- [ ] Cloud backup
- [ ] Device history analytics
- [ ] Custom update intervals
- [ ] Data export/import
- [ ] Biometric authentication

## Contributing

Contributions welcome! Please follow these guidelines:
1. Fork the repository
2. Create feature branch (git checkout -b feature/amazing-feature)
3. Commit changes (git commit -m 'Add amazing feature')
4. Push to branch (git push origin feature/amazing-feature)
5. Open Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Author

**Joule Howl**

## Acknowledgments

- Firebase for real-time database
- OpenWeatherMap for weather API
- Jetpack team for Compose framework
- Android community for best practices
# CI Build Ready
