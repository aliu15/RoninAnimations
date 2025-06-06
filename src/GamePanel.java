import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    private Ronin player;
    private Enemy enemy;  // <- Added enemy
    private Timer timer;
    private BackGround backGround;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(1400, 800));

        player = new Ronin(30, 30);
        enemy = new Enemy(800, 594);  // <- Spawn enemy to the right of player
        backGround = new BackGround();

        initKeyBindings();

        timer = new Timer(75, this); // updates ~13 times/sec
        timer.start();
    }

    private void initKeyBindings() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backGround != null) {
            g.drawImage(backGround.getImage(), 40, 40, this);
        }

        if (player != null) {
            g.drawImage(player.getImage(), player.getXPos(), 594, this);
        }

        if (enemy != null && enemy.isVisible()) {
            g.drawImage(enemy.getImage(), enemy.getXPos(), enemy.getYPos(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move();
        player.animate();

        enemy.move(player.getXPos());       // <- animate enemy
        enemy.animate();    //

        repaint();
    }
}
