import javax.swing.*;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;


public class BackGround extends JFrame{

    private boolean visible;
    private Image image;

    public BackGround(){
        visible = true;
        loadImage();
    }
    private void loadImage() {
        ImageIcon ii = new ImageIcon("src/Bamboo.png"); // Replace with your image path
        image = ii.getImage();
    }
    public Image getImage(){
        return image;
    }
}
