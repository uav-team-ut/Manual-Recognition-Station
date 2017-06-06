package org.uavteam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by jtb20 on 6/5/2017.
 */
public class ImageViewerGUI {
    private JPanel ImagePanel;
    private JPanel DataPanel;
    public JPanel ImageViewerPanel;
    private JButton SubmitButton;
    private JTextField LetterField;
    private JComboBox ShapeBox;
    private JComboBox ShapeColorBox;
    private JTextField LatitudeField;
    private JTextField LongitudeField;
    private JComboBox LetterColorBox;

    ImageData currImage;
    BufferStrategy bs;
    Canvas c;
    BufferedImage bufferImage;
    Graphics2D buffer;
    int state;
    Point topLeft;


    public ImageViewerGUI() {
        ImageViewerPanel.addKeyListener(new KeyAdapter(){
            //if right arrow and state zero, go to next image
        });
        ImagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(state){
                    case 0:
                        //want to process
                        state=1;
                        break;
                    case 1:
                        //center
                        currImage.setTargetCenter((int)e.getPoint().getX(),(int)e.getPoint().getY());
                        state=2;
                        break;
                    case 2:
                        //top left corner
                        topLeft.setLocation(e.getPoint());//x and y of mouseclick

                        state=3;
                        break;
                    case 3:
                        //bottom right corner
                        currImage.cropTarget((int)topLeft.getX(),(int)e.getPoint().getX(),(int)topLeft.getY(),(int)e.getPoint().getY());
                        topLeft.setLocation(0,0);
                        state=4;
                        break;
                    case 4:
                        //direction
                        currImage.setTargetRotation((int)e.getPoint().getX(),(int)e.getPoint().getY());
                        state=5;
                        break;
                    case 5:
                        //nothing
                        break;
                }
            }
        });
        SubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(state==5){
                    currImage.setTargetMetaData(ShapeBox.getSelectedItem().toString(),ShapeColorBox.getSelectedItem().toString(),LetterField.getText(),LetterColorBox.getSelectedItem().toString());
                    ImageViewer.submitImage(currImage.getTarget());
                    state=0;
                }
            }
        });
        //setup the preliminary data or read it from a file
        initLetterColorBox();
        initShapeBox();
        initShapeColorBox();


        state=0;
        c=new Canvas();
        c.setPreferredSize(new Dimension(ImagePanel.getWidth(),this.ImagePanel.getHeight()));
        c.createBufferStrategy(2);
        bs=c.getBufferStrategy();
        bufferImage=new BufferedImage(ImagePanel.getWidth(),ImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
        buffer=bufferImage.createGraphics();
        buffer.setBackground(Color.lightGray);
        currImage=null;
        topLeft.setLocation(0,0);
    }
    public void setImagePanel(Image img){
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        g.drawImage(img,0,0,ImageViewerPanel.getParent());
    }
    public void drawCircle(Point p,int radius){
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        g.setColor(Color.red);
        g.fillOval((int)p.getX()-radius/2,(int)p.getY()-radius/2,radius*2,radius*2);
    }
    public void drawRect(Point p1,Point p2){
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        g.setColor(Color.red);
        g.drawRect((int)p1.getX(),(int)p1.getY(),(int)(p2.getX()-p1.getX()),(int)(p2.getY()-p1.getY()));
    }
    public void setLatitudeField(String s){
        LatitudeField.setText(s);
    }
    public void setLongitudeField(String s) {
        LongitudeField.setText(s);
    }
    public void setCurrImage(ImageData img)
    {

    }
    private void initShapeBox(){
        ShapeBox.addItem("Circle");
        ShapeBox.addItem("Square");
        ShapeBox.addItem("Triangle");
        ShapeBox.addItem("Star");
        ShapeBox.addItem("Pentagon");
        ShapeBox.addItem("Trapezoid");
        ShapeBox.addItem("Semicircle");
        ShapeBox.addItem("Rectangle");
        ShapeBox.addItem("Diamond");

    }
    private void initShapeColorBox(){
        ShapeBox.addItem("Red");
        ShapeBox.addItem("Green");
        ShapeBox.addItem("Orange");
        ShapeBox.addItem("Black");
        ShapeBox.addItem("Yellow");
        ShapeBox.addItem("Blue");
        ShapeBox.addItem("Purple");
        ShapeBox.addItem("White");
        ShapeBox.addItem("Grey");

    }
    private void initLetterColorBox(){
        ShapeBox.addItem("Red");
        ShapeBox.addItem("Green");
        ShapeBox.addItem("Orange");
        ShapeBox.addItem("Black");
        ShapeBox.addItem("Yellow");
        ShapeBox.addItem("Blue");
        ShapeBox.addItem("Purple");
        ShapeBox.addItem("White");
        ShapeBox.addItem("Grey");
    }
}
