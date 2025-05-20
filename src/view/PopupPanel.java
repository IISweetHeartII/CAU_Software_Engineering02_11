package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PopupPanel extends JPanel {
    private GameController controller;
    public PopupPanel(GameController controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(150, 150, 400, 300); // 위치 및 크기 조절 가능
        setBackground(new Color(0, 0, 0, 180)); // 반투명 검정 배경

        // 이미지 삽입
        ImageIcon popupIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/custom/custom_steady.png"
        )));
        Image scaledPopupIcon = popupIcon.getImage().getScaledInstance(
                popupIcon.getIconWidth()/3,
                popupIcon.getIconHeight()/3,
                Image.SCALE_SMOOTH
        );
        JLabel popupImage = new JLabel(popupIcon);
        popupImage.setBounds(36, 445, scaledPopupIcon.getWidth(null), scaledPopupIcon.getHeight(null)); // 이미지 크기와 맞춤
        add(popupImage);

        // Create buttons for each section
        JButton[] sectionButtons = new JButton[6];
        int sectionWidth = scaledPopupIcon.getWidth(null) / 6;

        for (int i = 0; i < 6; i++) {
            sectionButtons[i] = new JButton();
            sectionButtons[i].setBounds(36 + (i * sectionWidth), 445, sectionWidth, scaledPopupIcon.getHeight(null));
            sectionButtons[i].setOpaque(false);
            sectionButtons[i].setContentAreaFilled(false);
            sectionButtons[i].setBorderPainted(false);

            final int section = i + 1;
            sectionButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    ImageIcon hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                            "/data/ui/custom/custom_" + section + ".png"
                    )));
                    Image scaledHoverIcon = hoverIcon.getImage().getScaledInstance(
                            hoverIcon.getIconWidth()/3,
                            hoverIcon.getIconHeight()/3,
                            Image.SCALE_SMOOTH
                    );
                    popupImage.setIcon(new ImageIcon(scaledHoverIcon));
                }
            });

            // action listener for button click
            sectionButtons[i].addActionListener(e -> {
                switch (section) {
                    case 1 -> controller.handleManualThrow(1);
                    case 2 -> controller.handleManualThrow(2);
                    case 3 -> controller.handleManualThrow(3);
                    case 4 -> controller.handleManualThrow(4);
                    case 5 -> controller.handleManualThrow(5);
                    case 6 -> controller.handleManualThrow(-1);
                }
                // Perform action based on the button clicked
                System.out.println("Button " + section + " clicked");
                // Close the popup
                hidePopup();
            });
            add(sectionButtons[i]);
        }
    }

    public void showPopup() {
        setVisible(true);
    }

    public void hidePopup() {
        setVisible(false);
    }
}
