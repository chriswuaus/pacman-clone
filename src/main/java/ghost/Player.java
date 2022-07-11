package ghost;

import processing.core.PImage;

/**
 * abstract class for player type game objects
 */
public abstract class Player extends GameObject{
  static final char[] direct = new char[] {'u', 'd', 'l', 'r'};
  static GameObject[][] objects;
  char direction;
  char nextMove;
  char prevMove;
  int horizOffset;
  int vertOffset;
  int speed;
  int tickCounter;
  int[] data;
  PImage[] sprites;
 
  public Player(int[] data, String ID) {
    super (data[0], data[1], ID);
    this.speed = data[2];
    this.data = data;
  }

  /**
   * detects if given direction is valid (if direction will lead to wall)
   * @param direction direction character
   * @return if given move will run into wall
   */
  public final boolean isWall(char direction) {
    // sets temporary coordinates at center of player
    int temX = this.x + this.horizOffset;
    int temY = this.y + this.vertOffset;
    if (direction == 'u') {
      temY -= 8;
    } else if (direction == 'd') {
      temY += 9;
    } else if (direction == 'l') {
      temX -= 8;
    } else if (direction == 'r') {
      temX += 9;
    } 
    // creates tile coordinates
    temY = (int) Math.floor(temY/16);
    temX = (int) Math.floor(temX/16);
    // if objects are wall, return true, else, return false
    return objects[temY][temX].ID.equals("wall") && !(objects[temY][temX] == null);
  }

  /**
   * @return boolean returns if player is at the centre of a tile
   */
  public final boolean isCentre() {
    int x = (this.x + this.horizOffset) % 16;
    int y = (this.y + this.vertOffset) % 16;
    // if middle pixel is in middle of tile, return true
    if ( x == 7 && y == 7) {
      return true;
    }
    return false;
  }

  /**
   * moves in direction by number of pixels given by speed
   */
   public final void move() {
    if (this.direction == 'u') {
      this.y -= this.speed;
    } else if (this.direction == 'd') {
      this.y += this.speed;
    } else if (this.direction == 'l') {
      this.x -= this.speed;
    } else if (this.direction == 'r') {
      this.x += this.speed;
    }
    // sets direction to previous move
    this.prevMove = this.direction;
  }

}