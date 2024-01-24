import processing.core.PApplet;
import processing.core.PImage;

/**
 * Runs a fixed shooter game where you have to dedeat all the enemies to win and
 * save your planet.
 * 
 * @author Joshua and Ethan
 */
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
    int intPlayerHealth = 5;
    boolean blnIsPlayerHit = false;
    long playerHitStartTime = 0;
    int playerHitDuration = 25; // milliseconds

    // Boss variables
    float fltBossX;
    float fltBossY;
    float fltBossSpeed;
    float fltBossSize = 200;
    boolean blnIsBossHit;
    int intBossHitCount;
    int intBossHealth = 45;
    int intBossHitStartTime = 0;
    int intBossHitDuration = 450;

    private static long lastRegularShootTime = 0;
    private static long lastSpecialShootTime = 0;

    // Declare player image variable
    PImage stage1PlayerImage;
    PImage stage2PlayerImage;
    PImage stage3PlayerImage;
    PImage playerStage1HitImage;
    PImage playerStage2HitImage;
    PImage playerStage3HitImage;

    // Declare enemy image variables
    PImage bossName;
    PImage enemyImage;
    PImage enemyHitImage;
    PImage bossImage;
    PImage bossHitImage;
    PImage bossImageEnraged;
    PImage bossHitEnraged;

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
    PImage bossWarningEnraged;

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
    int newWaveDuration = 3000;

    // Define Screen Size
    public void settings() {
        size(600, 750);
    }

    /**
     * The setup method initializes the game environment and sets up necessary
     * components
     * This method loads images, sets background colors, and initializes various
     * game elements.
     */
    public void setup() {
        // Set the background color
        background(255);

        // Initialize the game state
        resetGame();

        // Load player images
        stage1PlayerImage = loadImage("intergalaticmogger/player/stage1sprite.png");
        stage2PlayerImage = loadImage("intergalaticmogger/player/stage2sprite.png");
        stage3PlayerImage = loadImage("intergalaticmogger/player/stage3sprite.png");
        playerStage1HitImage = loadImage("intergalaticmogger/player/stage1spritehit.png");
        playerStage2HitImage = loadImage("intergalaticmogger/player/stage2spritehit.png");
        playerStage3HitImage = loadImage("intergalaticmogger/player/stage3spritehit.png");
        health = loadImage("intergalaticmogger/player/health.png");
        health.resize(60, 60); // Resize health image

        // Load enemy images
        enemyImage = loadImage("intergalaticmogger/enemies/enemy.png");
        enemyHitImage = loadImage("intergalaticmogger/enemies/enemyhit.png");

        // Load boss images
        bossImage = loadImage("intergalaticmogger/enemies/boss.png");
        bossHitImage = loadImage("intergalaticmogger/enemies/bosshit.png");
        bossImageEnraged = loadImage("intergalaticmogger/enemies/leahunenraged.png");
        bossHitEnraged = loadImage("intergalaticmogger/enemies/leahunhitenraged.png");
        bossName = loadImage("intergalaticmogger/enemies/leahun.png");

        // Load background images
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
        bossWarningEnraged = loadImage("intergalaticmogger/levels/enragedbosstext.png");

        // Initialize circle speeds with random values
        for (int i = 0; i < intNumCircles; i++) {
            fltCircleX[i] = (width / 2) - ((intNumCircles - 1) * fltCircleSpacing / 2) + (i * fltCircleSpacing);
            fltCircleSpeeds[i] = random(1.0f, 3.0f); // Set random speeds for each circle
        }

        fltCircleY = (float) (height / 4.5); // Set initial Y position for circles

        // Initialize laser positions and status
        for (int i = 0; i < intNumLasers; i++) {
            blnIsLaserActive[i] = false;
        }

        // Initialize player lasers
        for (int i = 0; i < intNumPlayerLasers; i++) {
            blnIsPlayerLaserActive[i] = false;
        }
    }

    /**
     * draws the methods that make up the game
     */
    public void draw() {
        // Draw Levels based on the current game state
        if (intLevel == 1) {
            image(startMenu, 0, 0, width, height);
        } else if (intLevel == 2) {
            image(optionsMenu, 0, 0, width, height);
            drawChangeMovementButton();
            drawChangeShootButton();
        } else if (intLevel == 3) {
            image(howToMenu, 0, 0, width, height);
        } else if (intLevel == 4) {
            // Check if it's a new wave
            if (newWaveStartTime == 0) {
                newWaveStartTime = millis(); // Set the start time of the new wave
            }

            long elapsedTime = millis() - newWaveStartTime; // Calculate elapsed time
            long timeRemaining = newWaveDuration - elapsedTime; // Calculate time remaining

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
                    image(level1, 0, 0, width, height); // Display Level 1 image
                }
            }

        } else if (intLevel == 5) {
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

        } else if (intLevel == 6) {
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
                    image(level2, 0, 0, width, height); // Display Level 1 image
                }
            }

        } else if (intLevel == 7) {
            background(level2);
            newWaveStartTime = 0;
            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);
            }
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
            // Move and display special lasers
            moveSpecialLasers();
            displaySpecialLasers();
            // Enemy shooting logic
            for (int i = 0; i < intNumCircles; i++) {
                enemyShoot(i);
                specialEnemyShoot(i);
            }

        } else if (intLevel == 8) {
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
                    image(bossWarning, 0, 0, width, height);
                } else {
                    image(level3, 0, 0, width, height); // Display Level 1 image
                }
            }
        }

        /**
         * Handles the logic for levels 9 and 10, where the player faces the boss.
         * Updates player and boss positions, manages player health, and handles
         * shooting.
         */
        else if (intLevel == 9) {
            // Set the background to the level 3 image
            background(level3);

            // Reset the new wave start time
            newWaveStartTime = 0;

            // Display the player's health at the top right of the screen
            for (int i = 0; i < intPlayerHealth; i++) {
                image(health, width - health.width * (i + 1), 10);
            }

            // Handle player input
            handleInput();

            // Move the player
            movePlayer();

            // Move and display player lasers
            movePlayerLasers();
            displayPlayerLasers();

            // Check for collisions with the player laser
            checkCollisions();

            // Check for collisions with purple lasers
            checkPlayerCollisions();

            // Move and display regular lasers
            moveLasers();
            displayLasers();

            // Move and display special lasers
            moveSpecialLasers();
            displaySpecialLasers();

            // Move and display the boss
            moveBoss();
            displayBoss();
            displayBossHealthBar();

            // Handle boss shooting logic
            bossShoot();

            // Check for victory condition
            if (isBossDefeated()) {
                // Player wins, transition to the next level
                intLevel = 10;
                resetGame();
            }

            // Adjust laser speeds
            fltSpecialLaserSpeed = 4f;
            fltSpecialLaserSpeedY = 4f;
            fltLaserSpeed = 2.5f;
        }

        /**
         * Handles the logic for level 10, displaying the end win screen.
         */
        else if (intLevel == 10) {
            image(endWinScreen, 0, 0, width, height);
        }

        /**
         * Handles the logic for level 11, displaying the game over screen.
         */
        else if (intLevel == 11) {
            image(gameOverScreen, 0, 0, width, height);
        }

        // Check if any circle has reached the bottom of the screen
        for (int i = 0; i < intNumCircles; i++) {
            if (fltCircleY + fltCircleDiameter / 2 > height) {
                blnGameOver = true;
            }
        }

        // Check if the game is over and transition to the appropriate level
        if (blnGameOver) {
            intLevel = 11;
        }

        // Check if all circles are hit to advance to the next level
        if (areAllCirclesHit()) {
            if (intLevel < 10) {
                intLevel++;
                resetGame();
            } else {
                // If at the last level, transition to the game over screen
                intLevel = 11;
                resetGame();
            }
        }

        // Display player images based on health and level
        if (intPlayerHealth > 0 && intLevel == 5) {

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

        if (intPlayerHealth > 0 && intLevel == 7) {

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

        if (intPlayerHealth > 0 && intLevel == 9) {

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

        // Logic for the button hover
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

    /**
     * Handles the movement of circles, updating their positions and behavior.
     */
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

    /**
     * Displays the circles for the first row, considering hit status and flashing.
     */
    void displayCircles() {
        // Display circles for the first row
        for (int i = 0; i < intNumCircles; i++) {
            if (!blnIsCircleHit[i]) {
                if (intCircleHitCount[i] > 0 && millis() % 500 > 250) {
                    // Flash the enemy when hit
                    image(enemyHitImage, fltCircleX[i] - fltCircleDiameter / 2, fltCircleY - fltCircleDiameter / 2,
                            fltCircleDiameter, fltCircleDiameter);
                } else {
                    image(enemyImage, fltCircleX[i] - fltCircleDiameter / 2, fltCircleY - fltCircleDiameter / 2,
                            fltCircleDiameter, fltCircleDiameter);
                }
            }
        }
    }

    /**
     * Handles key presses for movement and shooting based on the game state.
     */
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

    /**
     * Handles key releases for movement based on the game state.
     */
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

    /**
     * Handles player input, updating movement flags based on key presses.
     */
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

    /**
     * Moves the player within the screen bounds based on input.
     */
    void movePlayer() {
        // Add boundary checks to keep the player within the screen bounds
        intPlayerX = constrain(intPlayerX, 0, width - intPlayerSize);

        // Restrict the player from going above the top third
        intPlayerY = constrain(intPlayerY, (int) (height / 1.5), height - intPlayerSize);
    }

    /**
     * Handles enemy shooting logic, randomly activating lasers at a specific circle
     * position.
     *
     * @param circleIndex The index of the circle triggering the shooting.
     */
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

    /**
     * Moves the active regular lasers upward, deactivating them when off-screen.
     */
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

    /**
     * Displays the regular lasers on the screen, considering hit status and circle
     * collisions.
     */
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

    /**
     * Moves the active special lasers, updating their positions based on sine wave
     * trajectory.
     */
    void moveSpecialLasers() {

        for (int i = 0; i < intNumSpecialLasers; i++) {

            if (blnIsSpecialLaserActive[i]) {

                // Update X-coordinate with a sine wave trajectory
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

    /**
     * Displays the special lasers on the screen, considering hit status and circle
     * collisions.
     */
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

    /**
     * Initiates a special laser shot from an enemy circle at the specified index.
     *
     * @param circleIndex The index of the circle triggering the special laser shot.
     */
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

    /**
     * Moves the active player lasers upward, deactivating them when off-screen.
     */
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

    /**
     * Displays the player lasers on the screen in blue color.
     */
    void displayPlayerLasers() {
        fill(0, 0, 255); // Blue color for player lasers
        noStroke();

        for (int i = 0; i < intNumPlayerLasers; i++) {

            if (blnIsPlayerLaserActive[i]) {

                rect(fltPlayerLaserX[i], fltPlayerLaserY[i], 8, 30); // Adjust size as needed
            }
        }
    }

    /**
     * Checks for collisions between player and enemy lasers, triggering actions if
     * a collision occurs.
     */
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

    /**
     * Handles the logic when the player is hit by an enemy laser, including health
     * decrement and game over checks.
     */
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

    /**
     * Helper function to check if a point is inside a circle.
     *
     * @param pointX       X-coordinate of the point.
     * @param pointY       Y-coordinate of the point.
     * @param fltCircleX   X-coordinate of the circle's center.
     * @param fltCircleY   Y-coordinate of the circle's center.
     * @param circleRadius Radius of the circle.
     * @return True if the point is inside the circle, false otherwise.
     */
    boolean circleHit(float pointX, float pointY, float fltCircleX, float fltCircleY, float circleRadius) {
        float d = dist(pointX, pointY, fltCircleX, fltCircleY);
        return d < circleRadius;
    }

    /**
     * Initiates a player laser shot by activating an available player laser.
     * Sets the laser's initial position to the player's position.
     */
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

    /**
     * Checks for collisions between player lasers and enemy circles or the boss,
     * triggering appropriate actions such as hit counts, deactivating lasers, and
     * boss health reduction.
     */
    void checkCollisions() {

        checkBossCollisions();

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

    /**
     * Checks for collisions between player lasers and the boss,
     * triggering actions such as hit count increment, health reduction, and
     * displaying hit images.
     */
    void checkBossCollisions() {
        // Check player laser collisions with the boss
        for (int i = 0; i < intNumPlayerLasers; i++) {
            if (blnIsPlayerLaserActive[i] && !blnIsBossHit &&
                    circleHit(fltPlayerLaserX[i], fltPlayerLaserY[i], fltBossX, fltBossY, fltBossSize / 2)) {
                intBossHitCount++;
                intBossHealth--; // Decrease boss health when hit by a player laser
                blnIsPlayerLaserActive[i] = false; // Deactivate player laser

                blnIsBossHit = true;

                // Check if the boss has been hit enough times
                if (isBossDefeated()) {

                    // Optionally, you can reset intBossHitCount here if needed
                    // intBossHitCount = 0;
                    fltBossX = -1000; // Move the boss off-screen
                }
            }
        }
    }

    /**
     * Displays the boss image on the screen, considering hit status and health.
     * Enraged boss image is displayed when the boss is below half health.
     */
    void displayBoss() {
        if (!blnIsBossHit) {
            // Check if boss is below half health
            if (intBossHealth <= 23) {
                // Display enraged boss image
                image(bossImageEnraged, fltBossX - fltBossSize / 2, fltBossY - fltBossSize / 2, fltBossSize,
                        fltBossSize);
            } else {
                // Display regular boss image
                image(bossImage, fltBossX - fltBossSize / 2, fltBossY - fltBossSize / 2, fltBossSize, fltBossSize);
            }
        } else {
            // Show the bossHitEnraged image for 500 milliseconds when enraged
            if (intBossHealth <= 23) {
                image(bossHitEnraged, fltBossX - fltBossSize / 2, fltBossY - fltBossSize / 2, fltBossSize, fltBossSize);
            } else {
                // Show the bossHitImage for 500 milliseconds for the regular boss
                image(bossHitImage, fltBossX - fltBossSize / 2, fltBossY - fltBossSize / 2, fltBossSize, fltBossSize);
            }

            // Check if the display duration has elapsed
            if (millis() - intBossHitStartTime > intBossHitDuration) {
                blnIsBossHit = false; // Reset boss hit state
                intBossHitStartTime = millis(); // Reset hit start time for the next hit
            }
        }
    }

    /**
     * Moves the boss side to side with consistent speed, reversing direction when
     * reaching screen edges.
     * Moves the boss down after reaching the screen edges and resets hit count when
     * reaching the bottom.
     */
    void moveBoss() {
        // Move boss side to side with consistent speed
        boolean edgeReached = false; // Variable to track if the boss has reached the edge

        fltBossX += fltBossSpeed;

        // Reverse direction if the boss reaches the screen edges
        if (fltBossX > width - fltBossSize / 2 || fltBossX < fltBossSize / 2) {
            fltBossSpeed *= -1;
            // Ensure the boss stays within the screen bounds
            fltBossX = constrain(fltBossX, fltBossSize / 2, width - fltBossSize / 2);

            // Mark that the boss has reached the edge
            edgeReached = true;
        }

        // Move the boss down after reaching the screen edges
        if (edgeReached) {
            fltBossY += 10; // Adjust the vertical movement as needed

            // Reset hit count for the boss that reaches the bottom
            if (fltBossY + fltBossSize / 2 > height && !blnIsBossHit) {
                intBossHitCount = 0;
            }

            edgeReached = false; // Reset the edgeReached variable
        }
    }

    /**
     * Initiates a boss attack by shooting regular and special lasers at intervals,
     * considering the boss's hit status and elapsed time since the last shots.
     */
    void bossShoot() {
        // Check if the boss is not hit before shooting
        if (!blnIsBossHit) {
            long shootInterval = 1800;

            // Check if enough time has passed since the last regular shot
            if (millis() - lastRegularShootTime > shootInterval) {
                // Update the last regular shooting time
                lastRegularShootTime = millis();

                // Activate regular bullets
                for (int j = 0; j < intNumLasers; j += 3) {
                    if (!blnIsLaserActive[j]) {
                        // Center laser
                        fltLaserX[j] = fltBossX;
                        fltLaserY[j] = fltBossY;
                        blnIsLaserActive[j] = true;

                        // Left laser
                        fltLaserX[j + 1] = fltBossX - 110; // Adjust the offset as needed
                        fltLaserY[j + 1] = fltBossY;
                        blnIsLaserActive[j + 1] = true;

                        // Right laser
                        fltLaserX[j + 2] = fltBossX + 110; // Adjust the offset as needed
                        fltLaserY[j + 2] = fltBossY;
                        blnIsLaserActive[j + 2] = true;

                        // Exit the loop after activating three lasers
                        break;
                    }
                }
            }

            // Check if enough time has passed since the last special shot
            if (millis() - lastSpecialShootTime > shootInterval) {
                // Update the last special shooting time
                lastSpecialShootTime = millis();

                // Activate special bullets
                for (int j = 0; j < intNumSpecialLasers; j += 3) {
                    if (!blnIsSpecialLaserActive[j]) {
                        // Center special laser
                        fltSpecialLaserX[j] = fltBossX;
                        fltSpecialLaserY[j] = fltBossY;
                        blnIsSpecialLaserActive[j] = true;

                        // Left special laser
                        fltSpecialLaserX[j + 1] = fltBossX - 100; // Adjust the offset as needed
                        fltSpecialLaserY[j + 1] = fltBossY;
                        blnIsSpecialLaserActive[j + 1] = true;

                        // Right special laser
                        fltSpecialLaserX[j + 2] = fltBossX + 100; // Adjust the offset as needed
                        fltSpecialLaserY[j + 2] = fltBossY;
                        blnIsSpecialLaserActive[j + 2] = true;

                        // Exit the loop after activating three special lasers
                        break;
                    }
                }
            }
        }
    }

    /**
     * Checks if the boss has been defeated based on its remaining health.
     *
     * @return True if the boss's health is zero or less, indicating defeat;
     *         otherwise, false.
     */
    boolean isBossDefeated() {
        return intBossHealth <= 0;
    }

    /**
     * Displays the boss's health bar above its position, indicating the remaining
     * health.
     */
    void displayBossHealthBar() {
        // Set the color for the health bar background (white)
        fill(255);

        // Draw the white background for the health bar
        rect(fltBossX - fltBossSize / 2, fltBossY - fltBossSize / 2 - 20, fltBossSize, 15);

        // Set the color for the health bar (red)
        fill(255, 0, 0);

        // Calculate the width of the health bar based on the boss's health
        float healthBarWidth = map(intBossHealth, 0, 45, 0, fltBossSize);

        // Draw the health bar above the boss
        rect(fltBossX - fltBossSize / 2, fltBossY - fltBossSize / 2 - 20, healthBarWidth, 15);

        // Calculate the new size for the boss image
        float bossImageWidth = fltBossSize + 140; // Adjust as needed

        // Draw the resized boss image on top of the health bar
        image(bossName, fltBossX - bossImageWidth / 2, fltBossY - fltBossSize / 2 - 175, bossImageWidth,
                bossImageWidth * (bossName.height / bossName.width));
    }

    /**
     * Handles mouse press events, responding to button clicks such as starting the
     * game, accessing options, or returning to the menu.
     */
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
        if (intLevel == 11 && isMouseInsideGameOverButton()) {

            resetGame(); // Reset the game when the Game Over button is pressed
            intLevel = 1; // Set the level to return to the Menu screen

        }

        // Check if the mouse is pressed over the Win Game button
        if (intLevel == 10 && isMouseInsideWinGameButton()) {

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

    /**
     * Checks if all circles have been hit.
     *
     * @return True if all circles are hit; otherwise, false.
     */
    boolean areAllCirclesHit() {
        for (int i = 0; i < intNumCircles; i++) {
            if (!blnIsCircleHit[i]) {
                return false; // If any circle is not hit, return false
            }
        }
        return true; // All circles are hit
    }

    /**
     * Checks if the mouse coordinates are inside the Menu button boundaries.
     *
     * @return True if the mouse is inside the Menu button; otherwise, false.
     */
    boolean isMouseInsideMenuButton() {
        // Check if the mouse coordinates are within the button boundaries
        return mouseX >= intMenuButtonTopLeftX &&
                mouseX <= intMenuButtonBottomRightX &&
                mouseY >= intMenuButtonTopLeftY &&
                mouseY <= intMenuButtonBottomRightY;
    }

    /**
     * Checks if the mouse coordinates are inside the Start button boundaries.
     *
     * @return True if the mouse is inside the Start button; otherwise, false.
     */
    boolean isMouseInsideStartButton() {
        // Check if the mouse coordinates are within the button boundaries
        return mouseX >= intStartTopLeftX &&
                mouseX <= intStartBottomRightX &&
                mouseY >= intStartTopLeftY &&
                mouseY <= intStartBottomRightY;
    }

    /**
     * Checks if the mouse coordinates are inside the How to Play button boundaries.
     *
     * @return True if the mouse is inside the How to Play button; otherwise, false.
     */
    boolean isMouseInsideHowToPlayButton() {
        // Check if the mouse coordinates are within the How to Play button boundaries
        return mouseX >= intHTPButtonTopLeftX &&
                mouseX <= intHTPButtonBottomRightX &&
                mouseY >= intHTPButtonTopLeftY &&
                mouseY <= intHTPButtonBottomRightY;
    }

    /**
     * Checks if the mouse coordinates are inside the Options button boundaries.
     *
     * @return True if the mouse is inside the Options button; otherwise, false.
     */
    boolean isMouseInsideOptionsButton() {
        // check if the mouse coordinates are within the options button
        return mouseX >= intOptionsButtonTopLeftX &&
                mouseX <= intOptionsButtonBottomRightX &&
                mouseY >= intOptionsButtonTopLeftY &&
                mouseY <= intOptionsButtonBottomRightY;
    }

    /**
     * Checks if the mouse coordinates are inside the Game Over button boundaries.
     *
     * @return True if the mouse is inside the Game Over button; otherwise, false.
     */
    boolean isMouseInsideGameOverButton() {
        return mouseX >= intGameOverButtonTopLeftX &&
                mouseX <= intGameOverButtonBottomRightX &&
                mouseY >= intGameOverButtonTopLeftY &&
                mouseY <= intGameOverButtonBottomRightY;
    }

    /**
     * Checks if the mouse coordinates are inside the Win Game button boundaries.
     *
     * @return True if the mouse is inside the Win Game button; otherwise, false.
     */
    boolean isMouseInsideWinGameButton() {
        return mouseX >= intWinGameButtonTopLeftX &&
                mouseX <= intWinGameButtonBottomRightX &&
                mouseY >= intWinGameButtonTopLeftY &&
                mouseY <= intWinGameButtonBottomRightY;
    }

    /**
     * Checks if the mouse is inside the Change Movement button.
     *
     * @return True if the mouse is inside the Change Movement button; otherwise,
     *         false.
     */
    boolean isMouseInsideChangeMovementButton() {
        return mouseX >= intChangeMovementButtonTopLeftX &&
                mouseX <= intChangeMovementButtonBottomRightX &&
                mouseY >= intChangeMovementButtonTopLeftY &&
                mouseY <= intChangeMovementButtonBottomRightY;
    }

    /**
     * Checks if the mouse is inside the Change Shoot button.
     *
     * @return True if the mouse is inside the Change Shoot button; otherwise,
     *         false.
     */
    boolean isMouseInsideChangeShootButton() {
        return mouseX >= intChangeShootButtonTopLeftX &&
                mouseX <= intChangeShootButtonBottomRightX &&
                mouseY >= intChangeShootButtonTopLeftY &&
                mouseY <= intChangeShootButtonBottomRightY;
    }

    /**
     * Resets the game state to its initial configuration.
     */
    public void resetGame() {
        // Reset player variables
        intPlayerX = width / 2;
        intPlayerY = height - 80;
        intPlayerHealth = 5;
        blnIsPlayerHit = false; // Reset player hit state

        // Reset boss variables
        fltBossX = width / 2;
        fltBossY = 160; // Adjust the Y position of the boss
        fltBossSpeed = 2.0f;
        blnIsBossHit = false;
        intBossHitCount = 0;
        intBossHealth = 45;

        // Reset Bullet Speeds
        fltLaserSpeed = 5;
        fltPlayerLaserSpeed = 7;
        fltSpecialLaserSpeed = 3f;
        fltSpecialLaserSpeedY = 1.5f;

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

    /**
     * Draws the Change Movement button with a checkbox indicating the current
     * setting.
     */
    public void drawChangeMovementButton() {
        // Draw button background
        stroke(255);
        strokeWeight(7);
        fill(0, buttonAlpha);
        rect(intChangeMovementButtonTopLeftX, intChangeMovementButtonTopLeftY,
                intChangeMovementButtonBottomRightX - intChangeMovementButtonTopLeftX,
                intChangeMovementButtonBottomRightY - intChangeMovementButtonTopLeftY);

        if (blnChangeMovementButtonPressed) {
            // Draw checkmark inside the button
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

    /**
     * Draws the Change Shoot button with a checkbox indicating the current setting.
     */
    public void drawChangeShootButton() {
        // Draw button background
        stroke(255);
        strokeWeight(7);
        fill(0, buttonAlpha);
        rect(intChangeShootButtonTopLeftX, intChangeShootButtonTopLeftY,
                intChangeShootButtonBottomRightX - intChangeShootButtonTopLeftX,
                intChangeShootButtonBottomRightY - intChangeShootButtonTopLeftY);

        if (blnChangeShootButtonPressed) {
            // Draw checkmark inside the button
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
