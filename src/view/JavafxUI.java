package view;

import controller.GameController;
import model.GameManager;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class JavafxUI implements GameView {
    GameController controller;
    GameManager model;

    // JavaFX
    private Stage stage;  // JFrame → Stage
    private Pane root;  // JPanel → Pane

    private ImageView titleImage;  // JLabel (이미지용) → ImageView
    private ImageView[] playerScoreImages;  // JLabel[] → ImageView[]
    private ImageView turnImage;
    private ImageView yutImage;

    // 버튼들 (아직 미구현)
    private Button throwButton;
    private Button STARTButton;
    private Button ENDButton;

    private Map<String, Point2D> boardButtonPositions = new HashMap<>();

    private int numberOfPlayers; // 플레이어 수
    private int numberOfPieces; // 플레이어당 말 수

    // ------ 생성자: Constructor ------- //
    public JavafxUI(GameController controller, GameManager model) {
        this.controller = controller;
        this.model = model;

        this.numberOfPlayers = model.getNumberOfPlayers();
        this.numberOfPieces = model.getNumberOfPieces();


    }

    // ------ UI 초기화: initUI() -> start() ------- //
    public void start(Stage stage) {
        this.stage = stage;
        root = new Pane();
        Scene scene = new Scene(root, 700, 700);

        //일단 할당만해둠 추후에 이미지처리할예정
        initBoardButtonPositions();
        setupBackground();
        setupTitleImage();
        setupBoardButtons();
        setupYutImage(); // 윷 이미지 초기화
        setupPlayerScoreImages(); // 플레이어 점수 이미지 초기화
        setupTurnImage(); // 현재 턴 이미지 초기화

        stage.setTitle("윷놀이 게임(JavaFX)"); //-> 일단 추후 이미지처리
        stage.setScene(scene);
        stage.show();
    }


    // ------- 배경 설정 ------- //
    private void setupBackground() {
        Image bgImage = new Image(getClass().getResourceAsStream("/data/ui/board/board_four.png"));
        BackgroundImage background = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));
    }

    // ------ title 설정 ------ //
    private void setupTitleImage() {
        //이미지 처리 / 위치 처리 필요
    }


    // ------- Board 버튼 설정 -------- //
    private void setupBoardButtons() {
        for (String id : boardButtonPositions.keySet()) {
            Point2D point = boardButtonPositions.get(id);

            Button nodeButton = new Button();
            nodeButton.setPrefSize(40, 40);
            nodeButton.setLayoutX(point.getX());
            nodeButton.setLayoutY(point.getY());
            nodeButton.setOpacity(0); // 버튼 투명도 -> 0

            nodeButton.setOnAction(e -> controller.handleBoardClick(id));

            root.getChildren().add(nodeButton);
        }
    }

    // ------ Player Score 설정 ------ // (아직 미구현)

    // ------ START Button 설정 ------ // (아직 미구현)

    // ------ END Button 설정 ------ // (아직 미구현)

    // 추가 버튼들 (아직 미구현)











    // ---------- Board Buttons ---------- //
    private void initBoardButtonPositions()
    {
        boardButtonPositions = new HashMap<>();
        boardButtonPositions.put("P1", new Point2D(432-20, 353-20)); // 각 x, y의 20만큼 위치 보정
        boardButtonPositions.put("P2", new Point2D(432-20, 275-20));
        boardButtonPositions.put("P3", new Point2D(432-20, 197-20));
        boardButtonPositions.put("P4", new Point2D(432-20, 119-20));
        boardButtonPositions.put("P5", new Point2D(432-20, 42-20));

        boardButtonPositions.put("P6", new Point2D(355-20, 42-20));
        boardButtonPositions.put("P7", new Point2D(277-20, 42-20));
        boardButtonPositions.put("P8", new Point2D(199-20, 42-20));
        boardButtonPositions.put("P9", new Point2D(121-20, 42-20));
        boardButtonPositions.put("P10", new Point2D(42-20, 42-20));

        boardButtonPositions.put("P11", new Point2D(42-20, 119-20));
        boardButtonPositions.put("P12", new Point2D(42-20, 197-20));
        boardButtonPositions.put("P13", new Point2D(42-20, 275-20));
        boardButtonPositions.put("P14", new Point2D(42-20, 353-20));
        boardButtonPositions.put("P15", new Point2D(42-20, 432-20));

        boardButtonPositions.put("P16", new Point2D(121-20, 432-20));
        boardButtonPositions.put("P17", new Point2D(199-20, 432-20));
        boardButtonPositions.put("P18", new Point2D(277-20, 432-20));
        boardButtonPositions.put("P19", new Point2D(355-20, 432-20));
        boardButtonPositions.put("P20", new Point2D(432-20, 432-20));

        // 내부 경로
        boardButtonPositions.put("E1", new Point2D(364-20, 108-20));
        boardButtonPositions.put("E2", new Point2D(303-20, 171-20));
        boardButtonPositions.put("E3", new Point2D(171-20, 303-20));
        boardButtonPositions.put("E4", new Point2D(108-20, 364-20));

        boardButtonPositions.put("C", new Point2D(237-20, 237-20));

        boardButtonPositions.put("E5", new Point2D(108-20, 108-20));
        boardButtonPositions.put("E6", new Point2D(171-20, 171-20));
        boardButtonPositions.put("E7", new Point2D(303-20, 303-20));
        boardButtonPositions.put("E8", new Point2D(364-20, 364-20));
    }

    // 주요 메서드

    // ------ show yut result <---- controller ------ //
    public void showYutResult(Integer yutResult) {
        if (yutImage == null) {
            yutImage = new ImageView();
            yutImage.setFitWidth(70);  // 예시 크기 (기존 이미지 크기에 맞게 조절)
            yutImage.setFitHeight(70);
            yutImage.setLayoutX(20);   // Swing과 동일하게 좌측 하단 위치
            yutImage.setLayoutY(472);
            root.getChildren().add(yutImage);
        }

        String imagePath = "/data/ui/yut/yut_" + yutResult + ".png";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        yutImage.setImage(image);
    }

    private void setupYutImage() {
        Image initialImage = new Image(getClass().getResourceAsStream("/data/ui/yut/yut_5.png"));
        yutImage = new ImageView(initialImage);
        yutImage.setFitWidth(70);
        yutImage.setFitHeight(70);
        yutImage.setLayoutX(20);
        yutImage.setLayoutY(472);
        root.getChildren().add(yutImage);
    }

    // ------ showWinner() <---- controller ------ //
    public void showWinner(int winner) {
        String imagePath = "/data/ui/turn/winner_" + winner + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

        turnImage.setImage(img);  // 같은 뷰에 승리자 이미지 덮어쓰기
    }

    // ------ update board ------ //
    public void updateBoard() {
        // 1. 기존 말 이미지 제거 (id가 "piece_"로 시작하는 ImageView만)
        root.getChildren().removeIf(node ->
                node instanceof ImageView &&
                        node.getId() != null &&
                        node.getId().startsWith("piece_")
        );

        // 2. 모델에서 말 위치 데이터 가져오기 (nodeId -> pieceId 형식)
        Map<String, String> positionPieceMap = model.getPositionPieceMap();

        for (Map.Entry<String, String> entry : positionPieceMap.entrySet()) {
            String nodeId = entry.getKey();
            String pieceId = entry.getValue();

            // START와 END 위치는 UI에 표시하지 않음
            if (nodeId.equals("START") || nodeId.equals("END")) continue;

            // 3. 말 이미지 로드
            String imagePath = "/data/ui/player/" + pieceId + ".png";
            Image pieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

            // 4. 말 이미지 뷰 구성
            ImageView pieceView = new ImageView(pieceImage);
            pieceView.setId("piece_" + pieceId); // 다음 remove 시 식별용
            pieceView.setFitWidth(36);
            pieceView.setFitHeight(36);

            // 5. 위치 조정 (중앙 정렬)
            Point2D pos = boardButtonPositions.get(nodeId);
            pieceView.setLayoutX(pos.getX() - pieceView.getFitWidth() / 2);
            pieceView.setLayoutY(pos.getY() - pieceView.getFitHeight() / 2);

            // 6. 화면에 추가
            root.getChildren().add(pieceView);
        }
    }

// ------ updatePlayerScore() ------ //
public void updatePlayerScore() {
    int[] notStartedCounts = model.getCountOfPieceAtStart();

    for (int i = 0; i < numberOfPlayers; i++) {
        int playerNum = i + 1;
        int notStarted = notStartedCounts[i];

        String imagePath = "/data/ui/score/player" + playerNum + "/player" + playerNum + "_" + notStarted + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

        playerScoreImages[i].setImage(img);
    }
}

    private void setupPlayerScoreImages() {
        playerScoreImages = new ImageView[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            int playerNum = i + 1;
            int notStarted = model.getCountOfPieceAtStart()[i];

            String imagePath = "/data/ui/score/player" + playerNum + "/player" + playerNum + "_" + notStarted + ".png";
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

            ImageView scoreView = new ImageView(img);
            scoreView.setFitWidth(80); // 필요 시 조절
            scoreView.setFitHeight(30);
            scoreView.setLayoutX(485);
            scoreView.setLayoutY(181 + 47 * i); // 위치 보정 (Swing 기준)

            root.getChildren().add(scoreView);
            playerScoreImages[i] = scoreView;
        }
    }

    // ------ updateTurn() ------ //
    public void updateTurn() {
        int currentPlayer = model.getCurrentPlayerNumber();
        String imagePath = "/data/ui/turn/turn_" + currentPlayer + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

        turnImage.setImage(img);
    }

    private void setupTurnImage() {
        int currentPlayer = model.getCurrentPlayerNumber(); // 보통 1부터 시작
        String imagePath = "/data/ui/turn/turn_" + currentPlayer + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

        turnImage = new ImageView(img);
        turnImage.setFitWidth(90);  // 예시 크기 (조절 가능)
        turnImage.setFitHeight(30);
        turnImage.setLayoutX(485);  // Swing 기준 보정값
        turnImage.setLayoutY(392);

        root.getChildren().add(turnImage);
    }
}




