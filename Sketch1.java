import processing.core.PApplet;
import processing.core.PImage;
import ddf.minim.*;

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
    PImage enemyImage;

    // Declare background image variables
    PImage startMenu;
    PImage howToMenu;
    PImage optionsMenu;

    int numCircles = 4; // Set the number of circles to 4
    float[] circleX = new float[numCircles];
    float[] circleSpeeds = new float[numCircles];
    float circleSpacing = 120;
    float circleY;
    float circleDiameter = 50;

    // array to track whether each circle has been hit
    boolean[] isCircleHit = new boolean[numCircles];

    // Laser variables
    int numLasers = 5000;
    float[] laserX = new float[numLasers];
    float[] laserY = new float[numLasers];
    float laserSpeed = 7;
    boolean[] isLaserActive = new boolean[numLasers];

    // Laser variables for the player
    int numPlayerLasers = 1000;
    float[] playerLaserX = new float[numPlayerLasers];
    float[] playerLaserY = new float[numPlayerLasers];
    float playerLaserSpeed = 7;
    boolean[] isPlayerLaserActive = new boolean[numPlayerLasers];

    // Define a cooldown duration in milliseconds
    int laserCooldown = 450;

    // Start Button
    int startButtonTopLeftX = 185;
    int startButtonTopLeftY = 460;
    int startButtonBottomRightX = 417;
    int startButtonBottomRightY = 515;

    boolean startButtonPressed = false;

    // How to Play Button
    int howToPlayButtonTopLeftX = 183;
    int howToPlayButtonTopLeftY = 604;
    int howToPlayButtonBottomRightX = 417;
    int howToPlayButtonBottomRightY = 660;

    boolean howToPlayButtonPressed = false;

    // Audio Declarations
    Minim minim;
    AudioPlayer laserSound;

    long lastSpacebarTime = 0;

    public void settings() {
        size(600, 750);
    }

    public void setup() {
        background(255);
        playerX = width / 2;
        playerY = height - 80; // Start at the bottom center slightly spaced up

        // Audio
        minim = new Minim(this);
        laserSound = minim.loadFile("lasergun.mp3"); // Change the filename to your actual sound file

        // Load the player image
        playerImage = loadImage("nerd.png");

        // Load the enemy image
        enemyImage = loadImage("enemy.png");

        // Load backgrounds
        startMenu = loadImage("START_MENU.png");
        howToMenu = loadImage("HOW_TO_PLAY_FINAL.png");
        optionsMenu = loadImage("OPTIONS_MENU.png");

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

        // Draw the start button
        if (!startButtonPressed) {
            fill(100, 100, 100);
            rect(startButtonTopLeftX, startButtonTopLeftY, startButtonBottomRightX - startButtonTopLeftX,
                    startButtonBottomRightY - startButtonTopLeftY);
            if (isMouseInsideStartButton()) {
                fill(255);
                // Add any additional information or actions when the mouse is over the button
            }
        }

        // Draw the How to Play button
        if (!howToPlayButtonPressed) {
            fill(100, 100, 100);
            rect(howToPlayButtonTopLeftX, howToPlayButtonTopLeftY,
                    howToPlayButtonBottomRightX - howToPlayButtonTopLeftX,
                    howToPlayButtonBottomRightY - howToPlayButtonTopLeftY);
            if (isMouseInsideHowToPlayButton()) {
                fill(255);
                // Add any additional information or actions when the mouse is over the button
            }
        }

        // Draw other elements based on the current level
        if (level == 1) {
            image(startMenu, 0, 0, width, height);
        } else if (level == 2) {
            // Level 2: Options
            // Implement Options screen if needed
        } else if (level == 3) {
            // Level 3: How to Play
            image(howToMenu, 0, 0, width, height);
        } else if (level == 4) {

        } else if (level >= 5 && level <= 7) {
            // Levels 3-5: Gameplay
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
        } else if (level == 7) {
            // Level 6: Boss Fight
            fill(255);
            textSize(32);
            // No text boxes for level 6
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

    public void stop() {
        // Close Minim when the sketch is stopped
        minim.stop();
        super.stop();
    }

    void displayCircles() {
        // fill(255, 0, 0); // Remove this line

        // Display circles for the first row
        for (int i = 0; i < numCircles; i++) {
            if (!isCircleHit[i]) {
                image(enemyImage, circleX[i] - circleDiameter / 2, circleY - circleDiameter / 2, circleDiameter,
                        circleDiameter);
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
        fill(128, 0, 128);
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
                    rect(laserX[i], laserY[i], 6, 35);
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
                rect(playerLaserX[i], playerLaserY[i], 6, 30); // Adjust size as needed
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

                // Check if the sound is not already playing
                if (!laserSound.isPlaying()) {
                    laserSound.rewind(); // Rewind to the beginning
                    laserSound.play(); // Play the laser sound
                }
                break;
            }
        }
    }

    public void mousePressed() {
        // Check if the mouse is pressed over the start button
        if (isMouseInsideStartButton() && !startButtonPressed) {
            startButtonPressed = true; // Mark the start button as pressed
            level = 3; // Set the level to start the game or show How to Play screen
        }

        // Check if the mouse is pressed over the How to Play button
        if (isMouseInsideHowToPlayButton() && !howToPlayButtonPressed) {
            howToPlayButtonPressed = true; // Mark the How to Play button as pressed
            level = 3; // Set the level to show How to Play screen
        }
    }

    boolean isMouseInsideStartButton() {
        // Check if the mouse coordinates are within the button boundaries
        return mouseX >= startButtonTopLeftX &&
                mouseX <= startButtonBottomRightX &&
                mouseY >= startButtonTopLeftY &&
                mouseY <= startButtonBottomRightY;
    }

    boolean isMouseInsideHowToPlayButton() {
        // Check if the mouse coordinates are within the How to Play button boundaries
        return mouseX >= howToPlayButtonTopLeftX &&
                mouseX <= howToPlayButtonBottomRightX &&
                mouseY >= howToPlayButtonTopLeftY &&
                mouseY <= howToPlayButtonBottomRightY;
    }
}