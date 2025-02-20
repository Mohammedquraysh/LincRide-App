# LincRide-App
# Ride-Hailing Application

A mini ride-hailing application built using 
**Kotlin**,
**MVVM architecture**,
**Hilt for dependency injection**,
and **Room Database** for local data storage. The app allows users to request a ride, get a fare estimate, and view ride history.

---

## Features
1. **Core Features**:
   - Simple UI with Google Maps integration.
   - Input fields for pickup and destination locations.
   - Fare estimate calculation based on distance, demand, and traffic.
   - Ride request confirmation with driver details.
   - Ride history screen to view past rides.

2. **Technical Features**:
   - MVVM architecture.
   - Hilt for dependency injection.
   - Room Database for local data storage.
   - LiveData for reactive UI updates.
   - Jetpack Navigation for screen transitions.

---

## Prerequisites
- Android Studio (latest stable version).
- Kotlin plugin installed.
- Google Maps API key (for map integration).

---

## Setup Instructions

### 1. Clone the Repository
Clone the project repository to your local machine:


```

### 2. Add Google Maps API Key
1. Obtain a Google Maps API key from the [Google Cloud Console](https://console.cloud.google.com/).
2. Open the `string sresources and add your google api_key. 
3. Add your API key:

   ```properties
   <string name="maps_api_key"></string>
   ```

4. Update the `AndroidManifest.xml` file to use the API key:

   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="${maps_api_key}" />
   ```

### 3. Build the Project
1. Open the project in Android Studio.
2. Sync the project with Gradle files.
3. Build the project to ensure all dependencies are resolved.

---



## Dependencies
The project uses the following dependencies:
- **Hilt**: For dependency injection.
- **Room**: For local data storage.
- **LiveData**: For reactive UI updates.
- **Coroutines**: For background tasks.
- **Google Maps SDK**: For map integration.
- **Jetpack Navigation**: For screen transitions.

---

## Running the App
1. Connect an Android device or emulator.
2. Click **Run** in Android Studio .
3. The app will launch on the device.


## Contact
For questions or feedback, reach out to:
- **Name**  : Mohammed Quraysh
- **Email**: mohammedquraysh4@gmail.com  
- **GitHub**:(https://github.com/Mohammedquraysh)

