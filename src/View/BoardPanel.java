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
    private GameRestartListener gameRestartListener; // 게임 재시작 리스너
    
    // 초기 말 위치 및 정보 저장
    private Map<String, InitialPieceInfo> initialPieceInfo = new HashMap<>();

    public BoardPanel(String boardImageName) {
        setPreferredSize(new Dimension(700, 700));
        setLayout(null); // 좌표 기반 배치

        loadBackgroundImage(boardImageName);
        initializeNodePositions();
        createNodeButtons();
        createRestartButton();
        
        // 테스트용 말 추가 (실제로는 외부에서 addPiece 메서드로 추가함)
        pieceButtons = new HashMap<>();
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

    // 배경 이미지 로드 및 리사이징
    private void loadBackgroundImage(String boardImageName) {
        try {
            // src/data 폴더에서 이미지 로드 (경로 수정)
            String currentDir = System.getProperty("user.dir");
            File imageFile = new File(currentDir + "/src/data/" + boardImageName);
            if (imageFile.exists()) {
                BufferedImage rawImage = ImageIO.read(imageFile);
                backgroundImage = resizeImage(rawImage, 700, 700);
            } else {
                System.err.println("이미지 파일을 찾을 수 없습니다: " + imageFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
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
            File imageFile = new File(currentDir + "/src/data/" + imagePath);
            
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
            File imageFile = new File(currentDir + "/src/data/" + imagePath);
            
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

    // 재시작 버튼 생성
    private void createRestartButton() {
        restartButton = new JButton("게임 초기화");
        restartButton.setBounds(473, 643, 120, 30); // 지정된 좌표에 배치
        restartButton.setFocusPainted(false);
        restartButton.setBackground(new Color(220, 220, 220));
        restartButton.setForeground(Color.BLACK);
        restartButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        
        // 호버 효과 추가
        restartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                restartButton.setBackground(new Color(200, 200, 220));
                restartButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 180), 2),
                    BorderFactory.createEmptyBorder(3, 7, 3, 7)
                ));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                restartButton.setBackground(new Color(220, 220, 220));
                restartButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        // 버튼 스타일 설정
        restartButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        // 버튼 클릭 이벤트 처리
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        
        add(restartButton);
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
    
    // 게임 재시작 리스너 인터페이스
    public interface GameRestartListener {
        void onGameRestart();
    }
    
    // 게임 재시작 리스너 설정
    public void setGameRestartListener(GameRestartListener listener) {
        this.gameRestartListener = listener;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }
}
