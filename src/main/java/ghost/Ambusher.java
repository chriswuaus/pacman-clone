package ghost;

import java.util.ArrayList;
import org.json.simple.JSONArray;
/**
 * Ambusher Ghost
 */
public final class Ambusher extends Ghost {
  public Ambusher(int[] data, JSONArray modeLength, String ID) {
    super (data, modeLength, ID);
  }
  /** 
   * sets the target coordinates of Ambusher to either top right corner
   * or 4 spaces in front of waka's location
   */
  public void setNextTarget() {
    // sets coordinates to top right corner of map
    this.targetX = 448;
    this.targetY = 0;
    // sets coordinates to 4 spaces in front of waka's 
    // location and direction, plus offset;
    if (this.mode.equals("CHASE")) {
      this.targetY = waka.yCoor*16 + 8;
      this.targetX = waka.xCoor*16 + 8;
      if (this.waka.direction == 'u') {
        this.targetY -= 48;
      } else if (this.waka.direction == 'd') {
        this.targetY += 48;
      } else if (this.waka.direction == 'l') {
        this.targetX -= 48;
      } else if (this.waka.direction == 'r') {
        this.targetX += 48;
      }
    }
  }
}