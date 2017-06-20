package org.uavteam;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by jtb20 on 6/5/2017.
 */
public class ImageViewerGUI {
    public DrawablePanel ImagePanel;
    private JPanel DataPanel;
    public JPanel ImageViewerPanel;
    private JButton SubmitButton;
    private JButton UndoButton;
    private JButton PreviousButton;
    private JTextField LetterField;
    private JComboBox ShapeBox;
    private JComboBox ShapeColorBox;
    private JTextField LatitudeField;
    private JTextField LongitudeField;
    private JComboBox LetterColorBox;
    private JTextField InstructionsField;
    private JTextField IdField;
    private JTextField TypeField;
    private JTextField DescField;

    ImageData currImage;
    BufferStrategy bs;
    Canvas c;
    BufferedImage bufferImage;
    Graphics2D buffer;
    int state;
    Point topLeft;


    public ImageViewerGUI() {
        //So I hard-coded this, fight me
        LetterColorBox=new ColorComboBox();
        LetterColorBox.setEditable(true);
        LetterColorBox.setLightWeightPopupEnabled(false);
        LetterField=new JTextField();
        LetterField.setText("A");
        ShapeBox=new JComboBox();
        ShapeBox.setEditable(true);
        ShapeBox.setLightWeightPopupEnabled(false);
        ShapeColorBox = new ColorComboBox();
        ShapeColorBox.setEditable(true);
        ShapeColorBox.setLightWeightPopupEnabled(false);
        LatitudeField=new JTextField();
        LatitudeField.setEditable(false);
        LongitudeField=new JTextField();
        LongitudeField.setEditable(false);
        SubmitButton=new JButton("Submit");
        InstructionsField=new JTextField();
        UndoButton = new JButton("Undo");
        PreviousButton = new JButton("Previous");
        IdField=new JTextField();
        IdField.setEditable(false);
        TypeField=new JTextField();
        TypeField.setText("standard");
        DescField=new JTextField();
        DescField.setText("");
        DescField.setEditable(false);

        DataPanel=new JPanel();
        //DataPanel.setPreferredSize(new Dimension(1280,280));
        DataPanel.setLayout(new GridBagLayout());
        DataPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.black),new EmptyBorder(10,10,10,10)));
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.BOTH;
        gbc.weightx=.5;
        gbc.weighty=.5;
        gbc.insets=new Insets(5,5,5,5);

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=5;
        gbc.weighty=.2;
        DataPanel.add(InstructionsField,gbc);
        gbc.gridwidth=1;
        gbc.gridx=5;
        gbc.gridy=0;
        DataPanel.add(IdField,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        DataPanel.add(LatitudeField,gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        DataPanel.add(ShapeBox,gbc);
        gbc.gridx=2;
        gbc.gridy=1;
        DataPanel.add(LetterField,gbc);
        gbc.gridx=3;
        gbc.gridy=1;
        DataPanel.add(TypeField,gbc);
        gbc.gridx=4;
        gbc.gridy=1;
        DataPanel.add(UndoButton,gbc);
        gbc.gridx=5;
        gbc.gridy=1;
        gbc.gridheight=2;
        DataPanel.add(SubmitButton,gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridheight=1;
        DataPanel.add(LongitudeField,gbc);
        gbc.gridx=1;
        gbc.gridy=2;
        DataPanel.add(ShapeColorBox,gbc);
        gbc.gridx=2;
        gbc.gridy=2;
        DataPanel.add(LetterColorBox,gbc);
        gbc.gridx=3;
        gbc.gridy=2;
        DataPanel.add(DescField,gbc);
        gbc.gridx=4;
        gbc.gridy=2;
        DataPanel.add(PreviousButton,gbc);
        ImagePanel= new DrawablePanel();
        ImagePanel.setPreferredSize(new Dimension(1280,720));
        ImageViewerPanel = new JPanel();
        GridBagConstraints gbc2=new GridBagConstraints();
        ImageViewerPanel.setLayout(new GridBagLayout());
        ImageViewerPanel.setPreferredSize(new Dimension(1280,720));
        gbc2.gridx=0;
        gbc2.gridy=0;
        gbc2.fill=GridBagConstraints.BOTH;
        gbc2.weighty=.95;
        gbc2.weightx=1;
        ImageViewerPanel.add(ImagePanel,gbc2);
        gbc2.gridx=0;
        gbc2.gridy=1;
        gbc2.weightx=1;
        gbc2.weighty=.05;
        ImageViewerPanel.add(DataPanel,gbc2);


        SubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(state==5){
                    DescField.setText("");
                    DescField.setEditable(false);
                    TypeField.setText("standard");
                    currImage.setTargetMetaData(ShapeBox.getSelectedItem().toString(),ShapeColorBox.getSelectedItem().toString(),LetterField.getText(),LetterColorBox.getSelectedItem().toString(),TypeField.getText(),DescField.getText());
                    SubmitThread thread=new SubmitThread(currImage.getTarget());
                    thread.start();
                    state=0;
                    try{
                    Thread.sleep(5);}
                    catch (Exception exc){exc.printStackTrace();}
                    nextImage();

                }
            }
        });
        UndoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(state>0){
                    state--;
                    triggerInstructions();
                }
            }
        });
        PreviousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currImage=ImageViewer.getPreviousImage();
                topLeft.setLocation(0,0);
                currImage=ImageViewer.getNextImage();
                LatitudeField.setText("Latitude: "+currImage.getLat());
                LongitudeField.setText("Longitude: "+currImage.getLon());
                setImagePanel(currImage.getImage());
                state=0;
                triggerInstructions();
                IdField.setText("Id: "+currImage.getId());
            }
        });
        TypeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(!DescField.isEditable()) {
                    DescField.setEditable(true);
                }

            }
        });
        //setup the preliminary data or read it from a file
        initShapeBox();



        state=0;
        c=new Canvas();
        c.setPreferredSize(new Dimension(ImagePanel.getWidth(),this.ImagePanel.getHeight()));
        c.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                if(e.getKeyChar() =='.'&&state==0) {

                    nextImage();
                    state=0;
                }
            }
        });
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton()!=MouseEvent.BUTTON1)
                {
                    nextImage();
                    state=0;
                }
                else if(e.getButton()==MouseEvent.BUTTON1){
                switch(state){
                    case 0:
                        state=1;
                        //want to process
                        triggerInstructions();

                        break;
                    case 1:
                        state=2;
                        //center
                        triggerInstructions();
                        currImage.setTargetCenter((int)e.getPoint().getX(),(int)e.getPoint().getY());
                        drawCircle(e.getPoint(),3);
                        break;
                    case 2:
                        //top left corner
                        state=3;
                        triggerInstructions();
                        topLeft.setLocation(e.getPoint());//x and y of mouseclick
                        drawCircle(e.getPoint(),5);
                        break;
                    case 3:
                        state=4;
                        //bottom right corner
                        currImage.cropTarget((int)topLeft.getX(),(int)topLeft.getY(),(int)e.getPoint().getX(),(int)e.getPoint().getY());
                        triggerInstructions();
                        drawCircle(e.getPoint(),5);
                        drawRect(topLeft,e.getPoint());
                        break;
                    case 4:
                        state=5;
                        //direction
                        triggerInstructions();
                        currImage.setTargetRotation((int)e.getPoint().getX(),(int)e.getPoint().getY());
                        drawCircle(e.getPoint(),3);
                        break;
                    case 5:
                        InstructionsField.setText("Nothing left to click here. Press the submit button to submit");
                        //nothing
                        break;
                }}
            }
        });
        ImagePanel.setLayout(new GridLayout());
        ImagePanel.add(c);
        currImage=null;
        topLeft=new Point(0,0);
    }
    public void start(){

        c.createBufferStrategy(2);
        bs=c.getBufferStrategy();
        bufferImage=new BufferedImage(ImagePanel.getWidth(),ImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
        buffer=bufferImage.createGraphics();
        buffer.setBackground(Color.lightGray);
        nextImage();
    }
    public void setImagePanel(BufferedImage img){
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        g.setColor(Color.lightGray);
        g.fillRect(0,0,1280,720);
        g.drawImage(img,0,0,c);
        bs.show();

    }
    public void drawCircle(Point p,int radius){
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        g.setColor(Color.red);
        g.fillOval((int)p.getX()-radius/2,(int)p.getY()-radius/2,radius*2,radius*2);
        bs.show();
    }
    public void drawRect(Point p1,Point p2){
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();
        g.setColor(Color.red);
        g.drawRect((int)p1.getX(),(int)p1.getY(),(int)(p2.getX()-p1.getX()),(int)(p2.getY()-p1.getY()));
        bs.show();
    }
    public void triggerInstructions(){
        switch(state){
            case 0:

                InstructionsField.setText("Click the image to specify a target or press the right arrow to skip");
                break;
            case 1:
                InstructionsField.setText("Click the center of the target");
                break;
            case 2:
                InstructionsField.setText("Click the top left corner of the target");
                break;
            case 3:
                InstructionsField.setText("Click the bottom right corner of the target");
                break;
            case 4:

                InstructionsField.setText("Click to make the direction vector from the center of the target");
                break;
            case 5:

                InstructionsField.setText("Fill out any extra metadata");
                break;
            default:
                System.out.println("wut");
        }
    }
    public void nextImage() {
        System.out.println("getting next image");
        topLeft.setLocation(0,0);
        currImage=ImageViewer.getNextImage();
        LatitudeField.setText("Latitude: "+currImage.getLat());
        LongitudeField.setText("Longitude: "+currImage.getLon());
        setImagePanel(currImage.getImage());
        IdField.setText("Id: "+currImage.getId());
        triggerInstructions();
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

}