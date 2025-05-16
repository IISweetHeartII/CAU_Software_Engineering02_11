package model;

import java.util.Random;


public class Yut {
    private final Random random = new Random();

    /**
     * 랜덤 윷 던지기 로직
     * 도, 개, 걸, 윷, 모 or 빽도를 무작위 생성
     */
    public YutResult throwYut() {
        int rand = random.nextInt(100) + 1; // 1 ~ 100

        YutResultType result;
        if (rand <= 4) {
            result = YutResultType.BACK_DO;      // 1 ~ 4
        } else if (rand <= 28) {
            result = YutResultType.DO;           // 5 ~ 28
        } else if (rand <= 67) {
            result = YutResultType.GAE;          // 29 ~ 67
        } else if (rand <= 92) {
            result = YutResultType.GEOL;         // 68 ~ 92
        } else if (rand <= 96) {
            result = YutResultType.YUT;          // 93 ~ 96
        } else {
            result = YutResultType.MO;           // 97 ~ 100
        }

        return new YutResult(result);
    }

    /**
     * 지정 윷 던지기
     * 사용자가 입력한 문자열로 결과 생성
     * 유효하지 않은 입력은 예외 발생
     */
    public YutResult throwYut(String input) {
        try {
            YutResultType result = YutResultType.valueOf(input.toUpperCase());
            return new YutResult(result);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 윷 종류입니다: " + input);
        }
    }

    public YutResult throwYut(int n) {
        return switch (n) {
            case -1 -> new YutResult(YutResultType.BACK_DO);
            case 1 -> new YutResult(YutResultType.DO);
            case 2 -> new YutResult(YutResultType.GAE);
            case 3 -> new YutResult(YutResultType.GEOL);
            case 4 -> new YutResult(YutResultType.YUT);
            case 5 -> new YutResult(YutResultType.MO);
            default -> throw new IllegalArgumentException("잘못된 윷 종류입니다: " + n);
        };
    }
}
