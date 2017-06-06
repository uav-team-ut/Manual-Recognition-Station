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
    public ImageData(Image img, double lat, double lon, double alt, double width, double height){
        this.img=img;
        this.lat=lat;
        this.lon=lon;
        this.alt=alt;
        this.width=width;
        this.height=height;
        target=null;
    }
    public void createTarget(double rot, String shape, String sColor, String letter, String lColor){
        target=new TargetData(lat,lon,rot,shape,sColor,letter,lColor);
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
}
