package com.example;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.json.simple.parser.ParseException;
import org.omg.CORBA.DATA_CONVERSION;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.*;

class Window extends JFrame implements ActionListener, WindowListener{

    JComboBox <Place> options; // dropdown menu 
    TimeDisplayer timeDisplayer; // where the time is displayed, see timedisplayer for more details
    JTextField addPlaceBox; // add place textbox 
    JButton submitButton; // add place's submit button
    JButton removeButton; // add place's remove button (remove a place)
    JButton tempButton; // testing's change day button
    Color DAY_COLOUR_Panel = new Color (0, 0, 0, 30);
    Color NIGHT_COLOUR_panel = new Color (100, 50, 150, 40);
    Background backgrd; // background image panel
    boolean isNight = false; // if it is day or night
    JButton addTime; // testing's add time button
    JPanel northPanel; // holds dropdown, submit, remove, and testing componenets
    Timer opacityTime = new Timer (5, this);
    Timer minute = new Timer(500,this);
    int start;

    Window() throws MalformedURLException, IOException, ParseException{

        this.setLayout(null);
        this.setTitle("Time Around The World");
        this.setIconImage((new ImageIcon("demo\\src\\main\\java\\com\\example\\Time_Icon.png")).getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 797));
        this.addWindowListener(this);
        timeDisplayer = new TimeDisplayer();

        backgrd = new Background();

        backgrd.setBounds(0, 0, backgrd.getWidth(), backgrd.getHeight());

        timeDisplayer.setBounds(226, 250, timeDisplayer.WIDTH, timeDisplayer.HEIGHT);
        this.add(timeDisplayer);

        northPanel = new JPanel(new FlowLayout()); // panel that holds the options, submit, and remove boxes
        northPanel.setBackground(DAY_COLOUR_Panel);

        
        options = new JComboBox<Place>(); //declaring options box to hold place
        options.addItem(new Place ("Toronto","Canada",43.651070, -79.347015,"CA"));
        options.addItem(new Place ("London", "England", 51.509865, -0.118092,"GB"));
        options.addItem(new Place("Washington D.C", "USA", 38.9072, -77.0369, "US"));
        options.setSize(new Dimension(400, 50));
        options.addActionListener(this);
        northPanel.add(options);

        addPlaceBox = new JTextField("Add a new place here: city, country"); //declcaring the textbox
        addPlaceBox.setPreferredSize(new Dimension(250, 30));
        northPanel.add(addPlaceBox);

        submitButton = new JButton("Add Place"); // submit button to add places
        submitButton.setSize(new Dimension(60, 50));
        submitButton.addActionListener(this);
        northPanel.add(submitButton);

        removeButton = new JButton("Remove Place"); // remove button to remove places 
        removeButton.setSize(new Dimension(60, 50));
        removeButton.addActionListener(this);
        northPanel.add(removeButton);  

        // tempButton = new JButton("change day"); // testing's change day button
        // tempButton.setSize(new Dimension(60, 50));
        // tempButton.addActionListener(this);
        // northPanel.add(tempButton);


        northPanel.setBounds(0, 0, 1000, 100);


        this.add(northPanel);
        this.add(backgrd);        
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        start = LocalTime.now().getMinute(); //timer keeps track of time
        System.out.println(start);
        minute.start();

    }

    @Override
    public void actionPerformed(ActionEvent e){
        //System.out.println(backgrd.opacity);
        //System.out.println(isNight);
        //System.out.println("opacity time: "+opacityTime.isRunning());
        //System.out.println("minite time: "+ minute.isRunning());
        if (isNight == false && opacityTime.isRunning()){
            backgrd.opacity+=0.035f;
            if(backgrd.opacity >= 1.0f){
                backgrd.opacity = 1f;
                opacityTime.stop();
            }
        } else if (isNight == true && opacityTime.isRunning()){
            backgrd.opacity-=0.025f;
            if(backgrd.opacity <= 0f){
                backgrd.opacity = 0f;
                opacityTime.stop();
            }

        }

        this.repaint();

        if (e.getSource() == minute){ // option box activities 
            handleMinute();
        } else if (e.getSource() == options){
            handleOptions();
        } else if (e.getSource().equals(submitButton)){ // adding a place to the list
             handleSubmit();
        } else if (e.getSource().equals(removeButton)){ // removeing a place from the list
            handleRemove();
        } else if (e.getSource().equals(addTime)){
            timeDisplayer.changeTime();
        }   

    }

    public void changeBackground(boolean dayOrNight){
        isNight=dayOrNight;
        if (isNight){
            northPanel.setBackground(NIGHT_COLOUR_panel);
        } else {
            northPanel.setBackground(DAY_COLOUR_Panel);
        }
        this.revalidate();
        this.repaint();
        opacityTime.start();
    }

    private void handleSubmit(){

        System.out.println(options.getSelectedItem());
        boolean inList = false;
        try {
            if(addPlaceBox.getText().equals("Subway Mode")){ //testing mode
                
                timeDisplayer.time[0] = LocalTime.now().getHour();
                timeDisplayer.time[1] = LocalTime.now().getMinute();
                timeDisplayer.timeZoneString = "-4";
                timeDisplayer.timeText.setText(timeDisplayer.time[0] + ":" + timeDisplayer.time[1] + " UTC -4");
                timeDisplayer.timeInPlaceText.setText("Place Specified: Washington, Canada");

                addTime = new JButton ("Add Time");
                addTime.setSize(new Dimension(60, 50));
                addTime.addActionListener(this);
                northPanel.add(addTime);
            }
            System.out.println("hhhhhh");

            Place newPlace = timeDisplayer.timeGetter.findPlace(addPlaceBox.getText());
            for (int elm = 0; elm < options.getItemCount(); elm++){ // check if the place isn't already on the list
                if (newPlace.equals(options.getItemAt(elm))){
                    inList = true;
                    break;
                }
            }

            if (inList){ // if place is alreayd in list, add nothing
                System.out.println("place already there");
                addPlaceBox.setText("Place Given Had Already Been Added");

            } else { // adding said place
                System.out.println(newPlace.latLong[0] + ", " + newPlace.latLong[1]);
                addPlaceBox.setText("Place Given Successfully Added");
                options.addItem(newPlace);
                removeButton.setEnabled(true);
            }
            
        } catch (placeNotFoundException e1) {
            // TODO Auto-generated catch block
            System.out.println(e1.getMessage());
            addPlaceBox.setText("Place Was Unable to be Added");
        }
        this.revalidate();
        this.repaint();
    }

    private void handleRemove(){
        System.out.println("remove");
        options.removeItem(options.getSelectedItem());
        if (options.getItemCount() == 1){ // if there is only one place left, disable the remove button
            removeButton.setEnabled(false);
        }     
    }

    private void handleMinute(){
        //System.out.println(timeDisplayer.timeText.getText());
        if (LocalTime.now().getMinute() > start && (!timeDisplayer.timeText.getText().equals("Time placed here"))){
            timeDisplayer.changeTime();
            System.out.println(timeDisplayer.time[1]);
            if (timeDisplayer.time[0] >= 19 || timeDisplayer.time[0] < 7){
                changeBackground(true);
            } else if (timeDisplayer.time[0] >= 7 && timeDisplayer.time[0] < 19 ){
                changeBackground(false);
            }
            start=LocalTime.now().getMinute();

        }
        this.revalidate();
        this.repaint();
    }

    private void handleOptions(){
        try {
            // changes the city to the one that was selected
            boolean temp = timeDisplayer.changeCity((Place)options.getSelectedItem());
            if (timeDisplayer.time[0] >= 19 || timeDisplayer.time[0] < 7){
                changeBackground(true);
            } else if (timeDisplayer.time[0] >= 7 && timeDisplayer.time[0] < 19 ){
                changeBackground(false);
            }
            this.revalidate();
            this.repaint();                    

        } catch (IOException | ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }            

    } 
    @Override
    public void windowActivated(WindowEvent e) { // when you bring window back up
        // TODO Auto-generated method stub
        System.out.println("window activated");
    }

    @Override
    public void windowClosed(WindowEvent e) { //what happens after closing
        // TODO Auto-generated method stub
        System.out.println("window closed");
    }

    @Override
    public void windowClosing(WindowEvent e) { //what happens between clicking the X button
        // TODO Auto-generated method stub
        System.out.println("window closing");
        try {
            timeDisplayer.timeGetter.saveToFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.exit(1);
    }

    @Override
    public void windowDeactivated(WindowEvent e) { //when you minimize the window
        // TODO Auto-generated method stub
        System.out.println("window deactivated");
    }

    @Override
    public void windowDeiconified(WindowEvent e) { //when tab not on taskbar
        // TODO Auto-generated method stub
        System.out.println("window deiconed");
    }

    @Override
    public void windowIconified(WindowEvent e) { //when the tab turns into an icon on the taskbar
        // TODO Auto-generated method stub
        System.out.println("window icondized");
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        System.out.println("window opened");
    }
}