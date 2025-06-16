import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {

    private Ronin player;
    private ArrayList<Enemy> enemies;
    private Timer timer;
    private BackGround backGround;

    private boolean showStartScreen = true;
    private boolean showDeathScreen = false;

    private JButton playButton, optionsButton, aboutButton, replayButton;

    private int currentRound = 1;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(1319, 744));
        setLayout(null);
        setupStartScreen();

        timer = new Timer(75, this);
        timer.start();
    }

    private void setupStartScreen() {
        playButton = new JButton("Play");
        optionsButton = new JButton("Options");
        aboutButton = new JButton("About");

        playButton.setBounds(600, 300, 200, 50);
        optionsButton.setBounds(600, 370, 200, 50);
        aboutButton.setBounds(600, 440, 200, 50);

        Font btnFont = new Font("Arial", Font.BOLD, 20);
        playButton.setFont(btnFont);
        optionsButton.setFont(btnFont);
        aboutButton.setFont(btnFont);

        add(playButton);
        add(optionsButton);
        add(aboutButton);

        playButton.addActionListener(e -> startGame());
        optionsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Options menu coming soon!"));
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Game by Alex Liu"));
    }

    private void triggerDeathScreen() {
        showDeathScreen = true;
        replayButton = new JButton("Replay");
        replayButton.setBounds(560, 400, 200, 50);
        replayButton.setFont(new Font("Arial", Font.BOLD, 20));
        replayButton.addActionListener(e -> {
            showDeathScreen = false;
            currentRound = 1;
            startGame();
            remove(replayButton);
            revalidate();
            repaint();
        });
        add(replayButton);
        repaint();
    }

    private void startGame() {
        removeAll(); // Remove any buttons from previous screen

        player = new Ronin(30, 30);
        backGround = new BackGround();
        enemies = new ArrayList<>();

        showStartScreen = false;
        showDeathScreen = false;

        spawnEnemies(currentRound);
        initKeyBindings();

        revalidate();
        repaint();
    }

    private void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            int spacing = 100;
            int spawnX = 800 + i * spacing;
            enemies.add(new Enemy(spawnX, 594));
        }
    }

    private void initKeyBindings() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (player != null) player.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                if (player != null) player.keyReleased(e);
            }
        });
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showStartScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.BOLD, 64));
            g.drawString("Dingus", 480, 200);
            return;
        }

        if (showDeathScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 64));
            g.drawString("You Died", 500, 300);
            return;
        }

        if (backGround != null) {
            g.drawImage(backGround.getImage(), 0, 0, this);
        }

        if (player != null && player.isVisible()) {
            g.drawImage(player.getImage(), player.getXPos(), 550, this);
        }

        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                g.drawImage(enemy.getImage(), enemy.getXPos(), 550, this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!showStartScreen && !showDeathScreen && player != null) {
            player.move();
            player.animate();

            boolean allEnemiesDefeated = true;

            for (Enemy enemy : enemies) {
                if (enemy.isVisible()) {
                    allEnemiesDefeated = false;

                    enemy.move(player.getXPos());
                    enemy.animate();

                    // Enemy hits player
                    if (checkCollision(player, enemy) && enemy.getSwing()) {
                        System.out.println("Enemy hits player!");
                        player.setHealth();

                        if (player.getHealth() <= 0) {
                            player.setVisible(false);
                            triggerDeathScreen();
                        }
                    }

                    // Player hits enemy
                    if (checkCollision(player, enemy) && player.getSwing()) {
                        enemy.setHealth();
                        if (enemy.getHealth() < 0) {
                            enemy.setVisible(false);
                        }
                    }
                    if (checkCollision(player, enemy) && enemy.getSwing() && player.getParry()) {
                        player.parryReward();
                        enemy.Stunned();
                    }
                }
            }

            if (allEnemiesDefeated) {
                currentRound++;
                spawnEnemies(currentRound);
            }

            repaint();
        } else {
            repaint();
        }
    }

    private boolean checkCollision(Ronin player, Enemy enemy) {
        Rectangle playerBounds = new Rectangle(player.getXPos(), 594,
                player.getImage().getWidth(null), player.getImage().getHeight(null));

        Rectangle enemyBounds = new Rectangle(enemy.getXPos(), enemy.getYPos(),
                enemy.getImage().getWidth(null), enemy.getImage().getHeight(null));

        return playerBounds.intersects(enemyBounds);
    }
}
