package org.uavteam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
import sun.net.www.http.HttpClient;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javafx.css.StyleOrigin.USER_AGENT;

/**
 * Created by James on 6/5/2017.
 */
public class ImageViewer {//TODO: add ability to clear screen, go back an image, etc

    static String imageDir="C:\\images_unprocessed";
    static String processedDir="C:\\images_processed";
    static String targetDir="C:\\targets";
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
        frame.setPreferredSize(new Dimension(1400,800));
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
//        while(true) {
//            try {
//                URL url = new URL("http://127.0.0.1:25005/api/images?autonomous=false&count=1");//FIXME
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                //do I need a header?
//
//                int responseCode = con.getResponseCode();
//                System.out.println("\nSending 'GET' request to URL : " + url);
//                System.out.println("Response Code : " + responseCode);
//
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                JSONObject jsonImage = new JSONObject(response);
//                if(!jsonImage.has("data_warped"))
//                    continue;
//                BASE64Decoder bd = new BASE64Decoder();
//                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bd.decodeBuffer(jsonImage.getString("data_warped"))));
//                return new ImageData(image, jsonImage.getDouble("lat"), jsonImage.getDouble("lon"), jsonImage.getDouble("width"), jsonImage.getDouble("height"), "" + count++);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        File dir=new File(imageDir);
        File[] fileList=dir.listFiles(new FileFilter(){
            public boolean accept(File file)
            {
                return file.getAbsolutePath().toLowerCase().endsWith(".png");
            }
        });
        try {
            File imageFile=fileList[0];
         //   File dataFile=new File(fileList[0].getAbsolutePath().substring(0,fileList[0].getAbsolutePath().length()-3)+"txt");
            BufferedImage img = ImageIO.read(imageFile);
         //   FileReader fr = new FileReader(dataFile);
        //    BufferedReader br = new BufferedReader(fr);
            imageData=new ImageData(img,10.5,11.5,0,0,fileList[0].getName().substring(6,fileList[0].getName().length()-4));
//           ImageData imageData= new ImageData(img,Double.parseDouble(br.readLine()),Double.parseDouble(br.readLine()),Double.parseDouble
//                    (br.readLine()),Double.parseDouble(br.readLine()),fileList[0].getName().substring(5,fileList[0].getName().length()-3));
            //br.close();
           // fr.close();
            Files.move(imageFile.toPath(),Paths.get(processedDir+"\\"+imageFile.getName()),REPLACE_EXISTING);
           // Files.move(dataFile.toPath(),Paths.get(processedDir+"\\"+dataFile.getName()),REPLACE_EXISTING);
            return imageData;
        }catch(IOException e){e.printStackTrace();}
        return null;
    }
    public static void submitImage(TargetData target){
//        try{
//            BASE64Encoder be = new BASE64Encoder();
//            ByteArrayOutputStream baos=new ByteArrayOutputStream();
//            ImageIO.write(target.getImage(),"png",baos);
//            String imageString=be.encode(baos.toByteArray());
//            String jsonStringNoData = "\",\"type\":\""+target.getType()+"\",\"latitude\":"+target.getLat()+",\"longitude\":"+target.getLon()+",\"orientation\":\""+target.getRotation() +
//                    "\",\"shape\":\""+target.getShape()+"\",\"background_color\":\""+target.getShapeColor()+"\",\"alphanumeric\":\""
//                    +target.getLetter()+"\",\"alphanumeric_color\":\""+target.getLetterColor()
//                    +"\",\"autonomous\":false}";
//            String jsonString="{\"data\":\"";
//            String temp="";
//            for(int i=0;i<imageString.length();i++){
//                if(imageString.charAt(i)==13 || imageString.charAt(i)==10){
//                    continue;//lmao?
//                }
//                jsonString+=imageString.charAt(i);
//                temp+=imageString.charAt(i);
//            }
//            jsonString+=jsonStringNoData;
            //BASE64Decoder bd = new BASE64Decoder();
            //BufferedImage image = ImageIO.read(new ByteArrayInputStream(bd.decodeBuffer(temp)));
          //  System.out.println((int)jsonString.charAt(84)+"\t"+(int)jsonString.charAt(85)+"\t"+(int)jsonString.charAt(86));
           // System.out.println(jsonString);
//            byte[] postData=jsonString.getBytes(StandardCharsets.UTF_8);
//            URL url = new URL("http://127.0.0.1:25005/api/targets");//FIXME
//
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setDoOutput(true);
//            con.setInstanceFollowRedirects(false);
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type","application/json");
//            con.setRequestProperty("Content-Length",""+postData.length);
//            con.setRequestProperty("charset","utf-8");
//            DataOutputStream wr=new DataOutputStream(con.getOutputStream());
//            wr.write(postData);
//            System.out.println(con.getResponseCode());
//        }catch(Exception e){e.printStackTrace();}
        try {
            ImageIO.write(target.getImage(), "png", new File(targetDir+"\\target" + target.getId() + ".png"));
            FileWriter fw = new FileWriter(targetDir+"\\target-"+target.getId()+".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(""+target.getLat());
            bw.newLine();
            bw.write(""+target.getLon());
            bw.newLine();
            bw.write(""+target.getRotation());
            bw.newLine();
            bw.write(""+target.getShape());
            bw.newLine();
            bw.write(""+target.getShapeColor());
            bw.newLine();
            bw.write(""+target.getLetter());
            bw.newLine();
            bw.write(""+target.getLetterColor());
            bw.close();
            fw.close();
            previousImageData=imageData;
            atPrevious=true;
        }catch(IOException e){e.printStackTrace();}
    }
    public static ImageData getPreviousImage()
    {
        atPrevious=true;
        return previousImageData;
    }
}
