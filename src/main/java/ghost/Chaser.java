package ghost;

import org.json.simple.JSONArray;

/**
 * Chaser ghost
 */
public final class Chaser extends Ghost {
  public Chaser(int[] data, JSONArray modeLength, String ID) {
    super (data, modeLength, ID);
  }
  /**
   * sets chaser's target to either top left corner or waka's location
  */
  public void setNextTarget() {
    // set target to top left corner
    this.targetX = 0;
    this.targetY = 0;
    // set target to waka's location if chasing
    if (this.mode.equals("CHASE")) {
      this.targetX = waka.x + 12;
      this.targetY = waka.y + 13;
    }
  }
}