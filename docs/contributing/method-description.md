
## GameManager class

### Constructor

```java
public GameManager();
```

- 게임 매니저를 초기화하고 플레이어 수와 말의 수를 설정합니다.
- 시작 위치와 끝 위치의 말 개수를 초기화합니다.

### Getters

```java
public int getNumberOfPlayers();
public int getNumberOfPieces();
public int getCurrentPlayer();
public int[] getCountOfPieceAtStart();
public Map<String, String> getPositionPieceMap();
public int getSize();
```

- 각각 플레이어 수, 말의 수, 현재 플레이어, 시작 위치의 말 개수, 말의 위치 정보, 보드 크기를 반환합니다.

### 상태 확인 메서드

```java
public boolean isExtraMove();     // 추가 이동이 가능한지 확인
public boolean isExtraTurn();     // 추가 턴이 있는지 확인
public boolean isCurrentPlayersPiecePresent(String positionId);  // 해당 위치에 현재 플레이어의 말이 있는지 확인
public boolean isValidMove(String startPosition, String targetPosition);  // 해당 이동이 유효한지 확인
```

### 말 이동 관련 메서드

```java
public void controlMovePiece(String startPosition, String targetPosition);

```
- 말을 시작 위치에서 목표 위치로 이동시킵니다.
- 말의 캡처나 그룹화 등의 처리도 수행합니다.

### 윷 던지기 관련 메서드

``` java
public YutResult throwYutRandom()  // 무작위로 윷을 던집니다
public YutResult throwYutManual(int value)  // 지정된 값으로 윷을 던집니다
```

### 턴 관리 메서드들

``` java
public int getCurrentPlayerNumber()  // 현재 플레이어 번호 반환
public void nextTurn()  // 다음 턴으로 넘어갑니다
public boolean isGameEnd()  // 게임이 종료되었는지 확인
```

### 게임 초기화 메서드

``` java
public void resetGame()
```

- 게임을 초기 상태로 되돌립니다.
- 모든 말의 위치와 게임 상태를 리셋합니다.

## GameView interface

### 1. initUI()

``` java
void initUI();
```

- UI 컴포넌트들을 초기화하고 화면을 구성합니다.
- 게임 시작 시 호출되어 기본 UI 레이아웃을 설정합니다.

### 2. showYutResult()

``` java
void showYutResult(Integer yutResult);
```

- 윷 던지기의 결과를 화면에 표시합니다.
- `yutResult`: 윷 던지기 결과값을 나타내는 정수
- 결과에 따라 해당하는 윷 이미지나 텍스트를 화면에 업데이트합니다.

### 3. showWinner()

``` java
void showWinner(int winner);
```
- 게임이 종료되었을 때 승자를 화면에 표시합니다.
- `winner`: 승리한 플레이어의 번호
- turn을 표시하는 위치에 승자 정보를 화면 요소를 표시합니다.

### 4. updateBoard()

``` java
void updateBoard();
```

- 게임 보드의 현재 상태를 화면에 반영합니다.
- 말의 위치 변경이나 캡처 등의 변화를 보드에 표시합니다.
- 모든 말의 위치를 최신 상태로 갱신합니다.

### 5. updatePlayerScore()

``` java
void updatePlayerScore();
```

- 각 플레이어의 말의 상태를 업데이트합니다.
- 시작 지점과 도착 지점의 말 개수 등을 화면에 반영합니다.

### 6. updateTurn()

``` java
void updateTurn();
```

- 현재 차례인 플레이어를 표시하도록 화면을 업데이트합니다.
- 턴이 바뀔 때마다 현재 플레이어를 시각적으로 표시합니다.

