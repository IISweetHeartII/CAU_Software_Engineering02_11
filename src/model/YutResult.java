//윷 던지기 결과 정의

package model;

public class YutResult {
    private final YutResultType resultType;

    public YutResult(YutResultType resultType) {
        this.resultType = resultType;
    }

    public YutResultType getYutResultType() {
        return resultType;
    }

    //-----실제 이동 칸 수 제공-----//
    public int getValue() {
        return resultType.getSteps();
    }

    //-----추가 윷 던지기-----//
    public boolean isExtraTurn() {
        return resultType.hasExtraTurn();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof YutResult other)) return false;
        return this.resultType == other.resultType;
    }

    public String toString() {
        return resultType.toString();
    }
}

enum YutResultType {
    BACK_DO(-1, false),
    DO(1, false),
    GAE(2, false),
    GEOL(3, false),
    YUT(4, true),
    MO(5, true);

    private final int steps;
    private final boolean extraTurn;

    YutResultType(int steps, boolean extraTurn) {
        this.steps = steps;
        this.extraTurn = extraTurn;
    }

    public int getSteps() {
        return steps;
    }

    public boolean hasExtraTurn() {
        return extraTurn;
    }
}

