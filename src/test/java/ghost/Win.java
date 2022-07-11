package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;

class Win {
    @Test 
    public void winTest() {
      App app = new App();
      PApplet.runSketch(new String[]{"App"}, app);
      app.setup();
      app.settings();
      app.delay(2000);
      app.grid.waka.lives = 100;
      app.grid.lives = 100;

      for (Ghost ghost: app.grid.ghosts) {
        ghost.eaten = true;
      }

      app.grid.win = true;
      app.grid.end = true;
      app.delay(12000);
      assertFalse(app.grid.end);
      assertFalse(app.grid.restart);
      assertFalse(app.grid.win);
      assertTrue(app.grid.waka.fruitCount != 0);

      app.delay(10000);
      app.grid.waka.fruitCount = 0;
      assertTrue(app.grid.end);

      app.delay(500);
      assertTrue(app.grid.end);
    }
}
