package ghost;

import static java.lang.Math.abs;
import static java.lang.Math.hypot;

import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * abstract class
 * Ghosts are the antagonists of Waka Waka. 
 * Their goal is to prevent Waka from collecting all the fruit on the map. 
 * They do this by hunting him down and hitting him.
 * Like Waka, Ghosts can move horizontally and vertically on the map. 
 * They cannot pass through walls, however can pass through each other. 
 * Ghosts move at the same speed as Waka (specified in the game configuration file).
 * alternate between SCATTER, where they move to one of the corners of the map,
 * and CHASE, where they find their target 
 * location and then turn in the direction closest to that location.
 */
public abstract class Ghost extends Player {
  // possible moves that ghosts can make
  ArrayList<Character> possMoves = new ArrayList<>();
  boolean debug;
  boolean eaten;
  boolean soda; // if under influence of sodacan
  boolean trapped; // if trapped (no way to turn in map)
  Ghost chaser;
  int frightCounter; // timer for fright mode
  int frightLength; 
  static int order; // index of modelength
  int targetX;
  int targetY;
  int tickCounter;
  JSONArray modeLength; // time (seconds) each mode should be before alternating to another
  static String mode;
  Waka waka;

  public Ghost(int[] data, JSONArray modeLength, String ID) {
    super (data, ID);
    this.x = data[0] - 6;
    this.y = data[1] - 6;
    this.speed = data[2];
    this.frightLength = data[3];
    this.modeLength = modeLength;
    this.sprite = sprite;
    this.direction = 'l';
    this.horizOffset = 13;
    this.vertOffset = 13;
    this.mode = "SCATTER";
  }
  /**
   * frightens ghost- moves in random directions at intersection
   * only turning back on themseleves if they have no other choice
   * @param isSoda if waka has stepped on soda
   */
  public void frighten(boolean isSoda) {
    // sets mode to frightened
    this.mode = "FRIGHT";
    // starts timer for frightened mode
    this.frightCounter = 0;
    if (isSoda) {
      this.soda = true;
      // sets soda sprite (invisible-ish)
      this.sprite = this.sprites[2];
    } else {
      this.soda = false;
      // sets frightened sprite
      this.sprite = this.sprites[1];
    }
  }
  /**
   * manages changing of modes
   */
  public final void modeManager() {
    // if reached the end of modelength array, start at beginning
    if (order == modeLength.size()) {
      order = 0;
    }
    if (this.mode.equals("FRIGHT")) {
      // keeps debug mode off because no target coordinates
      this.debug = false; 
      // timer for frightened mode
      if (this.frightCounter != this.frightLength*60) {
        this.frightCounter++;
      } else {
        this.frightCounter = 0;
        // turns off soda mode 
        this.soda = false;
        // sets back to mode that it was before it was frightened
        if ((order) % 2 == 0) {
          this.mode = "CHASE";
        } else {
          this.mode = "SCATTER";
        }
      }
      return;
    }
    // sets sprite back to normal sprite
    this.sprite = this.sprites[0];
    this.soda = false;
    // gets length of the mode
    int length = (int) (long) modeLength.get(order);
    // timer
    if (tickCounter == length*60) {
      order++;
      // if index is first, chase, else, scatter
      if ((order + 1) % 2 == 0) {
        this.mode = "CHASE";
      } else {
        this.mode = "SCATTER";
      }
      // resets tick counter
      tickCounter = 0;
    }
    tickCounter++; 
  }
  /**
   * draws ghost object onto window
   * + optional debug mode, draws lines from ghost
   * to target location
   * @param app papplet
   */
  public final void draw(PApplet app) {
    super.draw(app);
    if (this.debug == true) {
      app.stroke(255, 255, 255); // white
      app.line(this.x + 13, this.y + 13, this.targetX, this.targetY);
    }
  }

  /**
   * adds different sprite based on ID
   * @param app papplet app
   */
  public final void addSprite(PApplet app) {
    String img = "";
    switch(ID) {
      case "ambusher":
        img = "ambusher.png";
        break;
      case "chaser":
        img = "chaser.png";
        break;
      case "ignorant":
        img = "ignorant.png";
        break;
      case "whim":
        img = "whim.png";
        break;
    }
    this.sprites = new PImage[] {app.loadImage(DIR + img),
        app.loadImage(DIR + "frightened.png"), app.loadImage(DIR + "sodacanG.png")};
    this.sprite = this.sprites[0];      
  }
  /**
   * Returns the opposite direction character of the current direction
   * if current direction is invalid, returns '0'
   * @return opposite character
   */
  public final char opposite() {
    if (this.direction == 'u') {
      return 'd';
    } else if (this.direction == 'd') {
      return 'u';
    } else if (this.direction == 'l') {
      return 'r';
    } else if (this.direction == 'r') {
      return 'l';
    }
    return '0';
  }
  /**
   * sets targetX and targetY for ghost to target based on conditions
   */
  public abstract void setNextTarget();

  /**
   * determines which move to make at intersection
   * @return character which will make ghost closer to target via straight line distance
   */
  public final char nextDeterMove() {
    Random rand = new Random();
    char move = direct[rand.nextInt((3) + 1)];
    if (this.mode.equals("FRIGHT")) {
      // randomly generates different moves until move that is
      // valid (wont hit a wall or not the opposite way)
      while (!(!this.isWall(move) && move!=this.opposite())) {
        rand = new Random();
        move = direct[rand.nextInt((3) + 1)];
      }
      return move;
    }
    char dir = '0';
    double length = 999999999;
    
    for (char direction: possMoves) {
      // if move will not hit into wall
      if (!this.isWall(direction)) {
        int projX = 0;
        int projY = 0;
        // projects possible coordinate based on what possible directions
        if (direction == 'u') {
          projY -= 16;
        } else if (direction == 'd') {
          projY += 16;
        } else if (direction == 'l') {
          projX -= 16;
        } else if (direction == 'r') {
          projX += 16;
        }
        // length between possible coordinate and the target coordinate
        double tDistance = hypot(abs(this.y + projY  - targetY), abs(this.x + projX - targetX));
        // if direction gives a smaller distance, it replaces the one before it
        if (tDistance <= length) {
          length = tDistance;
          dir = direction;
        }
      }
    }
    // clears possible moves 
    possMoves.clear();
    return dir;
  }
  /**
   * sets ghost back to starting position
   */

  public void reset() {
    this.x = data[0] - 6;
    this.y = data[1] - 6;
    this.sprite = this.sprites[0];
    this.soda = false;
  }

  /**
   * is called every frame
   */
  public final void tick() {
    // if ghost is eaten, do nothing and set sprite to null
    if (this.eaten) {
      this.sprite = null;
      return;
    }
    modeManager();

    if (this.isCentre() && this.isIntersection()) {
      if (this.trapped) {
        // if no direction to go, go back opposite
        this.direction = this.opposite();
      } else {
        // set next target at centre and intersection
        this.setNextTarget();
        // turn in given direction
        this.direction = nextDeterMove();
      }      
    }
    this.move();
  }
  /**
   * returns if ghost is at an intersection
   * @return if ghost is at an intersection, a place where it can turn
   */
  public final boolean isIntersection() {
    // iterates through all directions
    for (char direction: direct) {
      // if direction will not run into wall, and is not the opposite
      // add to arraylist of possible moves
      if (!this.isWall(direction) && direction != opposite()) {
        possMoves.add(direction);
      }
    }
    // if there is only one direction (which is the current direction)
    // return false, must be in a corridor type situation
    if (possMoves.size() == 1 && !isWall(direction)) {
      return false;
    }
    // if there are no possible directions, ghost is trapped
    if (possMoves.size() == 0) {
      this.trapped = true;
      return false;
    } 
    // undo trap
    this.trapped = false;
    // else, return true
    return true;
  }
  /**
   * gets waka object from game grid
   * @param waka waka object
   */
  public final void getWaka(Waka waka) {
    this.waka = waka;
  }

  /**
   * gets chaser ghost
   * @param chaser ghost chaser
   */
  public final void getChaser(Ghost chaser) {
    this.chaser = chaser;
  }
}