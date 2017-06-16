package org.uavteam;

import org.uavteam.TargetData;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;
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
    public ImageData(BufferedImage img, double lat, double lon, double width, double height, String id){
        this.img=img;
        this.lat=lat;
        this.lon=lon;
        this.width=width;
        this.height=height;
        tx=0;
        ty=0;
        this.id=id;
        target = new TargetData(lat,lon, id);
    }
    public void setTargetRotation(int x, int y){
        double dir= atan2(ty-y,x-tx);
        String dirString="";
        if(dir<0)
            dir=dir+2*Math.PI;
        if(dir>Math.PI*15/8||dir<Math.PI/8)
            dirString="e";
        else if(dir>Math.PI/8&&dir<Math.PI*3/8)
            dirString="ne";
        else if(dir>Math.PI*3/8&&dir<Math.PI*5/8)
            dirString="n";
        else if(dir>Math.PI*5/8&&dir<Math.PI*7/8)
            dirString="nw";
        else if(dir>Math.PI*7/8&&dir<Math.PI*9/8)
            dirString="w";
        else if(dir>Math.PI*9/8&&dir<Math.PI*11/8)
            dirString="sw";
        else if(dir>Math.PI*11/8&&dir<Math.PI*13/8)
            dirString="s";
        else
            dirString="se";
        target.setRotation(dirString);
    }
    public void setTargetMetaData(String shape, String sColor, String letter, String lColor, String t) {
        target.setMetaData(shape, sColor, letter, lColor, t);
    }
    public void cropTarget(int x1, int y1, int x2, int y2){//FIXME
        //crop out target
        BufferedImage imgCropped=img.getSubimage(x1,y1,x2-x1,y2-y1);

        target.addTarget(imgCropped);


    }
    public void setTargetCenter(int x, int y){//FIXME
        this.tx=x;
        this.ty=y;
        double a=6378137;
        double b=6356752;
        lat=Math.toRadians(lat);
        lon=Math.toRadians(lon);
        double rs=a*b/(Math.sqrt(b*b*pow(cos(lat),2)+a*a*pow(sin(lat),2)));
        double lambda=atan2(b*b*sin(lat),a*a*cos(lat));
        double locX=rs*cos(lambda)*cos(lon)+alt*cos(lat)*cos(lon);
        double locY=rs*cos(lambda)*sin(lon)+alt*cos(lat)*sin(lon);
        double locZ=rs*sin(lambda)+alt*sin(lat);
        double pi = Math.PI;

        double ratioX=1.0*x/img.getWidth()-.5;
        double ratioY=.5-1.0*y/img.getHeight();

        double x_enu=locX*(cos(lon + pi/2))+locY*(-cos(lat - pi/2)*sin(lon + pi/2))+locZ*(-sin(lat - pi/2)*sin(lon + pi/2));
        double y_enu=locX*(sin(lon + pi/2))+locY*(cos(lat - pi/2)*cos(lon + pi/2))+locZ*(cos(lon + pi/2)*sin(lat - pi/2));
        double z_enu=locY*(-sin(lat - pi/2))+locZ*(cos(lat - pi/2));

        x_enu+=width*cos(yaw)*ratioX+height*sin(yaw)*ratioY;
        y_enu+=width*(-sin(yaw))*ratioX+height*cos(yaw)*ratioY;

        locX=x_enu*(cos(lon + pi/2))+y_enu*(sin(lon + pi/2));
        locY=x_enu*(-cos(lat - pi/2)*sin(lon + pi/2))+y_enu*( cos(lat - pi/2)*cos(lon + pi/2))+z_enu*(-sin(lat - pi/2));
        locZ=x_enu*(-sin(lat - pi/2)*sin(lon + pi/2))+y_enu*(cos(lon + pi/2)*sin(lat - pi/2))+z_enu*(cos(lat - pi/2));

        double f=1/298.257223563;
        double e=sqrt(f*(2-f));
        double ep= sqrt(e*e/(1-e*e));
        double rho=sqrt(locX*locX+locY*locY);
        double theta=atan2(a*locZ,rho*b);
        double targetLon=atan2(locY,locX)*180/Math.PI;
        double targetLat=atan2(locZ+ep*ep*b*pow(sin(theta),3),rho-e*e*a*pow(cos(theta),3))*180/Math.PI;

        System.out.println(targetLat+"\t"+targetLon);
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
