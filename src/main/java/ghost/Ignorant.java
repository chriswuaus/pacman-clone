package ghost;

import static java.lang.Math.abs;
import static java.lang.Math.hypot;

import org.json.simple.JSONArray;

/**
 * Ignorant ghost
 */
public final class Ignorant extends Ghost {
  public Ignorant(int[] data, JSONArray modeLength, String ID) {
    super (data, modeLength, ID);
  }
  
  /**
   * sets target to bottom left or to waka location
   * if 8 units close to waka, target is bottom left
   */
  public void setNextTarget() {
    // bottom left target
    this.targetX = 0;
    this.targetY = 576;

    // distance between ignorant ghost and waka in tile units
    double wakaDistance = hypot(abs(this.y - waka.y), abs(this.x - waka.x))/16;
    // if distance is more than 8 and in chase mode, target waka
    if (wakaDistance > 8 && this.mode.equals("CHASE")) {
      this.targetX = waka.x + 12;
      this.targetY = waka.y + 13;
    }
  }
}