package org.uavteam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by jtb20 on 6/5/2017.
 */
public class DrawablePanel extends JPanel {
    BufferStrategy bs;
    Canvas c;
    BufferedImage bufferImage;
    Graphics2D buffer;
    int state;
    public DrawablePanel(){
        state=0;
        c=new Canvas();
        c.setPreferredSize(new Dimension(getWidth(),this.getHeight()));
        c.createBufferStrategy(2);
        bs=c.getBufferStrategy();
        bufferImage=new BufferedImage(getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);
        buffer=bufferImage.createGraphics();
        buffer.setBackground(Color.lightGray);
        buffer.setColor(Color.red);
        buffer.clearRect(0,0,getWidth(),this.getHeight());
    }

}
