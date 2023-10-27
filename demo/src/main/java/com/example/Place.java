package com.example;

import org.json.simple.JSONObject;

class Place{
    public String City;
    public String Country;
    public String CountryCode;
    public double[] latLong = new double[2];

    Place(String City, String Country, double latitude, double longitude, String CountryCode){
        this.City = City;
        this.Country = Country;
        this.latLong[0] = latitude;
        this.latLong[1] = longitude;
        this.CountryCode = CountryCode;
    }

    public String toString(){
        return City + ", " + Country;
    }

    public JSONObject toJsonObject(){
        /*
         * converts Place to a JSON object
         */
       JSONObject Jcountry = new JSONObject();
       Jcountry.put("country", CountryCode); // 2letter code
       Jcountry.put("country name",Country); //full country name
       Jcountry.put("latitude", latLong[0]); // latitude
       Jcountry.put("name", City); //city name 
       Jcountry.put("longitude", latLong[1]); // longitude

       return Jcountry;
    }

    public boolean equals(Place placeB){
        //technically its not overloading but equals is too much of a hassel to overload
        System.out.println("my comparison");
        if (this.latLong[0] == placeB.latLong[0] && this.latLong[1] == placeB.latLong[1]){
            return true;
        } else {
            return false;
        }
    }
    
}