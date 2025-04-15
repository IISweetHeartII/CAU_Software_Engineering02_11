package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 플레이어 설정 대화상자입니다.
 */
public class PlayerSetupDialog extends JDialog {
    private final List<JTextField> nameFields = new ArrayList<>();
    private final List<JSpinner> horseCountSpinners = new ArrayList<>();
    private boolean confirmed = false;
    
    /**
     * 플레이어 설정 대화상자의 생성자입니다.
     * 
     * @param parent 부모 프레임
     */
    public PlayerSetupDialog(JFrame parent) {
        super(parent, "플레이어 설정", true);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 플레이어 수 선택
        JPanel playerCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel playerCountLabel = new JLabel("플레이어 수:");
        JSpinner playerCountSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
        playerCountPanel.add(playerCountLabel);
        playerCountPanel.add(playerCountSpinner);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(playerCountPanel, gbc);
        
        // 플레이어 설정 패널
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        
        // 플레이어 수 변경 이벤트
        playerCountSpinner.addChangeListener(e -> {
            int count = (Integer) playerCountSpinner.getValue();
            updatePlayerFields(playersPanel, count);
            pack();
        });
        
        gbc.gridy = 1;
        mainPanel.add(playersPanel, gbc);
        
        // 확인/취소 버튼
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");
        
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridy = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // 초기 플레이어 필드 생성
        updatePlayerFields(playersPanel, 2);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void updatePlayerFields(JPanel panel, int count) {
        panel.removeAll();
        nameFields.clear();
        horseCountSpinners.clear();
        
        for (int i = 0; i < count; i++) {
            JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nameLabel = new JLabel("플레이어 " + (i + 1) + " 이름:");
            JTextField nameField = new JTextField(10);
            nameField.setText("플레이어 " + (i + 1));
            
            JLabel horseLabel = new JLabel("말 개수:");
            JSpinner horseSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 4, 1));
            
            playerPanel.add(nameLabel);
            playerPanel.add(nameField);
            playerPanel.add(horseLabel);
            playerPanel.add(horseSpinner);
            
            nameFields.add(nameField);
            horseCountSpinners.add(horseSpinner);
            panel.add(playerPanel);
        }
    }
    
    /**
     * 플레이어 이름 목록을 반환합니다.
     * 
     * @return 플레이어 이름 목록
     */
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        if (confirmed) {
            for (JTextField field : nameFields) {
                names.add(field.getText());
            }
        }
        return names;
    }
    
    /**
     * 플레이어별 말 개수를 반환합니다.
     * 
     * @return 플레이어별 말 개수
     */
    public List<Integer> getHorseCounts() {
        List<Integer> counts = new ArrayList<>();
        if (confirmed) {
            for (JSpinner spinner : horseCountSpinners) {
                counts.add((Integer) spinner.getValue());
            }
        }
        return counts;
    }
    
    /**
     * 확인 버튼을 눌렀는지 여부를 반환합니다.
     * 
     * @return 확인 버튼을 눌렀으면 true, 아니면 false
     */
    public boolean isConfirmed() {
        return confirmed;
    }
} 