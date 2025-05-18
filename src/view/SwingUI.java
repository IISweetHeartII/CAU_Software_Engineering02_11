package view;

import controller.GameController;
import model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SwingUI {
    /// 코드 해석용 참고할만한 사항
    /// 웬만한 코드는 추상적으로 표현되도록 작성돼었습니다.
    /// 쌩 중괄호{}로 감싸진 부분은 읽을 필요가 없거나, 중요도가 낮은 항목이므로 접어두시는 것을 권장드립니다.

    GameController controller;
    GameModel model;
    private JFrame frame;
    private BackgroundPanel backgroundPanel;

    private JLabel titleLabel;
    private JLabel[] playerScoreLabel;
    private JLabel turnLabel;
    private JLabel yutLabel;

    private JButton throwButton;
    private JButton STARTButton;

    private Map<String, JLabel> piecePositions = new HashMap<>();

    private int numberOfPlayers; // 플레이어 수
    private int numberOfPieces; // 플레이어당 말 수

    // ------ 생성자: Constructor ------- //
    public SwingUI(GameController controller, GameModel model) {
        // GUI 초기화 코드
        this.controller = controller;
        this.model = model;

        this.numberOfPlayers = model.getNumberOfPlayers();
        this.numberOfPieces = model.getNumberOfPieces();
        this.playerScoreLabel = new JLabel[numberOfPlayers + 1]; // 0번은 사용하지 않음

        initUI();
    }

    // ------- 메서드: Method ------ //
    public void initUI() {
        // ------- No Need Modification ------- //
        {
            frame = new JFrame("윷놀이 게임");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 700);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
        }

        // ------- 배경 설정 ------- //
        ImageIcon bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/board/board_four.png"
        )));
        Image bgImage = bgIcon.getImage();
        backgroundPanel = new BackgroundPanel(bgImage);
        backgroundPanel.setPreferredSize(new Dimension(700, 700));
        backgroundPanel.setLayout(null);  // 절대 위치 지정
        // ------------------------ //

        // ------ title 설정 ------ //
        {
            // 이미지 처리
            ImageIcon titleIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/title.PNG"
            )));
            Image scaledTitle = titleIcon.getImage().getScaledInstance(1242 / 6, 558 / 6, Image.SCALE_SMOOTH);
            titleLabel = new JLabel(new ImageIcon(scaledTitle));

            // 위치 처리 및 보정
            titleLabel.setBounds(473 - (1242/12), 20 - (558/12), 1242 / 3, 558 / 3);

            // 패널에 추가
            backgroundPanel.add(titleLabel);
        }

        // ------- Board 버튼 설정 -------- //
        {
            Map<String, NodeButton> boardButtons = createBoardButtons(boardButtonPositions);
            for (String id : boardButtons.keySet()) { // -> 각 Node 버튼의 ActionListener 설정: 나중에 메서드 책임이 바뀔 수 있음
                NodeButton btn = boardButtons.get(id);
                btn.addActionListener(e -> {
                    String clickedId = e.getActionCommand();

                    System.out.println("클릭된 노드: " + clickedId); // -> test용, 주석 처리 무관

                    controller.handleBoardClick(clickedId);
                });

                backgroundPanel.add(btn);
            }
        }
        // ------------------------------- //

        // ------ Player Score 설정 ------ //
        // 이 프로그램에서 Score는 시작하지않은(START에 위치하는) 말의 개수를 의미합니다.
        {
            for (int i = 1; i < numberOfPlayers + 1; i++) {
                // 이미지 처리
                ImageIcon playerScoreIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                        "/data/ui/score/player" + i + "/player" + i + "_" + numberOfPieces + ".png"
                )));
                Image scaledPlayerScore = playerScoreIcon.getImage().getScaledInstance(
                        playerScoreIcon.getIconWidth() / 3,
                        playerScoreIcon.getIconHeight() / 3,
                        Image.SCALE_SMOOTH);
                playerScoreLabel[i] = new JLabel(new ImageIcon(scaledPlayerScore));

                // 위치 처리 및 보정
                playerScoreLabel[i].setBounds(485, 181 + 47*(i - 1), scaledPlayerScore.getWidth(null), scaledPlayerScore.getHeight(null));

                // 패널에 추가
                backgroundPanel.add(playerScoreLabel[i]);
            }
        }
        // ------------------------------ //

        // ------ START Button 설정 ------ //
        {
            // 버튼 객체 할당
            STARTButton = new JButton("START");

            // 버튼 위치 할당
            STARTButton.setBounds(473+10, 161, 184, 202);

            // 버튼 옵션 설정
            STARTButton.setBorderPainted(false);
            STARTButton.setContentAreaFilled(false);
            STARTButton.setFocusPainted(false);
            STARTButton.setOpaque(false);
            STARTButton.setText("");

            // Action 설정
            STARTButton.addActionListener(e -> {
                controller.handleBoardClick("START");
                System.out.println("START Button Clicked");
            });

            // 패널에 버튼 추가
            backgroundPanel.add(STARTButton);
        }

        //------ Turn 설정 ------ //
        {
            // 이미지 처리 및 보정
            ImageIcon turnIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/turn/turn_1.png"
            )));
            Image scaledTurnIcon = turnIcon.getImage().getScaledInstance(turnIcon.getIconWidth() / 3, turnIcon.getIconHeight() / 3, Image.SCALE_SMOOTH);
            turnLabel = new JLabel(new ImageIcon(scaledTurnIcon));
            turnLabel.setBounds(485, 392, scaledTurnIcon.getWidth(null), scaledTurnIcon.getHeight(null));

            // 패널에 추가
            backgroundPanel.add(turnLabel);
        }
        // --------------------- //

        // ------ [Throw] Button 설정 ------ //
        {
            // 버튼 객체 할당
            throwButton = new JButton("Throw");

            // 이미지 처리 및 보정
            ImageIcon throwIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/button/button_throw_up.png"
            )));
            Image scaledThrowIcon = throwIcon.getImage().getScaledInstance(
                    throwIcon.getIconWidth() / 3,
                    throwIcon.getIconHeight() / 3,
                    Image.SCALE_SMOOTH);
            throwButton.setIcon(new ImageIcon(scaledThrowIcon));

            // 버튼 위치 할당
            throwButton.setBounds(473,473, scaledThrowIcon.getWidth(null), scaledThrowIcon.getHeight(null));

            // 버튼 옵션 설정
            throwButton.setBorderPainted(false);
            throwButton.setContentAreaFilled(false);
            throwButton.setFocusPainted(false);
            throwButton.setOpaque(false);
            throwButton.setText("");

            // Action 설정
            throwButton.addActionListener(e -> {
                controller.handleRandomThrow();
                System.out.println("Throw Button Clicked");
            });
            // view:throwButton -> controller:handleRandomThrow -> View:showYutResult

            // Hovering 처리
            ImageIcon quitHoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/button/button_throw_down.png"
            )));
            Image hoverScaled = quitHoverIcon.getImage().getScaledInstance(scaledThrowIcon.getWidth(null), scaledThrowIcon.getHeight(null), Image.SCALE_SMOOTH);
            throwButton.setRolloverIcon(new ImageIcon(hoverScaled));

            //패널에 버튼 추가
            Objects.requireNonNull(backgroundPanel).add(throwButton);

            // ------ 기본 윷 표시 ------ //
            ImageIcon yutIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/yut/yut_5.png"
            )));
            Image scaledYutIcon = yutIcon.getImage().getScaledInstance(yutIcon.getIconWidth() / 3, yutIcon.getIconHeight() / 3, Image.SCALE_SMOOTH);
            yutLabel = new JLabel(new ImageIcon(scaledYutIcon));
            yutLabel.setBounds(20, 472, scaledYutIcon.getWidth(null), scaledYutIcon.getHeight(null));
            backgroundPanel.add(yutLabel);
        }
        // --------------------------------- //

        // Todo: [Custom choice] Button

        // Todo: [Restart] Button

        // ------ Quit 버튼 설정 ------ //
        {
            // 난 버튼을 생성할 것이다 <- 버튼 객체 생성 및 위치 설정
            JButton quitButton = new JButton("Quit");
            quitButton.setBounds(584, 644, 279 / 3, 108 / 3);

            // 이미지 처리
            ImageIcon quitIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/button/button_quit_up.png"
            )));
            Image scaled = quitIcon.getImage().getScaledInstance(279 / 3, 108 / 3, Image.SCALE_SMOOTH);
            quitButton.setIcon(new ImageIcon(scaled));

            // 버튼 표시 옵션
            quitButton.setBorderPainted(false);
            quitButton.setContentAreaFilled(false);
            quitButton.setFocusPainted(false);
            quitButton.setOpaque(false);
            quitButton.setText("");

            // Action 설정
            quitButton.addActionListener(e -> {
                System.exit(0);
            });

            // Hovering 처리
            ImageIcon quitHoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/button/button_quit_down.png"
            )));
            Image hoverScaled = quitHoverIcon.getImage().getScaledInstance(279 / 3, 108 / 3, Image.SCALE_SMOOTH);
            quitButton.setRolloverIcon(new ImageIcon(hoverScaled));

            // 패널에 버튼 추가
            Objects.requireNonNull(backgroundPanel).add(quitButton);
        }
        // --------------------------- //

        // ------- 최종 설정 No Need Modification ------- //
        {
            Objects.requireNonNull(frame).setContentPane(backgroundPanel);
            frame.pack();
            frame.setVisible(true);
        }
    }


    // ---------- Board Buttons ---------- //
    Map<String, Point> boardButtonPositions = new HashMap<>();
    // 접어서 사용하세요. 다각형 보드를 제외하고는 더이상 참고할 필요 없습니다.
    {
        boardButtonPositions.put("P1", new Point(432-20, 353-20)); // 각 x, y의 20만큼 위치 보정
        boardButtonPositions.put("P2", new Point(432-20, 275-20));
        boardButtonPositions.put("P3", new Point(432-20, 197-20));
        boardButtonPositions.put("P4", new Point(432-20, 119-20));
        boardButtonPositions.put("P5", new Point(432-20, 42-20));

        boardButtonPositions.put("P6", new Point(355-20, 42-20));
        boardButtonPositions.put("P7", new Point(277-20, 42-20));
        boardButtonPositions.put("P8", new Point(199-20, 42-20));
        boardButtonPositions.put("P9", new Point(121-20, 42-20));
        boardButtonPositions.put("P10", new Point(42-20, 42-20));

        boardButtonPositions.put("P11", new Point(42-20, 119-20));
        boardButtonPositions.put("P12", new Point(42-20, 197-20));
        boardButtonPositions.put("P13", new Point(42-20, 275-20));
        boardButtonPositions.put("P14", new Point(42-20, 353-20));
        boardButtonPositions.put("P15", new Point(42-20, 432-20));

        boardButtonPositions.put("P16", new Point(121-20, 432-20));
        boardButtonPositions.put("P17", new Point(199-20, 432-20));
        boardButtonPositions.put("P18", new Point(277-20, 432-20));
        boardButtonPositions.put("P19", new Point(355-20, 432-20));
        boardButtonPositions.put("P20", new Point(432-20, 432-20));

        // 내부 경로
        boardButtonPositions.put("E1", new Point(364-20, 108-20));
        boardButtonPositions.put("E2", new Point(303-20, 171-20));
        boardButtonPositions.put("E3", new Point(171-20, 303-20));
        boardButtonPositions.put("E4", new Point(108-20, 364-20));

        boardButtonPositions.put("C", new Point(237-20, 237-20));

        boardButtonPositions.put("E5", new Point(108-20, 108-20));
        boardButtonPositions.put("E6", new Point(171-20, 171-20));
        boardButtonPositions.put("E7", new Point(303-20, 303-20));
        boardButtonPositions.put("E8", new Point(364-20, 364-20));
    }

    public static class NodeButton extends JButton {
        private final String nodeId;

        public NodeButton(String nodeId, int x, int y) {
            this.nodeId = nodeId;
            setBounds(x, y, 40, 40);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);

            setActionCommand(nodeId); // -> 버튼마다 고유 ID 지정 -> ID로 버튼 클릭 조작
        }

        public String getNodeId() {
            return nodeId;
        }
    }

    public static Map<String, NodeButton> createBoardButtons(Map<String, Point> positions) {
        Map<String, NodeButton> buttonMap = new HashMap<>();

        for (String id : positions.keySet()) {  // boardButtonPositions 에 저장된 위치 정보마다 버튼을 생성
            Point p = positions.get(id);        // └ boardButtonPositions Map만 수정하면 다각형으로 확장 가능
            NodeButton button = new NodeButton(id, p.x, p.y);
            buttonMap.put(id, button);
        }
        return buttonMap;
    }
    // ----------------------------------- //


    // ------ show yut result <---- controller ------ //
    public void showYutResult(Integer yutResult) {
        // 이미지 처리
        ImageIcon yutResultIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/yut/yut_" + yutResult + ".png"
        )));
        Image scaledYutResult = yutResultIcon.getImage().getScaledInstance(
                yutResultIcon.getIconWidth() / 3,
                yutResultIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);

        // 객체 생성
        yutLabel.setIcon(new ImageIcon(scaledYutResult));
    }
    // ---------------------------------------------- //

    // ------ update board ------ //
    private static final int PIECE_SCALE_FACTOR = 3;
    private static final int POSITION_ADJUSTMENT_FACTOR = 6;
    public void updateBoard() {
        // 기존에 표시된 모든 말 제거
        removePreviousPieces();

        // 새로운 말 위치 정보 가져오기
        Map<String, String> piecePositionsMap = model.getPiecePositionsMap();

        // 새로운 말들을 생성하고 표시
        piecePositionsMap.forEach((pieceId, nodeId) -> {
            if (!(nodeId.equals("START") || nodeId.equals("END"))) { // SART, END 제외
                JLabel pieceLabel = createScaledPieceLabel(pieceId);
                adjustPiecePosition(pieceLabel, nodeId);
                piecePositions.put(pieceId, pieceLabel);  // Map에 저장
                backgroundPanel.add(pieceLabel);
            }
        });

        // 화면 갱신
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    private JLabel createScaledPieceLabel(String pieceId) { // -> updateBoard()
        ImageIcon pieceIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/player/p" + pieceId.charAt(0) + "_" + pieceId.length() + ".png"
        )));

        Image scaledPiece = pieceIcon.getImage().getScaledInstance(
                pieceIcon.getIconWidth() / PIECE_SCALE_FACTOR,
                pieceIcon.getIconHeight() / PIECE_SCALE_FACTOR,
                Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(scaledPiece));
    }

    private void adjustPiecePosition(JLabel pieceLabel, String nodeId) { // -> updateBoard()
        Icon icon = pieceLabel.getIcon();
        Point position = boardButtonPositions.get(nodeId);

        pieceLabel.setBounds(
                position.x - ((ImageIcon) icon).getIconWidth() / POSITION_ADJUSTMENT_FACTOR,
                position.y - ((ImageIcon) icon).getIconHeight() / POSITION_ADJUSTMENT_FACTOR,
                ((ImageIcon) icon).getIconWidth() / PIECE_SCALE_FACTOR,
                ((ImageIcon) icon).getIconHeight() / PIECE_SCALE_FACTOR);
    }

    private void removePreviousPieces() { // -> updateBoard()
        // 기존 말들을 패널에서 제거
        piecePositions.values().forEach(backgroundPanel::remove);
        piecePositions.clear();
    }
    // ------------------ //

    public void updatePlayerScore() {
        int[] gameScores = model.getNotStartedCount();
        for (int i = 0; i < numberOfPlayers; i++) {
            // 플레이어의 점수 가져오기
            int notStartedCount = gameScores[i];

            // 이미지 처리
            ImageIcon playerScoreIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/score/player" + (i + 1) + "/player" + (i + 1) + "_" + notStartedCount + ".png"
            )));
            Image scaledPlayerScore = playerScoreIcon.getImage().getScaledInstance(
                    playerScoreIcon.getIconWidth() / 3,
                    playerScoreIcon.getIconHeight() / 3,
                    Image.SCALE_SMOOTH);

            // 사진 교체
            playerScoreLabel[i + 1].setIcon(new ImageIcon(scaledPlayerScore));
        }
    }

    public void showGameEnd(String playerID) {
    }

    // ------ update turn ------ //
    public void updateTurn(String playerID) {
        // 이미지 처리 및 보정
        ImageIcon turnIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/turn/turn_" + playerID + ".png"
        )));
        Image scaledTurnIcon = turnIcon.getImage().getScaledInstance(
                turnIcon.getIconWidth() / 3,
                turnIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);
        // 사진 교체
        turnLabel.setIcon(new ImageIcon(scaledTurnIcon));
    }
}
