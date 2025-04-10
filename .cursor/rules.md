# 팀 코딩 규칙 및 설계 원칙

# Cursor 프로젝트 규칙
- Java 코드는 MVC 아키텍처를 기반으로 작성한다.
- UI는 Java Swing 컴포넌트를 사용하며, 최소 두 개 이상의 Toolkit을 구현해야 한다.
- 클래스/변수/메서드명은 camelCase를 따른다.
- 테스트는 JUnit을 기반으로 작성하며, 각 기능에 대해 단위 테스트를 포함한다.
- 코드 주석은 필수 함수마다 JavaDoc 스타일로 작성한다.
- 커밋 메시지는 영어로, 기능 단위로 작성한다. (예: `feat: Add dice rolling logic`)

## 1. 코드 구조
- MVC 패턴을 기본으로 사용합니다.
- 각 클래스는 단일 책임 원칙(SRP)을 따릅니다.
- 패키지 구조는 기능별로 구분합니다.

## 2. 네이밍 규칙
- 클래스명: PascalCase (예: GameEngine)
- 메소드명: camelCase (예: startGame)
- 변수명: camelCase (예: playerScore)
- 상수: UPPER_SNAKE_CASE (예: MAX_PLAYERS)

## 3. 문서화
- 모든 public API에는 Javadoc 주석을 포함합니다.
- 복잡한 로직에는 인라인 주석을 추가합니다.
- README.md는 최신 상태를 유지합니다.

## 4. 테스트
- 모든 public 메소드에 대한 단위 테스트를 작성합니다.
- 테스트 커버리지는 80% 이상을 유지합니다.
- 테스트는 독립적이고 반복 가능해야 합니다.

## 5. 버전 관리
- 커밋 메시지는 명확하고 구체적으로 작성합니다.
- feature 브랜치를 사용하여 개발합니다.
- PR 전에 코드 리뷰를 진행합니다. 