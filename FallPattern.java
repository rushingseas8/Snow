import java.util.ArrayList;
import java.awt.*;

/**
 * A helper class that stores a given amount of snowflakes, and the wind for them.
 * This allows for multiple layers of snow falling, giving a realistic effect.
 */
public class FallPattern {
    //Main stuff
    int curNumParticles;
    int maxNumParticles;
    int flakeSize;
    int windDirection;
    ArrayList<Flake> snowflakes = new ArrayList<>();

    //Represents how heavy the snowfall is, as a percent.
    //100 = 1/update. 1 = 1/100 updates. 500 = 5 per update.
    int heaviness;

    //Stuff for drawing and adding snowflakes
    int width;
    int height;

    public FallPattern(int m, int s, int w, int h, int width, int height) {
        curNumParticles = 0;
        maxNumParticles = m;
        windDirection = w;
        flakeSize = s;
        heaviness = h;

        this.width = width;
        this.height = height;
    }

    //A special constructor meant for handling resizing smoothly. It copies over the previous elements and starts from there.
    public FallPattern(FallPattern f, int width, int height) {
        //Helps to space the flakes apart
        int oldWidth = f.width;
        double ratio = (double)width / oldWidth;

        for (Flake fl: f.snowflakes) {
            snowflakes.add(
                new Flake(fl.size, (int)(fl.x * ratio), fl.y, fl.dx, fl.dy)
            );
        }
        curNumParticles = f.curNumParticles;
        maxNumParticles = f.maxNumParticles;
        windDirection = f.windDirection;
        flakeSize = f.flakeSize;
        heaviness = f.heaviness;    

        this.width = width;
        this.height = height;        
    }

    //Adjusts heaviness
    public void setHeaviness(int to) {
        heaviness = to;
    }

    //Adjusts wind speed
    public void setWind(int to) {
        windDirection = to;
    }

    //Updates the snowflakes
    public void update() {
        //Accounts for heaviness variable
        if (heaviness > 100) {
            for (int i = 0; i < (int)(heaviness / 100.0); i++) {
                add();
            }
        } else {
            if ((int)(Math.random() * (100.0 / heaviness)) == 0) {
                add();
            }
        }

        for (int i = 0; i < snowflakes.size(); i++) {
            //System.out.println("Updating #" + i);
            snowflakes.get(i).update();
        }

        for (int i = 0; i < snowflakes.size(); i++) {
            if (checkDead(snowflakes.get(i))) {
                snowflakes.remove(i);
                curNumParticles--;                
            }
        }
    }

    //Adds a flake;
    private void add() {
        //Tries to add a new snowflake, if possible
        if (curNumParticles < maxNumParticles) {
            //Adds it in a random x position but always at the top
            /**Flake f = new Flake(flakeSize, (int)(Math.random() *  width), 0); //This is the basic one, for no wind.*/
            Flake f = new Flake(flakeSize, 
                    (int)(((Math.random() * (width + (height / 10.0 * Math.abs(windDirection))))) - (height / 10.0 * windDirection)),
                    0);
            f.setWind(windDirection);
            snowflakes.add(f);
            curNumParticles++;
        }        
    }

    private boolean checkDead(Flake f) {
        //if (f.x + (f.size / 2) > width || f.x - (f.size / 2) < 0 || f.y + (f.size / 2) > height) {
        if (f.y + (f.size / 2) > height) {
            f = null;
            return true;
        }
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < snowflakes.size(); i++) {
            snowflakes.get(i).draw(g);
        }
    }
}