//윷 던지기 결과 정의

package Model;

public class YutResult {
    private final YutResultType resultType;

    public YutResult(YutResultType resultType) {
        this.resultType = resultType;
    }

    public YutResultType getResultType() {
        return resultType;
    }

    //-----실제 이동 칸 수 제공-----//
    public int getSteps() {
        return resultType.getSteps();
    }

    //-----추가 윷 던지기-----//
    public boolean isExtraTurn() {
        return resultType.hasExtraTurn();
    }
}

