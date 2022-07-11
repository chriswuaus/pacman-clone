package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Waka is the player-controlled character in the game. 
 * Waka can move vertically and horizontally on the map and cannot pass 
 * through walls. Waka is controlled with the arrow keys (up, down, left and right).
 */
public final class Waka extends Player {
  int tickCounter;
  boolean archived;
  char savedMove;

  int lives;
  int xCoor;
  int yCoor;

  public Waka(int[] data, String ID) {
    super (data, ID);
    this.x = data[0] - 4;
    this.y = data[1] - 5;
    // tile coordinates
    this.xCoor = (int) Math.floor((this.y + this.vertOffset)/16);
    this.yCoor = (int) Math.floor((this.x + this.horizOffset)/16);
    this.horizOffset = 11;
    this.vertOffset = 12;

    this.speed = data[2];
    this.lives = data[3];
    this.savedMove = '0';
    this.prevMove = '0';
    this.direction = 'l'; // initial direction is left
  }
  /**
   * loads all different sprites of waka
   * @param app Papplet
   */
  public void addSprite(PApplet app) {
    this.sprites = new PImage[] {app.loadImage(DIR + "playerUp.png"), 
      app.loadImage(DIR + "playerDown.png"),app.loadImage(DIR + "playerLeft.png"),
      app.loadImage(DIR + "playerRight.png"), app.loadImage(DIR + "playerClosed.png")};
    this.sprite = sprites[2]; // initial sprite is left facing sprite
  }
  
  /**
   * calculates coordinates of tile waka is on
   */
  public void centre() {
    this.yCoor = (int) Math.floor((this.y + this.vertOffset)/16);
    this.xCoor = (int) Math.floor((this.x + this.horizOffset)/16);
  }

  /**
   * draws waka and number of lives waka has
   * @param app papplet
   */
  public void draw(PApplet app) {
    super.draw(app);
    for (int i = 0; i < this.lives; i++) {
      app.image(this.sprites[2], 7 + (30*i), 542);
    }
  }

  /**
   * alternates between open and closed waka
   */
  public void wakAnimation() {
    if (tickCounter == 8) {
      tickCounter = 0;
      if (this.sprite.equals(sprites[4])) {
      this.turn(this.direction);
      } else {
      this.sprite = sprites[4];
      }
    }
    tickCounter++;
  }

  /**
   * turns in direction given, and changes sprite in correct orientation
   * @param direction character direction
   */
  public void turn(char direction) {
    this.direction = direction;
    for (int i = 0; i < 4; i++) {
      if (direction == direct[i]){
        this.sprite = sprites[i];
      }
    }
  }
 /**
  * returns if waka can jitter (especially in corridor sitation
  * @return boolean if can jitter
  */
  public boolean canJitter() {
    if (prevMove == 'u' && savedMove == 'd') {
      return true;
    } else if (prevMove == 'd' && savedMove == 'u') {
      return true;
    } else if (prevMove == 'l' && savedMove == 'r') {
      return true;
    } else if (prevMove == 'r' && savedMove == 'l') {
      return true;
    }
    return false;
  }


  public void tick() {
    this.wakAnimation();
    this.centre();

    // if no moves have been pressed
    if (this.savedMove == '0') {

      // check if the current direction is okay to move
      if (this.isWall(this.direction) == false) {
        this.move();
      }
    } else {
      // if turn direction is okay and move has been archived (move was not valid at the time)
      if (this.isWall(this.savedMove) == false && this.archived == true) {
        // checks if it is at the centre to move
        if (this.isCentre()) {
          // turns correct direction
          if (this.savedMove != this.direction) {
            this.turn(this.savedMove);
          }
          this.move();
          this.savedMove = '0'; 
          this.archived = false;
        } else {
          // if turn is okay but not at centre, keep moving and check if at centre next tick
          this.move();
          this.archived = true; 
        }
      } 
      // if move is okay and there has been no archived moves
      else if (!this.isWall(this.savedMove) && !this.archived) {
        if (this.savedMove != this.direction) {
          // if jittering in opposite directions (e.g in a corridor) or at a centre, turn
          if (this.canJitter() || this.isCentre()) {
            this.turn(this.savedMove);
          } 
        }
        
        this.move();
        this.savedMove = '0';
      } else {
        // if current direction okay, move
        if (this.isWall(this.direction) == false) {
          this.move();
          this.archived = true;
        }
      }
    } 
  }
  /**
   * gives saved move to waka via keyinput from keyboard
   * @param keyCode int from keyboard
   */
  public void keyInput(int keyCode) {
    if (keyCode == 38) {
      this.savedMove = 'u';
    } else if (keyCode == 40) {
      this.savedMove = 'd';
    } else if (keyCode == 39) {
      this.savedMove = 'r';
    } else if (keyCode == 37) {
      this.savedMove = 'l';
    }
  }
  /**
   * if waka died (hit by ghost), resets waka to beginning and reduces life number
   */
  public void die() {
    this.lives--;
    this.x = data[0] - 4;
    this.y = data[1] - 5;
    this.direction = 'l';
  }
}