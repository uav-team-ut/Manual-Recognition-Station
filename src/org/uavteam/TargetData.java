package org.uavteam;

import java.awt.*;


/**
 * Created by jtb20 on 6/5/2017.
 */
public class TargetData {
    Image target;
    double lat;
    double lon;
    String rotation;
    String shape;
    String shapeColor;
    String letterColor;
    String letter;
    public TargetData(Image t, double lat, double lon, String rot, String shape, String sColor, String letter, String lColor){
        target=t;
        this.lat=lat;
        this.lon=lon;
        rotation=rot;
        this.shape=shape;
        this.shapeColor=sColor;
        this.letter=letter;
        this.letterColor=lColor;
    }
    public TargetData(double lat, double lon) {
        target = null;
        this.lat = lat;
        this.lon = lon;
        rotation = "";
        this.shape = "Semicircle";
        this.shapeColor = "Orange";
        this.letter = "A";
        this.letterColor = "White";
    }
    public void addTarget(Image t ){
        target=t;
    }
    public void setLocation(double lat, double lon){
        this.lat=lat;
        this.lon=lon;
    }
    public void setRotation(String s) {
        rotation=s;
    }
    public void setMetaData(String s, String sColor, String l, String lColor){
        shape=s;
        shapeColor=sColor;
        letter=l;
        letterColor=lColor;
    }
    public String getRotation(){return rotation;}
    public String getShape(){return shape;}
    public String getShapeColor(){return shapeColor;}
    public String getLetterColor(){return letterColor;}
    public String getLetter(){return letter;}
    public double getLat(){return lat;}
    public double getLon(){return lon;}
    public Image getTarget(){return target;}
}
