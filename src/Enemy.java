import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {

    private int x, y, dx;
    private boolean visible = true;
    private Image image;
    private final int SPEED = 10;

    private int frameIndex = 0;
    private int slashFrameIndex = 0;
    private boolean midSwing = false;

    private final Image[] runFrames = new Image[8];
    private final Image[] slashFrames = new Image[7];
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
        idleFrame = new ImageIcon("src/Idle.png").getImage();

        for (int i = 0; i < runFrames.length; i++) {
            runFrames[i] = new ImageIcon("src/Run.00" + (i == 0 ? "" : i) + ".png").getImage();
        }

        for (int i = 0; i < slashFrames.length; i++) {
            slashFrames[i] = new ImageIcon("src/Attack.00" + (i == 0 ? "" : i) + ".png").getImage();
        }
    }

    public void animate() {
        if (midSwing) {
            dx = 0;
            if (slashFrameIndex < slashFrames.length) {
                image = slashFrames[slashFrameIndex++];
            } else {
                midSwing = false;
                slashFrameIndex = 0;
                image = idleFrame;
            }
            return;
        }

        if (dx != 0) {
            image = runFrames[frameIndex++ % runFrames.length];
        } else {
            image = idleFrame;
        }
    }

    public void move(int playerX) {
        if (moveTimer-- <= 0) {
            int direction = random.nextInt(2); // 0 = toward player, 1 = away
            dx = (playerX < x ? -SPEED : SPEED) * (direction == 0 ? 1 : -1);
            moveTimer = 40 + random.nextInt(40);
        }

        if (!midSwing && attackTimer-- <= 0) {
            if (random.nextInt(2) == 1) {  // 50% chance to swing
                midSwing = true;
                slashFrameIndex = 0;
            }
            attackTimer = 60 + random.nextInt(60); // 1–2 seconds cooldown
        }

        x += dx;
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

