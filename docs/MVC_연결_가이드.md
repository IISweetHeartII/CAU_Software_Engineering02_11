# 윷놀이 게임 MVC 패턴 연결 가이드

## 목차
1. [MVC 패턴 개요](#mvc-패턴-개요)
2. [모델 (model) 구성](#모델-model-구성)
3. [뷰 (view) 구성](#뷰-view-구성)
4. [컨트롤러 (controller) 구성](#컨트롤러-controller-구성)
5. [MVC 연결 방법](#mvc-연결-방법)
6. [테스트 파일 관리 가이드](#테스트-파일-관리-가이드)

## MVC 패턴 개요

윷놀이 게임은 MVC(model-view-controller) 패턴을 적용하여 설계되었습니다. 이 패턴을 통해 코드의 유지보수성을 높이고, 각 구성 요소의 역할을 명확히 분리했습니다.

- **model**: 게임의 데이터와 비즈니스 로직을 담당
- **view**: 사용자 인터페이스와 시각적 요소를 담당
- **controller**: Model과 view 사이의 상호작용을 조정하고 게임 흐름을 제어

## 모델 (model) 구성

모델은 게임의 상태와 규칙을 관리합니다.

### 주요 클래스
- `GameModel`: 게임 전체 상태와 로직을 관리
- `Board`: 게임 보드와 경로를 관리
- `Player`: 플레이어 정보와 말 관리
- `Piece` / `MovablePiece`: 게임 말과 이동 로직 관리
- `Yut`: 윷 던지기 관련 로직
- `YutResult` / `YutResultType`: 윷 결과 처리

### 주요 책임
- 게임 상태 관리
- 플레이어 턴 관리
- 말 이동 규칙 적용
- 점수 계산과 승리 조건 확인

### GameModel 중요 메서드
- `throwAndSaveYut()`: 윷을 던지고 결과 저장
- `getPosableMoves()`: 이동 가능한 위치 계산
- `movePiece()`: 말 이동 처리
- `groupPiecesAtPosition()` / `captureOpponentPiece()`: 말 그룹화 및 상대편 말 잡기
- `nextTurn()`: 다음 턴으로 전환

## 뷰 (view) 구성

뷰는 사용자 인터페이스와 시각적 요소를 담당합니다.

### 주요 클래스
- `GameView`: 뷰 인터페이스 (view 계층의 최상위 인터페이스)
- `BoardPanel`: 게임 보드 표시 및 사용자 상호작용 처리
- `MainFrame`: 메인 UI 프레임
- `MainUI_Swing`: Swing 기반 UI 구현

### BoardPanel 주요 기능
- 보드 이미지 및 노드 표시
- 말 위치 및 이동 표시
- 윷 결과 표시
- 플레이어 턴 및 점수 표시
- 사용자 입력 (버튼 클릭, 말 선택 등) 처리

### 중요 리스너 인터페이스
- `NodeClickListener`: 노드 클릭 이벤트 처리
- `YutThrowListener`: 윷 던지기 이벤트 처리
- `CustomChoiceListener`: 커스텀 윷 선택 이벤트 처리
- `GameRestartListener`: 게임 재시작 이벤트 처리
- `GameQuitListener`: 게임 종료 이벤트 처리

## 컨트롤러 (controller) 구성

컨트롤러는 모델과 뷰 사이의 상호작용을 조정합니다.

### 주요 클래스
- `GameController`: 게임 전체 흐름 제어

### 주요 책임
- 사용자 입력 처리 및 모델 업데이트
- 모델의 변경사항을 뷰에 반영
- 게임 상태 전환 관리

### 중요 메서드
- `handleRandomThrow()`: 랜덤 윷 던지기 처리
- `handleManualThrow()`: 수동 윷 선택 처리
- `changeTurn()`: 턴 전환 처리
- `checkGameEnd()`: 게임 종료 조건 확인

## MVC 연결 방법

### model → view 연결
1. 모델은 상태 변경 후 컨트롤러에 알림
2. 컨트롤러는 뷰의 적절한 메서드를 호출하여 화면 업데이트
   - 예: `gameView.showYutResult(result)`, `gameView.updateCurrentPlayer(playerID)`

### view → model 연결
1. 뷰에서 사용자 입력 발생 시 리스너를 통해 컨트롤러에 전달
2. 컨트롤러는, 모델의 적절한 메서드를 호출하여 게임 상태 업데이트
   - 예: `yutThrowListener.onYutThrow()` → `gameModel.throwAndSaveYut()`

### controller 설정 과정
```java
// 컨트롤러 생성 및 연결 예시
GameModel gameModel = new GameModel(4, 5); // 4명의 플레이어, 각 5개의 말
GameView gameView = new MainUI_Swing(); // Swing 기반 UI 구현
GameController controller = new GameController(gameModel, gameView);

// 뷰의 이벤트 리스너 설정
boardPanel.setYutThrowListener(() -> {
    return controller.handleRandomThrow(); // 윷 던지기 처리 및 결과 반환
});

boardPanel.setCustomChoiceListener(new BoardPanel.CustomChoiceListener() {
    @Override
    public void onCustomChoice() {
        // 커스텀 선택 UI 표시
    }
    
    @Override
    public void onCustomYutSelected(int selection) {
        controller.handleManualThrow(YutResultType.fromValue(selection));
    }
});

// 노드 클릭 리스너 설정
boardPanel.setNodeClickListener(nodeId -> {
    // 노드 클릭 처리
    Position selectedPosition = new Position(nodeId);
    controller.handlePositionSelection(selectedPosition);
});
```

## 이미지 및 리소스 처리

BoardPanel 클래스에서 이미지 처리 시 다음 사항에 주의해야 합니다:

1. **이미지 경로 처리**: 모든 이미지는 `src/data/` 디렉토리 아래에 있어야 합니다.
   - 보드 이미지: `src/data/board/`
   - 버튼 이미지: `src/data/Button/`
   - 윷 결과 이미지: `src/data/yut/`
   - 턴 표시 이미지: `src/data/Turn/`
   - 점수 표시 이미지: `src/data/Score/`

2. **이미지 크기 조정**: 모든 이미지는 원본 크기의 1/3로 자동 리사이징됩니다.
   ```java
   // 이미지 리사이징 예시
   int originalWidth = image.getWidth();
   int originalHeight = image.getHeight();
   Image resizedImage = image.getScaledInstance(originalWidth/3, originalHeight/3, Image.SCALE_SMOOTH);
   ```

3. **예외 처리**: 이미지 파일이 없는 경우를 항상 처리해야 합니다.
   ```java
   try {
       // 이미지 로드 시도
       BufferedImage image = ImageIO.read(imageFile);
       // 이미지 처리...
   } catch (Exception e) {
       // 예외 처리: 텍스트로 대체 표시
       label.setIcon(null);
       label.setText("대체 텍스트");
   }
   ```

## 테스트 파일 관리 가이드

view 패키지에 포함된 테스트 파일들은 다음과 같이 관리하는 것을 권장합니다:

1. **유지해야 할 테스트 파일**:
   - `BoardPanelTest.java`: BoardPanel 기능 테스트에 필요하므로 유지
   
2. **제거 가능한 테스트 파일**:
   - `TestMain.java`: 실제 게임 구현 시 필요하지 않은 임시 테스트 파일
   
3. **테스트 파일 사용 시 유의사항**:
   - 테스트 코드는 실제 게임 로직과 분리되어야 함
   - 테스트 목적이 명확하게 주석으로 표시되어야 함
   - 테스트 완료 후 불필요한 코드는 제거하거나 `test` 디렉토리로 이동

4. **테스트 디렉토리 구성 권장사항**:
   ```
   src/
   ├── controller/
   ├── model/
   ├── view/
   └── test/
       ├── ControllerTest/
       ├── ModelTest/
       └── ViewTest/
   ```

## 최종 연결 체크리스트

MVC 연결이 올바르게 이루어졌는지 확인하기 위한 체크리스트:

1. model 클래스들이 View나 Controller에 의존하지 않는지 확인
2. View가 Model에 직접 접근하지 않고 Controller를 통해서만 접근하는지 확인
3. Controller가 View와 model 사이의 중재자 역할을 제대로 수행하는지 확인
4. 모든 사용자 이벤트가 적절한 리스너를 통해 Controller에 전달되는지 확인
5. 모든 UI 업데이트가 Controller를 통해 View에 전달되는지 확인
6. 이미지 및 리소스 파일이 올바른 경로에 위치하는지 확인
7. 예외 처리가 적절히 구현되어 있는지 확인 