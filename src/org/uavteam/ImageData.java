package org.uavteam;

import org.uavteam.TargetData;

import java.awt.*;
import java.awt.image.BufferedImage;
//TODO: Setup target cropping and localizing

/**
 * Created by James on 6/5/2017.
 */
public class ImageData {
    BufferedImage img;
    TargetData target;
    double lat;
    double lon;
    double alt;
    double width;// or perhaps horizontal fov
    double height;
    double yaw;
    double tx;
    double ty;
    String id;
    public ImageData(BufferedImage img, double lat, double lon, double alt, double width, double height, double yaw, String id){
        this.img=img;
        this.lat=lat;
        this.lon=lon;
        this.alt=alt;
        this.width=width;
        this.height=height;
        this.yaw=yaw;
        tx=0;
        ty=0;
        this.id=id;
        target = new TargetData(lat,lon, id);
    }
    public void setTargetRotation(int x, int y){
        double rot=Math.atan2(ty-y,x-tx);
        String dirString="";
        double dir=rot+yaw>Math.PI*2?rot+yaw-Math.PI*2:rot+yaw;
        if(dir>Math.PI*15/8||dir<Math.PI/8)
            dirString="e";
        else if(dir>Math.PI/8||dir<Math.PI*3/8)
            dirString="ne";
        else if(dir>Math.PI*3/8||dir<Math.PI*5/8)
            dirString="n";
        else if(dir>Math.PI*5/8||dir<Math.PI*7/8)
            dirString="nw";
        else if(dir>Math.PI*7/8||dir<Math.PI*9/8)
            dirString="w";
        else if(dir>Math.PI*9/8||dir<Math.PI*11/8)
            dirString="sw";
        else if(dir>Math.PI*11/8||dir<Math.PI*13/8)
            dirString="s";
        else
            dirString="se";
        target.setRotation(dirString);
    }
    public void setTargetMetaData(String shape, String sColor, String letter, String lColor) {
        target.setMetaData(shape, sColor, letter, lColor);
    }
    public void cropTarget(int x1, int y1, int x2, int y2){//FIXME
        //crop out target
        BufferedImage imgCropped=img.getSubimage(x1,y1,x2-x1,y2-y1);

        target.addTarget(imgCropped);


    }
    public void setTargetCenter(int x, int y){//FIXME
        this.tx=x;
        this.ty=y;

        //do math to convert lat and lon from image center to target center
        double targetLat=lat;
        double targetLon=lon;
        target.setLocation(targetLat,targetLon);
    }
    public String getId(){return id;}
    public BufferedImage getImage(){return img;}
    public double getLon(){return lon;}
    public double getLat(){return lat;}
    public TargetData getTarget(){
        return target;
    }
}
