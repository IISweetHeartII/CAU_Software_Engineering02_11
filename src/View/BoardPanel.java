package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class BoardPanel extends JPanel {
    private BufferedImage backgroundImage;
    private Map<String, Point> nodePositions; // 노드 ID와 위치를 저장하는 맵
    private Map<String, JButton> nodeButtons; // 노드 ID와 버튼을 저장하는 맵
    private Map<String, JButton> pieceButtons; // 말 ID와 버튼을 저장하는 맵
    private NodeClickListener nodeClickListener; // 노드 클릭 리스너

    public BoardPanel(String boardImageName) {
        setPreferredSize(new Dimension(700, 700));
        setLayout(null); // 좌표 기반 배치

        loadBackgroundImage(boardImageName);
        initializeNodePositions();
        createNodeButtons();
        
        // 테스트용 말 추가 (실제로는 외부에서 addPiece 메서드로 추가함)
        pieceButtons = new HashMap<>();
    }

    // 배경 이미지 로드 및 리사이징
    private void loadBackgroundImage(String boardImageName) {
        try {
            // 절대 경로 사용
            String currentDir = System.getProperty("user.dir");
            File imageFile = new File(currentDir + "/res/" + boardImageName);
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
        
        // 이미지에 표시된 순서대로 노드 번호 재설정
        // 가장자리 노드들 (시계 방향)
        nodePositions.put("n10", new Point(42, 42));       // 좌상단 모서리
        nodePositions.put("n11", new Point(42, 120));     // 왼쪽 변 위
        nodePositions.put("n12", new Point(42, 198));     // 왼쪽 변 중간
        nodePositions.put("n13", new Point(42, 276));      // 왼쪽 변 중간
        nodePositions.put("n14", new Point(42, 354));      // 왼쪽 변 아래
        nodePositions.put("n15", new Point(42, 432));      // 좌하단 모서리
        
        nodePositions.put("n16", new Point(120, 432));      // 오른쪽 상단
        nodePositions.put("n17", new Point(198, 432));     // 오른쪽 변 중간
        nodePositions.put("n18", new Point(276, 432));     // 오른쪽 변 중간
        nodePositions.put("n19", new Point(354, 432));    // 오른쪽 변 아래
        nodePositions.put("n20", new Point(432, 432));    // 우하단 모서리
        
        nodePositions.put("n1", new Point(432, 354));    // 왼쪽 대각선 중간
        nodePositions.put("n2", new Point(432, 276));    // 상단 대각선 중간
        nodePositions.put("n3", new Point(432, 198));    // 왼쪽 변
        nodePositions.put("n4", new Point(432, 120));    // 하단 왼쪽
        nodePositions.put("n5", new Point(432, 42));
        
        nodePositions.put("n6", new Point(354, 42));     // 상단
        nodePositions.put("n7", new Point(276, 42));    // 하단
        nodePositions.put("n8", new Point(198, 42));     // 우상단 모서리
        nodePositions.put("n9", new Point(120, 42));
             // 오른쪽 상단
        nodePositions.put("n26", new Point(109, 108));    // 오른쪽 하단
        nodePositions.put("n27", new Point(170, 171));
    // 오른쪽 대각선 하단
        nodePositions.put("n28", new Point(303, 303));    // 왼쪽 대각선 상단
        nodePositions.put("n29", new Point(365, 365));
        nodePositions.put("n25", new Point(237, 237));

        nodePositions.put("n24", new Point(108, 366));    // 중앙 우측
        nodePositions.put("n23", new Point(171, 303));    // 중앙
        nodePositions.put("n22", new Point(303, 171));     // 상단 중앙
        nodePositions.put("n21", new Point(365, 109));    // 하단 중앙
        
        // 시작 지점은 일반적으로 윷놀이에서 출발 지점으로 사용
        // nodePositions.put("start", new Point(238, 258));  // 중앙(n27과 동일)
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
                    g2d.setColor(Color.GRAY);
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
            
            // 이미지 파일 존재 확인 (절대 경로 사용)
            String currentDir = System.getProperty("user.dir");
            File imageFile = new File(currentDir + "/" + imagePath);
            
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
            
            // 이미지 파일 존재 확인 (절대 경로 사용)
            String currentDir = System.getProperty("user.dir");
            File imageFile = new File(currentDir + "/" + imagePath);
            
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }
}
