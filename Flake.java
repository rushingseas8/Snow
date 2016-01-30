import java.awt.*;

public class Flake {
    private static Image snowflake;

    public double size; //1-10, represents the diameter

    public double x,  y; //Center of this flake
    public double dx, dy; //Speed

    //Instantiates with size, and origin
    public Flake(double s, double x, double y) {
        //Initialises
        size = s;
        this.x = x;
        this.y = y;

        //Falling speed is 10 pixels/second as base
        //It's also related to the size
        //dy = (int)(1 + (size / 10.0));
        dy = size / 2.0;

        snowflake = null;
        try {
            snowflake = javax.imageio.ImageIO.read(new java.io.File("snowflake"));
        } catch (Exception e) {}
    }

    //Conserves momentum; for instantiating when changing size of screen
    public Flake(double s, double x, double y, double dx, double dy) {
        //Initialises
        size = s;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    //Horizontal speed
    public void setWind(int to) {
        dx = (int)(to * (size / 10.0));    
    }

    //Updates position
    public void update() {
        x+=dx;
        y+=dy;  
    }

    public void draw(Graphics g) {
        g.fillOval((int)x, (int)y, (int)size, (int)size);        
    }

    public String toString() {
        return("Flake at " + x + ", " + y + 
            "is travelling at speed " + dx + " hor. " + 
            dy + " vert. and is size " + size);
    }
}