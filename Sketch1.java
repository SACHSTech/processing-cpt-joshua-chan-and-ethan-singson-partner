import processing.core.PApplet;
import processing.core.PImage;






public class Sketch1 extends PApplet {

    // Movement key variables
    boolean blnUp = false;
    boolean blnDown = false;
    boolean blnLeft = false;
    boolean blnRight = false;

    int level = 1;
    int playerX, playerY;
    int playerSpeed = 4;
    int playerSize = 60;
    int playerHealth = 3;

    // Declare player image variable
    PImage playerImage;

    int numCircles = 4;  // Set the number of circles to 4
    float[] circleX = new float[numCircles];
    float[] circleSpeeds = new float[numCircles];
    float circleSpacing = 120;
    float circleY;
    float circleDiameter = 35;

    // array to track whether each circle has been hit
    boolean[] isCircleHit = new boolean[numCircles];

    // Laser variables
    int numLasers = 10000;
    float[] laserX = new float[numLasers];
    float[] laserY = new float[numLasers];
    float laserSpeed = 8;
    boolean[] isLaserActive = new boolean[numLasers];

    // Laser variables for the player
    int numPlayerLasers = 1000;
    float[] playerLaserX = new float[numPlayerLasers];
    float[] playerLaserY = new float[numPlayerLasers];
    float playerLaserSpeed = 10;
    boolean[] isPlayerLaserActive = new boolean[numPlayerLasers];

    // Define a cooldown duration in milliseconds
    int laserCooldown = 400;

    // Keep track of the time when the spacebar was last pressed
    long lastSpacebarTime = 0;




    public void settings() {
        size(600, 750);
    }

    public void setup() {
        background(255);
        playerX = width / 2;
        playerY = height - 80; // Start at the bottom center slightly spaced up

        // Load the player image
        playerImage = loadImage("nerd.png");

        
 

        // Initialize circle positions spaced equally for the first row
        for (int i = 0; i < numCircles; i++) {
            circleX[i] = (width / 2) - ((numCircles - 1) * circleSpacing / 2) + (i * circleSpacing);
            circleSpeeds[i] = (float) 1.5; // Set a fixed speed for all circles in the first row
        }

        circleY = (float) (height / 4.2);

        // Initialize laser positions and status
        for (int i = 0; i < numLasers; i++) {
            isLaserActive[i] = false;
        }

        // Initialize player lasers
        for (int i = 0; i < numPlayerLasers; i++) {
            isPlayerLaserActive[i] = false;
        }

        
    }



    public void draw() {
        background(0);

        if (level == 1) {
            // Level 1: Menu Screen
            fill(255);
            textSize(32);
            text("Menu Screen - Press Enter to Start", width / 20, height / 2);
        } else if (level >= 2 && level <= 4) {
            // Levels 2-4: Gameplay
            handleInput();
            movePlayer();

            // Display the player using the image
            image(playerImage, playerX, playerY, playerSize, playerSize);

            // Move and display player lasers
            movePlayerLasers();
            displayPlayerLasers();

            // Check for collisions with the player laser
            checkCollisions();

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

        } else if (level == 5) {
            // Level 5: Boss Fight
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
    
        // Display circles for the first row
        for (int i = 0; i < numCircles; i++) {
            if (!isCircleHit[i]) {
                ellipse(circleX[i], circleY, circleDiameter, circleDiameter);
            }
        }
    }
    
       
    

    public void keyPressed() {

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
            if (level == 1 || level == 5) {
                // Move to the next level when Enter is pressed on the Menu or Boss Fight screen
                level++;
            }
        }

        if (key == ' ') {
            // Check if enough time has passed since the last shot
            long currentTime = millis();
            if (currentTime - lastSpacebarTime >= laserCooldown) {
                // Shoot a player laser
                shootPlayerLaser();
                // Update the last spacebar press time
                lastSpacebarTime = currentTime;
            }
        }
    }

    public void keyReleased() {

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

        if (blnUp) {
            playerY -= playerSpeed;
        }
        if (blnDown) {
            playerY += playerSpeed;
        }
        if (blnLeft) {
            playerX -= playerSpeed;
        }
        if (blnRight) {
            playerX += playerSpeed;
        }
    }

    void movePlayer() {
        // Add boundary checks to keep the player within the screen bounds
        playerX = constrain(playerX, 0, width - playerSize);

        // Restrict the player from going above the top third
        playerY = constrain(playerY, (int) (height / 1.5), height - playerSize);
    }

    void displayPlayer() {
        fill(0, 255, 0); // Green color for the player
        rect(playerX, playerY, playerSize, playerSize);
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
                // Check if the corresponding circle is not hit before displaying the laser
                boolean isActive = true;
                for (int j = 0; j < numCircles; j++) {
                    if (circleHit(laserX[i], laserY[i], circleX[j], circleY, circleDiameter / 2) && isCircleHit[j]) {
                        isActive = false;
                        break;
                    }
                }
                if (isActive) {
                    rect(laserX[i], laserY[i], 5, 15); // Adjust size as needed
                } else {
                    isLaserActive[i] = false; // Deactivate the laser if the corresponding circle is hit
                }
            }
        }
    }

    void movePlayerLasers() {
        for (int i = 0; i < numPlayerLasers; i++) {
            if (isPlayerLaserActive[i]) {
                playerLaserY[i] -= playerLaserSpeed;

                // Deactivate player laser when it goes off-screen
                if (playerLaserY[i] < 0) {
                    isPlayerLaserActive[i] = false;
                }
            }
        }
    }

    void displayPlayerLasers() {
        fill(0, 0, 255); // Blue color for player lasers
        for (int i = 0; i < numPlayerLasers; i++) {
            if (isPlayerLaserActive[i]) {
                rect(playerLaserX[i], playerLaserY[i], 5, 15); // Adjust size as needed
            }
        }
    }

    void checkCollisions() {
        for (int i = 0; i < numPlayerLasers; i++) {
            for (int j = 0; j < numCircles; j++) {
                if (isPlayerLaserActive[i] && !isCircleHit[j] &&
                        circleHit(playerLaserX[i], playerLaserY[i], circleX[j], circleY, circleDiameter / 2)) {
                    // Deactivate player laser
                    isPlayerLaserActive[i] = false;

                    // Mark the circle as hit and move it off-screen
                    isCircleHit[j] = true;
                    circleX[j] = -1000; // Move the circle off-screen
                }
            }
        }
    }

    // Helper function to check if a point is inside a circle
    boolean circleHit(float pointX, float pointY, float circleX, float circleY, float circleRadius) {
        float d = dist(pointX, pointY, circleX, circleY);
        return d < circleRadius;
    }

    void shootPlayerLaser() {
        for (int i = 0; i < numPlayerLasers; i++) {
            if (!isPlayerLaserActive[i]) {
                playerLaserX[i] = playerX + playerSize / 2;
                playerLaserY[i] = playerY;
                isPlayerLaserActive[i] = true;

                
                break;
            }
        }
    }
}