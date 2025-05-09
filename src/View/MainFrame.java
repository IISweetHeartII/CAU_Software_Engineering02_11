package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

public class MainFrame extends JFrame {
    private BoardPanel boardPanel;
    private String selectedBoardImage = "board_four.png"; // 기본값
    
    public MainFrame() {
        setTitle("윷놀이 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // 메인 레이아웃 설정
        setLayout(new BorderLayout());
        
        // 보드 패널 (중앙) 초기화
        initializeBoardPanel();
        add(boardPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // 노드 클릭 리스너 설정
        boardPanel.setNodeClickListener(new BoardPanel.NodeClickListener() {
            @Override
            public void onNodeClicked(String nodeId) {
                System.out.println("노드 클릭 이벤트: " + nodeId);
                
                // 선택된 말이 있으면 해당 노드로 이동
                if (selectedPiece != null) {
                    boardPanel.movePiece(selectedPiece, nodeId);
                    selectedPiece = null; // 이동 후 선택 해제
                }
            }
        });
    }
    
    // 선택된 말 ID를 저장
    private String selectedPiece = null;
    
    // 말에 클릭 이벤트 추가
    public void addPieceClickEvent(String pieceId) {
        JButton pieceButton = boardPanel.getPieceButton(pieceId);
        if (pieceButton != null) {
            pieceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedPiece = pieceId;
                    System.out.println("말 선택: " + pieceId);
                    
                    // 이동 가능한 노드 하이라이트 (예시로 모든 노드를 하이라이트)
                    boardPanel.highlightAllNodes();
                    // 노드 버튼을 보이게 설정
                    boardPanel.setNodeButtonsVisible(true);
                }
            });
        }
    }
    
    // 말을 추가하고 클릭 이벤트 설정하는 메서드
    public void addPieceWithClickEvent(String pieceId, String imagePath, String initialNodeId) {
        // 말 추가
        boardPanel.addPiece(pieceId, imagePath, initialNodeId);
        
        // 말 클릭 리스너 설정
        JButton pieceButton = boardPanel.getPieceButton(pieceId);
        if (pieceButton != null) {
            pieceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedPiece = pieceId;
                    System.out.println("말 선택: " + pieceId);
                    
                    // 이동 가능한 노드 하이라이트 (예시로 모든 노드를 하이라이트)
                    boardPanel.highlightAllNodes();
                }
            });
        }
    }
    
    private void initializeBoardPanel() {
        boardPanel = new BoardPanel(selectedBoardImage);
    }
    
    // BoardPanel 접근자 메서드
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
