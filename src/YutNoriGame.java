import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class YutNoriGame extends JFrame {
    private int currentPlayer = 1;
    private JLabel statusLabel;
    private JLabel resultLabel;
    private JLabel remainingLabel;
    private YutBoardPanel boardPanel;
    private final int MAX_PLAYERS = 2;
    private final int TOKENS_PER_PLAYER = 4;
    private final int[][] positions = new int[MAX_PLAYERS + 1][TOKENS_PER_PLAYER];
    private final int[] remainingTokens = new int[MAX_PLAYERS + 1];

    // Interactive move state
    private int lastSteps = 0;
    private boolean waitingForMove = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(YutNoriGame::new);
    }

    public YutNoriGame() {
        for (int p = 1; p <= MAX_PLAYERS; p++) {
            Arrays.fill(positions[p], -1);
            remainingTokens[p] = TOKENS_PER_PLAYER;
        }

        setTitle("Yutnori Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 780);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boardPanel = new YutBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JPanel control = new JPanel(new GridLayout(2, 1));
        JPanel topRow = new JPanel();
        statusLabel = new JLabel();
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        updateStatusLabel();

        JButton throwButton = new JButton("Throw Yut");
        resultLabel = new JLabel(" ");

        throwButton.addActionListener(e -> {
            if (!waitingForMove) {
                String res = throwYut();
                resultLabel.setText("Result: " + res);
                lastSteps = resultToSteps(res);
                waitingForMove = true;
                boardPanel.prepareHighlight(currentPlayer, lastSteps);
                statusLabel.setText("Player " + currentPlayer + ", select destination");
                setStatusColor(currentPlayer);
            }
        });

        topRow.add(statusLabel);
        topRow.add(throwButton);
        topRow.add(resultLabel);

        JPanel bottomRow = new JPanel();
        remainingLabel = new JLabel();
        updateRemainingLabel();
        bottomRow.add(remainingLabel);

        control.add(topRow);
        control.add(bottomRow);
        add(control, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateRemainingLabel() {
        remainingLabel.setText(String.format("Remaining - P1: %d, P2: %d", remainingTokens[1], remainingTokens[2]));
    }

    private void updateStatusLabel() {
        statusLabel.setText("Player " + currentPlayer + "'s turn");
        setStatusColor(currentPlayer);
    }

    private void setStatusColor(int player) {
        statusLabel.setForeground(player == 1 ? Color.RED : Color.BLUE);
    }

    private String throwYut() {
        int backs = 0;
        for (int i = 0; i < 4; i++) if (Math.random() < 0.5) backs++;
        switch (backs) {
            case 1: return "도";
            case 2: return "개";
            case 3: return "걸";
            case 4: return "윷";
            default: return "모";
        }
    }

    private int resultToSteps(String res) {
        switch (res) {
            case "도": return 1;
            case "개": return 2;
            case "걸": return 3;
            case "윷": return 4;
            case "모": return 5;
            default: return 0;
        }
    }

    private class YutBoardPanel extends JPanel {
        private List<Point> trackPoints = new ArrayList<>();
        private Set<Integer> highlightIndices = new HashSet<>();

        public YutBoardPanel() {
            MouseAdapter ma = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!waitingForMove) return;
                    Point click = e.getPoint();
                    int idx = closestTrackIndex(click);
                    if (highlightIndices.contains(idx)) {
                        performMove(idx);
                    }
                }
            };
            addMouseListener(ma);
        }

        public void prepareHighlight(int player, int steps) {
            highlightIndices.clear();
            buildTrackPoints();
            for (int t = 0; t < TOKENS_PER_PLAYER; t++) {
                int pos = positions[player][t];
                int dest = pos < 0 ? steps : pos + steps;
                if (dest >= 0 && dest < trackPoints.size()) {
                    highlightIndices.add(dest);
                }
            }
            repaint();
        }

        private void performMove(int dest) {
            int player = currentPlayer;
            for (int t = 0; t < TOKENS_PER_PLAYER; t++) {
                int pos = positions[player][t];
                int required = pos < 0 ? lastSteps : pos + lastSteps;
                if (required == dest) {
                    if (pos < 0) remainingTokens[player]--;
                    positions[player][t] = dest;
                    int opp = 3 - player;
                    for (int o = 0; o < TOKENS_PER_PLAYER; o++) {
                        if (positions[opp][o] == dest) {
                            positions[opp][o] = -1;
                            remainingTokens[opp]++;
                            statusLabel.setText("Player " + player + " 잡았다!");
                            setStatusColor(player);
                        }
                    }
                    int count = 0;
                    for (int x = 0; x < TOKENS_PER_PLAYER; x++) if (positions[player][x] == dest) count++;
                    if (count > 1) {
                        statusLabel.setText("Player " + player + " 업었다! (" + count + ")");
                        setStatusColor(player);
                    }
                    break;
                }
            }
            waitingForMove = false;
            lastSteps = 0;
            highlightIndices.clear();
            updateRemainingLabel();
            repaint();
            String txt = resultLabel.getText();
            if (!txt.contains("윷") && !txt.contains("모")) {
                currentPlayer = 3 - player;
                updateStatusLabel();
            }
        }

        private int closestTrackIndex(Point p) {
            int best = -1;
            double md = Double.MAX_VALUE;
            for (int i = 0; i < trackPoints.size(); i++) {
                double d = trackPoints.get(i).distance(p);
                if (d < md) { md = d; best = i; }
            }
            return best;
        }

        private void buildTrackPoints() {
            trackPoints.clear();
            int w = getWidth(), h = getHeight();
            int margin = Math.min(w, h) / 10;
            int boardSize = Math.min(w, h) - 2 * margin;
            int spacing = boardSize / 5;
            Point[] corners = new Point[] {
                    new Point(margin, margin),
                    new Point(margin + 5*spacing, margin),
                    new Point(margin + 5*spacing, margin+5*spacing),
                    new Point(margin, margin+5*spacing)
            };
            for (int j = 0; j <= 5; j++) {
                double t = j/5.0;
                int x = (int)(corners[2].x + (corners[1].x-corners[2].x)*t);
                int y = (int)(corners[2].y + (corners[1].y-corners[2].y)*t);
                trackPoints.add(new Point(x,y));
            }
            for (int j=1;j<=5;j++) {
                double t=j/5.0;
                int x=(int)(corners[1].x+(corners[0].x-corners[1].x)*t);
                int y=(int)(corners[1].y+(corners[0].y-corners[1].y)*t);
                trackPoints.add(new Point(x,y));
            }
            for (int j=1;j<=5;j++) {
                double t=j/5.0;
                int x=(int)(corners[0].x+(corners[3].x-corners[0].x)*t);
                int y=(int)(corners[0].y+(corners[3].y-corners[0].y)*t);
                trackPoints.add(new Point(x,y));
            }
            for (int j=1;j<5;j++) {
                double t=j/5.0;
                int x=(int)(corners[3].x+(corners[2].x-corners[3].x)*t);
                int y=(int)(corners[3].y+(corners[2].y-corners[3].y)*t);
                trackPoints.add(new Point(x,y));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            buildTrackPoints();
            int w=getWidth(), h=getHeight();
            int margin=Math.min(w,h)/10;int boardSize=Math.min(w,h)-2*margin;
            int spacing=boardSize/5;
            Point[] corners=new Point[]{
                    new Point(margin,margin),
                    new Point(margin+5*spacing,margin),
                    new Point(margin+5*spacing,margin+5*spacing),
                    new Point(margin,margin+5*spacing)};
            Point center=new Point(margin+boardSize/2,margin+boardSize/2);
            int smallR=spacing/6, largeR=spacing/3;
            g2.setColor(Color.BLACK);
            for(int i=0;i<4;i++)g2.draw(new Line2D.Double(corners[i],corners[(i+1)%4]));
            for(Point c:corners)g2.draw(new Line2D.Double(c,center));
            for(Point pt:trackPoints)g2.draw(new Ellipse2D.Double(pt.x-smallR,pt.y-smallR,2*smallR,2*smallR));
            for(Point c:corners)g2.draw(new Ellipse2D.Double(c.x-largeR,c.y-largeR,2*largeR,2*largeR));
            g2.draw(new Ellipse2D.Double(center.x-largeR,center.y-largeR,2*largeR,2*largeR));
            // highlight empty spots
            for(int idx:highlightIndices){Point pt=trackPoints.get(idx);
                boolean hasToken=false;
                for(int p=1;p<=MAX_PLAYERS;p++)for(int t=0;t<TOKENS_PER_PLAYER;t++)if(positions[p][t]==idx)hasToken=true;
                if(!hasToken) {
                    g2.setColor(new Color(255,255,0,128));
                    g2.fill(new Ellipse2D.Double(pt.x-smallR,pt.y-smallR,2*smallR,2*smallR));
                }
            }
            // draw tokens with merged color if highlighted
            for(int p=1;p<=MAX_PLAYERS;p++){
                Map<Integer,Integer> cm=new HashMap<>();
                for(int t=0;t<TOKENS_PER_PLAYER;t++){int pos=positions[p][t];if(pos>=0&&pos<trackPoints.size())cm.put(pos,cm.getOrDefault(pos,0)+1);}
                for(Map.Entry<Integer,Integer> e:cm.entrySet()){Point pt=trackPoints.get(e.getKey());int c=e.getValue();
                    boolean highlight = highlightIndices.contains(e.getKey());
                    Color base = p==1?Color.RED:Color.BLUE;
                    if(highlight) {
                        int r=(base.getRed()+255)/2;
                        int gcol=(base.getGreen()+255)/2;
                        int b=(base.getBlue()+0)/2;
                        g2.setColor(new Color(r,gcol,b));
                    } else g2.setColor(base);
                    g2.fill(new Ellipse2D.Double(pt.x-smallR,pt.y-smallR,2*smallR,2*smallR));
                    g2.setColor(Color.BLACK);
                    g2.draw(new Ellipse2D.Double(pt.x-smallR,pt.y-smallR,2*smallR,2*smallR));
                    if(c>1){g2.drawString(String.valueOf(c),pt.x+smallR,pt.y-smallR);}                }
            }
        }
    }
}
