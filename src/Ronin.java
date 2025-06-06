import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Ronin {

    private int x, y, dx, dy;
    private boolean visible;
    private Image image;
    private final int SPEED = 15;

    private int frameIndex = 0;
    private final Image[] runFrames = new Image[8];
    private Image idleFrame;

    // Slash animation
    private final Image[] slashFrames = new Image[7];
    private int slashFrameIndex = 0;
    private boolean midSwing = false;

    public Ronin(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        loadFrames();
        image = idleFrame;
    }

    private void loadFrames() {
        try {
            // Idle
            idleFrame = new ImageIcon("src/Idle.png").getImage();

            // Run
            runFrames[0] = new ImageIcon("src/Run.png").getImage();
            runFrames[1] = new ImageIcon("src/Run.002.png").getImage();
            runFrames[2] = new ImageIcon("src/Run.003.png").getImage();
            runFrames[3] = new ImageIcon("src/Run.004.png").getImage();
            runFrames[4] = new ImageIcon("src/Run.005.png").getImage();
            runFrames[5] = new ImageIcon("src/Run.006.png").getImage();
            runFrames[6] = new ImageIcon("src/Run.007.png").getImage();
            runFrames[7] = new ImageIcon("src/Run.008.png").getImage();

            // Slash
            slashFrames[0] = new ImageIcon("src/Attack.00.png").getImage();
            slashFrames[1] = new ImageIcon("src/Attack.001.png").getImage();
            slashFrames[2] = new ImageIcon("src/Attack.002.png").getImage();
            slashFrames[3] = new ImageIcon("src/Attack.003.png").getImage();
            slashFrames[4] = new ImageIcon("src/Attack.004.png").getImage();
            slashFrames[5] = new ImageIcon("src/Attack.004.png").getImage();
            slashFrames[6] = new ImageIcon("src/Attack.004.png").getImage();

        } catch (Exception e) {
            System.err.println("Error loading frames: " + e.getMessage());
        }
    }

    public void animate() {
        if (midSwing) {
            dx = 0;
            if (slashFrameIndex < slashFrames.length) {
                image = slashFrames[slashFrameIndex];
                slashFrameIndex++;
            } else {
                midSwing = false;
                slashFrameIndex = 0;
                image = idleFrame;
            }
            return;
        }

        if (dx != 0 || dy != 0) {
            image = runFrames[frameIndex];
            frameIndex = (frameIndex + 1) % runFrames.length;
        } else {
            image = idleFrame;
        }
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -SPEED;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = SPEED;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -SPEED;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = SPEED;
        }
        if (key == KeyEvent.VK_E && !midSwing) {
            midSwing = true;
            slashFrameIndex = 0;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}
