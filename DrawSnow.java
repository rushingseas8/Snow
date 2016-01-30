import java.awt.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class DrawSnow extends JFrame implements Runnable, ChangeListener {
    BufferedImage backbuffer;
    Graphics2D g2d;

    JPanel GUI;
    JSlider heaviness;

    int width, height;

    Thread loop;

    int count;

    //How fast the wind is
    int wind = 0;

    //How strong the storm is
    int strength = 500;    

    //First three, basic needed for 3D effect
    FallPattern near;
    FallPattern mid;
    FallPattern far;

    //Additional few for a nice flurry
    FallPattern inYoFace;
    FallPattern intermid;
    FallPattern midfar;
    FallPattern farther;
    //FallPattern veryFar;

    //Added October 7, 2014. Background image
    BufferedImage background;

    public static void main() {
        new DrawSnow();
    }

    public DrawSnow() {
        super("Snowfall");
        setSize(1200, 700);
        width = getWidth(); height = getHeight();

        GUI = new JPanel();
        heaviness = new JSlider(JSlider.HORIZONTAL, 0, 1000, strength);
        heaviness.addChangeListener(this);
        heaviness.setVisible(true);
        GUI.add(heaviness, 0);
        GUI.setVisible(true);
        //add(GUI, 0);

        setVisible(true);        

        //Turn on labels at major tick marks.
        heaviness.setMajorTickSpacing(200);
        heaviness.setMinorTickSpacing(20);
        heaviness.setPaintTicks(true);
        heaviness.setPaintLabels(true);        

        loop = new Thread(this);
        loop.start();

        count = 0;

        //How many huge ones to add in
        int howMany = (int)((getWidth() * getHeight()) / 70000);

        near = new FallPattern(750, 10, wind, strength, getWidth(), getHeight());
        mid = new FallPattern(1000, 5, wind, strength, getWidth(), getHeight());
        far = new FallPattern(3500, 2, wind, strength, getWidth(), getHeight());

        inYoFace = new FallPattern(howMany, 20, wind, strength / 20, getWidth(), getHeight());
        intermid = new FallPattern(5000, 7, wind, strength, getWidth(), getHeight());
        midfar = new FallPattern(10000, 3, wind, strength, getWidth(), getHeight());
        farther = new FallPattern(25000, 1, wind, 100, getWidth(), getHeight());
        //veryfar = new FallPattern(50, 10, 0, getWidth(), getHeight());

        background = null;
        try {
            background = javax.imageio.ImageIO.read(new java.io.File("winter_forest"));
            BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Image scaled = background.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
            Graphics2D g = temp.createGraphics();
            g.drawImage(scaled, 0, 0, null);
            g.dispose();
            background = temp;
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            strength = (int)source.getValue();
            updatePatterns();
            //System.out.println(strength);
        }
    }    

    public void run() {
        Thread t = Thread.currentThread();
        while (t == loop) {
            repaint();
            try { Thread.sleep(5   ); } 
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error with frame!");
            }
            //GUI.repaint();            
        }
        System.out.println("Error: Thread is not the main thread!");
    }

    public void paint(Graphics g) {
        update();      
        g.drawImage(backbuffer, 0, 0, this);
    }

    public void update() {      
        //Creates a new image that is then drawn to. This image is the size of the window, allowing for resizing.
        backbuffer = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();         

        //Background

        if (background != null)
            g2d.drawImage(background, 0, 0, null);
        else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight()); 
        }

        if (getWidth() != width || getHeight() != height) {
            updatePatterns();
        }

        near.update();
        near.draw(g2d);

        mid.update();
        mid.draw(g2d);

        far.update();
        far.draw(g2d);

        inYoFace.update();
        inYoFace.draw(g2d);
        intermid.update();
        intermid.draw(g2d);
        midfar.update();
        midfar.draw(g2d);
        farther.update();
        farther.draw(g2d);   

        //count++;
        //wind = (int)((20 * Math.PI) * Math.sin(count % 10));
        //updatePatterns();
    }

    private void updatePatterns() {
        near = new FallPattern(near, getWidth(), getHeight());
        near.setHeaviness(strength);
        near.setWind(wind);        
        mid = new FallPattern(mid, getWidth(), getHeight());
        mid.setHeaviness(strength);  
        mid.setWind(wind);        
        far = new FallPattern(far, getWidth(), getHeight());
        far.setHeaviness(strength); 
        far.setWind(wind);        

        inYoFace = new FallPattern(inYoFace, getWidth(), getHeight());
        inYoFace.setHeaviness(strength);            
        inYoFace.setWind(wind);
        intermid = new FallPattern(intermid, getWidth(), getHeight());
        intermid.setHeaviness(strength);            
        intermid.setWind(wind);        
        midfar = new FallPattern(midfar, getWidth(), getHeight());
        midfar.setHeaviness(strength);            
        midfar.setWind(wind);        
        farther = new FallPattern(farther, getWidth(), getHeight());
        farther.setHeaviness(strength);            
        farther.setWind(wind);        
    }
}