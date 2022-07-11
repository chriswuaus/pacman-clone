package ghost;

import static java.lang.Math.abs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
/**
 * Class that holds all game information
 */
public final class GameGrid {
  PApplet app;

  static final int COLS = 36;
  static final int PIX = 16;
  static final int ROWS = 28;
  
  ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
  GameObject[][] gameObjects = new GameObject[36][28];

  boolean end; // if game has ended
  boolean restart; // if game needs to be restared
  boolean win; // if game has won

  Waka waka;

  int frightLength; // length of ghost frightened mode
  int lives; // waka's lives
  int speed; // waka's speeed
  int tickCount;

  JSONArray modeLength; // length of ghosts mode (scatter and chase)
  PFont font; // font to draw end screen in
  String mapName;
  
  /**
   * constructor for game grid
   * sets up all information from config and map file
   * @param app Papplet 
   */
  public GameGrid(PApplet app) {
    // creates new parseConfig information from filename
    ParseConfig jsonInfo = new ParseConfig("config.json");
    this.app = app;
    this.mapName = jsonInfo.mapName;
    this.lives = jsonInfo.lives;
    this.speed = jsonInfo.speed;
    this.modeLength = jsonInfo.modeLength;
    this.frightLength = jsonInfo.frightLength;
    this.parseMap();
    this.font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 30);

    waka.objects = this.gameObjects; // gives gameobjects to waka
    Ghost chaser = null;
    for (Ghost ghost: ghosts) {
      ghost.getWaka(waka); // gives waka to ghosts
      if (ghost.ID.equals("chaser")) {
        chaser = ghost; // identifies a chaser
      }
    } 
    for (Ghost ghost: ghosts) {
      if (ghost.ID.equals("whim")) {
        ghost.getChaser(chaser); // gives chaser to whim
      }
    }
  }

  /**
   * deals with all ghost iteractions with waka 
   * and controls ghost behaviour during waka eating
   */
  public void ghostInteract() {
    GameObject object = gameObjects[waka.yCoor][waka.xCoor];
    if (object == null) {
      return;
    }
    // if waka is at centre of tile
    if (waka.isCentre()) {
      // if this is the first time waka has stepped on a tile
      // collects edible
      if (object.step()) {
        for (Ghost ghost: ghosts) {
          if (object.ID.equals("8")) {
            // if superfruit, frightens ghost
            ghost.frighten(false);
          } else if (object.ID.equals("9")) {
            // if sodacan, frightens ghost
            ghost.frighten(true);
          }
        }
      }
    }
    for (Ghost ghost: ghosts) {
      if (ghost.eaten) {
        continue;
        // if difference between ghost and waka centres are small, register a collision
      } else if (abs(ghost.x + 13 - waka.x - 11) < 8 && abs(ghost.y + 13 - waka.y - 12) < 8) {
        // if collides while ghost is frightened (not soda mode), kills ghost
        if (ghost.mode.equals("FRIGHT") && !ghost.soda) {
          ghost.eaten = true;
          ghost.direction = 'l';
        } else {
          // kills waka
          waka.die();
          // resets all ghosts to starting position
          for (Ghost ghoss: ghosts) {
            ghoss.eaten = true;
            ghoss.order = 0;
            ghoss.tickCounter = 0;
            ghoss.reset();
            // revives any ghosts that have been eaten before
            ghoss.eaten = false;
          }
        }
      }
    }
  }
  /**
   * checks if win/ending condition has been reached
   */
  public void checkWin() {
    // if waka has collected all fruits, win and end game
    if (waka.fruitCount == 0) {
      this.win = true;
      this.end = true;
      // if waka has no lives left, lose and win game
    } else if (waka.lives == 0) {
      this.win = false;
      this.end = true;
    }
    if (this.end) {
      // wait for 10 seconds before restarting
      if (tickCount == 600) {
        tickCount = 0;
        this.end = false;
        this.win = false;
        this.restart = true;
        return;
      }
      tickCount++;
      return;
    }
  }

  /**
   * called every frame, moves each object in game grid
   * as they need to be moved
   */
  public void tick() {
    checkWin();
    ghostInteract();
    for (GameObject[] array : this.gameObjects) {
        for (GameObject obj: array) {
          if (obj != null) {
            obj.tick();
          }
        }
      }
  }

  /**
   * called every frame, fraws each object in the game grid
   */
  public void draw() {
    // if game has ended draw end screen
    if (this.end) {
        app.textFont(font, 50);
        app.textSize(30);
      if (this.win) {
        app.text("YOU WIN", 110, 250); // if win
      } else {
        app.text("GAME OVER", 90, 250); // if lose
      }
      return;
    }

    // draws all objects in grid so they do not overlap
    for (GameObject[] objects : this.gameObjects) {
      for (GameObject obj: objects) {
        // if object is a wall or edible, draw it
        if (obj!= null && (obj.ID.equals("wall") || obj.ID.equals("7") 
            || obj.ID.equals("8") || obj.ID.equals("9"))) {
          obj.draw(app);
        }
      }
    }
    waka.draw(app);
    for (GameObject ghost : this.ghosts) {
      ghost.draw(app);
    }
  }
  /**
   * depending on which key is pressed, either moves waka or toggles debug mode
   * @param keyCode keyCode int passed from app, code for key that is pressed
   */
  public void keyManager(int keyCode) {
    // if spacebar is pressed and ghosts are not frightened, toggle debug
    if (keyCode == 32 && !ghosts.get(0).mode.equals("FRIGHT")) {
      for (Ghost ghost: ghosts) {
        ghost.debug = !ghost.debug;
      }
    } else {
      waka.keyInput(keyCode); // gives input to waka
    }  
  }
  
  /**
   * given a mapname, opens a file of characters and creates a 2d array of
   * game objects
   */
  public void parseMap() {
    try{
      File file = new File(this.mapName);
      Scanner sc = new Scanner(file);
      char[][] map = new char[COLS][ROWS];
      int colCount = 0;

      while(sc.hasNext() && colCount < COLS){
        map[colCount] = sc.next().toCharArray(); // turns each line into a character array
        int row = 0;
        for (char character: map[colCount]) {
          // if characters : 1, 2, 3, 4, 5, 6... create a wall object
          if (Character.getNumericValue(character) < 7 && character != '0') {
            Wall wall = new Wall(row*PIX, colCount*PIX, "wall", character);
            this.gameObjects[colCount][row] = wall;
          // if characters : 7, 8, 9.. create an edible object
          } else if (Character.isDigit(character) && character != '0') {
            Edible eat = new Edible(row*PIX, colCount*PIX, String.valueOf(character));
            this.gameObjects[colCount][row] = eat;

          } else if (character == 'p') {
            // creates waka
            int[] data = {row*PIX, colCount*PIX, this.speed, this.lives};
            this.waka = new Waka(data, "waka");
            this.gameObjects[colCount][row] = waka;
            // if character is a, c, i, w... create ghost object
          } else if (Character.isLetter(character)) {
            Ghost ghost = null;
            int[] data = {row*PIX, colCount*PIX, this.speed, this.frightLength};
            switch(character) {
              case 'a':
                ghost = new Ambusher(data, this.modeLength, "ambusher");
                break;
              case 'c':
                ghost = new Chaser(data, this.modeLength, "chaser");
                break;
              case 'i':
                ghost = new Ignorant(data, this.modeLength, "ignorant");
                break;
              case 'w':
                ghost = new Whim(data, this.modeLength, "whim");
                break;
            }
            this.gameObjects[colCount][row] = ghost;     
            this.ghosts.add(ghost);       
          }
          if (this.gameObjects[colCount][row] != null) {
            // adds respective sprites to object
            this.gameObjects[colCount][row].addSprite(this.app);
          }
            row++;
        }
          colCount++;
      }
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
    /**
   * class that parses JSON configuration
   */
  final static class ParseConfig {
    @SuppressWarnings("unchecked")  

    String mapName;
    int lives;
    int speed;
    int frightLength;
    JSONArray modeLength;
    /**
   * parses the JSON config
   * collects information on map name, waka lives, waka speed
   * , length of ghost frighten mode, and modelength of ghost modes
   * @param JSONFilename String filename to open and parse information
   */
    public ParseConfig(String JSONfilename) {
      JSONParser parser = new JSONParser();
      try {
        Object object = parser.parse(new FileReader(JSONfilename));
        JSONObject jsonObject = (JSONObject) object;
        this.mapName = (String) jsonObject.get("map");
        this.lives = (int) (long) jsonObject.get("lives");
        this.speed = (int) (long) jsonObject.get("speed");
        this.modeLength = (JSONArray) jsonObject.get("modeLengths");
        this.frightLength = (int) (long) jsonObject.get("fright");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

