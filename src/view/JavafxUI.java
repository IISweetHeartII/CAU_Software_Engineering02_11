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


    // 버튼
    private Button throwButton;
    private Button STARTButton;
    private Button ENDButton;

    private Map<String, Point2D> piecePositions = new HashMap<>();

    private int numberOfPlayers; // 플레이어 수
    private int numberOfPieces; // 플레이어당 말 수

    // ------ 생성자: Constructor ------- //
    public JavafxUI(GameController controller, GameManager model) {
        this.controller = controller;
        this.model = model;

        this.numberOfPlayers = model.getNumberOfPlayers();
        this.numberOfPieces = model.getNumberOfPieces();


    }
    @Override
    public void initUI() {
        // JavaFX는 start(Stage)를 통해 UI를 시작하므로, 이 메서드는 실제로는 사용되지 않음
    }

    // ------ UI 초기화: initUI() -> start() ------- //
    public void start(Stage stage) {
        this.stage = stage;
        root = new Pane();
        root.setPrefSize(700, 700);
        Scene scene = new Scene(root, 700, 700);

        //일단 할당만해둠 추후에 이미지처리할예정
        initBoardButtonPositions();
        setupBackground();
        setupTitleImage();
        setupBoardButtons();
        setupYutImage(); // 윷 이미지 초기화
        setupPlayerScoreImages(); // 플레이어 점수 이미지 초기화
        setupTurnImage(); // 현재 턴 이미지 초기화
        createThrowButton();
        setupStartButton();
        setupEndButton();
        createRestartButton();
        createQuitButton();
        setupCustomPopupButton();


        stage.setTitle("윷놀이 게임(JavaFX)"); //-> 일단 추후 이미지처리
        stage.setScene(scene);
        stage.show();
    }


    // ------- 배경 설정 ------- //
    private void setupBackground() {
        int boardSize = model.getSize(); // GameManager에서 보드 크기 가져오기
        String imagePath;

        switch (boardSize) {
            case 4 -> imagePath = "/data/ui/board/board_four.png";
            case 5 -> imagePath = "/data/ui/board/board_five.png";
            case 6 -> imagePath = "/data/ui/board/board_six.png";
            default -> {
                System.out.println("Invalid board size");
                imagePath = "/data/ui/board/board_four.png";
            }
        }

        Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        BackgroundImage background = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(700, 700, false, false, false, false)
        );
        root.setBackground(new Background(background));
    }


    // ------ title 설정 ------ //
    private void setupTitleImage() {
        Image titleImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/data/ui/title.PNG")));

        titleImage = new ImageView(titleImg);

        // 이미지 비율: 1242 x 558 → 1/6 축소
        double originalWidth = 1242;
        double originalHeight = 558;
        double scaledWidth = originalWidth / 6;
        double scaledHeight = originalHeight / 6;

        titleImage.setFitWidth(scaledWidth);
        titleImage.setFitHeight(scaledHeight);
        titleImage.setPreserveRatio(true); // 혹시 비율 보정 원하면

        // 좌표 위치 보정 (Swing과 동일한 방식)
        titleImage.setLayoutX(475);
        titleImage.setLayoutY(20);

        // 보드 패널(root)에 추가
        root.getChildren().add(titleImage);
    }


    // ------- Board 버튼 설정 -------- //
    private void setupBoardButtons() {
        for (String id : piecePositions.keySet()) {
            Point2D point = piecePositions.get(id);

            Button nodeButton = new Button();
            nodeButton.setPrefSize(40, 40);
            nodeButton.setLayoutX(point.getX());
            nodeButton.setLayoutY(point.getY());
            nodeButton.setOpacity(0); // 버튼 투명도 -> 0

            nodeButton.setId(id); // ✔ 버튼에 직접 ID를 부여

            nodeButton.setOnAction(e -> {
                String clickedId = ((Button) e.getSource()).getId(); // ✔ 이벤트에서 ID 직접 꺼냄
                controller.handleBoardClick(clickedId);
            });

            root.getChildren().add(nodeButton);
        }
    }


    // ------ Player Score 설정 ------ //
    private void setupPlayerScoreImages() {
        playerScoreImages = new ImageView[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            int playerNum = i + 1;
            String imagePath = "/data/ui/score/player" + playerNum +
                    "/player" + playerNum + "_" + numberOfPieces + ".png";

            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

            ImageView scoreView = new ImageView(img);


            double scaledWidth = img.getWidth() / 3;
            double scaledHeight = img.getHeight() / 3;

            scoreView.setFitWidth(scaledWidth);
            scoreView.setFitHeight(scaledHeight);

            scoreView.setLayoutX(485);
            scoreView.setLayoutY(181 + 47 * i);


            root.getChildren().add(scoreView);
            playerScoreImages[i] = scoreView;
        }
    }


    // ------ START Button 설정 ------ //
    private void setupStartButton() {
        STARTButton = new Button();
        STARTButton.setLayoutX(483); // 473 + 10 보정
        STARTButton.setLayoutY(161);
        STARTButton.setPrefSize(184, 202);
        STARTButton.setStyle("-fx-background-color: transparent;");

        // START 위치 클릭 시 controller로 전달
        STARTButton.setOnAction(e -> {
            controller.handleBoardClick("START");
            System.out.println("START Button Clicked");
        });

        root.getChildren().add(STARTButton);
    }

    // ------ END Button 설정 ------ //
    private void setupEndButton() {
        ENDButton = new Button();
        ENDButton.setLayoutX(20);      // Swing과 동일
        ENDButton.setLayoutY(472);
        ENDButton.setPrefSize(433, 207);  // 1299/3, 621/3
        ENDButton.setStyle("-fx-background-color: transparent;");

        ENDButton.setOnAction(e -> {
            controller.handleBoardClick("END");
            System.out.println("END Button Clicked");
        });

        root.getChildren().add(ENDButton);
    }

    // ------ Turn Image 설정 ------ //
    private void setupTurnImage() {
        int currentPlayer = model.getCurrentPlayerNumber(); // 보통 1부터 시작
        String imagePath = "/data/ui/turn/turn_" + currentPlayer + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        double scaledWidth = img.getWidth() / 3;
        double scaledHeight = img.getHeight() / 3;

        turnImage = new ImageView(img);
        turnImage.setFitWidth(scaledWidth);
        turnImage.setFitHeight(scaledHeight);

        turnImage.setLayoutX(485);
        turnImage.setLayoutY(392);
        root.getChildren().add(turnImage);
    }

    // ---------- Board Buttons ---------- //
    private void initBoardButtonPositions() {
        piecePositions = new HashMap<>();
        int boardSize = model.getSize(); // 4, 5, 6

        switch (boardSize) {
            case 4 -> initBoardButtons4();
            case 5 -> initBoardButtons5();
            case 6 -> initBoardButtons6();
            default -> {
                System.out.println("Invalid board size.");
                initBoardButtons4();
            }
        }
    }
    private void initBoardButtons4() {
        piecePositions = new HashMap<>();
        piecePositions.put("P1", new Point2D(432-20, 353-20)); // 각 x, y의 20만큼 위치 보정
        piecePositions.put("P2", new Point2D(432-20, 275-20));
        piecePositions.put("P3", new Point2D(432-20, 197-20));
        piecePositions.put("P4", new Point2D(432-20, 119-20));
        piecePositions.put("P5", new Point2D(432-20, 42-20));

        piecePositions.put("P6", new Point2D(355-20, 42-20));
        piecePositions.put("P7", new Point2D(277-20, 42-20));
        piecePositions.put("P8", new Point2D(199-20, 42-20));
        piecePositions.put("P9", new Point2D(121-20, 42-20));
        piecePositions.put("P10", new Point2D(42-20, 42-20));

        piecePositions.put("P11", new Point2D(42-20, 119-20));
        piecePositions.put("P12", new Point2D(42-20, 197-20));
        piecePositions.put("P13", new Point2D(42-20, 275-20));
        piecePositions.put("P14", new Point2D(42-20, 353-20));
        piecePositions.put("P15", new Point2D(42-20, 432-20));

        piecePositions.put("P16", new Point2D(121-20, 432-20));
        piecePositions.put("P17", new Point2D(199-20, 432-20));
        piecePositions.put("P18", new Point2D(277-20, 432-20));
        piecePositions.put("P19", new Point2D(355-20, 432-20));
        piecePositions.put("P20", new Point2D(432-20, 432-20));

        // 내부 경로
        piecePositions.put("E1", new Point2D(364-20, 108-20));
        piecePositions.put("E2", new Point2D(303-20, 171-20));
        piecePositions.put("E3", new Point2D(171-20, 303-20));
        piecePositions.put("E4", new Point2D(108-20, 364-20));

        piecePositions.put("C", new Point2D(237-20, 237-20));

        piecePositions.put("E5", new Point2D(108-20, 108-20));
        piecePositions.put("E6", new Point2D(171-20, 171-20));
        piecePositions.put("E7", new Point2D(303-20, 303-20));
        piecePositions.put("E8", new Point2D(364-20, 364-20));
    }

    private void initBoardButtons5() {
        piecePositions.put("P1", new Point2D(372-20, 375-20)); // 각 x, y의 20만큼 위치 보정
        piecePositions.put("P2", new Point2D(388-20, 330-20));
        piecePositions.put("P3", new Point2D(402-20, 284-20));
        piecePositions.put("P4", new Point2D(417-20, 238-20));
        piecePositions.put("P5", new Point2D(432-20, 192-20));

        piecePositions.put("P6", new Point2D(393-20, 164-20));
        piecePositions.put("P7", new Point2D(354-20, 135-20));
        piecePositions.put("P8", new Point2D(315-20, 107-20));
        piecePositions.put("P9", new Point2D(276-20, 78-20));
        piecePositions.put("P10", new Point2D(236-20, 50-20));

        piecePositions.put("P11", new Point2D(197-20, 78-20));
        piecePositions.put("P12", new Point2D(158-20, 107-20));
        piecePositions.put("P13", new Point2D(119-20, 135-20));
        piecePositions.put("P14", new Point2D(80-20, 164-20));
        piecePositions.put("P15", new Point2D(41-20, 192-20));

        piecePositions.put("P16", new Point2D(56-20, 238-20));
        piecePositions.put("P17", new Point2D(71-20, 284-20));
        piecePositions.put("P18", new Point2D(87-20, 330-20));
        piecePositions.put("P19", new Point2D(101-20, 375-20));
        piecePositions.put("P20", new Point2D(115-20, 422-20));

        piecePositions.put("P21", new Point2D(164-20, 422-20));
        piecePositions.put("P22", new Point2D(212-20, 422-20));
        piecePositions.put("P23", new Point2D(261-20, 422-20));
        piecePositions.put("P24", new Point2D(309-20, 422-20));
        piecePositions.put("P25", new Point2D(358-20, 422-20));

        piecePositions.put("C", new Point2D(236-20, 255-20));

        piecePositions.put("E1", new Point2D(354-20, 217-20));
        piecePositions.put("E2", new Point2D(295-20, 236-20));
        piecePositions.put("E3", new Point2D(200-20, 305-20));
        piecePositions.put("E4", new Point2D(164-20, 355-20));
        piecePositions.put("E5", new Point2D(236-20, 132-20));
        piecePositions.put("E6", new Point2D(236-20, 195-20));
        piecePositions.put("E7", new Point2D(274-20, 305-20));
        piecePositions.put("E8", new Point2D(309-20, 355-20));
        piecePositions.put("E9", new Point2D(119-20, 217-20));
        piecePositions.put("E10", new Point2D(178-20, 236-20));
    }

    private void initBoardButtons6() {
        piecePositions.put("P1", new Point2D(354-20, 370-20)); // 각 x, y의 20만큼 위치 보정
        piecePositions.put("P2", new Point2D(373-20, 337-20));
        piecePositions.put("P3", new Point2D(393-20, 303-20));
        piecePositions.put("P4", new Point2D(409-20, 269-20));
        piecePositions.put("P5", new Point2D(431-20, 236-20));

        piecePositions.put("P6", new Point2D(409-20, 203-20));
        piecePositions.put("P7", new Point2D(393-20, 169-20));
        piecePositions.put("P8", new Point2D(373-20, 133-20));
        piecePositions.put("P9", new Point2D(354-20, 101-20));
        piecePositions.put("P10", new Point2D(335-20, 67-20));

        piecePositions.put("P11", new Point2D(295-20, 67-20));
        piecePositions.put("P12", new Point2D(256-20, 67-20));
        piecePositions.put("P13", new Point2D(217-20, 67-20));
        piecePositions.put("P14", new Point2D(178-20, 67-20));
        piecePositions.put("P15", new Point2D(139-20, 67-20));

        piecePositions.put("P16", new Point2D(118-20, 99-20));
        piecePositions.put("P17", new Point2D(98-20, 134-20));
        piecePositions.put("P18", new Point2D(80-20, 168-20));
        piecePositions.put("P19", new Point2D(60-20, 202-20));
        piecePositions.put("P20", new Point2D(41-20, 236-20));

        piecePositions.put("P21", new Point2D(60-20, 269-20));
        piecePositions.put("P22", new Point2D(80-20, 303-20));
        piecePositions.put("P23", new Point2D(98-20, 337-20));
        piecePositions.put("P24", new Point2D(118-20, 370-20));
        piecePositions.put("P25", new Point2D(139-20, 404-20));

        piecePositions.put("P26", new Point2D(178-20, 404-20));
        piecePositions.put("P27", new Point2D(217-20, 404-20));
        piecePositions.put("P28", new Point2D(256-20, 404-20));
        piecePositions.put("P29", new Point2D(295-20, 404-20));
        piecePositions.put("P30", new Point2D(335-20, 404-20));

        piecePositions.put("C", new Point2D(235-20, 236-20));

        piecePositions.put("E1", new Point2D(366-20, 236-20));
        piecePositions.put("E2", new Point2D(301-20, 236-20));
        piecePositions.put("E3", new Point2D(168-20, 236-20));
        piecePositions.put("E4", new Point2D(105-20, 236-20));

        piecePositions.put("E5", new Point2D(170-20, 123-20));
        piecePositions.put("E6", new Point2D(203-20, 180-20));
        piecePositions.put("E7", new Point2D(268-20, 292-20));
        piecePositions.put("E8", new Point2D(298-20, 348-20));

        piecePositions.put("E9", new Point2D(298-20, 123-20));
        piecePositions.put("E10", new Point2D(268-20, 180-20));
        piecePositions.put("E11", new Point2D(203-20, 292-20));
        piecePositions.put("E12", new Point2D(170-20, 348-20));
    }

    // ------ Swing: CreateCustomPopupButton을 JavaFX로 변환 ------ //
    // ------ Custom Popup Button 설정 ------ //
    private void setupCustomPopupButton() {
        Image imgUp = new Image(getClass().getResourceAsStream("/data/ui/button/button_custom_up.png"));
        Image imgDown = new Image(getClass().getResourceAsStream("/data/ui/button/button_custom_down.png"));

        ImageView imageView = new ImageView(imgUp);
        imageView.setFitWidth(621 / 3.0);
        imageView.setFitHeight(108 / 3.0);

        Button popupButton = new Button();
        popupButton.setGraphic(imageView);
        popupButton.setLayoutX(473);
        popupButton.setLayoutY(587);
        popupButton.setStyle("-fx-background-color: transparent;");

        popupButton.setOnMouseEntered(e -> imageView.setImage(imgDown));
        popupButton.setOnMouseExited(e -> imageView.setImage(imgUp));

        popupButton.setOnAction(e -> openYutPopup());

        root.getChildren().add(popupButton);
    }

    // ------ Yut Popup 열기 ------ //
    private void openYutPopup() {
        int POPUP_WIDTH = 1881 / 3 + 10;
        int POPUP_HEIGHT = 342 / 3 + 35;

        Stage popupStage = new Stage();
        popupStage.initOwner(stage);
        popupStage.setTitle("지정 윷 던지기");

        Pane popupRoot = new Pane();
        Scene scene = new Scene(popupRoot, POPUP_WIDTH, POPUP_HEIGHT);

        // 배경 이미지
        Image bgImage = new Image(getClass().getResourceAsStream("/data/ui/custom/custom_steady.png"));
        BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(POPUP_WIDTH, POPUP_HEIGHT, false, false, false, false));
        popupRoot.setBackground(new Background(bg));

        // 버튼 생성
        int BUTTON_WIDTH = POPUP_WIDTH / 6;
        for (int i = 0; i < 6; i++) {
            int throwValue = (i == 5) ? -1 : (i + 1);
            Button yutButton = new Button();
            yutButton.setLayoutX(BUTTON_WIDTH * i);
            yutButton.setLayoutY(-20); // 살짝 위로
            yutButton.setPrefSize(BUTTON_WIDTH, POPUP_HEIGHT);
            yutButton.setStyle("-fx-background-color: transparent;");

            int finalThrowValue = throwValue;
            yutButton.setOnAction(e -> {
                controller.handleManualThrow(finalThrowValue);
                popupStage.close();
            });

            popupRoot.getChildren().add(yutButton);
        }

        popupStage.setScene(scene);
        popupStage.setResizable(false);
        popupStage.show();
    }


    // ------ show yut result <---- controller ------ //
    public void showYutResult(Integer yutResult) {
        String imagePath = "/data/ui/yut/yut_" + yutResult + ".png";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

        double scaledWidth = image.getWidth() / 3;
        double scaledHeight = image.getHeight() / 3;

        if (yutImage == null) {
            yutImage = new ImageView();
            yutImage.setLayoutX(20);
            yutImage.setLayoutY(472);
            root.getChildren().add(yutImage);
        }

        yutImage.setImage(image);
        yutImage.setFitWidth(scaledWidth);
        yutImage.setFitHeight(scaledHeight);

    }

    private void setupYutImage() {
        Image initialImage = new Image(getClass().getResourceAsStream("/data/ui/yut/yut_5.png"));
        double scaledWidth = initialImage.getWidth() / 3;
        double scaledHeight = initialImage.getHeight() / 3;

        yutImage = new ImageView(initialImage);
        yutImage.setFitWidth(scaledWidth);
        yutImage.setFitHeight(scaledHeight);
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
            String imagePath = "/data/ui/player/p" + pieceId.charAt(0) + "_" + pieceId.length() + ".png";
            Image pieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

            // 4. 말 이미지 뷰 구성
            ImageView pieceView = new ImageView(pieceImage);
            pieceView.setFitWidth(36);
            pieceView.setFitHeight(36);
            pieceView.setPreserveRatio(true); // 비율 유지

            // 5. 위치 조정 (중앙 정렬)
            Point2D pos = piecePositions.get(nodeId);
            pieceView.setLayoutX(pos.getX() - 18); // 36/2
            pieceView.setLayoutY(pos.getY() - 18);

            // 6. 화면에 추가
            root.getChildren().add(pieceView);
        }

    }

    private void createThrowButton() {
        // 1. 이미지 로드
        Image imgUp = new Image(getClass().getResourceAsStream("/data/ui/button/button_throw_up.png"));
        Image imgDown = new Image(getClass().getResourceAsStream("/data/ui/button/button_throw_down.png"));

        // 2. 버튼 생성 및 배치
        throwButton = new Button();
        ImageView buttonImageView = new ImageView(imgUp);
        buttonImageView.setFitWidth(imgUp.getWidth() / 3);  // Swing 기준으로 3분의 1 축소
        buttonImageView.setFitHeight(imgUp.getHeight() / 3);

        throwButton.setGraphic(buttonImageView);
        throwButton.setLayoutX(473);
        throwButton.setLayoutY(473);
        throwButton.setStyle("-fx-background-color: transparent;");

        // 3. Hover 효과
        throwButton.setOnMouseEntered(e -> buttonImageView.setImage(imgDown));
        throwButton.setOnMouseExited(e -> buttonImageView.setImage(imgUp));

        // 4. 클릭 이벤트 → 컨트롤러로 던지기 요청
        throwButton.setOnAction(e -> {
            controller.handleRandomThrow();
            System.out.println("Throw Button Clicked");
        });

        // 5. UI에 추가
        root.getChildren().add(throwButton);
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

    private void createQuitButton() {
        // 1. 이미지 로드 및 크기 조정
        Image imgUp = new Image(getClass().getResourceAsStream("/data/ui/button/button_quit_up.png"));
        Image imgDown = new Image(getClass().getResourceAsStream("/data/ui/button/button_quit_down.png"));
        // 2. 이미지 뷰 생성
        ImageView imageView = new ImageView(imgUp);
        imageView.setFitWidth(279 / 3.0);
        imageView.setFitHeight(108 / 3.0);
        // 3. 버튼 생성 및 배치
        Button quitButton = new Button();
        quitButton.setGraphic(imageView);
        quitButton.setLayoutX(584);
        quitButton.setLayoutY(644);
        quitButton.setStyle("-fx-background-color: transparent;");

        // Hover 효과
        quitButton.setOnMouseEntered(e -> imageView.setImage(imgDown));
        quitButton.setOnMouseExited(e -> imageView.setImage(imgUp));
        // 4. 클릭 이벤트 → 종료 처리
        quitButton.setOnAction(e -> {
            System.out.println("Quit Button Clicked");
            System.exit(0);
        });
        // 5. UI에 추가
        root.getChildren().add(quitButton);
    }


    private void createRestartButton() {
        // 1. 이미지 로드 및 크기 조정
        Image imgUp = new Image(getClass().getResourceAsStream("/data/ui/button/button_restart_up.png"));
        Image imgDown = new Image(getClass().getResourceAsStream("/data/ui/button/button_restart_down.png"));
        // 2. 이미지 뷰 생성
        ImageView imageView = new ImageView(imgUp);
        imageView.setFitWidth(imgUp.getWidth() / 3);
        imageView.setFitHeight(imgUp.getHeight() / 3);
        // 3. 버튼 생성 및 배치
        Button restartButton = new Button();
        restartButton.setGraphic(imageView);
        restartButton.setLayoutX(473);
        restartButton.setLayoutY(644);
        restartButton.setStyle("-fx-background-color: transparent;");

        // Hover 효과
        restartButton.setOnMouseEntered(e -> imageView.setImage(imgDown));
        restartButton.setOnMouseExited(e -> imageView.setImage(imgUp));
        // 4. 클릭 이벤트 → 새 게임 시작
        restartButton.setOnAction(e -> {
            System.out.println("Restart Button Clicked");
            // 새 게임 모델과 컨트롤러, 뷰 생성
            GameManager newModel = new GameManager();
            GameController newController = new GameController(newModel);
            JavafxUI newView = new JavafxUI(newController, newModel);
            newController.setView(newView);

            // 새 게임 시작
            stage.close(); // 현재 창 닫고
            Stage newStage = new Stage();
            newView.start(newStage); // 새 게임 시작
        });
        // 5. UI에 추가
        root.getChildren().add(restartButton);
    }

    // ------ updateTurn() ------ //
    public void updateTurn() {
        int currentPlayer = model.getCurrentPlayerNumber();
        String imagePath = "/data/ui/turn/turn_" + currentPlayer + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

        turnImage.setImage(img);
    }


}




