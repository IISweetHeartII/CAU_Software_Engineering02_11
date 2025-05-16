package view;

import javax.swing.*;
import java.awt.*;

/**
 * BackgroundPanel은 JPanel을 상속받아 배경 이미지를 설정할 수 있는 패널입니다.
 * 이 패널은 paintComponent 메서드를 오버라이드하여 배경 이미지를 그립니다.
 */
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
