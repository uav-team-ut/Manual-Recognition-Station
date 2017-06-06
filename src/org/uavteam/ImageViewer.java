package org.uavteam;

import javax.swing.*;

/**
 * Created by James on 6/5/2017.
 */
public class ImageViewer {//TODO: add ability to clear screen, go back an image, etc

    public static void main(String args[]){
        //open swing window
        //start loading images
        //listen to keyboard input to see whether to go to next or stop and find target
        //on find target, all user to crop target, specify rotation of letter with a dot, and add metadata to a form on the side
        //allow user to clear screen or go back if need be
        //give submit button capabilities
        JFrame frame=new JFrame("Manual Recognition Station");
        frame.setContentPane(new ImageViewerGUI().ImageViewerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
    }
    public static ImageData getNextImage(){
        return null;
    }
    public static void submitImage(TargetData target){

    }
}
