import processing.core.PApplet;
import processing.core.PImage;

public class Sketch1 extends PApplet {

    // Movement key variables
    boolean blnUp = false;
    boolean blnDown = false;
    boolean blnLeft = false;
    boolean blnRight = false;

    int intLevel = 1;
    int intPlayerX, intPlayerY;
    int intPlayerSpeed = 4;
    int intPlayerSize = 60;
    int intPlayerHealth = 3;

    // Declare player image variable
    PImage playerImage;
    PImage enemyImage;

    // Declare background image variables
    PImage startMenu;
    PImage howToMenu;
    PImage optionsMenu;
    PImage level1;
    PImage health;

    int intNumCircles = 10; // Set the number of circles to 4
    float[] fltCircleX = new float[intNumCircles];
    float[] fltCircleSpeeds = new float[intNumCircles];
    float fltCircleSpacing = 120;
    float fltCircleY;
    float fltCircleDiameter = 50;

    // array to track whether each circle has been hit
    boolean[] blnIsCircleHit = new boolean[intNumCircles];

    // Laser variables
    int intNumLasers = 5000;
    float[] fltLaserX = new float[intNumLasers];
    float[] fltLaserY = new float[intNumLasers];
    float fltLaserSpeed = 5 ;
    boolean[] blnIsLaserActive = new boolean[intNumLasers];

    // Laser variables for the player
    int intNumPlayerLasers = 1000;
    float[] fltPlayerLaserX = new float[intNumPlayerLasers];
    float[] fltPlayerLaserY = new float[intNumPlayerLasers];
    float fltPlayerLaserSpeed = 7
    ;
    boolean[] blnIsPlayerLaserActive = new boolean[intNumPlayerLasers];

    // Define a cooldown duration in milliseconds
    int intLaserCooldown = 450;

    // Start Button
    int intStartTopLeftX = 185;
    int intStartTopLeftY = 460;
    int intStartBottomRightX = 417;
    int intStartBottomRightY = 515;

    boolean blnStartPressed = false;

    // How to Play Button
    int intHTPButtonTopLeftX = 183;
    int intHTPButtonTopLeftY = 604;
    int intHTPButtonBottomRightX = 417;
    int intHTPButtonBottomRightY = 660;

    boolean howToPlayButtonPressed = false;

    // Back to Menu Button

    int intMenuButtonTopLeftX = 497;
    int intMenuButtonTopLeftY = 691;
    int intMenuButtonBottomRightX = 590;
    int intMenuButtonBottomRightY = 727;

    boolean blnMenuButtonPressed = false;

    // Options Menu Button

    int intOptionsButtonTopLeftX = 183;
    int intOptionsButtonTopLeftY = 531;
    int intOptionsButtonBottomRightX = 418;
    int intOptionsButtonBottomRightY = 588;

    boolean blnOptionsButtonPressed = false;

    long lastSpacebarTime = 0;

    public void settings() {
        size(600, 750);
    }

    public void setup() {
        background(255);
        intPlayerX = width / 2;
        intPlayerY = height - 80; // Start at the bottom center slightly spaced up

        // Load the player image
        playerImage = loadImage("nerd.png");

        // Load the enemy image
        enemyImage = loadImage("enemy.png");

        // Load backgroundsa
        startMenu = loadImage("START_MENU.png");
        howToMenu = loadImage("HOW_TO_PLAY_FINAL.png");
        optionsMenu = loadImage("OPTIONS_MENU.png");
        level1 = loadImage("level1.png");
        health = loadImage("health.png");
        health.resize(60, 60);

        // Initialize circle speeds with random values
    for (int i = 0; i < intNumCircles; i++) {
        fltCircleX[i] = (width / 2) - ((intNumCircles - 1) * fltCircleSpacing / 2) + (i * fltCircleSpacing);
        fltCircleSpeeds[i] = random(1.0f, 3.0f); // Set random speeds for each circle
    }


        fltCircleY = (float) (height / 4.2);

        // Initialize laser positions and status
        for (int i = 0; i < intNumLasers; i++) {
            blnIsLaserActive[i] = false;
        }

        // Initialize player lasers
        for (int i = 0; i < intNumPlayerLasers; i++) {
            blnIsPlayerLaserActive[i] = false;
        }
    }

    public void draw() {
        background(level1);

        // Draw the start button
        if (!blnStartPressed) {
            fill(100, 100, 100);
            rect(intStartTopLeftX, intStartTopLeftY, intStartBottomRightX - intStartTopLeftX,
                    intStartBottomRightY - intStartTopLeftY);
            if (isMouseInsideStartButton()) {
                fill(255);
            }
        }

        // Draw the How to Play button
        if (!howToPlayButtonPressed) {
            fill(100, 100, 100);
            rect(intHTPButtonTopLeftX, intHTPButtonTopLeftY,
                    intHTPButtonBottomRightX - intHTPButtonTopLeftX,
                    intHTPButtonBottomRightY - intHTPButtonTopLeftY);
            if (isMouseInsideHowToPlayButton()) {
                fill(255);

            }
        }

        // Draw the back to menu button

        if (!blnMenuButtonPressed) {
            fill(100, 100, 100);
            rect(intMenuButtonTopLeftX, intMenuButtonTopLeftY,
                    intMenuButtonBottomRightX - intMenuButtonTopLeftX,
                    intMenuButtonBottomRightY - intMenuButtonTopLeftY);
            if (isMouseInsideMenuButton()) {
                fill(255);

            }

        }

        // Draw other elements based on the current level
        if (intLevel == 1) {
            // Reset the How to Play button state when returning to the Menu
            image(startMenu, 0, 0, width, height);
        } else if (intLevel == 2) {
            // Level 2: Options
            // Implement Options screen if needed
        } else if (intLevel == 3) {
            // Level 3: How to Play
            image(howToMenu, 0, 0, width, height);
        } else if (intLevel >= 4 && intLevel <= 7) {
            // Draw health.png at the top right of the screen
            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);

            }

            handleInput();
            movePlayer();

            // Display the player using the image
            image(playerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

            // Move and display player lasers
            movePlayerLasers();
            displayPlayerLasers();

            // Check for collisions with the player laser
            checkCollisions();

            // Check for collisions with purple lasers
            checkPlayerCollisions();

            // Move and display circles
            moveCircles();
            displayCircles();

            // Move and display lasers
            moveLasers();
            displayLasers();

            // Enemy shooting logic
            for (int i = 0; i < intNumCircles; i++) {
                enemyShoot(i);
            }
        } else if (intLevel == 7) {
            // Level 6: Boss Fight
            fill(255);
            textSize(32);
            // No text boxes for level 6
        }
    }

    void moveCircles() {
        // Move circles side to side with consistent speed
        for (int i = 0; i < intNumCircles; i++) {
            fltCircleX[i] += fltCircleSpeeds[i];

            // Reverse direction if the circle reaches the screen edges
            if (fltCircleX[i] > width - fltCircleDiameter || fltCircleX[i] < 0) {
                fltCircleSpeeds[i] *= -1;
                // Ensure the circle stays within the screen bounds
                fltCircleX[i] = constrain(fltCircleX[i], 0, width - fltCircleDiameter);
            }
        }
    }

    void displayCircles() {
        // Display circles for the first row
        for (int i = 0; i < intNumCircles; i++) {
            if (!blnIsCircleHit[i]) {
                image(enemyImage, fltCircleX[i] - fltCircleDiameter / 2, fltCircleY - fltCircleDiameter / 2,
                        fltCircleDiameter,
                        fltCircleDiameter);
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
            if (currentTime - lastSpacebarTime >= intLaserCooldown) {
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
        intPlayerY = constrain(intPlayerY, (int) (height / 1.5), height - intPlayerSize);
    }

    void displayPlayer() {
        fill(0, 255, 0); // Green color for the player
        rect(intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
    }

    void enemyShoot(int circleIndex) {
        // Randomly decide when to shoot
        if (random(1) < 0.01) {
            // Activate a laser at the current circle position
            for (int j = 0; j < intNumLasers; j++) {
                if (!blnIsLaserActive[j]) {
                    fltLaserX[j] = fltCircleX[circleIndex];
                    fltLaserY[j] = fltCircleY;
                    blnIsLaserActive[j] = true;
                    break; // Exit the loop after activating one laser
                }
            }
        }
    }

    void moveLasers() {
        for (int i = 0; i < intNumLasers; i++) {
            if (blnIsLaserActive[i]) {
                fltLaserY[i] += fltLaserSpeed;
                // Deactivate laser when it goes off-screen
                if (fltLaserY[i] < 0) {
                    blnIsLaserActive[i] = false;
                }
            }
        }
    }

    void displayLasers() {
        fill(128, 0, 128);
        for (int i = 0; i < intNumLasers; i++) {
            if (blnIsLaserActive[i]) {
                // Check if the corresponding circle is not hit before displaying the laser
                boolean isActive = true;
                for (int j = 0; j < intNumCircles; j++) {
                    if (circleHit(fltLaserX[i], fltLaserY[i], fltCircleX[j], fltCircleY, fltCircleDiameter / 2)
                            && blnIsCircleHit[j]) {
                        isActive = false;
                        break;
                    }
                }
                if (isActive) {
                    rect(fltLaserX[i], fltLaserY[i], 6, 35);
                } else {
                    blnIsLaserActive[i] = false; // Deactivate the laser if the corresponding circle is hit
                }
            }
        }
    }

    void movePlayerLasers() {
        for (int i = 0; i < intNumPlayerLasers; i++) {
            if (blnIsPlayerLaserActive[i]) {
                fltPlayerLaserY[i] -= fltPlayerLaserSpeed;

                // Deactivate player laser when it goes off-screen
                if (fltPlayerLaserY[i] < 0) {
                    blnIsPlayerLaserActive[i] = false;
                }
            }
        }
    }

    void displayPlayerLasers() {
        fill(0, 0, 255); // Blue color for player lasers
        for (int i = 0; i < intNumPlayerLasers; i++) {
            if (blnIsPlayerLaserActive[i]) {
                rect(fltPlayerLaserX[i], fltPlayerLaserY[i], 6, 30); // Adjust size as needed
            }
        }
    }

    void checkCollisions() {
        for (int i = 0; i < intNumPlayerLasers; i++) {
            for (int j = 0; j < intNumCircles; j++) {
                if (blnIsPlayerLaserActive[i] && !blnIsCircleHit[j] &&
                        circleHit(fltPlayerLaserX[i], fltPlayerLaserY[i], fltCircleX[j], fltCircleY,
                                fltCircleDiameter / 2)) {
                    // Deactivate player laser
                    blnIsPlayerLaserActive[i] = false;

                    // Mark the circle as hit and move it off-screen
                    blnIsCircleHit[j] = true;
                    fltCircleX[j] = -1000; // Move the circle off-screen
                }
            }
        }
    }

    void checkPlayerCollisions() {
        for (int i = 0; i < intNumLasers; i++) {
            if (blnIsLaserActive[i] && circleHit(fltLaserX[i], fltLaserY[i], intPlayerX + intPlayerSize / 2,
                    intPlayerY + intPlayerSize / 2, intPlayerSize / 2)) {
                // Deactivate the purple laser
                blnIsLaserActive[i] = false;

                // Decrement player health
                intPlayerHealth--;

                // Check if player health is zero, and handle game over logic if needed
                if (intPlayerHealth <= 0) {
                    // Game over logic
                }
            }
        }
    }

    // Helper function to check if a point is inside a circle
    boolean circleHit(float pointX, float pointY, float fltCircleX, float fltCircleY, float circleRadius) {
        float d = dist(pointX, pointY, fltCircleX, fltCircleY);
        return d < circleRadius;
    }

    void shootPlayerLaser() {
        for (int i = 0; i < intNumPlayerLasers; i++) {
            if (!blnIsPlayerLaserActive[i]) {
                fltPlayerLaserX[i] = intPlayerX + intPlayerSize / 2;
                fltPlayerLaserY[i] = intPlayerY;
                blnIsPlayerLaserActive[i] = true;
                break;
            }
        }
    }

    public void mousePressed() {
        // Check if the mouse is pressed over the start button
        if (isMouseInsideStartButton() && !blnStartPressed) {
            blnStartPressed = true;
            howToPlayButtonPressed = true;
            blnMenuButtonPressed = true;
            blnOptionsButtonPressed = true;
            intLevel = 5; // Set the level to start the game or show How to Play screen
        }

        // Check if the mouse is pressed over the How to Play button
        if (isMouseInsideHowToPlayButton() && !howToPlayButtonPressed) {
            howToPlayButtonPressed = true; // Mark the How to Play button as pressed
            blnMenuButtonPressed = false; // Reset the Back to Menu button state
            intLevel = 3; // Set the level to show How to Play screen
        }

        // Check if the mouse is pressed over the back to menu button
        if (isMouseInsideMenuButton() && !blnMenuButtonPressed) {
            blnMenuButtonPressed = true; // Mark the Menu button as pressed
            howToPlayButtonPressed = false;
            intLevel = 1; // Set the level to show Menu screen
        }

        // Check if the mouse is pressed over the options button
        if (isMouseInsideOptionsButton() && !blnOptionsButtonPressed) {
            blnOptionsButtonPressed = true; // Mark the options button as pressed
            intLevel = 2;
        }
    }

    boolean isMouseInsideMenuButton() {
        return mouseX >= intMenuButtonTopLeftX &&
                mouseX <= intMenuButtonBottomRightX &&
                mouseY >= intMenuButtonTopLeftY &&
                mouseY <= intMenuButtonBottomRightY;
    }

    boolean isMouseInsideStartButton() {
        // Check if the mouse coordinates are within the button boundaries
        return mouseX >= intStartTopLeftX &&
                mouseX <= intStartBottomRightX &&
                mouseY >= intStartTopLeftY &&
                mouseY <= intStartBottomRightY;
    }

    boolean isMouseInsideHowToPlayButton() {
        // Check if the mouse coordinates are within the How to Play button boundaries
        return mouseX >= intHTPButtonTopLeftX &&
                mouseX <= intHTPButtonBottomRightX &&
                mouseY >= intHTPButtonTopLeftY &&
                mouseY <= intHTPButtonBottomRightY;

    }

    boolean isMouseInsideOptionsButton() {
        // check if the mouse coordinates are within the options button
        return mouseX >= intOptionsButtonTopLeftX &&
                mouseX <= intOptionsButtonBottomRightX &&
                mouseY >= intOptionsButtonTopLeftY &&
                mouseY <= intOptionsButtonBottomRightY;
    }

}