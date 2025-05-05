package Model;

import java.util.Random;

public class YutThrow {
    private final Random random = new Random();

    /**
     * 랜덤 윷 던지기 로직
     * 도, 개, 걸, 윷, 모 or 빽도를 무작위 생성
     */
    public YutResult throwRandom() {
        int backCount = 0;

        // 윷 4개 던짐 (1은   앞면, 0은 뒷면)
        for (int i = 0; i < 4; i++) {
            int Yut = random.nextBoolean() ? 1 : 0;
            if (Yut == 0) backCount++;
        }

        YutResultType result = switch (backCount) {
            case 0 -> YutResultType.MO;
            case 1 -> YutResultType.YUT;
            case 2 -> YutResultType.GEOL;
            case 3 -> YutResultType.GAE;
            case 4 -> YutResultType.DO;
            default -> YutResultType.BACK_DO; // 예외 대비
        };

        return new YutResult(result);
    }

    /**
     * 지정 윷 던지기
     * 사용자가 입력한 문자열로 결과 생성
     * 유효하지 않은 입력은 예외 발생
     */
    public YutResult throwManual(String input) {
        try {
            YutResultType result = YutResultType.valueOf(input.toUpperCase());
            return new YutResult(result);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 윷 종류입니다: " + input);
        }
    }
}
