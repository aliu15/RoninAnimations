import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {

    private int x, y, dx;
    private boolean visible = true;
    private Image image;
    private final int SPEED = 10;
    private int health = 10;

    private int frameIndex = 0;
    private int slashFrameIndex = 0;
    private boolean midSwing = false;
    private int swingDirection = 0;  // NEW: remembers which direction to animate swing

    private final Image[] runFrames = new Image[8];
    private final Image[] slashFrames = new Image[4];
    private final Image[] slashFrames_Left = new Image[4];
    private Image idleFrame;

    private int moveTimer = 0;
    private int attackTimer = 0;

    private final Random random = new Random();

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        loadFrames();
        image = idleFrame;
    }

    private void loadFrames() {
        try {
            // Idle
            idleFrame = new ImageIcon("src/Idle_Commander.png").getImage();

            // Run
            runFrames[0] = new ImageIcon("src/Run.00.png").getImage();
            runFrames[1] = new ImageIcon("src/Run.01.png").getImage();
            runFrames[2] = new ImageIcon("src/Run.02.png").getImage();
            runFrames[3] = new ImageIcon("src/Run.03.png").getImage();
            runFrames[4] = new ImageIcon("src/Run.04.png").getImage();
            runFrames[5] = new ImageIcon("src/Run.05.png").getImage();
            runFrames[6] = new ImageIcon("src/Run.06.png").getImage();
            runFrames[7] = new ImageIcon("src/Run.07.png").getImage();

            // Slash_Right
            slashFrames[0] = new ImageIcon("src/Attack_2/Attack_1.png").getImage();
            slashFrames[1] = new ImageIcon("src/Attack_2/Attack_2.png").getImage();
            slashFrames[2] = new ImageIcon("src/Attack_2/Attack_3.png").getImage();
            slashFrames[3] = new ImageIcon("src/Attack_2/Attack_4.png").getImage();

            // Slash_Left
            slashFrames_Left[0] = new ImageIcon("src/Attack_3/Attack_1_Left.png").getImage();
            slashFrames_Left[1] = new ImageIcon("src/Attack_3/Attack_2_Left.png").getImage();
            slashFrames_Left[2] = new ImageIcon("src/Attack_3/Attack_3_Left.png").getImage();
            slashFrames_Left[3] = new ImageIcon("src/Attack_3/Attack_4_Left.png").getImage();

        } catch (Exception e) {
            System.err.println("Error loading frames: " + e.getMessage());
        }
    }

    public void animate() {
        if (midSwing) {
            // Play full slash animation while still moving
            if (swingDirection > 0) {
                if (slashFrameIndex < slashFrames.length) {
                    image = slashFrames[slashFrameIndex++];
                } else {
                    midSwing = false;
                    slashFrameIndex = 0;
                    image = idleFrame;
                }
            } else {
                if (slashFrameIndex < slashFrames_Left.length) {
                    image = slashFrames_Left[slashFrameIndex++];
                } else {
                    midSwing = false;
                    slashFrameIndex = 0;
                    image = idleFrame;
                }
            }
            return;
        }

        // Running animation
        if (dx != 0) {
            frameIndex = (frameIndex + 1) % runFrames.length;
            image = runFrames[frameIndex];
        } else {
            image = idleFrame;
        }
    }

    public void move(int playerX) {
        int distance = Math.abs(playerX - x);

        // Always face player
        dx = (playerX < x) ? -SPEED : SPEED;

        // Trigger attack if close and not cooling down
        if (!midSwing && distance <= 80 && attackTimer-- <= 0) {
            midSwing = true;
            slashFrameIndex = 0;
            swingDirection = dx;  // Remember direction for animation
            attackTimer = 60 + random.nextInt(60); // Cooldown
        }

        // Always move
        x += dx;
    }

    public boolean getSwing() {
        return midSwing;
    }

    public void setHealth() {
        health -= 1;
    }

    public int getHealth() {
        return health;
    }

    public Image getImage() {
        return image;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean isVisible) {
        visible = isVisible;
    }

    public void Stunned()  {
        dx = 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}
