package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Board {
    protected final Map<Position, List<Position>> pathGraph = new HashMap<>();
    protected final Position center = new Position("C");
    protected final Position START = new Position("START");
    protected final Position END = new Position("END");
    protected int boardFigure = loadBoardFigure();


    private int loadBoardFigure() {
        try (Scanner scanner = new Scanner(new File("src/data/figure.txt"))) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0; // 기본값
    }

    // Constructor
    public Board() {
        switch (boardFigure) {
            case 4 -> init4Graph();
            case 5 -> init5Graph();
            case 6 -> init6Graph();
            default -> throw new IllegalArgumentException("Invalid board figure: " + boardFigure);
        }
    }

    public int getBoardFigure() {
        return boardFigure;
    }

    /* find and return n-th next position from specific positon */
    public Position getNNextPosition(Position position, int n) {
        // Field
        List<Position> nextPositionList = pathGraph.get(position);
        Position previousPosition = null;

        // Exception Handling
        if (position == null || n <= 0)
            return null;
        if (nextPositionList == null || nextPositionList.isEmpty())
            return null;

        // Main Logic
        for (int i = 0; i < n; i++) {
            // END에 도달하면 END를 반환
            if (nextPositionList.isEmpty()) {
                return new Position("END");
            }

            // NextPosition 계산
            if (position.equals(center)) { // center:[E3, E7]
                if (previousPosition == null) {
                    previousPosition = position;
                    position = nextPositionList.getLast();
                } else if (previousPosition.equals("E2")) {
                    previousPosition = position;
                    position = nextPositionList.getFirst();
                } else if (previousPosition.equals("E6")) {
                    previousPosition = position;
                    position = nextPositionList.getLast();
                } else if (previousPosition.equals("E10")) { // 오각형
                    previousPosition = position;
                    position = nextPositionList.getLast();
                } else if (previousPosition.equals("E12")) { // 육각형
                    previousPosition = position;
                    position = nextPositionList.get(1); // 중간으로 이동
                }
            } else if (nextPositionList.size() > 1 && i == 0) {
                previousPosition = position;
                position = nextPositionList.getLast();
            } else {
                previousPosition = position;
                position = nextPositionList.getFirst();
            }

            nextPositionList = pathGraph.get(position);
        }

        return position;
    }

    private void init4Graph() {
        // 일반 외곽 경로
        Position START = new Position("START");
        Position P1 = new Position("P1"); // 시작점
        Position P2 = new Position("P2");
        Position P3 = new Position("P3");
        Position P4 = new Position("P4");
        Position P5 = new Position("P5");

        Position P6 = new Position("P6");
        Position P7 = new Position("P7");
        Position P8 = new Position("P8");
        Position P9 = new Position("P9");
        Position P10 = new Position("P10");

        Position P11 = new Position("P11");
        Position P12 = new Position("P12");
        Position P13 = new Position("P13");
        Position P14 = new Position("P14");
        Position P15 = new Position("P15");

        Position P16 = new Position("P16");
        Position P17 = new Position("P17");
        Position P18 = new Position("P18");
        Position P19 = new Position("P19");
        Position P20 = new Position("P20");

        Position END = new Position("END"); // 종료점

        // 중심점 및 교차 경로
        Position C = center;

        Position E1 = new Position("E1");
        Position E2 = new Position("E2");

        Position E3 = new Position("E3");
        Position E4 = new Position("E4");

        Position E5 = new Position("E5");
        Position E6 = new Position("E6");

        Position E7 = new Position("E7");
        Position E8 = new Position("E8");


        // 외곽 경로 설정
        pathGraph.put(START, List.of(P1)); // 시작점
        pathGraph.put(P1, List.of(P2));
        pathGraph.put(P2, List.of(P3));
        pathGraph.put(P3, List.of(P4));
        pathGraph.put(P4, List.of(P5));
        pathGraph.put(P5, List.of(P6, E1)); // 분기점

        pathGraph.put(P6, List.of(P7));
        pathGraph.put(P7, List.of(P8));
        pathGraph.put(P8, List.of(P9));
        pathGraph.put(P9, List.of(P10));
        pathGraph.put(P10, List.of(P11, E5)); // 분기점

        pathGraph.put(P11, List.of(P12));
        pathGraph.put(P12, List.of(P13));
        pathGraph.put(P13, List.of(P14));
        pathGraph.put(P14, List.of(P15));
        pathGraph.put(P15, List.of(P16));

        pathGraph.put(P16, List.of(P17));
        pathGraph.put(P17, List.of(P18));
        pathGraph.put(P18, List.of(P19));
        pathGraph.put(P19, List.of(P20));
        pathGraph.put(P20, List.of(END));
        pathGraph.put(END, List.of()); // 종료점

        // 중심 경로 설정
        pathGraph.put(E1, List.of(E2)); // P5 -> E1 -> E2 -> C -> E3 -> E4 -> P15
        pathGraph.put(E2, List.of(C));
        pathGraph.put(E3, List.of(E4));
        pathGraph.put(E4, List.of(P15));

        pathGraph.put(E5, List.of(E6)); // P10 -> E5 -> E6 -> C -> E7 -> E8 -> P20
        pathGraph.put(E6, List.of(C));
        pathGraph.put(E7, List.of(E8));
        pathGraph.put(E8, List.of(P20));

        pathGraph.put(C, List.of(E3, E7)); // 교차점
    }

    private void init5Graph() {
        // 일반 외곽 경로
        Position START = new Position("START");
        Position P1 = new Position("P1"); // 시작점
        Position P2 = new Position("P2");
        Position P3 = new Position("P3");
        Position P4 = new Position("P4");
        Position P5 = new Position("P5");

        Position P6 = new Position("P6");
        Position P7 = new Position("P7");
        Position P8 = new Position("P8");
        Position P9 = new Position("P9");
        Position P10 = new Position("P10");

        Position P11 = new Position("P11");
        Position P12 = new Position("P12");
        Position P13 = new Position("P13");
        Position P14 = new Position("P14");
        Position P15 = new Position("P15");

        Position P16 = new Position("P16");
        Position P17 = new Position("P17");
        Position P18 = new Position("P18");
        Position P19 = new Position("P19");
        Position P20 = new Position("P20");

        Position P21 = new Position("P21");
        Position P22 = new Position("P22");
        Position P23 = new Position("P23");
        Position P24 = new Position("P24");
        Position P25 = new Position("P25");
        Position END = new Position("END"); // 종료점

        Position C = center;

        Position E1 = new Position("E1");
        Position E2 = new Position("E2");

        Position E3 = new Position("E3");
        Position E4 = new Position("E4");

        Position E5 = new Position("E5");
        Position E6 = new Position("E6");

        Position E7 = new Position("E7");
        Position E8 = new Position("E8");

        // new inner path
        Position E9 = new Position("E9");
        Position E10 = new Position("E10");

        // 외곽 경로 설정
        pathGraph.put(START, List.of(P1)); // 시작점
        pathGraph.put(P1, List.of(P2));
        pathGraph.put(P2, List.of(P3));
        pathGraph.put(P3, List.of(P4));
        pathGraph.put(P4, List.of(P5));

        pathGraph.put(P5, List.of(P6, E1)); // 분기점
        pathGraph.put(P6, List.of(P7));
        pathGraph.put(P7, List.of(P8));
        pathGraph.put(P8, List.of(P9));
        pathGraph.put(P9, List.of(P10));

        pathGraph.put(P10, List.of(P11, E5)); // 분기점
        pathGraph.put(P11, List.of(P12));
        pathGraph.put(P12, List.of(P13));
        pathGraph.put(P13, List.of(P14));
        pathGraph.put(P14, List.of(P15));

        pathGraph.put(P15, List.of(P16, E9)); // 분기점
        pathGraph.put(P16, List.of(P17));
        pathGraph.put(P17, List.of(P18));
        pathGraph.put(P18, List.of(P19));
        pathGraph.put(P19, List.of(P20));

        pathGraph.put(P20, List.of(P21));
        pathGraph.put(P21, List.of(P22));
        pathGraph.put(P22, List.of(P23));
        pathGraph.put(P23, List.of(P24));
        pathGraph.put(P24, List.of(P25));
        pathGraph.put(P25, List.of(END)); // 종료점
        pathGraph.put(END, List.of()); // 종료점

        // 중심 경로 설정
        pathGraph.put(E1, List.of(E2)); // P5 -> E1 -> E2 -> C -> E3 -> E4 -> P20
        pathGraph.put(E2, List.of(C));

        pathGraph.put(E3, List.of(E4));
        pathGraph.put(E4, List.of(P20));

        pathGraph.put(E5, List.of(E6)); // P10 -> E5 -> E6 -> C -> E7 -> E8 -> P25
        pathGraph.put(E6, List.of(C));

        pathGraph.put(E7, List.of(E8));
        pathGraph.put(E8, List.of(P25));

        pathGraph.put(E9, List.of(E10)); // P15 -> E9 -> E10 -> C -> E7 -> E8 -> P25
        pathGraph.put(E10, List.of(C));

        // 교차점
        pathGraph.put(C, List.of(E3, E7)); // 교차점
    }

    private void init6Graph() {
// 일반 외곽 경로
        Position START = new Position("START");
        Position P1 = new Position("P1"); // 시작점
        Position P2 = new Position("P2");
        Position P3 = new Position("P3");
        Position P4 = new Position("P4");
        Position P5 = new Position("P5");

        Position P6 = new Position("P6");
        Position P7 = new Position("P7");
        Position P8 = new Position("P8");
        Position P9 = new Position("P9");
        Position P10 = new Position("P10");

        Position P11 = new Position("P11");
        Position P12 = new Position("P12");
        Position P13 = new Position("P13");
        Position P14 = new Position("P14");
        Position P15 = new Position("P15");

        Position P16 = new Position("P16");
        Position P17 = new Position("P17");
        Position P18 = new Position("P18");
        Position P19 = new Position("P19");
        Position P20 = new Position("P20");

        Position P21 = new Position("P21");
        Position P22 = new Position("P22");
        Position P23 = new Position("P23");
        Position P24 = new Position("P24");
        Position P25 = new Position("P25");

        Position P26 = new Position("P26");
        Position P27 = new Position("P27");
        Position P28 = new Position("P28");
        Position P29 = new Position("P29");
        Position P30 = new Position("P30");
        Position END = new Position("END"); // 종료점

        Position C = center;

        Position E1 = new Position("E1");
        Position E2 = new Position("E2");

        Position E3 = new Position("E3");
        Position E4 = new Position("E4");

        Position E5 = new Position("E5");
        Position E6 = new Position("E6");

        Position E7 = new Position("E7");
        Position E8 = new Position("E8");

        // new inner path
        Position E9 = new Position("E9");
        Position E10 = new Position("E10");

        Position E11 = new Position("E11");
        Position E12 = new Position("E12");

        // 외곽 경로 설정
        pathGraph.put(START, List.of(P1)); // 시작점
        pathGraph.put(P1, List.of(P2));
        pathGraph.put(P2, List.of(P3));
        pathGraph.put(P3, List.of(P4));
        pathGraph.put(P4, List.of(P5));

        pathGraph.put(P5, List.of(P6, E1)); // 분기점
        pathGraph.put(P6, List.of(P7));
        pathGraph.put(P7, List.of(P8));
        pathGraph.put(P8, List.of(P9));
        pathGraph.put(P9, List.of(P10));

        pathGraph.put(P10, List.of(P11, E11)); // 분기점
        pathGraph.put(P11, List.of(P12));
        pathGraph.put(P12, List.of(P13));
        pathGraph.put(P13, List.of(P14));
        pathGraph.put(P14, List.of(P15));

        pathGraph.put(P15, List.of(P16, E5)); // 분기점
        pathGraph.put(P16, List.of(P17));
        pathGraph.put(P17, List.of(P18));
        pathGraph.put(P18, List.of(P19));
        pathGraph.put(P19, List.of(P20));

        pathGraph.put(P20, List.of(P21));
        pathGraph.put(P21, List.of(P22));
        pathGraph.put(P22, List.of(P23));
        pathGraph.put(P23, List.of(P24));
        pathGraph.put(P24, List.of(P25));

        pathGraph.put(P25, List.of(P26));
        pathGraph.put(P26, List.of(P27));
        pathGraph.put(P27, List.of(P28));
        pathGraph.put(P28, List.of(P29));
        pathGraph.put(P29, List.of(P30));
        pathGraph.put(P30, List.of(END)); // 종료점
        pathGraph.put(END, List.of()); // 종료점

        // 중심 경로 설정
        pathGraph.put(E1, List.of(E2)); // P5 -> E1 -> E2 -> C -> E3 -> E4 -> P20
        pathGraph.put(E2, List.of(C));

        pathGraph.put(E3, List.of(E4));
        pathGraph.put(E4, List.of(P20));

        pathGraph.put(E11, List.of(E12)); // P10 -> E11 -> E12 -> C -> E9 -> E10 -> P25
        pathGraph.put(E12, List.of(C));

        pathGraph.put(E9, List.of(E10));
        pathGraph.put(E10, List.of(P25));

        pathGraph.put(E5, List.of(E6)); // P15 -> E5 -> E6 -> C -> E7 -> E8 -> P30
        pathGraph.put(E6, List.of(C));

        pathGraph.put(E7, List.of(E8));
        pathGraph.put(E8, List.of(P30));

        // 교차점
        pathGraph.put(C, List.of(E3, E9, E7)); // 교차점
    }
}