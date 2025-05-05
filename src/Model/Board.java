package Model;

import java.util.*;
import java.util.ArrayDeque;

public class Board {
    private final Map<Position, List<Position>> pathGraph = new HashMap<>();
    private final Position center = new Position("C");

    // Constructor
    public Board() {
        initGraph();
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

    private void initGraph() {
        // 일반 외곽 경로
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
}