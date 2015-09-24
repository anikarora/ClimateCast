ClimateCast - Android Application
=====================================
+ Author: Anik Arora, Vinod Kumar Gone

Description
-----------
An Android application which provides weather information of favorite cities and weather notifications based on the alerts which can be set by users in the application.

## Features
+ User Account Login/Sign Up in the application
+ Show current weather information of favorites city in the Home activity
+ Add New City in favorites list
+ Show Hourly and Daily forecast of the city selected
+ Change of temperature Scale between (C/F)
+ Setting Alert Notifications for rain and temperature range
+ Alert Notifications can be Activated/Deactivated
+ Manage Alerts

## Scenarios:

#### For Temperature Alert
User A has chosen city as Charlotte. He has set a notification with a temperature range between 5-10 °C with Alert me before 4 hours.
The service would get the next 4 hours forecast data of Charlotte. If the forecasted temperature falls between 5-10 °C (inclusive) at any one of those 4 hours, then an alert would be sent from service to the device.
Timer shall run again in the next hour to check whether temperature of next 4 hours would fall in the specified range.

#### For Rain Alert
User A has set Rain alert for city Charlotte with Alert me before 6 hours.
The service would check if the climate changes to RAIN in next 6 hours. If so, an alert would be sent to the device.

## Implementation
+ Wunder Ground API
+ Parse.com
+ Fragments
