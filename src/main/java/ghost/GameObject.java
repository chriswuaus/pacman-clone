package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * abstract representation of game obejct in game !
 */
public abstract class GameObject {
  static final String DIR = "src/main/resources/";
  static int fruitCount;

  int x;
  int y;
  final String ID;
  PImage sprite;
  
  /**  
   * constructs new game object
   * @param x X-coordinate of object
   * @param y Y-coordinate of object
   * @param ID identifying string
   */
  public GameObject(int x, int y, String ID) {
    this.x = x;
    this.y = y;
    this.ID = ID;
  }
  
  public abstract void addSprite(PApplet app);
  /**
   * draws sprite of game object at given x and y coordinates
   * @param app PApplet
   */
  public void draw(PApplet app) {
    if (this.sprite == null) {
      return;
    }
    app.image(this.sprite, this.x, this.y);
  }
  /** 
   * called every frame, manipulates game object every frame
   */
  public void tick() {}

  public boolean step() {
    return false;
  }
}