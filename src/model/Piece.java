package model;

import java.util.ArrayDeque;
import java.util.Collections;

public class Piece {
    /// field ///
    protected ArrayDeque<Unit> unitArrayDeque; // ArrayDeque로 변경
    protected Position currentPosition;
    protected String playerID;
    private String pieceId = "";
    protected int size;
    protected boolean isArrived;

    /// Constructor ///
    public Piece(Unit... units) { // 가변 인자를 사용해 여러 개의 말을 그룹화
        this.unitArrayDeque = new ArrayDeque<>(); // ArrayDeque 초기화
        Collections.addAll(this.unitArrayDeque, units);
        this.currentPosition = units[0].getCurrentPosition();
        this.playerID = units[0].getPlayerID();
        this.size = units.length;
        this.isArrived = isArrived();
        for (Unit unit : units) {
            this.pieceId = this.pieceId + unit.getPieceID(); // 각 Piece의 ID 업데이트
        }
    }

    /// getters ///
    public ArrayDeque<Unit> getPieceArrayDeque() {
        return unitArrayDeque;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public String getPlayerID() {
        return playerID;
    }

    /// methods ///
    public void moveTo(int n) {
        for (Unit unit : unitArrayDeque) {
            unit.moveTo(n);
        }
        if (unitArrayDeque.peekFirst() != null) {
            currentPosition = unitArrayDeque.peekFirst().getCurrentPosition(); // 그룹의 첫 번째 말의 위치로 업데이트
        }
        this.isArrived = isArrived();
    }

    public boolean isArrived() {
        return currentPosition.equals(new Position("END")); // 그룹의 첫 번째 말이 END에 도착했음
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece other = (Piece) obj;
        return unitArrayDeque.equals(other.unitArrayDeque);
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