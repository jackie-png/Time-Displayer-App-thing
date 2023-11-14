package com.example;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * TimeGetter
 * Handles getting the time of a place based on the city latitude and longitudes
 */
public class TimeGetter {


    //URLs 
    String timeHandle = "https://timeapi.io/api/Time/current/coordinate?";
    String latLongHandle = "https://api.api-ninjas.com/v1/geocoding?";
    String timezoneHandle = "https://timeapi.io/api/TimeZone/coordinate?";

    //API key for the lat long api 
    String latLongKey = "Eo5jjSER4n243/p5RcBCQw==xnmevndZG8EQTDSz";

    //JSON parser
    static JSONParser parser = new JSONParser();

    //Cities Array List
    static ArrayList <Place> countryDataArray = new ArrayList<Place>();

    //ISO code to COuntry name dictionary
    static HashMap <String, String> countryCode = new HashMap <String, String>();
    
    Scanner Jreader;

    TimeGetter() throws IOException{
        
        Scanner Creader;
        try {
            Creader = new Scanner (new File("demo\\src\\main\\java\\com\\example\\Name,Code.txt"));
            while (Creader.hasNext()){
                String[] Clist = Creader.nextLine().split(",");
                countryCode.put(Clist[1], Clist[0]); //ISO code = key, Country name = value
            }

            Creader.close();

            Object obj = parser.parse(new FileReader("demo\\src\\main\\java\\com\\example\\Cities Added Before.JSON"));

            JSONArray JCarray = (JSONArray)obj;
            for (int i = 0; i < JCarray.size(); i++){
                Place placeToBeAdded = new Place (((JSONObject)JCarray.get(i)).get("name").toString(),((JSONObject)JCarray.get(i)).get("country name").toString(),Double.parseDouble(((JSONObject)JCarray.get(i)).get("latitude").toString()),Double.parseDouble(((JSONObject)JCarray.get(i)).get("longitude").toString()),((JSONObject)JCarray.get(i)).get("country").toString());
                countryDataArray.add(placeToBeAdded); // adds all cities read in the "Cities added before.JSON" into the array
            }
            System.out.println(countryDataArray);
            System.out.println(countryDataArray.get(0));
            System.out.println("Country List done");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("FIle problem: whoops");
        } catch (ParseException e){ // only happens in the event of "Cities added before.JSON" is empty 
            System.out.println("Country List done");
        }
    }


    public String getTime(Place place){
        // the thing that gets the time based on city and country given 

        try {
            //getting the time with latitude and longitude
            System.out.println(place.latLong[0]);

            URL timeURL = new URL (timeHandle + "latitude=" + place.latLong[0] + "&longitude=" + place.latLong[1]);

            HttpURLConnection timeConn = (HttpURLConnection)timeURL.openConnection();
            timeConn.setRequestMethod("GET");
            timeConn.connect();

            Scanner timeScanner = new Scanner (timeURL.openStream());

            String timeInfo = "";

            //time info read in here
            while (timeScanner.hasNext()){
                timeInfo = timeScanner.nextLine();
            }


            JSONObject timeNeeded = (JSONObject) parser.parse(timeInfo); 

            //getting the timezone with the latitude and longitude
            URL timezoneURL = new URL (timezoneHandle + "latitude=" + place.latLong[0] + "&longitude=" + place.latLong[1]);
            HttpURLConnection timezoneConn = (HttpURLConnection) timezoneURL.openConnection();

            timezoneConn.setRequestMethod("GET");
            timezoneConn.connect();

            Scanner timezoneScanner = new Scanner(timezoneURL.openStream());
            String timezoneInfo = "";
            while(timezoneScanner.hasNext()){
                timezoneInfo = timezoneScanner.nextLine();
            }
            //System.out.println(timezoneInfo);
            //System.out.println("time zone parse: " + ((JSONObject)parser.parse(timezoneInfo)).toString());


            //calculating the timezone's UTC offset (Standard time only cause daylight savings is stupid)
            long timezoneInitial = (long)((JSONObject)((JSONObject)parser.parse(timezoneInfo)).get("currentUtcOffset")).get("seconds")/3600;
            

            //disconnect and close everything that can be closed
            timeConn.disconnect();
            timeScanner.close();
            timezoneConn.disconnect();
            timezoneScanner.close();
            
            //returns the time and the timezone ---> (time,timzone)
            System.out.println(timezoneInitial);

            if (timezoneInitial >= 0){
                return timeNeeded.get("time").toString() + ", +" + timezoneInitial;
            } else {
                return timeNeeded.get("time").toString() + ", " + timezoneInitial;
            }
        } catch (Exception e){
            return "error, time";
        }        
    }
    //how to write json objects into json file
    // FileWriter writer = new FileWriter("demo\\src\\main\\java\\com\\example\\out.JSON");
    // writer.write(JArray.toJSONString());
    // writer.close();
    // Scanner read2 = new Scanner(new File("demo\\src\\main\\java\\com\\example\\out.JSON"));
    // while(read2.hasNext()){
    //     JSONIn = read2.nextLine();
    // }
    //System.out.println(((JSONArray)parser.parse(JSONIn)).contains(JArray.get(1)));


    public Place isInCList(String name, String country) throws placeNotInListException{
        // returns the city given, if the city is in the list
        for (Place elm : countryDataArray){
            if (name.equals(elm.City) && country.equals(elm.Country)){
                return elm;
            }
        }
        throw new placeNotInListException();
    }

    public boolean containsPlace(Place place){
        // too much work to overright .equals, so this checks if a city is contained in the list
        for (Place elm : countryDataArray){
            if(elm.equals(place)){
                return true;
            }
        }
        return false;
    }

    public Place findPlace (String specPlace) throws placeNotFoundException{
        /*
         * Responsible for finding the place's latitude and longitude
         */
        String[] splitPlace = specPlace.split(", ");

        if (splitPlace.length == 2){
            
            try { //this attempts to retrieve the city from the cities list
                Place placeToBeAdded = isInCList(splitPlace[0],splitPlace[1]);
                System.out.println("found");
                return placeToBeAdded;          

            } catch (placeNotInListException e){ //if city is not in the list, then find and add the city to the list
                try{

                    System.out.println(splitPlace[0] + ": " + splitPlace[1]);

                    double[] latLongArr = new double[2];
                    URL latlongURL = new URL(latLongHandle + "city="+ splitPlace[0] + "&country=" + splitPlace[1]);
                    HttpURLConnection latLongConn = (HttpURLConnection) latlongURL.openConnection();

                    //connect to website
                    latLongConn.setRequestMethod("GET");
                    latLongConn.addRequestProperty("X-Api-Key", "Eo5jjSER4n243/p5RcBCQw==xnmevndZG8EQTDSz");
                    latLongConn.connect();
                    System.out.println("latlong"+latLongConn.getResponseCode());

                    Scanner latLongScanner = new Scanner (latLongConn.getInputStream());

                    String latLongInfo = "";

                    //lat and long read in here
                    while (latLongScanner.hasNext()){
                        latLongInfo = latLongScanner.nextLine();
                    }

                    JSONArray latLongArray = (JSONArray)parser.parse(latLongInfo);
                    JSONObject locationNeeded = (JSONObject)latLongArray.get(0);

                    //latitude and longitude and country name recieved
                    latLongArr[0]= (double)locationNeeded.get("latitude");
                    latLongArr[1] = (double)locationNeeded.get("longitude");
                    String CName = countryCode.get(locationNeeded.get("country")); // converts ISO country code to their actual name

                    latLongScanner.close();

                    Place placeToBeAdded = new Place ((String)locationNeeded.get("name"), CName, latLongArr[0], latLongArr[1], locationNeeded.get("country").toString());
                    
                    System.out.println(containsPlace(placeToBeAdded));

                    if(!containsPlace(placeToBeAdded)){ //if the place that has been retrieved is not in the cities list, add it in
                        System.out.println("place added to list");
                        countryDataArray.add(placeToBeAdded);
                    }
                    System.out.println(countryDataArray);
                    
                    return new Place ((String)locationNeeded.get("name"), CName, latLongArr[0], latLongArr[1], locationNeeded.get("country").toString());

                } catch (IOException | ParseException | IndexOutOfBoundsException f) {
                    throw new placeNotFoundException();
                }            
            } //end of outer try catch
        } else {
            throw new placeNotFoundException();
        }
    }

    public void saveToFile() throws IOException{
        /*
         * Saves the countryDataArray back into "Cities Added Before.JSON"
         */
        JSONArray jArrayOut = new JSONArray();
        for (Place place : countryDataArray){
            jArrayOut.add(place.toJsonObject());
        }

        FileWriter writer = new FileWriter("demo\\src\\main\\java\\com\\example\\Cities Added Before.JSON");
        writer.write(jArrayOut.toJSONString()); // JSON File isnt formatted, might change later if I care enough
        writer.close();  
    }
}



class placeNotFoundException extends Exception{
    placeNotFoundException(){
        super("placeNotFoundException: Place specified is invalid");
    }
}

class placeNotInListException extends Exception{
    placeNotInListException(){
        super("ignore this");
    }
}
