import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {

  int level = 1;
  int playerX, playerY;
  int playerSpeed = 100;
  int playerSize = 30;
  int playerHealth = 3;
  PImage imgCharacter1;

  public void settings() {
    size(600, 750);
  }

  public void setup() {
    background(0);
    playerX = width / 2;
    playerY = height - 50;
  }

  public void draw() {
    background(0);

    if (level == 1) {
      // Level 1: Menu Screen
      fill(255);
      textSize(32);
      text("Menu Screen - Press Enter to Start", width/4, height/2);
    } else if (level >= 2 && level <= 4) {
      // Levels 2-4: Gameplay
      handleInput();
      movePlayer();
      displayPlayer();
      // Add enemy movement and collision logic here
      // Check for conditions to move to the next level or end the game
    } else if (level == 5) {
      // Level 5: Boss Fight
      fill(255);
      textSize(32);
      text("Boss Fight - Press Enter to End", width/4, height/2);
      // Add boss mechanics here
      // Check for conditions to end the game
    }
  }

  void handleInput() {
    if (keyPressed) {
      if (keyCode == UP) {
        playerY -= playerSpeed;
      } else if (keyCode == DOWN) {
        playerY += playerSpeed;
      } else if (keyCode == LEFT) {
        playerX -= playerSpeed;
      } else if (keyCode == RIGHT) {
        playerX += playerSpeed;
      }
    }
  }

  void movePlayer() {
    // Add boundary checks to keep the player within the screen
    playerX = constrain(playerX, 0, width - playerSize);
    playerY = constrain(playerY, 0, height - playerSize);
  }

  void displayPlayer() {
    fill(0, 255, 0); // Green color for the player
    rect(playerX, playerY, playerSize, playerSize);
  }

  public void keyPressed() {
    if (keyCode == ENTER) {
      if (level == 1 || level == 5) {
        // Move to the next level when Enter is pressed on the Menu or Boss Fight screen
        level++;
      }
    }
  }
}
