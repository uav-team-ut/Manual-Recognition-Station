package org.uavteam;

import org.uavteam.TargetData;

import java.awt.*;

/**
 * Created by James on 6/5/2017.
 */
public class ImageData {
    Image img;
    TargetData target;
    double lat;
    double lon;
    double alt;
    double width;// or perhaps horizontal fov
    double height;
    double yaw;
    double tx;
    double ty;
    public ImageData(Image img, double lat, double lon, double alt, double width, double height, double yaw){
        this.img=img;
        this.lat=lat;
        this.lon=lon;
        this.alt=alt;
        this.width=width;
        this.height=height;
        this.yaw=yaw;
        tx=0;
        ty=0;
        target = new TargetData(lat,lon);
    }
    public void setTargetRotation(int x, int y){
        double rot=Math.atan2(y-ty,x-tx);
        String dirString="";
        int dir=(int)Math.round((rot+yaw>Math.PI*2?rot+yaw-Math.PI*2:rot+yaw));
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
        double xc=(x1+x2)/2.0;
        double yc=(y1+y2)/2.0;
        //do math to convert lat and lon
        double targetLat=lat;
        double targetLon=lon;
        //crop out target
        Image imgCropped=img;

        target.addTarget(imgCropped,targetLat,targetLon);


    }
    public void setTargetCenter(int x, int y){
        this.tx=x;
        this.ty=y;
    }
    public TargetData getTarget(){
        return target;
    }
}
