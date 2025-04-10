# 우트노리 게임 (Wutnori Game)

2025년 1학기 중앙대 소프트웨어공학 02분판 11팀 github 프로젝트 레포지토리입니다.

## 프로젝트 개요
우트노리 게임은 전통적인 한국의 보드게임을 컴퓨터로 구현한 프로젝트입니다.

## 개발 환경
- Java 17
- JUnit 5
- Maven

## 프로젝트 구조
```
/wutnori-game
│
├─ cursor.json                  ← Cursor 전용 세팅 파일
├─ README.md                    ← 프로젝트 문서
├─ .cursor/
│   ├─ instructions.md          ← Cursor 작업 지침
│   ├─ rules.md                 ← 팀 코딩 규칙
│
├─ /src/                        ← 소스 코드
│   ├─ Main.java
│   ├─ Board.java
│   ├─ GameEngine.java
│
├─ /docs/                       ← 문서
│   ├─ SRS.md                   ← 요구사항 명세서
│   ├─ SDD.md                   ← 설계 문서
│
├─ /test/                       ← 테스트 코드
│   └─ GameEngineTest.java
```

## 설치 및 실행 방법
1. Java 17 설치
2. Maven 설치
3. 프로젝트 클론
4. `mvn install` 실행
5. `java -jar target/wutnori-game-1.0.jar` 실행

## 게임 규칙
1. 2명의 플레이어가 번갈아가며 진행
2. 각 플레이어는 자신의 말을 이동
3. 상대방의 말을 잡으면 승리

## 기여 방법
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request