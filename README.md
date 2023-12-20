# Time-Displayer-App-thing
 A Clock that displays the time from other places
 
 Written in Java, UI created with Java's Swing library

## To Download and Run
 Download the zip file and open it as a project on any java IDE like VScode, then run the program

## Timegetter
 This is responsible for retrieving a city's latitiude and longitude and retirving the time of that place as well

 -  APIs used
  - TimeAPI.io (for the time of place)
  - APIninja's Geocoding API (for the latitude and longitude)
 -  Program also saves previously searched locations via a JSON file such that the Geocoding API doesn't need to be called all the time
 -  File's main purpose is to send a formatted string of the place as well as the time to TimeDisplayer

## TimeDisplayer
 Responsible for setting up and displaying the time of the place given

 It is also responsible for updating the time every time a minute has passed

## Place
 Each place that is added is a class that holds 5 values
  - The city name
  - The country it is in
  - The country's 2-letter ISO country code
  - the Latitude (stored in an array)
  - the longitude (stored in an array)
 This class also has a method to convert the information inside into a JSON object that would be used by Timegetter to save into a JSON file

## Window 
 Responsible for running the whole applications

 Handles various event such as 
  - Selecting a place to display
  - Adding a place to the dropdown menu
  - Removing a place from the dropdown menu
  - Updating the time displayed
  - Updating the background based on whether or not the time is night or day
