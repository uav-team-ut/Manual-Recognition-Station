package org.uavteam;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by jtb20 on 6/5/2017.
 */
public class TargetData {
    BufferedImage target;
    double lat;
    double lon;
    String rotation;
    String shape;
    String shapeColor;
    String letterColor;
    String letter;
    String id;
    String type;
    public TargetData(BufferedImage t, double lat, double lon, String rot, String shape, String sColor, String letter, String lColor, String id, String type){
        target=t;
        this.lat=lat;
        this.lon=lon;
        rotation=rot;
        this.shape=shape;
        this.shapeColor=sColor;
        this.letter=letter;
        this.letterColor=lColor;
        this.id=id;
        this.type=type;
    }
    public TargetData(double lat, double lon, String id) {
        target = null;
        this.lat = lat;
        this.lon = lon;
        rotation = "n";
        this.shape = "Semicircle";
        this.shapeColor = "Orange";
        this.letter = "A";
        this.letterColor = "White";
        this.id=id;
        type="standard";
    }
    public void addTarget(BufferedImage t ){
        target=t;
    }
    public void setLocation(double lat, double lon){
        this.lat=lat;
        this.lon=lon;
    }
    public void setRotation(String s) {
        rotation=s;
    }
    public void setMetaData(String s, String sColor, String l, String lColor, String t){
        shape=s.toLowerCase();
        shapeColor=sColor.toLowerCase();
        letter=l.toLowerCase();
        letterColor=lColor.toLowerCase();
        t=t.toLowerCase();
        if(t.equals("standard")||t.equals("emergent")||t.equals("off-axis")) {
            type = t;
        }
    }
    public String getRotation(){return rotation;}
    public String getShape(){return shape;}
    public String getShapeColor(){return shapeColor;}
    public String getLetterColor(){return letterColor;}
    public String getLetter(){return letter;}
    public double getLat(){return lat;}
    public double getLon(){return lon;}
    public BufferedImage getImage(){return target;}
    public String getId(){return id;}
}
