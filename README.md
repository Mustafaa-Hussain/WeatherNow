# Weather Forecast Application
---
<div>
<img src="https://github.com/user-attachments/assets/74960cc3-31b0-4b0a-a6bf-8cf8eb465ef3" align="left" width="100" hspace="10" vspace="10" />
A native Android mobile application that provides detailed weather updates based on your current location or any chosen city. It supports setting weather alerts, saving favorite locations, and customizing units and languages.

<a href="https://github.com/Mustafaa-Hussain/WeatherNow/releases/download/v1.0/WeatherNow.apk">APK</a>

</div>


## Screenshots
<div>
  <img src="https://github.com/user-attachments/assets/9b2b24ef-037e-47e2-b6c1-4f49207167e7" width="150" alt="Screenshot 1">
  <img src="https://github.com/user-attachments/assets/3cc5883b-3d8c-4c82-ae41-d8de52c393ac" width="150" alt="Screenshot 2">
  <img src="https://github.com/user-attachments/assets/0a9f8f8b-b919-40c2-9721-b19ed38dbb95" width="150" alt="Screenshot 3">
  <img src="https://github.com/user-attachments/assets/23639149-bf65-4dc5-be26-303c550f3bc8" width="150" alt="Screenshot 4">
  <img src="https://github.com/user-attachments/assets/c4ca9257-7cba-4f4f-a038-ead0a4ab86b5" width="150" alt="Screenshot 5">
  <img src="https://github.com/user-attachments/assets/ea863457-a516-4a65-a78e-d29f04897dd2" width="150" alt="Screenshot 6">
  <img src="https://github.com/user-attachments/assets/f3a7e1d1-b56e-4b6e-8d8b-0df2f30371a0" width="150" alt="Screenshot 7">
  <img src="https://github.com/user-attachments/assets/aae0847f-61f7-4c89-8cac-30dee79793d7" width="150" alt="Screenshot 8">
  <img src="https://github.com/user-attachments/assets/2b80d001-180b-446d-a229-02c0b74536c9" width="150" alt="Screenshot 9">
  <img src="https://github.com/user-attachments/assets/6f29f941-6bfa-4c69-90bd-3718ebcb6179" width="150" alt="Screenshot 10">
</div>


## 📲 Application Screens

### 🏠 Home Screen
Displays:
- Current temperature, date, and time.
- Humidity, wind speed, pressure, clouds.
- City name and weather icon.
- Weather state description (e.g., light rain).
- Hourly data for the day.
- Daily data for the next seven days.

### ⚙️ Settings Screen
- Choose location method: GPS or from map.
- Select units:
  - Temperature: Kelvin, Celsius, Fahrenheit.
  - Wind Speed: m/s, mph.
- Select language: English or Arabic, Device Language.

### 🚨 Weather Alerts Screen
- Set alerts for a specific location at a specific time.
- Show alert as notification with custom sound.
- Enable/disable alerts or stop notifications.

### ⭐ Favorites Screen
- List of favorite locations.
- Click an item to view full forecast.
- Add new favorite using map or search.
- Remove a favorite as needed.



# 🛠️ Used Technologies & Tools

This section outlines the main technologies and tools utilized in the Weather Forecast Application.

- **Kotlin** – Programming language for Android development.  
- **Android SDK** – Development platform for building Android apps.  
- **MVVM Architecture** – Clean architecture pattern separating logic, UI, and data layers.  
- **Retrofit** – For making HTTP requests to the weather API.  
- **Room Database** – For local data storage (e.g., favorite locations).  
- **Coroutines** – For handling asynchronous operations in a lightweight way.  
- **AlarmManager** – For scheduling weather alerts and background notifications.  
- **Flow, StateFlow, SharedFlow** – For managing and observing reactive data streams in a lifecycle-aware manner.
- **Open Street Maps** – For selecting and displaying locations on a map.  
- **OpenWeatherMap API** – For retrieving weather data.  
- **Material Design** – For modern and consistent UI components.

---

## APIs
[OpenWeatherMap Forecast API](https://api.openweathermap.org/data/2.5/forecast)

[Search by cuntry name API](https://nominatim.openstreetmap.org/search)
