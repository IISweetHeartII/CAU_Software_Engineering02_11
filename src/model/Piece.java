package model;

import java.util.ArrayDeque;
import java.util.Collections;

public class Piece {
    // ------ fields ------ //
    protected String playerID;
    private String pieceId = "";

    protected Position currentPosition;
    protected int size;
    protected boolean isArrived;

    protected ArrayDeque<Unit> unitArrayDeque = new ArrayDeque<>(); // ArrayDeque로 변경

    // ------ constructor ------ //
    public Piece(Unit... units) { // 가변 인자를 사용해 여러 개의 말을 그룹화
        this.playerID = units[0].getPlayerID();
        for (Unit unit : units) {
            this.pieceId = this.pieceId + unit.getPieceID(); // 각 Piece의 ID 업데이트
        }

        this.currentPosition = units[0].getCurrentPosition();
        this.size = units.length;
        this.isArrived = isArrived();

        Collections.addAll(this.unitArrayDeque, units);
    }

    public Piece(Unit unit) { // 단일 Unit으로 초기화
        this.playerID = unit.getPlayerID();
        this.pieceId = unit.getPieceID();
        this.currentPosition = unit.getCurrentPosition();
        this.size = 1;
        this.isArrived = isArrived();

        this.unitArrayDeque.add(unit);
    }

    public Piece(Piece other) { // 깊은 복사 생성자
        this.playerID = other.playerID;
        this.pieceId = other.pieceId;
        this.currentPosition = other.currentPosition;
        this.size = other.size;
        this.isArrived = other.isArrived;

        for (Unit unit : other.unitArrayDeque) {
            this.unitArrayDeque.add(new Unit(unit)); // 깊은 복사
        }
    }

    // ------ getters ------ //
    public ArrayDeque<Unit> getUnitArrayDeque() {
        return unitArrayDeque;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public String getPlayerID() {
        return playerID;
    }

    // ------ methods ------ //
    public void moveTo(int n) {
        for (Unit unit : unitArrayDeque) {
            unit.moveTo(n);
        }
        if (unitArrayDeque.peekFirst() != null) {
            currentPosition = unitArrayDeque.peekFirst().getCurrentPosition(); // 그룹의 첫 번째 말의 위치로 업데이트
            this.isArrived = isArrived();
            updatePieceId();
        }
    }

    public boolean isArrived() {
        return currentPosition.equals(new Position("END")); // 그룹의 첫 번째 말이 END에 도착했음
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Piece other = (Piece) obj;
        return this.pieceId.equals(other.pieceId)
                && this.currentPosition.equals(other.currentPosition);
    }

    public void updatePieceId() {
        this.pieceId = ""; // 초기화
        for (Unit unit : unitArrayDeque) {
            this.pieceId = this.pieceId + unit.getPieceID(); // 각 Piece의 ID 업데이트
        }
    }

    public String getPieceId() {
        return pieceId;
    }
}