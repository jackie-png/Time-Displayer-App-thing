package com.example;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;

class Background extends JPanel{

    Color DAY_COLOUR = new Color (0, 100, 255, 100);
    Color NIGHT_COLOUR = new Color (0, 0, 10, 100);
    BufferedImage[] timeBackground = new BufferedImage[2];
    boolean changingDay = false;
    boolean dayOrNight = true;
    float opacity = 1.0f;

    Background(){
        loadImages();
        this.setBackground(DAY_COLOUR); 
        this.setBounds(new Rectangle(new Dimension (1000, 750)));
        this.setVisible(true);
    }

    private void loadImages(){
        try {
            timeBackground[0] = ImageIO.read(getClass().getResource("Day Dev v2.png"));
            timeBackground[1] = ImageIO.read(getClass().getResource("Night Dev V2.png"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.drawImage(timeBackground[1], 0, 0, null);
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2D.drawImage(timeBackground[0],0,0,null);

    }
}