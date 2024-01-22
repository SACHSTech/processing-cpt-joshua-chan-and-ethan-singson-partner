import processing.core.PApplet;
import processing.core.PImage;

/**
 * Main game class extending PApplet.
 */
public class Maingame extends PApplet {

    // Movement key variables
    boolean blnUp = false;
    boolean blnDown = false;
    boolean blnLeft = false;
    boolean blnRight = false;

    // Player Variables
    int intLevel = 4;
    int intPlayerX, intPlayerY;
    int intPlayerSpeed = 4;
    int intPlayerSize = 60;
    int intPlayerHealth = 5;
    boolean blnIsPlayerHit = false;
    long playerHitStartTime = 0;
    int playerHitDuration = 25; // milliseconds

    // Declare player image variable
    PImage stage1PlayerImage;
    PImage stage2PlayerImage;
    PImage stage3PlayerImage;
    PImage enemyImage;
    PImage enemyHitImage;
    PImage playerStage1HitImage;
    PImage playerStage2HitImage;
    PImage playerStage3HitImage;

    // Declare background image variables
    PImage startMenu;
    PImage startHover;
    PImage howToMenu;
    PImage howToHover;
    PImage optionsMenu;
    PImage optionsHover;
    PImage level1;
    PImage level2;
    PImage level3;
    PImage health;
    PImage gameOverScreen;
    PImage endWinScreen;
    PImage level6;
    PImage newWave;
    PImage bossWarning;

    // Enemy Variables
    int intNumCircles = 7;
    float[] fltCircleX = new float[intNumCircles];
    float[] fltCircleSpeeds = new float[intNumCircles];
    int[] intCircleHitCount = new int[intNumCircles];
    float fltCircleSpacing = 120;
    float fltCircleY;
    float fltCircleDiameter = 50;

    // Array to track whether each circle has been hit
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

    // Cooldown variables for player laser
    int intLaserCooldown = 450;
    long lastSpacebarTime = 0;
    long lastClickTime = 0;

    // Special Laser variables
    int intNumSpecialLasers = 1500;
    float[] fltSpecialLaserX = new float[intNumSpecialLasers];
    float[] fltSpecialLaserY = new float[intNumSpecialLasers];
    float fltSpecialLaserSpeed = 3f;
    float fltSpecialLaserSpeedY = 1.5f;
    boolean[] blnIsSpecialLaserActive = new boolean[intNumSpecialLasers];
    float fltWaveFrequencyX = 0.1f;

    // Start Button
    int intStartTopLeftX = 185;
    int intStartTopLeftY = 460;
    int intStartBottomRightX = 417;
    int intStartBottomRightY = 515;

    // How to Play Button
    int intHTPButtonTopLeftX = 183;
    int intHTPButtonTopLeftY = 604;
    int intHTPButtonBottomRightX = 417;
    int intHTPButtonBottomRightY = 660;

    // Back to Menu Button
    int intMenuButtonTopLeftX = 497;
    int intMenuButtonTopLeftY = 691;
    int intMenuButtonBottomRightX = 590;
    int intMenuButtonBottomRightY = 727;

    // Options Menu Button
    int intOptionsButtonTopLeftX = 183;
    int intOptionsButtonTopLeftY = 531;
    int intOptionsButtonBottomRightX = 418;
    int intOptionsButtonBottomRightY = 588;

    // Change Keybinds Buttons
    int intChangeMovementButtonTopLeftX = 80;
    int intChangeMovementButtonTopLeftY = 430;
    int intChangeMovementButtonBottomRightX = 130;
    int intChangeMovementButtonBottomRightY = 480;

    int buttonAlpha = 200;

    int intChangeShootButtonTopLeftX = 80;
    int intChangeShootButtonTopLeftY = 520;
    int intChangeShootButtonBottomRightX = 130;
    int intChangeShootButtonBottomRightY = 570;

    boolean blnChangeMovementButtonPressed = false;
    boolean blnChangeShootButtonPressed = false;

    // Game Over Button
    int intGameOverButtonTopLeftX = 136;
    int intGameOverButtonTopLeftY = 407;
    int intGameOverButtonBottomRightX = 467;
    int intGameOverButtonBottomRightY = 444;

    // Win Game Button
    int intWinGameButtonTopLeftX = 162;
    int intWinGameButtonTopLeftY = 715;
    int intWinGameButtonBottomRightX = 438;
    int intWinGameButtonBottomRightY = 732;

    // Variable for game state
    boolean blnGameOver = false;

    // Variable to store the start time of the new wave
    long newWaveStartTime = 0;
    // Duration of the new wave display in milliseconds
    int newWaveDuration = 2000; // Change this value to set the duration (in milliseconds)

    // Define Screen Size
    public void settings() {
        size(600, 750);
    }

    public void setup() {
        background(255);
        resetGame();

        // Load the player image
        stage1PlayerImage = loadImage("intergalaticmogger/player/stage1sprite.png");
        stage2PlayerImage = loadImage("intergalaticmogger/player/stage2sprite.png");
        stage3PlayerImage = loadImage("intergalaticmogger/player/stage3sprite.png");
        playerStage1HitImage = loadImage("intergalaticmogger/player/stage1spritehit.png");
        playerStage2HitImage = loadImage("intergalaticmogger/player/stage2spritehit.png");
        playerStage3HitImage = loadImage("intergalaticmogger/player/stage3spritehit.png");
        health = loadImage("intergalaticmogger/player/health.png");
        health.resize(60, 60);

        // Load the enemy image
        enemyImage = loadImage("intergalaticmogger/enemies/enemy.png");
        enemyHitImage = loadImage("intergalaticmogger/enemies/enemyhit.png");

        // Load backgrounds
        startMenu = loadImage("intergalaticmogger/levels/startmenu.png");
        startHover = loadImage("intergalaticmogger/levels/startgamehover.png");
        howToMenu = loadImage("intergalaticmogger/levels/howtoplay.png");
        howToHover = loadImage("intergalaticmogger/levels/htphover.png");
        optionsMenu = loadImage("intergalaticmogger/levels/options.png");
        optionsHover = loadImage("intergalaticmogger/levels/optionshover.png");
        gameOverScreen = loadImage("intergalaticmogger/levels/gameover.png");
        endWinScreen = loadImage("intergalaticmogger/levels/end.png");
        level1 = loadImage("intergalaticmogger/levels/level1.png");
        level2 = loadImage("intergalaticmogger/levels/level2.png");
        level3 = loadImage("intergalaticmogger/levels/level3.png");
        newWave = loadImage("intergalaticmogger/levels/newwave.png");
        bossWarning = loadImage("intergalaticmogger/levels/warningboss.png");

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
    // Draw Levels
    if (intLevel == 1) {
        image(startMenu, 0, 0, width, height);
    } else if (intLevel == 2) {
        image(optionsMenu, 0, 0, width, height);
        drawChangeMovementButton();
        drawChangeShootButton();
    } else if (intLevel == 3) {
        image(howToMenu, 0, 0, width, height);
    } else if (intLevel == 4) {
        background(level1);
        // Draw health.png at the top right of the screen
        for (int i = 0; i < intPlayerHealth; i++) {
            image(health, width - health.width * (i + 1), 10);
        }
        // Movement of the player
        handleInput();
        movePlayer();
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
        // Display the player using the image
        if (intPlayerHealth > 0 && !blnIsPlayerHit) {
            image(stage1PlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
        } else if (intPlayerHealth > 0) {
            // Check if enough time has passed since the player was hit
            if (millis() - playerHitStartTime >= playerHitDuration) {
                blnIsPlayerHit = false;
            } else {
                image(playerStage1HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
            }
        }
     } else if (intLevel == 5) {
        // Check if it's a new wave
        if (newWaveStartTime == 0) {
            // Set the start time of the new wave
            newWaveStartTime = millis();
        }
        // Calculate the elapsed time since the new wave started
        long elapsedTime = millis() - newWaveStartTime;
        // Calculate the time remaining until the new wave ends
        long timeRemaining = newWaveDuration - elapsedTime;

        // Check if it's time to transition to the next level
        if (timeRemaining <= 0) {
            intLevel++;
            resetGame();
            newWaveStartTime = 0; // Reset new wave start time
        } else {
            // Flash the new wave screen by displaying it for half of the remaining time
            if (timeRemaining % 1000 > 500) {
                image(newWave, 0, 0, width, height);
            } else {
                image(level1,0,0,width,height); // Display Level 1 image
            }
        }


        } else if (intLevel == 6) {
            background(level2);
            newWaveStartTime = 0;
            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);
            }
            handleInput();
            movePlayer();
            // Display the player using the image
            if (intPlayerHealth > 0 && !blnIsPlayerHit) {
                image(stage2PlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
            } else if (intPlayerHealth > 0) {
                // Check if enough time has passed since the player was hit
                if (millis() - playerHitStartTime >= playerHitDuration) {
                    blnIsPlayerHit = false;
                } else {
                    image(playerStage2HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
                }
            }
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
            newWaveStartTime = 0;
            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);
            }
            handleInput();
            movePlayer();
            // Display the player using the image
            if (intPlayerHealth > 0 && !blnIsPlayerHit) {
                image(stage3PlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
            } else if (intPlayerHealth > 0) {
                // Check if enough time has passed since the player was hit
                if (millis() - playerHitStartTime >= playerHitDuration) {
                    blnIsPlayerHit = false;
                } else {
                    image(playerStage3HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);
                }
            }
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
        }

        else if (intLevel == 8) {

            image(endWinScreen, 0, 0, width, height);

        } else if (intLevel == 9) {

            image(gameOverScreen, 0, 0, width, height);

        }

        // Check if any circle has reached the bottom of the screen
        for (int i = 0; i < intNumCircles; i++) {

            if (fltCircleY + fltCircleDiameter / 2 > height) {

                blnGameOver = true;

            }
        }

        if (blnGameOver) {

            intLevel = 9;

        }

        // Check if all circles are hit to advance to the next level
        if (areAllCirclesHit()) {

            if (intLevel < 9) {

                intLevel++;
                resetGame();

            } else {

                intLevel = 9;
                resetGame();

            }
        }

        if (intPlayerHealth > 0 && intLevel == 4) {

            if (blnIsPlayerHit) {
                // Check if enough time has passed since the player was hit
                if (millis() - playerHitStartTime >= playerHitDuration) {

                    blnIsPlayerHit = false;

                } else {

                    image(playerStage1HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

                }
            } else {

                image(stage1PlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

            }
        }

        if (intPlayerHealth > 0 && intLevel == 6) {

            if (blnIsPlayerHit) {

                // Check if enough time has passed since the player was hit
                if (millis() - playerHitStartTime >= playerHitDuration) {

                    blnIsPlayerHit = false;

                } else {

                    image(playerStage2HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

                }

            } else {

                image(stage2PlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

            }
        }

        if (intPlayerHealth > 0 && intLevel == 7) {

            if (blnIsPlayerHit) {

                if (millis() - playerHitStartTime >= playerHitDuration) {

                    blnIsPlayerHit = false;

                } else {

                    image(playerStage3HitImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

                }

            } else {

                image(stage3PlayerImage, intPlayerX, intPlayerY, intPlayerSize, intPlayerSize);

            }
        }

        if (isMouseInsideStartButton() && intLevel == 1) {

            image(startHover, intStartTopLeftX - 18, intStartTopLeftY - 6.02f);

        }

        if (isMouseInsideHowToPlayButton() && intLevel == 1) {

            image(howToHover, intHTPButtonTopLeftX - 16, intHTPButtonTopLeftY - 4);

        }

        if (isMouseInsideOptionsButton() && intLevel == 1) {

            image(optionsHover, intOptionsButtonTopLeftX - 16, intOptionsButtonTopLeftY - 4);

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

        if (blnChangeMovementButtonPressed) {

            // Handle custom keybinds
            if (keyCode == UP) {

                blnUp = true;

            } else if (keyCode == RIGHT) {

                blnRight = true;

            } else if (keyCode == DOWN) {

                blnDown = true;

            } else if (keyCode == LEFT) {

                blnLeft = true;

            }

        } else {

            // Handle default keybinds
            if (key == 'w' || key == 'W') {

                blnUp = true;

            } else if (key == 'd' || key == 'D') {

                blnRight = true;

            } else if (key == 's' || key == 'S') {

                blnDown = true;
                
            } else if (key == 'a' || key == 'A') {

                blnLeft = true;

            } else if (key == ' ' && !blnChangeShootButtonPressed) {

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
    }

    public void keyReleased() {

        if (blnChangeMovementButtonPressed) {

            // Handle custom keybinds
            if (keyCode == UP) {

                blnUp = false;

            } else if (keyCode == RIGHT) {

                blnRight = false;

            } else if (keyCode == DOWN) {

                blnDown = false;

            } else if (keyCode == LEFT) {

                blnLeft = false;

            }

        } else {

            // Handle default keybinds
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

    void enemyShoot(int circleIndex) {

        // Randomly decide when to shoot
        if (random(1) < 0.008) {

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
        if (random(1) < 0.003) {

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

            if (blnIsLaserActive[i]) {

                float distance = dist(fltLaserX[i], fltLaserY[i], intPlayerX + intPlayerSize / 2,
                        intPlayerY + intPlayerSize / 2);

                if (distance < intPlayerSize / 2) {

                    handlePlayerHit();
                    // Deactivate the regular laser when it hits the player
                    blnIsLaserActive[i] = false;
                    
                }
            }
        }

        // Check special laser collisions
        for (int i = 0; i < intNumSpecialLasers; i++) {

            if (blnIsSpecialLaserActive[i]) {

                float distance = dist(fltSpecialLaserX[i], fltSpecialLaserY[i], intPlayerX + intPlayerSize / 2,
                        intPlayerY + intPlayerSize / 2);

                if (distance < intPlayerSize / 2) {

                    handlePlayerHit();
                    // Deactivate the special laser when it hits the player
                    blnIsSpecialLaserActive[i] = false;

                }
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
    if (isMouseInsideStartButton() && intLevel == 1) {

        resetGame(); // Reset the game when the Start Game button is pressed
        intLevel = 4;

    }

    // Check if the mouse is pressed over the How to Play button
    if (isMouseInsideHowToPlayButton() && intLevel == 1) {

        intLevel = 3; // Set the level to show How to Play screen

    }

    // Check if the mouse is pressed over the back to menu button
    if (isMouseInsideMenuButton() && (intLevel == 2 || intLevel == 3)) {

        intLevel = 1; // Set the level to show Menu screen

    }

    // Check if the mouse is pressed over the options button
    if (isMouseInsideOptionsButton() && intLevel == 1) {

        intLevel = 2;

    }

    // Check if the mouse is pressed over the Game Over button
    if (intLevel == 8 && isMouseInsideGameOverButton()) {

        resetGame(); // Reset the game when the Game Over button is pressed
        intLevel = 1; // Set the level to return to the Menu screen

    }

    // Check if the mouse is pressed over the Win Game button
    if (intLevel == 7 && isMouseInsideWinGameButton()) {

        resetGame();
        intLevel = 1; // Set the level to return to the Menu screen

    }

    // Check if the mouse is pressed over the checkbox in the options menu
    if (intLevel == 2 && mouseX >= intChangeMovementButtonTopLeftX
            && mouseX <= intChangeMovementButtonBottomRightX && mouseY >= intChangeMovementButtonTopLeftY
            && mouseY <= intChangeMovementButtonBottomRightY) {

        blnChangeMovementButtonPressed = !blnChangeMovementButtonPressed;

    }

    // Check if the mouse is pressed over the checkbox in the options menu
    if (intLevel == 2 && mouseX >= intChangeShootButtonTopLeftX
            && mouseX <= intChangeShootButtonBottomRightX && mouseY >= intChangeShootButtonTopLeftY
            && mouseY <= intChangeShootButtonBottomRightY) {

        blnChangeShootButtonPressed = !blnChangeShootButtonPressed;

    }

    if (blnChangeShootButtonPressed && mouseButton == LEFT) {

        // Check if enough time has passed since the last shot
        long currentTime = millis();
        if (currentTime - lastClickTime >= intLaserCooldown) {

            // Shoot a player laser
            shootPlayerLaser();
            lastClickTime = currentTime;

        }
    }
}


    boolean isMouseInsideMenuButton() {
        //h Check if the moue coordinates are within the button boundaries
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

    boolean isMouseInsideWinGameButton() {
        return mouseX >= intWinGameButtonTopLeftX &&
                mouseX <= intWinGameButtonBottomRightX &&
                mouseY >= intWinGameButtonTopLeftY &&
                mouseY <= intWinGameButtonBottomRightY;
    }

    public void resetGame() {
        // Reset player variables
        intPlayerX = width / 2;
        intPlayerY = height - 80;
        intPlayerHealth = 5;
        blnIsPlayerHit = false; // Reset player hit state

        // Reset circle variables
        fltCircleY = (float) (height / 10); // Adjust the Y position of circles

        for (int i = 0; i < intNumCircles; i++) {

            fltCircleX[i] = (width / 2) - ((intNumCircles - 1) * fltCircleSpacing / 2) + (i * fltCircleSpacing);
            fltCircleSpeeds[i] = random(1.0f, 3.0f);
            blnIsCircleHit[i] = false;
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

        blnGameOver = false; // Reset game over state
    }

    boolean areAllCirclesHit() {

        for (int i = 0; i < intNumCircles; i++) {

            if (!blnIsCircleHit[i]) {
                return false; // If any circle is not hit, return false

            }
        }

        return true; // All circles are hit

    }

    // Helper function to check if the mouse is inside the Change Movement button
    boolean isMouseInsideChangeMovementButton() {
        return mouseX >= intChangeMovementButtonTopLeftX &&
                mouseX <= intChangeMovementButtonBottomRightX &&
                mouseY >= intChangeMovementButtonTopLeftY &&
                mouseY <= intChangeMovementButtonBottomRightY;
    }

    // Helper function to check if the mouse is inside the Change Shoot button
    boolean isMouseInsideChangeShootButton() {
        return mouseX >= intChangeShootButtonTopLeftX &&
                mouseX <= intChangeShootButtonBottomRightX &&
                mouseY >= intChangeShootButtonTopLeftY &&
                mouseY <= intChangeShootButtonBottomRightY;
    }

    public void drawChangeMovementButton() {

        stroke(255);
        strokeWeight(7);
        fill(0, buttonAlpha);
        rect(intChangeMovementButtonTopLeftX, intChangeMovementButtonTopLeftY,
                intChangeMovementButtonBottomRightX - intChangeMovementButtonTopLeftX,
                intChangeMovementButtonBottomRightY - intChangeMovementButtonTopLeftY);

        if (blnChangeMovementButtonPressed) {

            stroke(255);
            strokeWeight(7);
            noFill();

            float checkSize = 25;
            float checkX = intChangeMovementButtonTopLeftX + 12;
            float checkY = intChangeMovementButtonTopLeftY - 5
                    + (intChangeMovementButtonBottomRightY - intChangeMovementButtonTopLeftY) / 2;
            line(checkX, checkY, checkX + checkSize / 2, checkY + checkSize);
            line(checkX + checkSize / 2, checkY + checkSize, checkX + checkSize, checkY - checkSize / 2);

        }

        // Draw checkbox text on the right-hand side of the checkmark
        fill(255);
        textAlign(LEFT, CENTER);
        textSize(20);

        float textX = intChangeMovementButtonTopLeftX + 60;
        float textY = intChangeMovementButtonTopLeftY
                + (intChangeMovementButtonBottomRightY - intChangeMovementButtonTopLeftY) / 2;

        int boldness = 3;

        for (int i = 0; i < boldness; i++) {

            text("CHANGE MOVEMENT TO ARROW KEYS.", textX + i, textY);

        }

    }

    public void drawChangeShootButton() {

        stroke(255);
        strokeWeight(7);
        fill(0, buttonAlpha);
        rect(intChangeShootButtonTopLeftX, intChangeShootButtonTopLeftY,
                intChangeShootButtonBottomRightX - intChangeShootButtonTopLeftX,
                intChangeShootButtonBottomRightY - intChangeShootButtonTopLeftY);

        if (blnChangeShootButtonPressed) {

            stroke(255);
            strokeWeight(7);
            noFill();

            float checkSize = 25;
            float checkX = intChangeShootButtonTopLeftX + 12;
            float checkY = intChangeShootButtonTopLeftY - 5
                    + (intChangeShootButtonBottomRightY - intChangeShootButtonTopLeftY) / 2;
            line(checkX, checkY, checkX + checkSize / 2, checkY + checkSize);
            line(checkX + checkSize / 2, checkY + checkSize, checkX + checkSize, checkY - checkSize / 2);

        }

        // Draw checkbox text on the right-hand side of the checkmark
        fill(255);
        textAlign(LEFT, CENTER);
        textSize(20);

        float textX = intChangeShootButtonTopLeftX + 60;
        float textY = intChangeShootButtonTopLeftY
                + (intChangeShootButtonBottomRightY - intChangeShootButtonTopLeftY) / 2;

        int boldness = 3;

        for (int i = 0; i < boldness; i++) {

            text("CHANGE SHOOT TO LEFT MOUSE BUTTON.", textX + i, textY);
            
        }

    }
}