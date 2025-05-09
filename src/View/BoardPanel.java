package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class BoardPanel extends JPanel {
    private BufferedImage backgroundImage;
    private Map<String, Point> nodePositions; // 노드 ID와 위치를 저장하는 맵
    private Map<String, JButton> nodeButtons; // 노드 ID와 버튼을 저장하는 맵
    private Map<String, JButton> pieceButtons; // 말 ID와 버튼을 저장하는 맵
    private NodeClickListener nodeClickListener; // 노드 클릭 리스너
    private JButton restartButton; // 재시작 버튼
    private JButton quitButton; // 종료 버튼
    private JButton customChoiceButton; // 커스텀 선택 버튼
    private JButton yutThrowButton; // 윷던지기 버튼
    private GameRestartListener gameRestartListener; // 게임 재시작 리스너
    private GameQuitListener gameQuitListener; // 게임 종료 리스너
    private CustomChoiceListener customChoiceListener; // 커스텀 선택 리스너
    private YutThrowListener yutThrowListener; // 윷던지기 리스너
    private JLabel turnImageLabel; // 턴 이미지 라벨
    private int currentPlayerTurn = 1; // 현재 플레이어 턴 (기본 값: 1)
    private JLabel player1ScoreLabel; // 플레이어 1 점수 라벨
    private JLabel player2ScoreLabel; // 플레이어 2 점수 라벨
    private JLabel player3ScoreLabel; // 플레이어 3 점수 라벨
    private JLabel player4ScoreLabel; // 플레이어 4 점수 라벨
    private int player1RemainingPieces = 5; // 플레이어 1 남은 말 개수 (기본값: 5)
    private int player2RemainingPieces = 5; // 플레이어 2 남은 말 개수 (기본값: 5)
    private int player3RemainingPieces = 5; // 플레이어 3 남은 말 개수 (기본값: 5)
    private int player4RemainingPieces = 5; // 플레이어 4 남은 말 개수 (기본값: 5)
    private JLabel customPopupLabel; // 커스텀 윷 선택 팝업 레이블
    private boolean isCustomPopupVisible = false; // 커스텀 팝업 표시 여부
    private int selectedCustomYut = -1; // 선택된 커스텀 윷 값 (-1: 선택 안됨)
    private JLabel yutResultLabel; // 윷 결과 이미지 라벨
    private int currentYutResult = -1; // 현재 윷 결과 (-1: 없음, 0: 빽도, 1: 도, 2: 개, 3: 걸, 4: 윷, 5: 모)

    // 초기 말 위치 및 정보 저장
    private Map<String, InitialPieceInfo> initialPieceInfo = new HashMap<>();

    public BoardPanel(String boardImageName) {
        setPreferredSize(new Dimension(700, 700));
        setLayout(null); // 좌표 기반 배치

        loadBackgroundImage(boardImageName);
        initializeNodePositions();
        createNodeButtons();
        createGameButtons(); // 게임 버튼들(Custom Choice, Restart, Quit) 생성
        createTurnImage(); // 턴 이미지 생성
        createPlayerScoreLabels(); // 플레이어 점수 라벨 생성
        createYutResultLabel(); // 윷 결과 이미지 라벨 생성
        
        // 키 이벤트 리스너 추가 (Esc로 팝업 닫기)
        setFocusable(true); // 키 이벤트를 받기 위해 필요
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && isCustomPopupVisible) {
                    hideCustomPopup();
                }
            }
        });
        
        // 패널 클릭 이벤트 (팝업 외부 클릭시 닫기)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isCustomPopupVisible && customPopupLabel != null) {
                    // 팝업 영역 계산
                    Rectangle popupBounds = customPopupLabel.getBounds();
                    // 클릭 위치가 팝업 영역 밖이면 팝업 닫기
                    if (!popupBounds.contains(e.getPoint())) {
                        hideCustomPopup();
                    }
                }
            }
        });
        
        // 테스트용 말 추가 (실제로는 외부에서 addPiece 메서드로 추가함)
        pieceButtons = new HashMap<>();
    }
    
    // 게임 버튼 생성 (Custom Choice, Restart, Quit)
    private void createGameButtons() {
        createRestartButton();
        createQuitButton();
        createCustomChoiceButton();
        createYutThrowButton();
    }
    
    // 윷던지기 버튼 생성
    private void createYutThrowButton() {
        yutThrowButton = createImageButton(
            "src/data/Button/button_throw_up.png",
            "src/data/Button/button_throw_down.png",
            473, 473, 207, 93
        );
        
        // 버튼 클릭 이벤트 처리
        yutThrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yutThrowListener != null) { 
                    int result = yutThrowListener.onYutThrow();
                    showYutResult(result); // 윷 결과 표시
                }
            }
        });
        
        add(yutThrowButton);
    }

    // 윷 결과 이미지 라벨 생성
    private void createYutResultLabel() {
        yutResultLabel = new JLabel();
        yutResultLabel.setBounds(20, 472, 433, 207); // 지정된 위치에 표시
        add(yutResultLabel);
        hideYutResult(); // 초기에는 숨김
    }
    
    // 윷 결과 표시 메서드
    public void showYutResult(int yutResult) {
        currentYutResult = yutResult;
        
        try {
            // 윷 결과에 따른 이미지 파일 경로 결정
            String imagePath;
            switch (yutResult) {
                case 0:
                    imagePath = "src/data/yut/yut_1.png"; // 도
                    break;
                case 1:
                    imagePath = "src/data/yut/yut_2.png"; // 개
                    break;
                case 2:
                    imagePath = "src/data/yut/yut_3.png"; // 걸
                    break;
                case 3:
                    imagePath = "src/data/yut/yut_4.png"; // 윷
                    break;
                case 4:
                    imagePath = "src/data/yut/yut_5.png"; // 모
                    break;
                case 5:
                    imagePath = "src/data/yut/yut_0.png"; // 빽도
                    break;
                default:
                    hideYutResult();
                    return;
            }
            
            File imageFile = getImageFile(imagePath);
            if (imageFile.exists()) {
                BufferedImage yutImage = ImageIO.read(imageFile);
                
                // 이미지를 적절한 크기로 리사이징
                int originalWidth = yutImage.getWidth();
                int originalHeight = yutImage.getHeight();
                Image resizedImage = yutImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                
                yutResultLabel.setIcon(new ImageIcon(resizedImage));
                yutResultLabel.setVisible(true);
            } else {
                // 이미지 파일이 없는 경우 텍스트로 표시
                yutResultLabel.setIcon(null);
                String[] yutNames = {"도", "개", "걸", "윷", "모", "빽도"};
                yutResultLabel.setText(yutNames[yutResult]);
                yutResultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
                yutResultLabel.setForeground(Color.BLACK);
                yutResultLabel.setVisible(true);
            }
        } catch (Exception e) {
            System.err.println("윷 결과 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 예외 발생 시 텍스트로 표시
            yutResultLabel.setIcon(null);
            String[] yutNames = {"도", "개", "걸", "윷", "모", "빽도"};
            if (yutResult >= 0 && yutResult < yutNames.length) {
                yutResultLabel.setText(yutNames[yutResult]);
            } else {
                yutResultLabel.setText("알 수 없음");
            }
            yutResultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
            yutResultLabel.setForeground(Color.BLACK);
            yutResultLabel.setVisible(true);
        }
    }
    
    // 윷 결과 숨기기 메서드
    public void hideYutResult() {
        yutResultLabel.setIcon(null);
        yutResultLabel.setText("");
        yutResultLabel.setVisible(false);
        currentYutResult = -1;
    }
    
    // 현재 윷 결과 반환 메서드
    public int getCurrentYutResult() {
        return currentYutResult;
    }

    // 커스텀 선택 버튼 생성
    private void createCustomChoiceButton() {
        customChoiceButton = createImageButton(
            "src/data/Button/button_custom_up.png", 
            "src/data/Button/button_custom_down.png",
            473, 587, 207, 36
        );
        
        // 버튼 클릭 이벤트 처리
        customChoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customChoiceListener != null) {
                    customChoiceListener.onCustomChoice();
                }
                showCustomPopup(); // 커스텀 윷 선택 팝업 표시
            }
        });
        
        add(customChoiceButton);
    }

    // 재시작 버튼 생성
    private void createRestartButton() {
        restartButton = createImageButton(
            "src/data/Button/button_restart_up.png", 
            "src/data/Button/button_restart_down.png",
            473, 644, 93, 36
        );
        
        // 버튼 클릭 이벤트 처리
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        
        add(restartButton);
    }
    
    // 종료 버튼 생성
    private void createQuitButton() {
        quitButton = createImageButton(
            "src/data/Button/button_quit_up.png", 
            "src/data/Button/button_quit_down.png",
            584, 644, 93, 36
        );
        
        // 버튼 클릭 이벤트 처리
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameQuitListener != null) {
                    gameQuitListener.onGameQuit();
                }
            }
        });
        
        add(quitButton);
    }
    
    // 이미지 파일 경로 처리 유틸리티 메서드
    private File getImageFile(String imagePath) {
        // 절대 경로인지 확인
        File file = new File(imagePath);
        if (file.isAbsolute() && file.exists()) {
            return file;
        }
        
        // 프로젝트 루트 경로 확인
        String projectRoot = System.getProperty("user.dir");
        
        // 다양한 가능한 경로 조합 시도
        String[] possiblePaths = {
            imagePath,                                // 있는 그대로
            projectRoot + "/" + imagePath,            // 프로젝트 루트 + 경로
            projectRoot + File.separator + imagePath, // OS에 맞는 구분자 사용
            imagePath.replace("src/data/", "src/data/") // 중복 경로 제거
        };
        
        for (String path : possiblePaths) {
            file = new File(path);
            if (file.exists()) {
                System.out.println("이미지 파일 발견: " + file.getAbsolutePath());
                return file;
            }
        }
        
        // 마지막 시도 - src/data/ 접두사가 없는 경우 추가
        if (!imagePath.startsWith("src/data/")) {
            String pathWithPrefix = "src/data/" + imagePath;
            file = new File(projectRoot + File.separator + pathWithPrefix);
            if (file.exists()) {
                System.out.println("이미지 파일 발견: " + file.getAbsolutePath());
                return file;
            }
        }
        
        System.err.println("이미지 파일을 찾을 수 없음: " + imagePath);
        return file; // 파일을 찾지 못함
    }
    
    // 배경 이미지 로드 및 리사이징
    private void loadBackgroundImage(String boardImageName) {
        try {
            File imageFile = null;
            
            // 경로가 포함된 경우 그대로 시도
            if (boardImageName.contains("/") || boardImageName.contains("\\")) {
                imageFile = getImageFile(boardImageName);
            } else {
                // 1. 일반 파일명만 있는 경우 -> src/data/board/ 디렉토리에서 찾기
                imageFile = getImageFile("src/data/board/" + boardImageName);
                
                // 2. 찾지 못한 경우 -> board/ 디렉토리에서 찾기
                if (!imageFile.exists()) {
                    imageFile = getImageFile("board/" + boardImageName);
                }
                
                // 3. 찾지 못한 경우 -> 파일 이름 그대로 찾기
                if (!imageFile.exists()) {
                    imageFile = getImageFile(boardImageName);
                }
            }
            
            if (imageFile.exists()) {
                System.out.println("배경 이미지 로드: " + imageFile.getAbsolutePath());
                BufferedImage rawImage = ImageIO.read(imageFile);
                backgroundImage = resizeImage(rawImage, 700, 700);
            } else {
                System.err.println("배경 이미지 파일을 찾을 수 없습니다: " + boardImageName);
                // 배경이 없어도 패널은 표시되도록 빈 이미지 생성
                backgroundImage = new BufferedImage(700, 700, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = backgroundImage.createGraphics();
                g2d.setColor(new Color(240, 240, 240)); // 연한 회색 배경
                g2d.fillRect(0, 0, 700, 700);
                g2d.dispose();
            }
        } catch (Exception e) {
            System.err.println("배경 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 예외 발생해도 패널은 표시되도록 빈 이미지 생성
            backgroundImage = new BufferedImage(700, 700, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(new Color(240, 240, 240)); // 연한 회색 배경
            g2d.fillRect(0, 0, 700, 700);
            g2d.dispose();
        }
    }
    
    // 이미지 버튼 생성 유틸리티 메서드 (일반 이미지와 호버 이미지 지정)
    private JButton createImageButton(String normalImagePath, String hoverImagePath, int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        try {
            File normalFile = getImageFile(normalImagePath);
            File hoverFile = getImageFile(hoverImagePath);
            
            if (!normalFile.exists()) {
                System.err.println("일반 버튼 이미지 파일을 찾을 수 없습니다: " + normalImagePath);
                button.setText("Button"); // 이미지 로드 실패시 텍스트 표시
                return button;
            }
            
            if (!hoverFile.exists()) {
                System.err.println("호버 버튼 이미지 파일을 찾을 수 없습니다: " + hoverImagePath);
                hoverFile = normalFile; // 호버 이미지 없으면 일반 이미지로 대체
            }
            
            // 기본 이미지와 호버 이미지 로드
            final BufferedImage normalImage = ImageIO.read(normalFile);
            final BufferedImage hoverImage = ImageIO.read(hoverFile);
            
            // 버튼 이미지 리사이징
            Image normalResized = normalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            Image hoverResized = hoverImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            
            // 버튼 이미지 설정
            final ImageIcon normalIcon = new ImageIcon(normalResized);
            final ImageIcon hoverIcon = new ImageIcon(hoverResized);
            
            button.setIcon(normalIcon);
            
            // 호버 효과 추가
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setIcon(hoverIcon);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setIcon(normalIcon);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        } catch (Exception e) {
            System.err.println("버튼 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            button.setText("Button"); // 이미지 로드 실패시 텍스트 표시
        }
        
        return button;
    }
    
    // 게임 종료 리스너 인터페이스
    public interface GameQuitListener {
        void onGameQuit();
    }
    
    // 게임 종료 리스너 설정
    public void setGameQuitListener(GameQuitListener listener) {
        this.gameQuitListener = listener;
    }

    // 윷던지기 리스너 인터페이스
    public interface YutThrowListener {
        int onYutThrow(); // 던진 윷 결과 반환
    }
    
    // 윷던지기 리스너 설정
    public void setYutThrowListener(YutThrowListener listener) {
        this.yutThrowListener = listener;
    }

    // 커스텀 선택 리스너 인터페이스
    public interface CustomChoiceListener {
        void onCustomChoice();
        void onCustomYutSelected(int selection);
    }
    
    // 커스텀 선택 리스너 설정
    public void setCustomChoiceListener(CustomChoiceListener listener) {
        this.customChoiceListener = listener;
    }
    
    // 게임 재시작 리스너 인터페이스
    public interface GameRestartListener {
        void onGameRestart();
    }
    
    // 게임 재시작 리스너 설정
    public void setGameRestartListener(GameRestartListener listener) {
        this.gameRestartListener = listener;
    }
    
    // 초기 말 정보 저장 클래스
    private class InitialPieceInfo {
        String imagePath;
        String nodeId;
        Point position;
        int playerNumber;
        boolean usePosition; // 위치 사용 여부 (nodeId 대신 x,y 좌표 사용)
        
        // 노드 기반 위치 저장
        InitialPieceInfo(String imagePath, String nodeId, int playerNumber) {
            this.imagePath = imagePath;
            this.nodeId = nodeId;
            this.playerNumber = playerNumber;
            this.usePosition = false;
        }
        
        // 좌표 기반 위치 저장
        InitialPieceInfo(String imagePath, Point position, int playerNumber) {
            this.imagePath = imagePath;
            this.position = position;
            this.playerNumber = playerNumber;
            this.usePosition = true;
        }
    }

    // 노드 위치 초기화 (보드 이미지에 맞게 좌표 설정)
    private void initializeNodePositions() {
        nodePositions = new HashMap<>();
        
        // 가장자리 노드들 (시계 방향)
        nodePositions.put("n1", new Point(432, 354)); 
        nodePositions.put("n2", new Point(432, 276)); 
        nodePositions.put("n3", new Point(432, 198)); 
        nodePositions.put("n4", new Point(432, 120));
        nodePositions.put("n5", new Point(432, 42));
        
        nodePositions.put("n6", new Point(354, 42));   
        nodePositions.put("n7", new Point(276, 42));  
        nodePositions.put("n8", new Point(198, 42));    
        nodePositions.put("n9", new Point(120, 42));
        nodePositions.put("n10", new Point(42, 42));   

        nodePositions.put("n11", new Point(42, 120)); 
        nodePositions.put("n12", new Point(42, 198));    
        nodePositions.put("n13", new Point(42, 276));    
        nodePositions.put("n14", new Point(42, 354));      
        nodePositions.put("n15", new Point(42, 432));   
        
        nodePositions.put("n16", new Point(120, 432));   
        nodePositions.put("n17", new Point(198, 432));  
        nodePositions.put("n18", new Point(276, 432)); 
        nodePositions.put("n19", new Point(354, 432));
        nodePositions.put("n20", new Point(432, 432));
         
        nodePositions.put("n21", new Point(365, 109));  
        nodePositions.put("n22", new Point(303, 171));   
        nodePositions.put("n23", new Point(171, 303));  
        nodePositions.put("n24", new Point(108, 366));   

        nodePositions.put("n25", new Point(237, 237));

        nodePositions.put("n26", new Point(109, 108));  
        nodePositions.put("n27", new Point(170, 171));
        nodePositions.put("n28", new Point(303, 303));  
        nodePositions.put("n29", new Point(365, 365));
    }
    
    // 노드 버튼 생성
    private void createNodeButtons() {
        nodeButtons = new HashMap<>();
        
        // 각 노드 위치에 버튼 생성
        for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
            String nodeId = entry.getKey();
            Point position = entry.getValue();
            
            // 원형 버튼 생성
            JButton nodeButton = new JButton();
            int buttonSize = 24; // 버튼 크기
            
            nodeButton.setBounds(position.x - buttonSize/2, position.y - buttonSize/2, buttonSize, buttonSize);
            nodeButton.setOpaque(true);
            nodeButton.setContentAreaFilled(true);
            nodeButton.setBorderPainted(false);
            nodeButton.setBackground(new Color(255, 255, 255, 70)); // 반투명 흰색
            
            // 원형 모양 설정 (내부 클래스 대신 직접 페인트 로직 구현)
            nodeButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            nodeButton.setContentAreaFilled(false); // 원형 모양을 위해 내부 채우기 비활성화
            
            // 원 그리기를 위한 paintComponent 오버라이드
            nodeButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    if (getModel().isArmed()) {
                        g.setColor(getBackground().darker());
                    } else if (getModel().isRollover()) {
                        // 호버 상태일 때 밝은 색상으로 변경
                        g.setColor(getBackground().brighter());
                    } else {
                        g.setColor(getBackground());
                    }
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
                    super.paintComponent(g);
                }
                
                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isRollover()) {
                        // 호버 상태일 때 테두리 색상 변경
                        g2d.setColor(Color.WHITE);
                        g2d.setStroke(new BasicStroke(2));
                    } else {
                        g2d.setColor(Color.GRAY);
                    }
                    
                    g2d.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
                }
                
                // 원 모양 클릭을 위한 히트 감지
                @Override
                public boolean contains(int x, int y) {
                    if (shape == null || !shape.getBounds().equals(getBounds())) {
                        shape = new java.awt.geom.Ellipse2D.Float(0, 0, getWidth(), getHeight());
                    }
                    return shape.contains(x, y);
                }
                
                private Shape shape;
            };
            
            nodeButton.setBounds(position.x - buttonSize/2, position.y - buttonSize/2, buttonSize, buttonSize);
            nodeButton.setOpaque(false);
            nodeButton.setContentAreaFilled(false);
            nodeButton.setBorderPainted(false);
            nodeButton.setBackground(new Color(255, 255, 255, 70)); // 반투명 흰색
            
            // 롤오버(호버) 활성화
            nodeButton.setRolloverEnabled(true);
            
            // 버튼에 노드 ID 저장 (나중에 식별용)
            nodeButton.setName(nodeId);
            
            // 버튼 클릭 이벤트 설정
            nodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String clickedNodeId = button.getName();
                    
                    // 노드 클릭 리스너가 있으면 호출
                    if (nodeClickListener != null) {
                        nodeClickListener.onNodeClicked(clickedNodeId);
                    }
                    
                    System.out.println("노드 클릭: " + clickedNodeId); // 디버깅용
                }
            });
            
            // 노드 버튼 기본적으로 숨기기
            nodeButton.setVisible(false);
            
            // 버튼 추가
            add(nodeButton);
            nodeButtons.put(nodeId, nodeButton);
        }
    }
    
    // 노드 버튼 가시성 설정
    public void setNodeButtonsVisible(boolean visible) {
        for (JButton button : nodeButtons.values()) {
            button.setVisible(visible);
        }
        repaint();
    }
    
    // 노드 클릭 리스너 설정
    public void setNodeClickListener(NodeClickListener listener) {
        this.nodeClickListener = listener;
    }
    
    // 노드 클릭 인터페이스
    public interface NodeClickListener {
        void onNodeClicked(String nodeId);
    }

    // 이미지 리사이징
    private BufferedImage resizeImage(BufferedImage original, int width, int height) {
        Image tmp = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    // 이미지 컬러 변경 메서드
    private BufferedImage changeImageColor(BufferedImage image, Color color) {
        BufferedImage coloredImage = new BufferedImage(
                image.getWidth(), 
                image.getHeight(), 
                BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = coloredImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        
        // 컬러 오버레이 적용
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();
        
        return coloredImage;
    }

    // 외부에서 말 추가 메서드
    public void addPiece(String pieceId, String imagePath, String initialNodeId) {
        try {
            JButton pieceButton;
            Color playerColor = Color.RED; // 기본 색상
            
            // 플레이어 ID에 따라 다른 색상 지정
            if (pieceId.contains("1")) {
                playerColor = new Color(255, 60, 60); // 빨간색
            } else if (pieceId.contains("2")) {
                playerColor = new Color(80, 80, 255); // 파란색
            } else if (pieceId.contains("3")) {
                playerColor = new Color(60, 200, 60); // 녹색
            } else if (pieceId.contains("4")) {
                playerColor = new Color(255, 255, 60); // 노랑
            }
            
            // 초기 말 정보 저장
            int playerNum = Integer.parseInt(pieceId.substring(pieceId.indexOf("player") + 6, pieceId.indexOf("_")));
            initialPieceInfo.put(pieceId, new InitialPieceInfo(imagePath, initialNodeId, playerNum));
            
            // src/data 폴더에서 이미지 로드 (경로 수정)
            String currentDir = System.getProperty("user.dir");
            File imageFile = new File(currentDir + "/src/data/board/" + imagePath);
            
            if (imageFile.exists()) {
                // 이미지가 있으면 이미지로 버튼 생성
                BufferedImage originalImg = ImageIO.read(imageFile);
                BufferedImage coloredImg = changeImageColor(originalImg, playerColor);
                
                // 이미지 리사이징
                Image scaledImg = coloredImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImg);
                
                pieceButton = new JButton(icon);
                pieceButton.setBackground(new Color(0, 0, 0, 0)); // 투명 배경
            } else {
                // 이미지가 없으면 색상 원형 버튼으로 대체
                System.err.println("이미지 파일을 찾을 수 없습니다: " + imageFile.getAbsolutePath());
                pieceButton = new JButton();
                pieceButton.setOpaque(true);
                pieceButton.setBackground(playerColor);
            }
            
            pieceButton.setBorderPainted(false);
            pieceButton.setContentAreaFilled(true);
            pieceButton.setFocusPainted(false);
            
            // 호버 효과 추가
            final Color finalPlayerColor = playerColor;
            pieceButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    button.setBorder(null);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            
            // 노드 위치에 말 배치
            if (nodePositions.containsKey(initialNodeId)) {
                Point position = nodePositions.get(initialNodeId);
                pieceButton.setBounds(position.x - 20, position.y - 20, 40, 40); // 중앙에 위치하도록 조정
                add(pieceButton);
                pieceButtons.put(pieceId, pieceButton);
                // 말 버튼에 ID 설정
                pieceButton.setName(pieceId);
            } else {
                System.err.println("노드 ID가 유효하지 않습니다: " + initialNodeId);
            }
        } catch (Exception e) {
            System.err.println("말 생성 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 말 버튼 가져오기
    public JButton getPieceButton(String pieceId) {
        return pieceButtons.get(pieceId);
    }
    
    // 말 이동 메서드
    public void movePiece(String pieceId, String nodeId) {
        JButton pieceButton = pieceButtons.get(pieceId);
        Point position = nodePositions.get(nodeId);
        
        if (pieceButton != null && position != null) {
            pieceButton.setBounds(position.x - 20, position.y - 20, 40, 40);
            repaint();
        }
    }
    
    // 가능한 이동 위치 표시 메서드
    public void highlightNodes(String[] nodeIds) {
        // 모든 노드 버튼 원래 색상으로 초기화
        for (JButton button : nodeButtons.values()) {
            button.setBackground(new Color(255, 255, 255, 70)); // 반투명 흰색
        }
        
        // 선택된 노드만 하이라이트
        for (String nodeId : nodeIds) {
            JButton nodeButton = nodeButtons.get(nodeId);
            if (nodeButton != null) {
                nodeButton.setBackground(new Color(255, 255, 0, 150)); // 반투명 노란색
            }
        }
        repaint();
    }
    
    // 하이라이트 초기화 메서드
    private void resetHighlights() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component.getName() != null && component.getName().startsWith("highlight_")) {
                remove(component);
            }
        }
        repaint();
    }

    // 노드 위치 반환 메서드
    public Map<String, Point> getNodePositions() {
        return nodePositions;
    }
    
    // 노드 위치에 표시하는 메서드 (디버깅용)
    public void showNodePositions(boolean show) {
        // 기존 노드 마커 제거
        Component[] components = getComponents();
        for (Component component : components) {
            if (component.getName() != null && component.getName().startsWith("node_")) {
                remove(component);
            }
        }
        
        if (show) {
            // 모든 노드 위치에 작은 점 표시
            for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
                String nodeId = entry.getKey();
                Point position = entry.getValue();
                
                JLabel marker = new JLabel(nodeId);
                marker.setName("node_" + nodeId);
                marker.setForeground(Color.WHITE);
                marker.setFont(new Font("Arial", Font.BOLD, 10));
                marker.setBounds(position.x - 15, position.y - 15, 40, 20);
                add(marker);
            }
        }
        
        repaint();
    }

    // 모든 노드 하이라이트
    public void highlightAllNodes() {
        String[] allNodeIds = nodePositions.keySet().toArray(new String[0]);
        highlightNodes(allNodeIds);
    }

    // 지정된 위치에 말 추가 메서드
    public void addPieceAtPosition(String pieceId, String imagePath, int x, int y, int playerNumber) {
        try {
            JButton pieceButton;
            Color playerColor = Color.RED; // 기본 색상
            
            // 초기 말 정보 저장
            initialPieceInfo.put(pieceId, new InitialPieceInfo(imagePath, new Point(x, y), playerNumber));
            
            // 플레이어 번호에 따라 다른 색상 지정
            switch (playerNumber) {
                case 1:
                    playerColor = new Color(255, 60, 60); // 빨간색
                    break;
                case 2:
                    playerColor = new Color(80, 80, 255); // 파란색
                    break;
                case 3:
                    playerColor = new Color(60, 200, 60); // 녹색
                    break;
                case 4:
                    playerColor = new Color(255, 255, 60); // 노랑
                    break;
                default:
                    playerColor = Color.GRAY;
            }
            
            // src/data 폴더에서 이미지 로드 (경로 수정)
            String currentDir = System.getProperty("user.dir");
            File imageFile = new File(currentDir + "/src/data/board/" + imagePath);
            
            if (imageFile.exists()) {
                // 이미지가 있으면 이미지로 버튼 생성
                BufferedImage originalImg = ImageIO.read(imageFile);
                BufferedImage coloredImg = changeImageColor(originalImg, playerColor);
                
                // 이미지 리사이징
                Image scaledImg = coloredImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImg);
                
                pieceButton = new JButton(icon);
                pieceButton.setBackground(new Color(0, 0, 0, 0)); // 투명 배경
            } else {
                // 이미지가 없으면 색상 원형 버튼으로 대체
                System.err.println("이미지 파일을 찾을 수 없습니다: " + imageFile.getAbsolutePath());
                pieceButton = new JButton();
                pieceButton.setOpaque(true);
                pieceButton.setBackground(playerColor);
            }
            
            pieceButton.setBorderPainted(false);
            pieceButton.setContentAreaFilled(true);
            pieceButton.setFocusPainted(false);
            
            // 호버 효과 추가
            final Color finalPlayerColor = playerColor;
            pieceButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    // 테두리 추가하고 약간 밝게 변경
                    button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    button.setBorder(null);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            
            // 지정된 위치에 말 배치
            pieceButton.setBounds(x - 20, y - 20, 40, 40); // 중앙에 위치하도록 조정
            add(pieceButton);
            pieceButtons.put(pieceId, pieceButton);
            // 말 버튼에 ID 설정
            pieceButton.setName(pieceId);
            
        } catch (Exception e) {
            System.err.println("말 생성 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 게임 재시작 메서드
    public void restartGame() {
        // 모든 말 제거
        for (JButton pieceButton : pieceButtons.values()) {
            remove(pieceButton);
        }
        pieceButtons.clear();
        
        // 모든 노드 하이라이트 제거
        resetHighlights();
        
        // 노드 버튼 숨기기
        setNodeButtonsVisible(false);
        
        // 초기 말 정보로 말 다시 생성
        restorePieces();
        
        // 점수 초기화 추가
        resetPlayerScores();
        
        // 패널 다시 그리기
        repaint();
        
        // 게임 재시작 리스너가 설정되어 있으면 콜백 호출
        if (gameRestartListener != null) {
            gameRestartListener.onGameRestart();
        }
        
        System.out.println("게임이 초기화되었습니다.");
    }
    
    // 초기 말 복원
    private void restorePieces() {
        for (Map.Entry<String, InitialPieceInfo> entry : initialPieceInfo.entrySet()) {
            String pieceId = entry.getKey();
            InitialPieceInfo info = entry.getValue();
            
            if (info.usePosition) {
                // 좌표 기반 말 생성
                addPieceAtPosition(pieceId, info.imagePath, info.position.x, info.position.y, info.playerNumber);
            } else {
                // 노드 기반 말 생성
                addPiece(pieceId, info.imagePath, info.nodeId);
            }
        }
    }

    // Model 데이터를 받아 게임판 업데이트
    public void updateFromModel(Map<String, List<String>> playerPieces, Map<String, String> piecePositions) {
        // 기존 말들 모두 제거
        for (JButton pieceButton : pieceButtons.values()) {
            remove(pieceButton);
        }
        pieceButtons.clear();
        
        // Model 데이터로부터 말 생성 및 배치
        for (Map.Entry<String, List<String>> entry : playerPieces.entrySet()) {
            String playerId = entry.getKey();
            List<String> pieces = entry.getValue();
            int playerNum = Integer.parseInt(playerId.replace("player", ""));
            
            for (String pieceId : pieces) {
                String nodeId = piecePositions.get(pieceId);
                if (nodeId != null) {
                    addPiece(pieceId, "horse.png", nodeId);
                }
            }
        }
        
        repaint();
    }
    
    // 이동 가능한 노드 목록 업데이트
    public void updateMovableNodes(List<String> movableNodeIds) {
        if (movableNodeIds == null || movableNodeIds.isEmpty()) {
            setNodeButtonsVisible(false);
            return;
        }
        
        // 모든 노드 버튼 원래 색상으로 초기화
        for (JButton button : nodeButtons.values()) {
            button.setBackground(new Color(255, 255, 255, 70)); // 반투명 흰색
            button.setVisible(false);
        }
        
        // 이동 가능한 노드만 하이라이트하고 보이게 설정
        for (String nodeId : movableNodeIds) {
            JButton nodeButton = nodeButtons.get(nodeId);
            if (nodeButton != null) {
                nodeButton.setBackground(new Color(255, 255, 0, 150)); // 반투명 노란색
                nodeButton.setVisible(true);
            }
        }
        
        repaint();
    }
    
    // 게임 상태 완전 초기화 (Model로부터 새로운 게임 시작)
    public void resetGameState() {
        // 모든 말 제거
        for (JButton pieceButton : pieceButtons.values()) {
            remove(pieceButton);
        }
        pieceButtons.clear();
        
        // 모든 노드 하이라이트 제거
        resetHighlights();
        
        // 노드 버튼 숨기기
        setNodeButtonsVisible(false);
        
        // 패널 다시 그리기
        repaint();
    }

    // 턴 이미지 생성 메서드
    private void createTurnImage() {
        turnImageLabel = new JLabel();
        turnImageLabel.setBounds(485, 392, 183, 30); // 지정된 절대 좌표에 위치
        updateTurnImage(1); // 초기 이미지는 플레이어 1
        add(turnImageLabel);
    }

    // 턴 이미지 업데이트 메서드
    public void updateTurnImage(int playerTurn) {
        currentPlayerTurn = playerTurn;
        try {
            String imagePath;
            // 플레이어 번호에 따라 적절한 이미지 선택
            switch (playerTurn) {
                case 1:
                    imagePath = "src/data/Turn/turn_1.png";
                    break;
                case 2:
                    imagePath = "src/data/Turn/turn_2.png";
                    break;
                case 3:
                    imagePath = "src/data/Turn/turn_3.png";
                    break;
                case 4:
                    imagePath = "src/data/Turn/turn_4.png";
                    break;
                default:
                    imagePath = "src/data/Turn/turn_1.png"; // 기본값
                    break;
            }
            
            File imageFile = getImageFile(imagePath);
            if (imageFile.exists()) {
                BufferedImage turnImage = ImageIO.read(imageFile);
                // 이미지 원래 크기 확인
                int originalWidth = turnImage.getWidth();
                int originalHeight = turnImage.getHeight();
                // 이미지를 3분의 1 크기로 리사이징
                Image resizedImage = turnImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                turnImageLabel.setIcon(new ImageIcon(resizedImage));
            } else {
                // 이미지가 없는 경우 텍스트로 대체
                turnImageLabel.setIcon(null);
                turnImageLabel.setText("Player " + playerTurn + "'s Turn");
                turnImageLabel.setFont(new Font("Arial", Font.BOLD, 16));
                turnImageLabel.setForeground(Color.BLACK);
            }
        } catch (Exception e) {
            System.err.println("턴 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 예외 발생 시 텍스트로 대체
            turnImageLabel.setIcon(null);
            turnImageLabel.setText("Player " + playerTurn + "'s Turn");
            turnImageLabel.setFont(new Font("Arial", Font.BOLD, 16));
            turnImageLabel.setForeground(Color.BLACK);
        }
    }
    
    // 승리 이미지 표시 메서드
    public void showWinnerImage(int playerNumber) {
        try {
            String imagePath;
            // 플레이어 번호에 따라 적절한 승리 이미지 선택
            switch (playerNumber) {
                case 1:
                    imagePath = "src/data/Turn/winner_1.png";
                    break;
                case 2:
                    imagePath = "src/data/Turn/winner_2.png";
                    break;
                case 3:
                    imagePath = "src/data/Turn/winner_3.png";
                    break;
                case 4:
                    imagePath = "src/data/Turn/winner_4.png";
                    break;
                default:
                    imagePath = "src/data/Turn/winner_1.png"; // 기본값
                    break;
            }
            
            BufferedImage winnerImage = ImageIO.read(new File(imagePath));
            // 이미지 원래 크기 확인
            int originalWidth = winnerImage.getWidth();
            int originalHeight = winnerImage.getHeight();
            // 이미지를 3분의 1 크기로 리사이징
            Image resizedImage = winnerImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
            turnImageLabel.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            System.err.println("승리 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 현재 플레이어 업데이트 메서드 (GameView 인터페이스와는 관계 없음)
    public void updateCurrentPlayer(String playerID) {
        // playerID 형식이 "playerX"로 가정
        if (playerID != null && playerID.startsWith("player")) {
            try {
                int playerNum = Integer.parseInt(playerID.substring(6));
                updateTurnImage(playerNum);
            } catch (NumberFormatException e) {
                System.err.println("잘못된 플레이어 ID 형식: " + playerID);
            }
        }
    }

    // 플레이어 점수 라벨 생성 메서드
    private void createPlayerScoreLabels() {
        // 플레이어 1 점수 라벨 생성
        player1ScoreLabel = new JLabel();
        player1ScoreLabel.setBounds(485, 181, 183,19); // 지정된 절대 좌표에 위치
        updatePlayer1Score(player1RemainingPieces); // 초기 이미지 설정
        add(player1ScoreLabel);
        
        // 플레이어 2 점수 라벨 생성
        player2ScoreLabel = new JLabel();
        player2ScoreLabel.setBounds(485, 228, 183,19); // 플레이어 1 아래에 위치
        updatePlayer2Score(player2RemainingPieces); // 초기 이미지 설정
        add(player2ScoreLabel);
        
        // 플레이어 3 점수 라벨 생성 
        player3ScoreLabel = new JLabel();
        player3ScoreLabel.setBounds(485, 275, 183,19); // 플레이어 2 아래에 위치
        updatePlayer3Score(player3RemainingPieces); // 초기 이미지 설정
        add(player3ScoreLabel);
        
        // 플레이어 4 점수 라벨 생성 
        player4ScoreLabel = new JLabel();
        player4ScoreLabel.setBounds(485, 322, 183,19); // 플레이어 3 아래에 위치
        updatePlayer4Score(player4RemainingPieces); // 초기 이미지 설정
        add(player4ScoreLabel);
    }

    // 플레이어 1 점수 업데이트 메서드
    public void updatePlayer1Score(int remainingPieces) {
        if (remainingPieces < 0) remainingPieces = 0;
        if (remainingPieces > 5) remainingPieces = 5;
        
        player1RemainingPieces = remainingPieces;
        
        try {
            String imagePath = "src/data/Score/player1/player1_" + remainingPieces + ".png";
            File imageFile = getImageFile(imagePath);
            
            if (imageFile.exists()) {
                BufferedImage scoreImage = ImageIO.read(imageFile);
                // 이미지 원래 크기 확인
                int originalWidth = scoreImage.getWidth();
                int originalHeight = scoreImage.getHeight();
                // 이미지를 3분의 1 크기로 리사이징
                Image resizedImage = scoreImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                player1ScoreLabel.setIcon(new ImageIcon(resizedImage));
            } else {
                // 이미지가 없는 경우 텍스트로 대체
                player1ScoreLabel.setIcon(null);
                player1ScoreLabel.setText("Player 1: " + remainingPieces);
                player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
                player1ScoreLabel.setForeground(Color.RED);
            }
        } catch (Exception e) {
            System.err.println("플레이어 1 점수 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 예외 발생 시 텍스트로 대체
            player1ScoreLabel.setIcon(null);
            player1ScoreLabel.setText("Player 1: " + remainingPieces);
            player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
            player1ScoreLabel.setForeground(Color.RED);
        }
    }

    // 플레이어 2 점수 업데이트 메서드
    public void updatePlayer2Score(int remainingPieces) {
        if (remainingPieces < 0) remainingPieces = 0;
        if (remainingPieces > 5) remainingPieces = 5;
        
        player2RemainingPieces = remainingPieces;
        
        try {
            String imagePath = "src/data/Score/player2/player2_" + remainingPieces + ".png";
            File imageFile = getImageFile(imagePath);
            
            if (imageFile.exists()) {
                BufferedImage scoreImage = ImageIO.read(imageFile);
                // 이미지 원래 크기 확인
                int originalWidth = scoreImage.getWidth();
                int originalHeight = scoreImage.getHeight();
                // 이미지를 3분의 1 크기로 리사이징
                Image resizedImage = scoreImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                player2ScoreLabel.setIcon(new ImageIcon(resizedImage));
            } else {
                // 이미지가 없는 경우 텍스트로 대체
                player2ScoreLabel.setIcon(null);
                player2ScoreLabel.setText("Player 2: " + remainingPieces);
                player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
                player2ScoreLabel.setForeground(Color.BLUE);
            }
        } catch (Exception e) {
            System.err.println("플레이어 2 점수 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 예외 발생 시 텍스트로 대체
            player2ScoreLabel.setIcon(null);
            player2ScoreLabel.setText("Player 2: " + remainingPieces);
            player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
            player2ScoreLabel.setForeground(Color.BLUE);
        }
    }
    
    // 플레이어 3 점수 업데이트 메서드
    public void updatePlayer3Score(int remainingPieces) {
        if (remainingPieces < 0) remainingPieces = 0;
        if (remainingPieces > 5) remainingPieces = 5;
        
        player3RemainingPieces = remainingPieces;
        
        try {
            // 플레이어 3의 이미지 경로가 존재할 경우 사용
            String imagePath = "src/data/Score/player3/player3_" + remainingPieces + ".png";
            File imageFile = new File(imagePath);
            
            if (imageFile.exists()) {
                BufferedImage scoreImage = ImageIO.read(imageFile);
                // 이미지 원래 크기 확인
                int originalWidth = scoreImage.getWidth();
                int originalHeight = scoreImage.getHeight();
                // 이미지를 3분의 1 크기로 리사이징
                Image resizedImage = scoreImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                player3ScoreLabel.setIcon(new ImageIcon(resizedImage));
            } else {
                // 대체 이미지 사용 (없을 경우)
                player3ScoreLabel.setIcon(null);
                player3ScoreLabel.setText("Player 3: " + remainingPieces);
            }
        } catch (Exception e) {
            System.err.println("플레이어 3 점수 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            player3ScoreLabel.setIcon(null);
            player3ScoreLabel.setText("Player 3: " + remainingPieces);
        }
    }
    
    // 플레이어 4 점수 업데이트 메서드
    public void updatePlayer4Score(int remainingPieces) {
        if (remainingPieces < 0) remainingPieces = 0;
        if (remainingPieces > 5) remainingPieces = 5;
        
        player4RemainingPieces = remainingPieces;
        
        try {
            // 플레이어 4의 이미지 경로가 존재할 경우 사용
            String imagePath = "src/data/Score/player4/player4_" + remainingPieces + ".png";
            File imageFile = new File(imagePath);
            
            if (imageFile.exists()) {
                BufferedImage scoreImage = ImageIO.read(imageFile);
                // 이미지 원래 크기 확인
                int originalWidth = scoreImage.getWidth();
                int originalHeight = scoreImage.getHeight();
                // 이미지를 3분의 1 크기로 리사이징
                Image resizedImage = scoreImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                player4ScoreLabel.setIcon(new ImageIcon(resizedImage));
            } else {
                // 대체 이미지 사용 (없을 경우)
                player4ScoreLabel.setIcon(null);
                player4ScoreLabel.setText("Player 4: " + remainingPieces);
            }
        } catch (Exception e) {
            System.err.println("플레이어 4 점수 이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
            player4ScoreLabel.setIcon(null);
            player4ScoreLabel.setText("Player 4: " + remainingPieces);
        }
    }
    
    // 말이 도착점에 도달했을 때 호출하는 메서드
    public void decreasePlayerPieceCount(int playerNumber) {
        switch (playerNumber) {
            case 1:
                updatePlayer1Score(player1RemainingPieces - 1);
                break;
            case 2:
                updatePlayer2Score(player2RemainingPieces - 1);
                break;
            case 3:
                updatePlayer3Score(player3RemainingPieces - 1);
                break;
            case 4:
                updatePlayer4Score(player4RemainingPieces - 1);
                break;
        }
    }

    // 게임 재시작 시 점수 초기화 메서드
    public void resetPlayerScores() {
        updatePlayer1Score(5);
        updatePlayer2Score(5);
        updatePlayer3Score(5);
        updatePlayer4Score(5);
    }

    // 커스텀 윷 선택 팝업 표시
    private void showCustomPopup() {
        if (customPopupLabel == null) {
            customPopupLabel = new JLabel();
            customPopupLabel.setBounds(36, 445, 627, 117); // 지정된 위치에 표시
            add(customPopupLabel); // 먼저 추가 (getParent가 null이 되지 않도록)
            customPopupLabel.setVisible(false);
            
            try {
                File imageFile = getImageFile("src/data/custom/custom_steady.png");
                if (imageFile.exists()) {
                    BufferedImage steadyImage = ImageIO.read(imageFile);
                    
                    // 이미지를 3분의 1 크기로 리사이징
                    int originalWidth = steadyImage.getWidth();
                    int originalHeight = steadyImage.getHeight();
                    Image resizedImage = steadyImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                    
                    customPopupLabel.setIcon(new ImageIcon(resizedImage));
                    
                    // 마우스 움직임 감지를 위한 리스너 추가
                    customPopupLabel.addMouseMotionListener(new MouseMotionAdapter() {
                        @Override
                        public void mouseMoved(MouseEvent e) {
                            handleCustomPopupHover(e.getX(), e.getY());
                        }
                    });
                    
                    // 마우스 클릭 이벤트 처리 - 즉시 선택 및 팝업 닫기
                    customPopupLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int selection = getCustomSelectionFromPosition(e.getX(), e.getY());
                            if (selection >= 1 && selection <= 6) {
                                // 선택한 값 처리 후 팝업 닫기
                                handleCustomSelection(selection);
                                hideCustomPopup();
                            }
                        }
                        
                        @Override
                        public void mouseExited(MouseEvent e) {
                            // 마우스가 팝업을 벗어나면 기본 이미지로 복원
                            try {
                                File steadyFile = getImageFile("src/data/custom/custom_steady.png");
                                if (steadyFile.exists()) {
                                    BufferedImage steadyImage = ImageIO.read(steadyFile);
                                    
                                    // 이미지를 3분의 1 크기로 리사이징
                                    int originalWidth = steadyImage.getWidth();
                                    int originalHeight = steadyImage.getHeight();
                                    Image resizedImage = steadyImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                                    
                                    customPopupLabel.setIcon(new ImageIcon(resizedImage));
                                }
                            } catch (Exception ex) {
                                System.err.println("이미지 로드 실패: " + ex.getMessage());
                            }
                        }
                    });
                } else {
                    System.err.println("커스텀 팝업 이미지 파일을 찾을 수 없습니다");
                    customPopupLabel.setText("Custom Choice");
                }
            } catch (Exception e) {
                System.err.println("커스텀 팝업 이미지 로드 실패: " + e.getMessage());
                e.printStackTrace();
                customPopupLabel.setText("Custom Choice");
            }
        }
        
        customPopupLabel.setVisible(true);
        isCustomPopupVisible = true;
        
        // 팝업을 맨 앞으로 가져오기 (부모가 있을 때만 실행)
        if (customPopupLabel.getParent() != null) {
            customPopupLabel.getParent().setComponentZOrder(customPopupLabel, 0);
        }
        
        // 키보드 포커스 얻기 (Esc로 닫기 위함)
        requestFocusInWindow();
        
        repaint();
    }
    
    // 커스텀 팝업 숨기기
    private void hideCustomPopup() {
        if (customPopupLabel != null) {
            customPopupLabel.setVisible(false);
            isCustomPopupVisible = false;
            repaint();
        }
    }
    
    // 마우스 위치에 따른 호버 이미지 변경
    private void handleCustomPopupHover(int x, int y) {
        int selection = getCustomSelectionFromPosition(x, y);
        
        if (selection >= 1 && selection <= 6) {
            try {
                File imageFile = getImageFile("src/data/custom/custom_" + selection + ".png");
                if (imageFile.exists()) {
                    BufferedImage hoverImage = ImageIO.read(imageFile);
                    
                    // 이미지를 3분의 1 크기로 리사이징
                    int originalWidth = hoverImage.getWidth();
                    int originalHeight = hoverImage.getHeight();
                    Image resizedImage = hoverImage.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
                    
                    customPopupLabel.setIcon(new ImageIcon(resizedImage));
                } else {
                    System.err.println("호버 이미지 파일을 찾을 수 없습니다: " + selection);
                }
            } catch (Exception e) {
                System.err.println("호버 이미지 로드 실패: " + e.getMessage());
            }
        }
    }
    
    // 마우스 위치를 기반으로 선택값 반환 (1=도, 2=개, 3=걸, 4=윷, 5=모, 6=빽)
    private int getCustomSelectionFromPosition(int x, int y) {
        // 마우스 위치가 어떤 영역에 있는지 판단
        // 이미지를 6등분하여 각 영역에 대응하는 값 반환
        int width = customPopupLabel.getWidth();
        int sectionWidth = width / 6;
        
        // x 좌표가 어느 섹션에 속하는지 계산 (0부터 5까지의 인덱스로 변환)
        int sectionIndex = x / sectionWidth;
        
        // 범위를 벗어나는 경우 보정
        if (sectionIndex < 0) sectionIndex = 0;
        if (sectionIndex > 5) sectionIndex = 5;
        
        // 섹션 인덱스를 1~6 값으로 변환 (1=도, 2=개, 3=걸, 4=윷, 5=모, 6=빽)
        return sectionIndex + 1;
    }
    
    // 커스텀 선택 처리 메서드
    private void handleCustomSelection(int selection) {
        // 선택된 값 저장
        selectedCustomYut = selection;
        
        // 선택된 값에 따른 처리 로직
        System.out.println("선택된 윷 값: " + getYutNameFromSelection(selection));
        
        // 윷 결과 이미지 표시 (커스텀 선택 값에 따라 적절한 윷 결과로 매핑)
        // 선택 값(1~6)을 윷 결과(0~5)로 변환하는 매핑
        // 1(도) → 0, 2(개) → 1, 3(걸) → 2, 4(윷) → 3, 5(모) → 4, 6(빽도) → 5
        showYutResult(selection - 1);
        
        // 이벤트 리스너에게 선택 통보
        if (customChoiceListener != null) {
            customChoiceListener.onCustomYutSelected(selection);
        }
    }
    
    // 선택된 커스텀 윷 값 반환
    public int getSelectedCustomYut() {
        return selectedCustomYut;
    }
    
    // 선택된 커스텀 윷 이름 반환
    public String getSelectedCustomYutName() {
        return getYutNameFromSelection(selectedCustomYut);
    }
    
    // 선택값에 따른 윷 이름 반환
    private String getYutNameFromSelection(int selection) {
        switch (selection) {
            case 1: return "도";
            case 2: return "개";
            case 3: return "걸";
            case 4: return "윷";
            case 5: return "모";
            case 6: return "빽도";
            default: return "알 수 없음";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }
}
