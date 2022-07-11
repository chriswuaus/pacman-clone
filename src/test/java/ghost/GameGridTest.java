package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;
import java.io.FileNotFoundException;


public class GameGridTest{
  @Test 
  public void json(){ // checks if json parser isfinr
    App app = new App();
    PApplet.runSketch(new String[]{"App"}, app);
    app.setup();
    app.settings();
    assertEquals(app.grid.mapName, "map.txt");
    assertEquals(app.grid.lives, 3);
    assertEquals(app.grid.speed, 1);
    assertEquals(app.grid.frightLength, 7);
    app.grid.mapName =  "ee";
    app.grid.parseMap();
  }

  @Test
  public void map() { // checks that map initialises properly 
    App papp = new App();
    PApplet.runSketch(new String[]{"App"}, papp);
    papp.setup();
    papp.settings();
    assertEquals(papp.grid.ghosts.get(0).mode, "SCATTER");
    
    assertNotNull(papp.grid);
    assertNotNull(papp.grid.ghosts);
    assertNotNull(papp.grid.gameObjects);
    assertNotNull(papp.grid.gameObjects[26][0]);
    assertEquals(papp.grid.gameObjects[26][0].x, 0);
    assertEquals(papp.grid.gameObjects[26][0].y, 416);
    assertEquals(papp.grid.gameObjects[26][0].ID, "wall");
    Wall wall = new Wall(0, 416, "wall", '2');
    Edible edible = new Edible(0, 0, "fruit");
    Edible edible1 = new Edible(0, 0, "super");
    Edible edible2 = new Edible(0, 0, "soda");

    papp.grid.keyManager(32);
    papp.grid.keyManager(39);
    assertEquals(papp.grid.waka.savedMove, 'r');

  }
}