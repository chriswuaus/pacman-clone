package ghost;
import processing.core.PApplet;
/**
 * wall class of gameobject, no one can run into it!
 */
public final class Wall extends GameObject {
  private final char WALL_ID; // id of wall
  public Wall(int x, int y, String ID, char WALL_ID) {
    super(x, y, ID);
    this.WALL_ID = WALL_ID;
  }

  /**
   * adds sprite based on what id given to it during constructor
   * @param app papplet
   */
  public void addSprite(PApplet app) {
    String img = "";
    switch(WALL_ID) {
      case '1':
        img = "horizontal.png";
        break;
      case '2':
        img = "vertical.png";
        break;
      case '3':
        img = "upLeft.png";
        break;
      case '4':
        img = "upRight.png";
        break;
      case '5':
        img = "downLeft.png";
        break;
      case '6':
        img = "downRight.png";
        break;
    }
    this.sprite = app.loadImage(DIR + img);
  }
}