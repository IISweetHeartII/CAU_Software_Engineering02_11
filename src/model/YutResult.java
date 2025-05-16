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
}

