package ghost;

import processing.core.PApplet;

/**
 * edible type game objects
 * waka collects these by stepping on them
 *  When this occurs, the edible is removed from the map.
 * edible entities do not move, nor are they collected by ghosts.
 */
public final class Edible extends GameObject {
  private boolean eaten; // if edible has been collected by waka

  public Edible(int x, int y, String ID) {
    super(x, y, ID);
    // when edible is created, increments amount of fruit
    fruitCount++;
  }

  // sets sprite of edible to either fruit, superfruit, sodacan
  public final void addSprite(PApplet app) {
    String img = "";
    switch(ID) {
      case "7":
        img = "fruit.png";
        break;
      case "8":
        img = "superfruit.png";
        break;
      case "9":
        img = "sodacan.png";
        break;
    }
    this.sprite = app.loadImage(DIR + img);
  }
  /**
   * if stepped on by waka, sets sprite to null
   * and decrements fruitcount
   * @return if first time stepped on
   */
  public final boolean step() {
    if (this.eaten == false) {
      this.sprite = null;
      fruitCount--;
      this.eaten = true;
      return true;
    }
    return false;
  }
}