package view;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setLayout(null); // 절대 위치 지정
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 이미지를 패널 크기에 맞게 그림
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
