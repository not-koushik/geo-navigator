# geo-navigator

This is a simple Android application that retrieves the user's current location (latitude and longitude) using the FusedLocationProviderClient and provides an option to navigate to the location via Google Maps. The location data is also stored in Firebase Realtime Database for further use.

## Features
- *Get Current Location*: Fetches the user's current latitude and longitude.
- *Navigate to Google Maps*: Opens Google Maps to the user's location with an option to start navigation.
- *Firebase Integration*: Stores the user's location (latitude and longitude) in Firebase Realtime Database.

  You can directly download the APK file included in the repository to try it out.

## Getting started
### Prerequisites
1. Android Studio installed on your machine.
2. A Firebase project set up with Realtime Database enabled. Add the google-services.json file in the app module.

### Installing

1. Clone the repository
`git clone https://github.com/not-koushik/geo-navigator.git`
`cd geo-navigator`
2. Open the project in Android Studio.
3. Connect to Firebase:
- Add your google-services.json file under the app/ directory.
- Ensure that Firebase Realtime Database is enabled for storing location data.
- Build and run the project on an emulator or a real device

### Usage
- *Get Current Location*: Click on the "Get Current Location" button to retrieve and display your current latitude and longitude.
- *Navigate to Google Maps*: After retrieving the location, click on the "Navigate to Google Maps" button to launch Google Maps at the coordinates.
- *Firebase*: The fetched latitude and longitude are automatically stored in the Firebase Realtime Database.

### Project Structure
- *MainActivity.java*: Handles fetching the location, storing it in Firebase, and navigating to Google Maps.
- *activity_main.xml*: Defines the user interface with buttons for location retrieval and navigation.
- *app-release.apk*: The APK file for directly installing the app.
