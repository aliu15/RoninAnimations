import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        JFrame frame = new JFrame("Ronin Game");
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}