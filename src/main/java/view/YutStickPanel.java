package view;

import javax.swing.*;
import java.awt.*;
import model.*;

/**
 * 윷가락 패널입니다.
 */
public class YutStickPanel extends JPanel {
    private static final int STICK_WIDTH = 20;
    private static final int STICK_HEIGHT = 100;
    private static final int MARGIN = 20;
    private static final Color STICK_COLOR = new Color(139, 69, 19); // 갈색
    
    private YutStick yutStick;
    
    /**
     * 윷가락 패널의 생성자입니다.
     */
    public YutStickPanel() {
        setPreferredSize(new Dimension(
            STICK_WIDTH * 4 + MARGIN * 5,
            STICK_HEIGHT + MARGIN * 2
        ));
        setBackground(Color.WHITE);
    }
    
    /**
     * 윷가락을 설정합니다.
     * 
     * @param yutStick 윷가락
     */
    public void setYutStick(YutStick yutStick) {
        this.yutStick = yutStick;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (yutStick == null) return;
        
        // 윷가락 그리기
        g.setColor(STICK_COLOR);
        for (int i = 0; i < 4; i++) {
            int x = MARGIN + i * (STICK_WIDTH + MARGIN);
            int y = MARGIN;
            
            if (yutStick.getSticks()[i]) {
                // 앞면 (평평한 면)
                g.fillRect(x, y, STICK_WIDTH, STICK_HEIGHT);
            } else {
                // 뒷면 (둥근 면)
                g.drawRect(x, y, STICK_WIDTH, STICK_HEIGHT);
            }
        }
    }
} 