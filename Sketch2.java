import processing.core.PApplet;
import processing.core.PImage;

public class Sketch2 extends PApplet {

    // Arrow key variables
    boolean blnUpArrow = false;
    boolean blnDownArrow = false;
    boolean blnRightArrow = false;
    boolean blnLeftArrow = false;

    // Movement key variables
    boolean blnUp = false;
    boolean blnDown = false;
    boolean blnLeft = false;
    boolean blnRight = false;

    int intLevel = 1;
    int intPlayerX, intPlayerY;
    int intPlayerSpeed = 4;
    int intPlayerSize = 30;
    int intPlayerHealth = 3;

    PImage imgCharacter1;

    int numCircles = 5;
    float[] circleX = new float[numCircles];
    float[] circleSpeeds = new float[numCircles];
    float circleSpacing = 120;
    float circleY;
    float circleDiameter = 30;

    // Laser variables
    int numLasers = 5;
    float[] laserX = new float[numLasers];
    float[] laserY = new float[numLasers];
    float laserSpeed = 1;
    boolean[] isLaserActive = new boolean[numLasers];
    int intLaserCooldown = 5; 
    int intCurrentCooldown = 0;

    public void settings() {
        size(600, 750);
    }

    public void setup() {
        background(255);
        intPlayerX = width / 2;
        intPlayerY = height - 80; // Start at the bottom center slightly spaced up

        imgCharacter1 = loadImage("nerd.png");
        imgCharacter1.resize(imgCharacter1.width/3, imgCharacter1.height/3);
    


        // Initialize circle positions spaced equally
        for (int i = 0; i < numCircles; i++) {
            circleX[i] = (width / 2) - ((numCircles - 1) * circleSpacing / 2) + (i * circleSpacing);
            circleSpeeds[i] = (float) 1.5; // Set a fixed speed for all circles
        }

        circleY = (float) (height / 4.2);

        // Initialize laser positions and status
        for (int i = 0; i < numLasers; i++) {
            isLaserActive[i] = false;
        }
    }

    public void draw() {
        background(0);

        if (intLevel == 1) {
            // intLevel 1: Menu Screen
            fill(255);
            textSize(32);
            text("Menu Screen - Press Enter to Start", width / 20, height / 2);
        } else if (intLevel >= 2 && intLevel <= 4) {
            // Levels 2-4: Gameplay
            handleInput();
            movePlayer();
            displayPlayer();

            // Move and display circles
            moveCircles();
            displayCircles();

            // Move and display lasers
            moveLasers();
            displayLasers();

            // Enemy shooting logic 
        for (int i = 0; i < numCircles; i++) {
            enemyShoot(i);
        }

        } else if (intLevel == 5) {
            // intLevel 5: Boss Fight
            fill(255);
            textSize(32);
            text("Boss Fight - Press Enter to End", width / 4, height / 2);
            // Add boss mechanics here
            // Check for conditions to end the game
        }
    }

    void moveCircles() {
        // Move circles side to side with consistent speed
        for (int i = 0; i < numCircles; i++) {
            circleX[i] += circleSpeeds[i];

            // Reverse direction if the circle reaches the screen edges
            if (circleX[i] > width - circleDiameter || circleX[i] < 0) {
                circleSpeeds[i] *= -1;
                // Ensure the circle stays within the screen bounds
                circleX[i] = constrain(circleX[i], 0, width - circleDiameter);
            }
        }
    }

    void displayCircles() {
        fill(255, 0, 0); // Red color for circles
        for (int i = 0; i < numCircles; i++) {
            ellipse(circleX[i], circleY, circleDiameter, circleDiameter);
        }
    }

    public void keyPressed() {
        if (keyCode == UP) {
            blnUpArrow = true;
        } else if (keyCode == DOWN) {
            blnDownArrow = true;
        } else if (keyCode == LEFT) {
            blnLeftArrow = true;
        } else if (keyCode == RIGHT) {
            blnRightArrow = true;
        }

        if (key == 'w' || key == 'W') {
            blnUp = true;
        } else if (key == 'd' || key == 'D') {
            blnRight = true;
        } else if (key == 's' || key == 'S') {
            blnDown = true;
        } else if (key == 'a' || key == 'A') {
            blnLeft = true;
        }

        if (keyCode == ENTER) {
            if (intLevel == 1 || intLevel == 5) {
                // Move to the next intLevel when Enter is pressed on the Menu or Boss Fight screen
                intLevel++;
            }
        }
    }

    public void keyReleased() {
        if (keyCode == UP) {
            blnUpArrow = false;
        } else if (keyCode == DOWN) {
            blnDownArrow = false;
        } else if (keyCode == LEFT) {
            blnLeftArrow = false;
        } else if (keyCode == RIGHT) {
            blnRightArrow = false;
        }

        if (key == 'w' || key == 'W') {
            blnUp = false;
        } else if (key == 'd' || key == 'D') {
            blnRight = false;
        } else if (key == 's' || key == 'S') {
            blnDown = false;
        } else if (key == 'a' || key == 'A') {
            blnLeft = false;
        }
    }

    void handleInput() {
        if (blnUpArrow) {
            intPlayerY -= intPlayerSpeed;
        }
        if (blnDownArrow) {
            intPlayerY += intPlayerSpeed;
        }
        if (blnLeftArrow) {
            intPlayerX -= intPlayerSpeed;
        }
        if (blnRightArrow) {
            intPlayerX += intPlayerSpeed;
        }

        if (blnUp) {
            intPlayerY -= intPlayerSpeed;
        }
        if (blnDown) {
            intPlayerY += intPlayerSpeed;
        }
        if (blnLeft) {
            intPlayerX -= intPlayerSpeed;
        }
        if (blnRight) {
            intPlayerX += intPlayerSpeed;
        }
    }

    void movePlayer() {
        // Add boundary checks to keep the player within the screen bounds
        intPlayerX = constrain(intPlayerX, 0, width - intPlayerSize);

        // Restrict the player from going above the top third
        intPlayerY = constrain(intPlayerY, height / 3, height - intPlayerSize);
    }

    void displayPlayer() {
        fill(0, 255, 0); // Green color for the player
        image(imgCharacter1, intPlayerX, intPlayerY);
    }

    void enemyShoot(int circleIndex) {
        // Randomly decide when to shoot
        if (random(1) < 0.01) {
            // Activate a laser at the current circle position
            for (int j = 0; j < numLasers; j++) {
                if (!isLaserActive[j]) {
                    laserX[j] = circleX[circleIndex];
                    laserY[j] = circleY;
                    isLaserActive[j] = true;
                    break; // Exit the loop after activating one laser
                }
            }
        }
    }

    void moveLasers() {
        for (int i = 0; i < numLasers; i++) {
            if (isLaserActive[i]) {
                laserY[i] += laserSpeed;
                // Deactivate laser when it goes off-screen
                if (laserY[i] < 0) {
                    isLaserActive[i] = false;
                }
            }
        }
    }

    void displayLasers() {
        fill(255, 0, 0); // Red color for lasers
        for (int i = 0; i < numLasers; i++) {
            if (isLaserActive[i]) {
                rect(laserX[i], laserY[i], 5, 15); // Adjust size as needed
            }
        }
    }
}
