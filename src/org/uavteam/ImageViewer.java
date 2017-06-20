package org.uavteam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javafx.css.StyleOrigin.USER_AGENT;

/**
 * Created by James on 6/5/2017.
 */
public class ImageViewer {//TODO: add ability to clear screen, go back an image, etc

    static String imageDir="C:\\images_unprocessed";
    static String processedDir="C:\\images_processed";
    static String targetDir="C:\\targets";
    static String baseIp = "http://192.168.0.13:25005";
    static ImageData imageData,previousImageData;
    static int count;
    static boolean atPrevious;
    public static void main(String args[]){
        //open swing window
        //start loading images
        //listen to keyboard input to see whether to go to next or stop and find target
        //on find target, all user to crop target, specify rotation of letter with a dot, and add metadata to a form on the side
        //allow user to clear screen or go back if need be
        //give submit button capabilities
        JFrame frame=new JFrame("Manual Recognition Station");
        frame.setPreferredSize(new Dimension(1280,1000));
        ImageViewerGUI ivg=new ImageViewerGUI();
        frame.setContentPane(ivg.ImageViewerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();


        count=0;
        atPrevious=false;

        frame.setVisible(true);
        frame.requestFocus();
        ivg.start();
    }
    public static ImageData getNextImage(){
        if(atPrevious)
        {
            atPrevious=false;
            return imageData;
        }
         while(true) {
            try {
                URL url = new URL(baseIp+"/api/images?processed_manual=false&limit=1");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                //do I need a header?

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonImage = new JSONObject(response.substring(1,response.length()-1));
                if(!jsonImage.has("data_warped"))
                    continue;
                BASE64Decoder bd = new BASE64Decoder();
                int imgX=1280;
                int imgY=720;
                //BufferedImage i2=ImageIO.read(new ByteArrayInputStream(bd.decodeBuffer(jsonImage.getString("data_warped"))));
                BufferedImage image = new BufferedImage(imgX,imgY,BufferedImage.TYPE_INT_ARGB_PRE);
                BufferedImage fromServer= ImageIO.read(new ByteArrayInputStream(bd.decodeBuffer(jsonImage.getString("data_warped"))));
                Graphics2D g = image.createGraphics();
                g.setComposite(AlphaComposite.Src);
                double ratio=1.0*fromServer.getWidth()/imgX>1.0*fromServer.getHeight()/imgY?1.0*fromServer.getHeight()/imgY:1.0*fromServer.getWidth()/imgX;
                AffineTransform at =AffineTransform.getScaleInstance(1/ratio,1/ratio);
                g.drawRenderedImage(fromServer,at);


                String postDataString="{\"processed_manual\":true}";
                byte[] postData=postDataString.getBytes(StandardCharsets.UTF_8);
                URL url2 = new URL(baseIp+"/api/images/"+jsonImage.getString("_id"));
                HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                con2.setDoOutput(true);
                con2.setRequestMethod("PUT");
                con2.setRequestProperty("Content-Type","application/json");
                con2.setRequestProperty("Content-Length",""+postData.length);
                con2.setRequestProperty("charset","utf-8");
                DataOutputStream wr=new DataOutputStream(con2.getOutputStream());
                wr.write(postData);
                System.out.println("\nSending 'PATCH' request to URL : " + url2);
                System.out.println("Response Code : " + con2.getResponseCode());
                return new ImageData(image, jsonImage.getDouble("lat"), jsonImage.getDouble("lon"), jsonImage.getDouble("width"), jsonImage.getDouble("height"),0, "" + count++);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
//        File dir=new File(imageDir);
//        File[] fileList=dir.listFiles(new FileFilter(){
//            public boolean accept(File file)
//            {
//                return file.getAbsolutePath().toLowerCase().endsWith(".png");
//            }
//        });
//        try {
//            File imageFile=fileList[0];
//           // File dataFile=new File(fileList[0].getAbsolutePath().substring(0,fileList[0].getAbsolutePath().length()-3)+"txt");
//            BufferedImage img = new BufferedImage(1280,720,BufferedImage.TYPE_INT_ARGB_PRE);
//            //image=(BufferedImage)i2.getScaledInstance(1024,576,Image.SCALE_DEFAULT);
//            Graphics2D g = img.createGraphics();
//            g.setComposite(AlphaComposite.Src);
//            AffineTransform at =AffineTransform.getScaleInstance(1920/1024,1080/576);
//            g.drawRenderedImage(ImageIO.read(imageFile),at);
//         //   FileReader fr = new FileReader(dataFile);
//        //    BufferedReader br = new BufferedReader(fr);
//            imageData=new ImageData(img,10.5,11.5,0,0,fileList[0].getName().substring(6,fileList[0].getName().length()-4));
////           ImageData imageData= new ImageData(img,Double.parseDouble(br.readLine()),Double.parseDouble(br.readLine()),Double.parseDouble
////                    (br.readLine()),Double.parseDouble(br.readLine()),fileList[0].getName().substring(5,fileList[0].getName().length()-3));
//            //br.close();
//           // fr.close();
//            Files.move(imageFile.toPath(),Paths.get(processedDir+"\\"+imageFile.getName()),REPLACE_EXISTING);
//          //  Files.move(dataFile.toPath(),Paths.get(processedDir+"\\"+dataFile.getName()),REPLACE_EXISTING);
//        return imageData;
//        }catch(IOException e){e.printStackTrace();}
//        return null;
    }
    public static void submitImage(TargetData target){
        try{
            BASE64Encoder be = new BASE64Encoder();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(target.getImage(),"png",baos);
            String imageString=be.encode(baos.toByteArray());
            String jsonStringNoData="";
            if(target.getType().equals("standard")) {
                jsonStringNoData = "\",\"type\":\"" + target.getType() + "\",\"latitude\":" + target.getLat() + ",\"longitude\":" + target.getLon() + ",\"orientation\":\"" + target.getRotation() +
                        "\",\"shape\":\"" + target.getShape() + "\",\"background_color\":\"" + target.getShapeColor() + "\",\"alphanumeric\":\""
                        + target.getLetter() + "\",\"alphanumeric_color\":\"" + target.getLetterColor()
                        + "\",\"autonomous\":false}";
            }
            else if(target.getType().equals("emergent")){
                jsonStringNoData = "\",\"type\":\"" + target.getType() + "\",\"latitude\":" + target.getLat() + ",\"longitude\":" + target.getLon() + ",\"description\":\""+target.getDesc() +"\"}";
            }
            String jsonString="{\"data\":\"";
            //remove carriage returns
            for(int i=0;i<imageString.length();i++){
                if(imageString.charAt(i)==13 || imageString.charAt(i)==10){
                    continue;//lmao?
                }
                jsonString+=imageString.charAt(i);
            }
            jsonString+=jsonStringNoData;
            byte[] postData=jsonString.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseIp+"/api/targets");//FIXME

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Content-Length",""+postData.length);
            con.setRequestProperty("charset","utf-8");
            DataOutputStream wr=new DataOutputStream(con.getOutputStream());
            wr.write(postData);
            System.out.println(con.getResponseCode());
        }catch(Exception e){e.printStackTrace();}
//        try {
//            ImageIO.write(target.getImage(), "png", new File(targetDir+"\\target" + target.getId() + ".png"));
//            FileWriter fw = new FileWriter(targetDir+"\\target-"+target.getId()+".txt");
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write("{\"type\": \""+target.getType()+"\",");
//            bw.write("\"latitude\": "+target.getLat()+",");
//            bw.write("\"longitude\": "+target.getLon()+",");
//            if(target.getType().equals("standard")) {
//                bw.write("\"orientation\": \"" + target.getRotation()+"\",");
//                bw.write("\"shape\": \"" + target.getShape()+"\",");
//                bw.write("\"background_color\": \"" + target.getShapeColor()+"\",");
//                bw.write("\"alphanumeric\": \"" + target.getLetter()+"\",");
//                bw.write("\"alphanumeric_color\": \"" + target.getLetterColor());
//            }
//            else if(target.getType().equals("emergent")){
//                bw.write("\"description: \""+target.getDesc());
//            }
//            bw.close();
//            fw.close();
//            previousImageData=imageData;
//            atPrevious=true;
//        }catch(IOException e){e.printStackTrace();}
    }
    public static ImageData getPreviousImage()
    {
        atPrevious=true;
        return previousImageData;
    }
}
