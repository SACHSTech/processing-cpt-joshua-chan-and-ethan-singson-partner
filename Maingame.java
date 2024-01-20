import processing.core.PApplet;
import processing.core.PImage;

public class Maingame extends PApplet {

    // Movement key variables
    boolean blnUp = false;
    boolean blnDown = false;
    boolean blnLeft = false;
    boolean blnRight = false;

    // Player Variables
    int intLevel = 1;
    int intPlayerX, intPlayerY;
    int intPlayerSpeed = 4;
    int intPlayerSize = 60;
    int intPlayerHealth = 3;
    boolean blnIsPlayerHit = false;
    long playerHitStartTime = 0;
    int playerHitDuration = 500; // milliseconds

    // Declare player image variable
    PImage stageOnePlayerImage;
    PImage stageTwoPlayerImage;
    PImage stageThreePlayerImage;
    PImage enemyImage;
    PImage enemyHitImage;
    PImage playerStage1HitImage;

    // Declare background image variables
    PImage startMenu;
    PImage howToMenu;
    PImage optionsMenu;
    PImage level1;
    PImage level2;
    PImage level3;
    PImage health;
    PImage gameOverScreen;


    // Enemy Variables
    int intNumCircles = 7; // Set the number of circles to 4
    float[] fltCircleX = new float[intNumCircles];
    float[] fltCircleSpeeds = new float[intNumCircles];
    int[] intCircleHitCount = new int[intNumCircles];
    float fltCircleSpacing = 120;
    float fltCircleY;
    float fltCircleDiameter = 50;

    // array to track whether each circle has been hit
    boolean[] blnIsCircleHit = new boolean[intNumCircles];

    // Laser variables
    int intNumLasers = 3000;
    float[] fltLaserX = new float[intNumLasers];
    float[] fltLaserY = new float[intNumLasers];
    float fltLaserSpeed = 5;
    boolean[] blnIsLaserActive = new boolean[intNumLasers];

    // Laser variables for the player
    int intNumPlayerLasers = 1000;
    float[] fltPlayerLaserX = new float[intNumPlayerLasers];
    float[] fltPlayerLaserY = new float[intNumPlayerLasers];
    float fltPlayerLaserSpeed = 7;
    boolean[] blnIsPlayerLaserActive = new boolean[intNumPlayerLasers];

    // Define a cooldown duration in milliseconds
    int intLaserCooldown = 450;

    // Special Laser variables
    int intNumSpecialLasers = 1500;
    float[] fltSpecialLaserX = new float[intNumSpecialLasers];
    float[] fltSpecialLaserY = new float[intNumSpecialLasers];
    float fltSpecialLaserSpeed = 2;
    float fltSpecialLaserSpeedY = 3;
    boolean[] blnIsSpecialLaserActive = new boolean[intNumSpecialLasers];
    float fltWaveFrequencyX = 0.05f;
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

    boolean blnHTPButtonPressed = false;

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

    // Game Over Button

    int intGameOverButtonTopLeftX = 136;
    int intGameOverButtonTopLeftY = 407;
    int intGameOverButtonBottomRightX = 467;
    int intGameOverButtonBottomRightY = 444;

    boolean blnGameOverButtonPressed = false;

    long lastSpacebarTime = 0;

    boolean blnGameOver = false;

    public void settings() {
        size(600, 750);
    }

    public void setup() {
        background(255);
        intPlayerX = width / 2;
        intPlayerY = height - 80; // Start at the bottom center slightly spaced up

        // Load the player image
        stageOnePlayerImage = loadImage("nerd.png");
        stageTwoPlayerImage = loadImage("secondstagesprite.png");
        stageThreePlayerImage = loadImage("FINAL_SPRITE.png");
        enemyHitImage = loadImage("enemyhit.png");
        playerStage1HitImage = loadImage("nerdhit.png");

        // Load the enemy image

        enemyImage = loadImage("enemy.png");

        // Load backgroundsa
        startMenu = loadImage("START_MENU.png");
        howToMenu = loadImage("HOW_TO_PLAY_FINAL.png");
        optionsMenu = loadImage("options.png");
        gameOverScreen = loadImage("gameover.png");
        level1 = loadImage("level1.png");
        level2 = loadImage("level2.png");
        level3 = loadImage("level3.png");
        health = loadImage("health.png");
        health.resize(60, 60);

        // Initialize circle speeds with random values
        for (int i = 0; i < intNumCircles; i++) {
            fltCircleX[i] = (width / 2) - ((intNumCircles - 1) * fltCircleSpacing / 2) + (i * fltCircleSpacing);
            fltCircleSpeeds[i] = random(1.0f, 3.0f); // Set random speeds for each circle
        }

        fltCircleY = (float) (height / 4.5);

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
        if (!blnHTPButtonPressed) {
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

        // Draw the Game Over button
        if (intLevel == 7 && !blnGameOverButtonPressed) {
            fill(100, 100, 100);
            rect(intGameOverButtonTopLeftX, intGameOverButtonTopLeftY,
                    intGameOverButtonBottomRightX - intGameOverButtonTopLeftX,
                    intGameOverButtonBottomRightY - intGameOverButtonTopLeftY);
            if (isMouseInsideGameOverButton()) {
                fill(255);
            }
        }

        // Draw other elements based on the current level
        if (intLevel == 1) {
            // Reset the How to Play button state when returning to the Menu
            image(startMenu, 0, 0, width, height);
        } else if (intLevel == 2) {
            // Level 2: Options
            image(optionsMenu, 0, 0, width, height);
        } else if (intLevel == 3) {
            // Level 3: How to Play
            image(howToMenu, 0, 0, width, height);
        } else if (intLevel == 4) {
            // Draw health.png at the top right of the screen
            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);

            }

            handleInput();
            movePlayer();

            // Display the player using the image
            image(stageOnePlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

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

            // Test Delete later
            moveSpecialLasers();
            displaySpecialLasers();

            // Enemy shooting logic
            for (int i = 0; i < intNumCircles; i++) {
                enemyShoot(i);
                specialEnemyShoot(i);
            }

        } else if (intLevel == 5) {

            background(level2);

            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);

            }

            handleInput();
            movePlayer();

            // Display the player using the image
            image(stageTwoPlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

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

            // Move and display special lasers
            moveSpecialLasers();
            displaySpecialLasers();

            // Enemy shooting logic
            for (int i = 0; i < intNumCircles; i++) {
                enemyShoot(i);
                specialEnemyShoot(i);
            }

        } else if (intLevel == 6) {
            
              background(level3);

            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);

            }

            handleInput();
            movePlayer();

            // Display the player using the image
            image(stageThreePlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

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

        }

        else if (intLevel == 7) {
            image(gameOverScreen, 0, 0, width, height);
        }

        // Check if any circle has reached the bottom of the screen
        for (int i = 0; i < intNumCircles; i++) {
            if (fltCircleY + fltCircleDiameter / 2 > height) {
                blnGameOver = true;

                blnGameOverButtonPressed = false; // Reset game over button state
            }
        }

        if (blnGameOver) {
            intLevel = 7;
        }

        // Check if all circles are hit to advance to the next level
        if (areAllCirclesHit()) {
            if (intLevel < 7) { // Max level is 7 in your code
                intLevel++;
                resetGame(); // Reset the game for the next level
            } else {
                // Game is completed, you can handle this accordingly
                // For now, let's reset to level 1
                intLevel = 1;
                resetGame();
            }
        }

        if (intPlayerHealth > 0 || intLevel == 4) {
            if (blnIsPlayerHit) {
                // Check if enough time has passed since the player was hit
                if (millis() - playerHitStartTime >= playerHitDuration) {
                    blnIsPlayerHit = false;
                } else {
                    image(playerStage1HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
                }
            } else {
                image(stageOnePlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
            }
        }
    }

    void moveCircles() {
        // Move circles side to side with consistent speed
        boolean edgeReached = false; // Variable to track if any circle has reached the edge

        for (int i = 0; i < intNumCircles; i++) {
            fltCircleX[i] += fltCircleSpeeds[i];

            // Reverse direction if the circle reaches the screen edges
            if (fltCircleX[i] > width - 5 || fltCircleX[i] < -5) {
                fltCircleSpeeds[i] *= -1;
                // Ensure the circle stays within the screen bounds
                fltCircleX[i] = constrain(fltCircleX[i], 0, width - fltCircleDiameter);

                // Mark that a circle has reached the edge
                edgeReached = true;
            }
        }

        // Move the circles down after reaching the screen edges (outside the loop)
        if (edgeReached) {
            fltCircleY += 10;

            // Reset hit count for circles that reach the bottom
            for (int i = 0; i < intNumCircles; i++) {
                if (fltCircleY + fltCircleDiameter / 2 > height && !blnIsCircleHit[i]) {
                    intCircleHitCount[i] = 0;
                }
            }

            edgeReached = false; // Reset the edgeReached variable
        }

    }

    void displayCircles() {
        // Display circles for the first row
        for (int i = 0; i < intNumCircles; i++) {
            if (!blnIsCircleHit[i]) {
                if (intCircleHitCount[i] > 0 && millis() % 500 > 250) {
                    // Flash the enemy when hit
                    image(enemyHitImage, fltCircleX[i] - fltCircleDiameter / 2, fltCircleY - fltCircleDiameter / 2,
                            fltCircleDiameter,
                            fltCircleDiameter);
                } else {
                    image(enemyImage, fltCircleX[i] - fltCircleDiameter / 2, fltCircleY - fltCircleDiameter / 2,
                            fltCircleDiameter,
                            fltCircleDiameter);
                }
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
        fill(0, 250, 0); // Dark Green color
        noStroke(); // Remove the outline of the ellipse
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
                    rect(fltLaserX[i], fltLaserY[i], 7, 35);
                } else {
                    blnIsLaserActive[i] = false; // Deactivate the laser if the corresponding circle is hit
                }
            }
        }
    }

    void moveSpecialLasers() {
        for (int i = 0; i < intNumSpecialLasers; i++) {
            if (blnIsSpecialLaserActive[i]) {
                // Update X-coordinate with a since wave trajectory

                fltSpecialLaserX[i] += fltSpecialLaserSpeed * sin(fltWaveFrequencyX * frameCount);

                // Update Y-coordinate directly
                fltSpecialLaserY[i] += fltSpecialLaserSpeedY;

                // Deactivate special laser when it goes off-screen
                if (fltSpecialLaserY[i] < 0) {
                    blnIsSpecialLaserActive[i] = false;
                }
            }
        }
    }

    void displaySpecialLasers() {
        fill(255, 255, 0);
        noStroke(); // Remove the outline of the ellipse
        for (int i = 0; i < intNumSpecialLasers; i++) {
            if (blnIsSpecialLaserActive[i]) {
                // Check if the corresponding circle is not hit before displaying the special
                // laser
                boolean isActive = true;
                for (int j = 0; j < intNumCircles; j++) {
                    if (circleHit(fltSpecialLaserX[i], fltSpecialLaserY[i], fltCircleX[j], fltCircleY,
                            fltCircleDiameter / 2) && blnIsCircleHit[j]) {
                        isActive = false;
                        break;
                    }
                }
                if (isActive) {
                    ellipse(fltSpecialLaserX[i], fltSpecialLaserY[i], 15, 15);
                } else {
                    blnIsSpecialLaserActive[i] = false; // Deactivate the special laser if the corresponding circle is
                                                        // hit
                }
            }
        }
    }

    void specialEnemyShoot(int circleIndex) {
        // Randomly decide when to shoot a special laser
        if (random(1) < 0.005) {
            // Activate a special laser at the current circle position
            for (int j = 0; j < intNumSpecialLasers; j++) {
                if (!blnIsSpecialLaserActive[j]) {
                    fltSpecialLaserX[j] = fltCircleX[circleIndex];
                    fltSpecialLaserY[j] = fltCircleY;
                    blnIsSpecialLaserActive[j] = true;
                    break; // Exit the loop after activating one special laser
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
        noStroke();
        for (int i = 0; i < intNumPlayerLasers; i++) {
            if (blnIsPlayerLaserActive[i]) {
                rect(fltPlayerLaserX[i], fltPlayerLaserY[i], 8, 30); // Adjust size as needed
            }
        }
    }

    void checkCollisions() {
        for (int i = 0; i < intNumPlayerLasers; i++) {
            for (int j = 0; j < intNumCircles; j++) {
                if (blnIsPlayerLaserActive[i] && !blnIsCircleHit[j] &&
                        circleHit(fltPlayerLaserX[i], fltPlayerLaserY[i], fltCircleX[j], fltCircleY,
                                fltCircleDiameter / 2)) {
                    // Increment the hit count for the circle
                    intCircleHitCount[j]++;

                    // Check if the circle has been hit twice
                    if (intCircleHitCount[j] >= 2) {
                        // Mark the circle as hit and move it off-screen
                        blnIsCircleHit[j] = true;
                        fltCircleX[j] = -1000; // Move the circle off-screen
                    }

                    // Deactivate player laser
                    blnIsPlayerLaserActive[i] = false;
                }
            }
        }
    }

    void checkPlayerCollisions() {
        // Check regular laser collisions
        for (int i = 0; i < intNumLasers; i++) {
            if (blnIsLaserActive[i] && circleHit(fltLaserX[i], fltLaserY[i], intPlayerX + intPlayerSize / 2,
                    intPlayerY + intPlayerSize / 2, intPlayerSize / 2)) {
                handlePlayerHit();
                // Deactivate the regular laser when it hits the player
                blnIsLaserActive[i] = false;
            }
        }

        // Check special laser collisions
        for (int i = 0; i < intNumSpecialLasers; i++) {
            if (blnIsSpecialLaserActive[i] && circleHit(fltSpecialLaserX[i], fltSpecialLaserY[i],
                    intPlayerX + intPlayerSize / 2, intPlayerY + intPlayerSize / 2, intPlayerSize / 2)) {
                handlePlayerHit();
                // Deactivate the special laser when it hits the player
                blnIsSpecialLaserActive[i] = false;
            }
        }
    }

    void handlePlayerHit() {
        if (!blnIsPlayerHit) {
            // Set the player hit state and record the start time
            blnIsPlayerHit = true;
            playerHitStartTime = millis();
            // Decrement player health
            intPlayerHealth--;

            // Check if player health is zero, and handle game over logic if needed
            if (intPlayerHealth <= 0) {
                blnGameOver = true;
                blnGameOverButtonPressed = false;
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
            resetGame(); // Reset the game when the Start Game button is pressed
            blnStartPressed = true;
            blnHTPButtonPressed = true;
            blnMenuButtonPressed = true;
            blnOptionsButtonPressed = true;
            intLevel = 4;
        }

        // Check if the mouse is pressed over the How to Play button
        if (isMouseInsideHowToPlayButton() && !blnHTPButtonPressed) {
            blnHTPButtonPressed = true; // Mark the How to Play button as pressed
            blnMenuButtonPressed = false; // Reset the Back to Menu button state
            blnOptionsButtonPressed = true;
            blnStartPressed = true;
            intLevel = 3; // Set the level to show How to Play screen
        }

        // Check if the mouse is pressed over the back to menu button
        if (isMouseInsideMenuButton() && !blnMenuButtonPressed) {
            blnMenuButtonPressed = true; // Mark the Menu button as pressed
            blnHTPButtonPressed = false;
            blnOptionsButtonPressed = false;
            blnStartPressed = false;
            intLevel = 1; // Set the level to show Menu screen
        }

        // Check if the mouse is pressed over the options button
        if (isMouseInsideOptionsButton() && !blnOptionsButtonPressed) {
            blnOptionsButtonPressed = true; // Mark the options button as pressed
            blnHTPButtonPressed = true;
            blnMenuButtonPressed = false;
            blnStartPressed = true;
            intLevel = 2;
        }

        // Check if the mouse is pressed over the Game Over button
        if (intLevel == 7 && isMouseInsideGameOverButton()) {
            resetGame(); // Reset the game when the Game Over button is pressed
            blnGameOverButtonPressed = false; // Reset game over button state
            intLevel = 1; // Set the level to return to the Menu screen
            blnStartPressed = false;
            blnOptionsButtonPressed = false;
            blnHTPButtonPressed = false;

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

    boolean isMouseInsideGameOverButton() {
        return mouseX >= intGameOverButtonTopLeftX &&
                mouseX <= intGameOverButtonBottomRightX &&
                mouseY >= intGameOverButtonTopLeftY &&
                mouseY <= intGameOverButtonBottomRightY;
    }

    public void resetGame() {
        // Reset player variables
        intPlayerX = width / 2;
        intPlayerY = height - 80;
        intPlayerHealth = 3;

        // Reset circle variables
        fltCircleY = (float) (height / 10); // Adjust the Y position of circles

        for (int i = 0; i < intNumCircles; i++) {
            fltCircleX[i] = (width / 2) - ((intNumCircles - 1) * fltCircleSpacing / 2) + (i * fltCircleSpacing);
            fltCircleSpeeds[i] = random(1.0f, 3.0f);
            blnIsCircleHit[i] = false;
        }

        for (int i = 0; i < intNumCircles; i++) {
            intCircleHitCount[i] = 0;
        }

        // Reset laser variables
        for (int i = 0; i < intNumLasers; i++) {
            blnIsLaserActive[i] = false;
        }

        // Reset special laser variables
        for (int i = 0; i < intNumSpecialLasers; i++) {
            blnIsSpecialLaserActive[i] = false;
        }

        // Reset player laser variables
        for (int i = 0; i < intNumPlayerLasers; i++) {
            blnIsPlayerLaserActive[i] = false;
        }

        // Set game over state to false
        blnGameOver = false;
    }

    boolean areAllCirclesHit() {
        for (int i = 0; i < intNumCircles; i++) {
            if (!blnIsCircleHit[i]) {
                return false; // If any circle is not hit, return false
            }
        }
        return true; // All circles are hit
    }

}