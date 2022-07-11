package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;

class PlayerTest {
    @Test 
    public void direction() {
        App app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.settings();
        app.delay(1000);
        app.grid.keyManager(32);
        assertTrue(app.grid.ghosts.get(0).debug);
        app.grid.keyManager(32);
        assertFalse(app.grid.ghosts.get(0).debug);
        app.grid.keyManager(32);
        assertTrue(app.grid.ghosts.get(0).debug);
        app.grid.keyManager(32);
        assertFalse(app.grid.ghosts.get(0).debug);

        app.grid.keyManager(39);
        assertTrue(app.grid.waka.savedMove == 'r');
        app.grid.keyManager(38);
        assertTrue(app.grid.waka.savedMove == 'u');
        app.grid.keyManager(40);
        assertTrue(app.grid.waka.savedMove == 'd');
        app.grid.keyManager(39);
        assertTrue(app.grid.waka.savedMove == 'r');
        app.grid.keyManager(37);
        assertTrue(app.grid.waka.savedMove == 'l');
        app.grid.keyManager(39);
        assertTrue(app.grid.waka.savedMove == 'r');
        app.grid.keyManager(39);
        assertTrue(app.grid.waka.savedMove == 'r');

    }
    @Test
    public void testGhosts() {
      App app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.settings();
        // app.delay(1000);
        assertTrue(app.grid.gameObjects[4][2].step());
        assertFalse(app.grid.gameObjects[4][2].step());
        assertFalse(app.grid.gameObjects[4][2].step());
        assertFalse(app.grid.gameObjects[4][2].step());
        assertFalse(app.grid.gameObjects[4][2].step());
        assertFalse(app.grid.ghosts.get(0).soda);
        assertFalse(app.grid.ghosts.get(1).soda);
        assertFalse(app.grid.ghosts.get(2).soda);
        assertFalse(app.grid.ghosts.get(3).soda);
        assertTrue(app.grid.gameObjects[8][12].step());
        assertFalse(app.grid.gameObjects[8][12].step());
        assertFalse(app.grid.gameObjects[8][12].step());
        assertFalse(app.grid.gameObjects[8][12].step());
        assertFalse(app.grid.gameObjects[8][12].step());
        assertFalse(app.grid.gameObjects[8][12].step());


        for (Ghost ghost:app.grid.ghosts) {
          assertEquals(ghost.mode, "SCATTER");
        }
        for (Ghost ghost:app.grid.ghosts) {
          ghost.frighten(false);
        }
        for (Ghost ghost:app.grid.ghosts) {
          assertEquals(ghost.mode, "FRIGHT");
        }
        for (Ghost ghost:app.grid.ghosts) {
          assertFalse(ghost.soda);
        }
        for (Ghost ghost:app.grid.ghosts) {
          ghost.frighten(true);
        }
        for (Ghost ghost:app.grid.ghosts) {
          assertTrue(ghost.soda);
          assertTrue(ghost.mode.equals("FRIGHT"));
          ghost.modeManager();
        }
        app.delay(8000);
        for (Ghost ghost:app.grid.ghosts) {
          assertFalse(ghost.soda);
          assertFalse(ghost.mode.equals("FRIGHT"));
        }
        for (Ghost ghost: app.grid.ghosts) {
          ghost.order = 0;
          ghost.tickCounter = 0;
          ghost.reset();
          ghost.eaten = false;
        }
    }
    @Test
    public void ghostModes() {
       App afpp = new App();
        PApplet.runSketch(new String[]{"App"}, afpp);
        afpp.setup();
        afpp.settings();

        for (Ghost ghost: afpp.grid.ghosts) {
          ghost.order = 8;
        }

    }
    @Test
    public void playerMoves() {
      App app = new App();
      PApplet.runSketch(new String[]{"App"}, app);
      app.setup();
      app.settings();

      for (Ghost ghost: app.grid.ghosts) {
        ghost.eaten = true;
        ghost.soda = false;
      }

      app.grid.keyManager(39);
      app.delay(1000);
      app.grid.keyManager(37);
      app.delay(1000);
      app.grid.keyManager(39);
      app.delay(1000);
      app.delay(1000);
      app.grid.keyManager(37);

      app.grid.keyManager(38);
      app.delay(1000);
      app.grid.keyManager(40);
        app.grid.keyManager(38);
      app.delay(1000);
      app.grid.keyManager(40);
      app.delay(4000);
      app.grid.keyManager(40);
    }

    @Test
    public void chaserEaten() {
      App app = new App();
      PApplet.runSketch(new String[]{"App"}, app);
      app.setup();
      app.settings();

      for (Ghost ghost: app.grid.ghosts) {
        ghost.mode = "CHASE";
      }
     
      for (Ghost ghost: app.grid.ghosts) {
        if (!ghost.ID.equals("whim")) {
          ghost.eaten = true;
        }
      }
      for (Ghost ghost: app.grid.ghosts) {
        if (ghost.ID.equals("whim")) {
          assertTrue(ghost.mode.equals("CHASE"));
        }
      }
      int x = 0;
      int y = 0;
      app.delay(2000);
      for (Ghost ghost: app.grid.ghosts) {
        if (ghost.ID.equals("whim")) {
          // while (x)
          x = app.grid.waka.x + 12;
          y = app.grid.waka.y + 13;
          assertTrue(Math.abs(x- ghost.targetX) < 10);
          assertTrue(Math.abs(y- ghost.targetY) < 10);
        }
      }
    }
    @Test
    public void wakaMovement() {
      App app = new App();
      PApplet.runSketch(new String[]{"App"}, app);
      app.setup();
      app.settings();

      for (Ghost ghost: app.grid.ghosts) {
        ghost.order = 2;
      }
      app.delay(500);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(37);
      app.delay(2000);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(39);
      app.delay(1000);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(37);
      app.delay(1000);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(39);
      app.delay(3000);

    }
    @Test
    public void ghostChaser() {
      App app = new App();
      PApplet.runSketch(new String[]{"App"}, app);
      app.setup();
      app.settings();

      for (Ghost ghost: app.grid.ghosts) {
        ghost.order = 3;
      }
      app.delay(500);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(37);
      app.delay(2000);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(39);
      app.delay(1000);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(37);
      app.delay(1000);
      app.grid.keyManager(40);
      app.delay(1000);
      app.grid.keyManager(39);
      app.delay(3000);

      for (Ghost ghost: app.grid.ghosts) {
        ghost.order = 3;
      }

    }
}
