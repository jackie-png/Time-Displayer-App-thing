package com.example; 

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) {
        // try {
            
        //     TimeGetter timeGetter = new TimeGetter();
        //     Place toronto = new Place ("Toronto","Canada",43.651070, -79.347015,"CA");
        //     System.out.println(timeGetter.findPlace("toronto, Canada"));
        //     System.out.println(toronto.toJsonObject().get("country"));

        // } catch (IOException | placeNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     System.out.println(":whoops");
        // }
        //test this here
        try {
            new Window();
            System.out.println("good done");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}