package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.json.simple.parser.ParseException;

class TimeDisplayer extends JPanel{

    final int WIDTH = 550;
    final int HEIGHT = 250; 
    String City = "London";
    String Country = "England";
    JLabel timeText;
    TimeGetter timeGetter;
    JLabel timeInPlaceText;
    String timeZoneString = "";
    public int[] time = new int[2];
    Color DAY_COLOUR_Panel = new Color (0, 0, 0, 70);
    Color DAY_COLOUR_Text = new Color (0, 0, 0, 90);

    Color NIGHT_COLOUR_Panel = new Color (100, 50, 150, 40);
    Color NIGHT_COLOUR_Text = new Color (100, 50, 150, 40);

    TimeDisplayer() throws MalformedURLException, IOException, ParseException{
        
        
        this.setLayout(new FlowLayout(0,50,40));
        this.setBackground(DAY_COLOUR_Panel);
        this.setBounds(new Rectangle(new Dimension(WIDTH, HEIGHT)));
        timeGetter = new TimeGetter();



        // city name displayer --> Place Specified: the city name, country name
        // Ex| Place Specified: London, England
        JPanel timeInPlacePanel = new JPanel(new FlowLayout());
        timeInPlaceText = new JLabel("Place Specified: City, Country");

        timeInPlacePanel.setBackground(DAY_COLOUR_Text);

        timeInPlacePanel.setBounds(new Rectangle(new Dimension(WIDTH/2, HEIGHT)));

        Font font = new Font("Arial", Font.PLAIN, 25);       
        timeInPlaceText.setFont(font); 
        timeInPlaceText.setForeground(Color.WHITE);
        timeInPlacePanel.add(timeInPlaceText);
        
        this.add(timeInPlacePanel);

        //time display area

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timePanel.setBackground(DAY_COLOUR_Text);
        timePanel.setForeground(Color.WHITE);
        timeText = new JLabel();
        timeText.setText("Time placed here");
        timePanel.add(timeText);

        Font timeFont = new Font("Arial", Font.PLAIN, 50);       
        timeText.setFont(timeFont); 
        timeText.setForeground(Color.WHITE);

        timePanel.setBounds(300, 300, 1000, 1000);
        this.add(timePanel);

    }

    public boolean changeCity(Place place) throws MalformedURLException, IOException, ParseException{
        City = place.City;
        Country = place.Country;
        String[] timeString;
        System.out.println(City);
        timeString = timeGetter.getTime(place).split(",");        
        timeInPlaceText.setText("Place Specified: " + City + ", " + Country);            
        if (!timeString[0].equals("error")){

            time[0] = Integer.parseInt(timeString[0].split(":")[0]); //get hrs
            time[1] = Integer.parseInt(timeString[0].split(":")[1]); //get mins

            timeZoneString = timeString[1];

            if (time[0] < 10){ // formatting issue with 12 AM
                if(time[1] < 10){
                    timeText.setText("0" + time[0]+ ":0" + time[1] + ", UTC " + timeZoneString);
                } else {
                    timeText.setText("0" + time[0]+ ":" + time[1] + ", UTC " + timeZoneString);
                }
            } else if (time[1] < 10){
                timeText.setText(time[0]+ ":0" + time[1] + ", UTC " + timeZoneString);
            } else {
                timeText.setText(time[0]+ ":" + time[1] + ", UTC " + timeZoneString);
            }

            if (time[0] >= 7 && time[0] < 19){
                this.setBackground(DAY_COLOUR_Panel);
                timeInPlaceText.setBackground(DAY_COLOUR_Text);
                timeText.setBackground(DAY_COLOUR_Text);
                return true; //true = day time

            } else {
                this.setBackground(NIGHT_COLOUR_Panel);
                timeInPlaceText.setBackground(NIGHT_COLOUR_Text);
                timeText.setBackground(NIGHT_COLOUR_Text);
                return false; // false= night time
            }
        } else {
            throw new IOException();  
        }
    }

    public void changeTime(){
        System.out.println("Time before: " + time[0] + ":" + time[1]);
        time[1] ++;
        if (time[1] == 60){
            time[1] = 0;
            time[0] += 1;
        }
        if (time[0] == 23 && time[1] == 59){
            time[1] = 0;
            time[0] = 0;
        }

        System.out.println("Time after: " + time[0] + ":" + time[1]);

        if (time[0] < 10){ // formatting issue with 12 AM
            if(time[1] < 10){
                timeText.setText("0" + time[0]+ ":0" + time[1] + ", UTC " + timeZoneString);
            } else {
                timeText.setText("0" + time[0]+ ":" + time[1] + ", UTC " + timeZoneString);
            }
        } else if (time[1] < 10){
            timeText.setText(time[0]+ ":0" + time[1] + ", UTC " + timeZoneString);
        } else {
            timeText.setText(time[0]+ ":" + time[1] + ", UTC " + timeZoneString);
        }
    }

    public Place callFindPlace(String specString) throws placeNotFoundException{
        return timeGetter.findPlace(specString);
    }
}