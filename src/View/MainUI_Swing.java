//[메인 프레임, 윷 던지기 버튼 처리]윷 던지기 버튼 UI 초안 + 이벤트 처리 기능 넣음
//GameView 구현중.

package View;

import Controller.GameController;
import Model.Position;
import Model.YutResult;
import View.BoardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

//JFrame을 상속받아서 윈도우 창 만듦.
public class MainUI_Swing extends JFrame implements GameView {
    /// responsibilities ///
    /// 1. get game state from controller by parameter
    /// 2. show game state to user
    /// 3. UI update
    ///
    /// This class MUST NOT use Controller class !!!
    /// It should only use Model class and GameView interface.

    private GameController controller;
    private BoardPanel boardPanel;
    private JPanel controlPanel;
    private JPanel statusPanel;
    private JLabel playerInfoLabel;
    private JLabel resultInfoLabel;
    private String currentBoardType = "board_four.png"; // 기본값
    private ArrayDeque<Position> currentPosableMoves;
    private Map<String, String> nodeIdMapping; // 모델의 Position을 BoardPanel의 노드 ID로 매핑
    
    public MainUI_Swing(GameController controller) {
        this.controller = controller;
        initUI();
        initNodeMapping();
    }

    // 모델의 Position과 보드 패널의 노드 ID 간 매핑 초기화
    private void initNodeMapping() {
        nodeIdMapping = new HashMap<>();
        // 예시 매핑 (실제 모델의 Position enum 값에 따라 조정 필요)
        // nodeIdMapping.put("MODEL_START", "start");
        // nodeIdMapping.put("MODEL_NODE1", "n1");
        // 등등...
    }

    //화면 구성 (UI 초기화)
    private void initUI() {
        setTitle("윷놀이 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // 컨트롤 패널 (상단)
        createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // 보드 패널 (중앙)
        boardPanel = new BoardPanel(currentBoardType);
        add(boardPanel, BorderLayout.CENTER);
        
        // 상태 패널 (하단)
        createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        
        // 보드 타입 선택
        JLabel boardLabel = new JLabel("보드 선택: ");
        controlPanel.add(boardLabel);
        
        ButtonGroup boardGroup = new ButtonGroup();
        JRadioButton fourBoard = new JRadioButton("4인용", true);
        JRadioButton fiveBoard = new JRadioButton("5인용");
        JRadioButton sixBoard = new JRadioButton("6인용");
        
        boardGroup.add(fourBoard);
        boardGroup.add(fiveBoard);
        boardGroup.add(sixBoard);
        
        controlPanel.add(fourBoard);
        controlPanel.add(fiveBoard);
        controlPanel.add(sixBoard);
        
        // 보드 변경 이벤트 처리
        fourBoard.addActionListener(e -> changeBoardType("board_four.png"));
        fiveBoard.addActionListener(e -> changeBoardType("board_five.png"));
        sixBoard.addActionListener(e -> changeBoardType("board_six.png"));
        
        // 윷 던지기 버튼
        JButton throwButton = new JButton("윷 던지기");
        throwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 컨트롤러에게 윷 던지기 요청 (실제 구현에서는 컨트롤러가 호출)
                // controller.throwYut();
            }
        });
        
        controlPanel.add(throwButton);
    }
    
    private void createStatusPanel() {
        statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(2, 1));
        
        playerInfoLabel = new JLabel("현재 차례: 플레이어 1");
        resultInfoLabel = new JLabel("윷 결과: ");
        
        statusPanel.add(playerInfoLabel);
        statusPanel.add(resultInfoLabel);
    }
    
    private void changeBoardType(String boardType) {
        currentBoardType = boardType;
        
        // 보드 패널 교체
        remove(boardPanel);
        boardPanel = new BoardPanel(currentBoardType);
        add(boardPanel, BorderLayout.CENTER);
        
        // 화면 갱신
        revalidate();
        repaint();
    }

    @Override
    public void showYutResult(YutResult yutResult) {
        resultInfoLabel.setText("윷 결과: " + yutResult.toString());
    }

    @Override
    public void updateCurrentPlayer(String playerID) {
        playerInfoLabel.setText("현재 차례: " + playerID);
    }

    @Override
    public void showPosableMoves(ArrayDeque<Position> posableMoves) {
        this.currentPosableMoves = posableMoves;
        
        // 이동 가능한 위치를 노드 ID로 변환하여 하이라이트
        String[] nodeIds = new String[posableMoves.size()];
        int i = 0;
        for (Position pos : posableMoves) {
            String nodeId = convertPositionToNodeId(pos);
            if (nodeId != null) {
                nodeIds[i++] = nodeId;
            }
        }
        
        boardPanel.highlightNodes(nodeIds);
    }

    // Position을 노드 ID로 변환하는 메서드
    private String convertPositionToNodeId(Position position) {
        return nodeIdMapping.get(position.toString());
    }

    @Override
    public Position getUserSelectedPosition(ArrayDeque<Position> posableMoves) {
        // 실제 구현에서는 보드 패널의 노드 클릭 이벤트를 처리하여 선택된 위치 반환
        // 여기서는 간단히 첫 번째 이동 가능한 위치를 반환 (테스트용)
        if (posableMoves != null && !posableMoves.isEmpty()) {
            return posableMoves.getFirst();
        }
        return null;
    }

    @Override
    public void BoardRendering() {
        // 모든 말의 위치를 업데이트
        // 실제 구현에서는 모델에서 각 말의 정보를 가져와서 boardPanel.movePiece() 호출
        
        // 화면 갱신
        revalidate();
        repaint();
    }

    @Override
    public void showGameEnd(String playerID) {
        JOptionPane.showMessageDialog(this, 
                playerID + "님이 게임에서 승리했습니다!", 
                "게임 종료", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // BoardPanel에 새 말을 추가하는 메서드 (컨트롤러에서 호출)
    public void addPieceToBoard(String pieceId, String imagePath, Position position) {
        String nodeId = convertPositionToNodeId(position);
        if (nodeId != null) {
            boardPanel.addPiece(pieceId, imagePath, nodeId);
        }
    }
    
    // BoardPanel에서 말을 이동시키는 메서드 (컨트롤러에서 호출)
    public void movePieceOnBoard(String pieceId, Position position) {
        String nodeId = convertPositionToNodeId(position);
        if (nodeId != null) {
            boardPanel.movePiece(pieceId, nodeId);
        }
    }
}