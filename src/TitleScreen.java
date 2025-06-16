import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TitleScreen extends JFrame {

    private JButton play;
    private JFrame frame;
    private JPanel Title;
    private boolean pressed = true;
    public boolean visible = true;
    int intPressed = 0;


    public TitleScreen() {
        play = new JButton("Play");
        play.setVisible(pressed);
        play.setBounds(200, 200, 70, 70);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        Title = new JPanel();
        Title.setVisible(visible);
        Title.setSize(400, 400);
        Title.add(play);

        add(Title);
        setVisible(visible);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CLicked button");
                pressed = false;
                play.setVisible(pressed);
                dispose();
            }
        });

    }
}


