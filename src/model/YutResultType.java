package model;

public enum YutResultType {
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

