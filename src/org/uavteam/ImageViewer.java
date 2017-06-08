package org.uavteam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by James on 6/5/2017.
 */
public class ImageViewer {//TODO: add ability to clear screen, go back an image, etc

    static String imageDir="C:\\images_unprocessed";
    static String processedDir="C:\\images_processed";
    static String targetDir="C:\\targets";
    public static void main(String args[]){
        //open swing window
        //start loading images
        //listen to keyboard input to see whether to go to next or stop and find target
        //on find target, all user to crop target, specify rotation of letter with a dot, and add metadata to a form on the side
        //allow user to clear screen or go back if need be
        //give submit button capabilities
        JFrame frame=new JFrame("Manual Recognition Station");
        ImageViewerGUI ivg=new ImageViewerGUI();
        frame.setContentPane(ivg.ImageViewerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
        frame.requestFocus();
        ivg.start();
    }
    public static ImageData getNextImage(){
        File dir=new File(imageDir);
        File[] fileList=dir.listFiles(new FileFilter(){
            public boolean accept(File file)
            {
                return file.getAbsolutePath().toLowerCase().endsWith(".png");
            }
        });
        try {
            File imageFile=fileList[0];
            File dataFile=new File(fileList[0].getAbsolutePath().substring(0,fileList[0].getAbsolutePath().length()-3)+"txt");
            BufferedImage img = ImageIO.read(imageFile);
            FileReader fr = new FileReader(dataFile);
            BufferedReader br = new BufferedReader(fr);

            ImageData imageData= new ImageData(img,Double.parseDouble(br.readLine()),Double.parseDouble(br.readLine()),Double.parseDouble
                    (br.readLine()),Double.parseDouble(br.readLine()),Double.parseDouble(br.readLine()),Double.parseDouble
                    (br.readLine()),fileList[0].getName().substring(5,fileList[0].getName().length()-4));
            br.close();
            fr.close();
            Files.move(imageFile.toPath(),Paths.get(processedDir+"\\"+imageFile.getName()),REPLACE_EXISTING);
            Files.move(dataFile.toPath(),Paths.get(processedDir+"\\"+dataFile.getName()),REPLACE_EXISTING);
            return imageData;
        }catch(IOException e){e.printStackTrace();}
        return null;
    }
    public static void submitImage(TargetData target){
        try {
            ImageIO.write(target.getImage(), "png", new File(targetDir+"\\target" + target.getId() + ".jpg"));
            FileWriter fw = new FileWriter(targetDir+"\\target"+target.getId()+".txt");
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
        }catch(IOException e){e.printStackTrace();}
    }
}
