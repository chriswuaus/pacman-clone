package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * PApplet class that sets up and prints grid onto window
 */
public class App extends PApplet {
    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    GameGrid grid;

    public App() {}

    /** 
     * sets frame rate to 60 and initialises new game grid
     */
    public void setup() {
        frameRate(60);
        grid = new GameGrid(this);
    }

    /** 
     * sets size of window
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * draws a frame (which is called 60 times per second)
     */
    public void draw() { 
      // draws black background
      background(0, 0, 0); 
      // if game is finished, create a new grid
      if (grid.restart) {
        grid = new GameGrid(this);
        grid.restart = false;
        grid.end = false;
      }
      // ticks all objects in grid
      grid.tick();
      // draws all objects in grid
      grid.draw();
    }
    /**
     * detects if key is pressed and passes it to grid
     * key pressed is held in variable keyCode
     */
    public void keyPressed() {
      grid.keyManager(keyCode);
    }
    
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}