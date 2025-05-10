//[메인 프레임, 윷 던지기 버튼 처리]윷 던지기 버튼 UI 초안 + 이벤트 처리 기능 넣음

package View;

import Controller.GameController;
import Model.GameModel;
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
public class MainUI_Swing extends JFrame {
    /// responsibilities ///
    /// 1. get game state from controller by parameter
    /// 2. show game state to user
    /// 3. UI update
    ///
    /// This class MUST NOT use Controller class !!!
    /// It should only use Model class and GameView interface.

    private GameController controller;
    private GameModel gameModel;
    private BoardPanel boardPanel;
    private JPanel controlPanel;
    private JPanel statusPanel;
    private JLabel playerInfoLabel;
    private JLabel resultInfoLabel;
    private String currentBoardType = "board_four.png"; // 기본값
    private ArrayDeque<Position> currentPosableMoves;
    private Map<String, String> nodeIdMapping; // 모델의 Position을 BoardPanel의 노드 ID로 매핑
    
    public MainUI_Swing(GameController controller, GameModel gameModel) {
        this.controller = controller;
        this.gameModel = gameModel;
        initUI();
        //this.nodeIdMapping = initNodeMapping(controller.getBoardFigure());

    }

    // 노드 매핑 초기화
    public static Map<String, String> initNodeMapping(int boardFigure) {
        Map<String, String> mapping = new HashMap<>();

        switch (boardFigure) {
            case 4 -> {
                for (int i = 1; i <= 20; i++) mapping.put("P" + i, "p" + i);
                for (int i = 1; i <= 8; i++) mapping.put("E" + i, "e" + i);
                mapping.put("START", "start");
                mapping.put("END", "end");
                mapping.put("C", "c");
            }
            case 5 -> {
                for (int i = 1; i <= 25; i++) mapping.put("P" + i, "p" + i);
                for (int i = 1; i <= 10; i++) mapping.put("E" + i, "e" + i);
                mapping.put("START", "start");
                mapping.put("END", "end");
                mapping.put("C", "c");
            }
            case 6 -> {
                for (int i = 1; i <= 30; i++) mapping.put("P" + i, "p" + i);
                for (int i = 1; i <= 12; i++) mapping.put("E" + i, "e" + i);
                mapping.put("START", "start");
                mapping.put("END", "end");
                mapping.put("C", "c");
            }
        }

        return mapping;
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

        // 보드 패널에 노드 클릭 리스너 설정
        boardPanel.setNodeClickListener(nodeId -> {
            Position clickedPosition = convertNodeIdToPosition(nodeId);
            if (clickedPosition != null) {
                controller.handleMoveRequest(clickedPosition);
            } else {
                JOptionPane.showMessageDialog(this, "이동할 수 없는 위치입니다.", "오류", JOptionPane.WARNING_MESSAGE);
            }
        });




        // 상태 패널 (하단)
        createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
        
        // 게임 종료 리스너 설정
        boardPanel.setGameQuitListener(new BoardPanel.GameQuitListener() {
            @Override
            public void onGameQuit() {
                int option = JOptionPane.showConfirmDialog(MainUI_Swing.this, 
                        "정말 게임을 종료하시겠습니까?", 
                        "게임 종료", 
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    controller.endGame();
                    System.exit(0);
                }
            }
        });
        
        // 게임 재시작 리스너 설정
        boardPanel.setGameRestartListener(new BoardPanel.GameRestartListener() {
            @Override
            public void onGameRestart() {
                // 컨트롤러를 통해 게임 재시작 로직 실행
                // 여기서는 컨트롤러가 이미 설정되어 있다고 가정
                if (controller != null) {
                    try {
                        // restart 메서드가 존재하면 호출
                        controller.getClass().getMethod("restart").invoke(controller);
                    } catch (Exception ex) {
                        System.err.println("재시작 기능을 사용할 수 없습니다: " + ex.getMessage());
                    }
                }
            }
        });
        
        // 커스텀 선택 리스너 설정
        boardPanel.setCustomChoiceListener(new BoardPanel.CustomChoiceListener() {
            @Override
            public void onCustomChoice() {
                showCustomChoiceDialog();
            }
        });
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Position convertNodeIdToPosition(String nodeId) {
        for (Map.Entry<String, String> entry : nodeIdMapping.entrySet()) {
            if (entry.getValue().equals(nodeId)) {
                return new Position(entry.getKey()); // Position 클래스에 맞게 생성자 사용
            }
        }
        return null;
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
        
        // 게임 종료 리스너 설정
        boardPanel.setGameQuitListener(new BoardPanel.GameQuitListener() {
            @Override
            public void onGameQuit() {
                int option = JOptionPane.showConfirmDialog(MainUI_Swing.this, 
                        "정말 게임을 종료하시겠습니까?", 
                        "게임 종료", 
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        // 게임 재시작 리스너 설정
        boardPanel.setGameRestartListener(new BoardPanel.GameRestartListener() {
            @Override
            public void onGameRestart() {
                // 컨트롤러를 통해 게임 재시작 로직 실행
                if (controller != null) {
                    try {
                        // restart 메서드가 존재하면 호출
                        controller.getClass().getMethod("restart").invoke(controller);
                    } catch (Exception ex) {
                        System.err.println("재시작 기능을 사용할 수 없습니다: " + ex.getMessage());
                    }
                }
            }
        });
        
        // 커스텀 선택 리스너 설정
        boardPanel.setCustomChoiceListener(new BoardPanel.CustomChoiceListener() {
            @Override
            public void onCustomChoice() {
                showCustomChoiceDialog();
            }
        });
        
        // 화면 갱신
        revalidate();
        repaint();
    }

    @Override
    public void showYutResult(YutResult yutResult) {

    }

    @Override
    public void updateCurrentPlayer(String playerID) {
        playerInfoLabel.setText("현재 차례: " + playerID);
        
        // BoardPanel의 턴 이미지도 함께 업데이트
        if (playerID != null && playerID.startsWith("player")) {
            try {
                int playerNum = Integer.parseInt(playerID.substring(6));
                boardPanel.updateTurnImage(playerNum);
            } catch (NumberFormatException e) {
                System.err.println("잘못된 플레이어 ID 형식: " + playerID);
            }
        }
    }

/*    public void showPosableMoves(ArrayDeque<Position> posableMoves) {
        this.currentPosableMoves = posableMoves;
        
        // 이동 가능한 위치를 노드 ID로 변환하여 하이라이트
        String[] nodeIds = new String[posableMoves.size()];
        int i = 0;
        for (Position pos : posableMoves) {
            String nodeId = convertPositionToNodeId(pos);
            if (nodeId != null) {
                nodeIds[i++] = nodeId;
            }
        }*/
        
 /*       boardPanel.highlightNodes(nodeIds);
    }
*/
    // Position을 노드 ID로 변환하는 메서드
    private String convertPositionToNodeId(Position position) {
        return nodeIdMapping.get(position.toString());
    }


    //테스트용 코드//
    /*@Override
    public Position getUserSelectedPosition(ArrayDeque<Position> posableMoves) {
        // 실제 구현에서는 보드 패널의 노드 클릭 이벤트를 처리하여 선택된 위치 반환
        // 여기서는 간단히 첫 번째 이동 가능한 위치를 반환 (테스트용)
        if (posableMoves != null && !posableMoves.isEmpty()) {
            return posableMoves.getFirst();
        }
        return null;
    }*/


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

    // 커스텀 선택 대화상자 표시
    private void showCustomChoiceDialog() {
        // 여기에 커스텀 선택 대화상자 구현
        String[] options = {"옵션 1", "옵션 2", "옵션 3", "옵션 4"};
        String selected = (String) JOptionPane.showInputDialog(
                this,
                "원하는 옵션을 선택하세요:",
                "커스텀 선택",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        if (selected != null) {
            System.out.println("선택된 옵션: " + selected);
            // 선택된 옵션에 따른 처리 로직
        }
    }

    public static void main(String[] args) {
        // ★ 컨트롤러에서 넘어오는 설정값(예시) ★
        YutnoriMain.BoardType boardType   = YutnoriMain.BoardType.FOUR; // FOUR / FIVE / SIX
        int playerCount       = 2;              // 1 ~4
        int piecePerPlayer    = 3;              // 2~5

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Yutnori Game");
            frame.setSize(700, 700);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            /* STEP 4 : 배경 + 플레이어 이미지가 포함된 보드 패널 */
            YutnoriMain.BoardPanel boardPanel = new YutnoriMain.BoardPanel(boardType, playerCount, piecePerPlayer);
            frame.setContentPane(boardPanel);

            frame.setVisible(true);
        });
    }
}