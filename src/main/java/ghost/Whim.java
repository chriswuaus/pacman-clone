package ghost;

import org.json.simple.JSONArray;

/**
 * whim type ghost, requires a chase ghost
 */
public final class Whim extends Ghost {
  
  public Whim(int[] data, JSONArray modeLength, String ID) {
    super (data, modeLength, ID);
  }
  /**
   * sets target to either Double the vector from Chaser to 
   * 2 grid spaces ahead of waka or bottom right corner
   */
  public void setNextTarget() {
    // sets to bottom right corner
    this.targetX = 448;
    this.targetY = 576;

    if (this.mode.equals("CHASE")) {
      // if chaser is eaten (nothing to base vector off of), 
      // chases waka like chaser (avenging his lost friend)
      if (chaser.eaten) {
        this.targetX = waka.x + 12;
        this.targetY = waka.y + 13;
        return;
      }
      int offY = 0;
      int offX = 0;

      // off sets by 2 grid spaces ahead of waka
      if (this.waka.direction == 'u') {
        offY = -32;
      } else if (this.waka.direction == 'd') {
        offY = 32;
      } else if (this.waka.direction == 'l') {
        offX = -32;
      } else if (this.waka.direction == 'r') {
        offX = 32;
      }
      // finds the distance between chaser and 2 units ahead of waka
      int diffX = waka.x - this.chaser.x + offX;
      int diffY = waka.y - this.chaser.y + offY;
      // sets target to twice that vector
      this.targetX = chaser.x + 2*diffX;
      this.targetY = chaser.y + 2*diffY;
    }
  }
}