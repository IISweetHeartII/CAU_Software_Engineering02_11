package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {
    private static int boardSize;
    private static int playerCount;
    private static int pieceCount;
    private static int uiType;
    private static boolean isTestMode;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (Scanner scanner = new Scanner(new File("src/data/config.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // 주석이나 빈 줄 무시
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(":");
                if (parts.length != 2) {
                    continue;
                }

                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "board":
                        boardSize = Integer.parseInt(value);
                        break;
                    case "player":
                        playerCount = Integer.parseInt(value);
                        break;
                    case "piece":
                        pieceCount = Integer.parseInt(value);
                        break;
                    case "ui":
                        uiType = Integer.parseInt(value);
                        break;
                    case "mode":
                        isTestMode = Integer.parseInt(value) == 2;
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("config.txt 파일을 찾을 수 없습니다.");
            setDefaultValues();
        } catch (NumberFormatException e) {
            System.err.println("설정 값이 올바르지 않습니다.");
            setDefaultValues();
        }
    }

    private static void setDefaultValues() {
        boardSize = 4;    // 기본 보드 크기: 사각형
        playerCount = 2;  // 기본 플레이어 수
        pieceCount = 1;   // 기본 말 개수
        uiType = 1;       // 기본 UI: Swing
        isTestMode = false; // 기본 모드: 일반 모드
    }

    // Getter 메서드들
    public static int getBoardSize() {
        return boardSize;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static int getPieceCount() {
        return pieceCount;
    }

    public static int getUiType() {
        return uiType;
    }

    public static boolean isTestMode() {
        return isTestMode;
    }
}