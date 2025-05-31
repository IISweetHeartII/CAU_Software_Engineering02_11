# 내부 팀원용 설계 문서

## Board

- BoardManager는 Board를 관리하는 클래스입니다.
- Board와 관련된 모든 모델 로직을 담당합니다.

### Node 저장 방식

- Board는 Map 자료형을 사용해 각 Node를 저장하며 이때 Key: Position class, Value: List[Position] 형태로 저장됩니다.
- 하나의 노드는 Position class를 사용해 표현되며, Board에 다음과 같은 정보로 저장됩니다.
    - `Key`: 자신의 Position
    - `Value`: 다음으로 이동 가능한 Position 리스트
        - `직선 경로`는 가장 먼저 리스트에 저장되며, 최단 경로를 갖는 `분기 경로`는 그 다음에 저장됩니다.
        - 즉, 리스트에서 마지막 요소로 갈수록 `최단 경로`를 갈 수 있는 경로가 됩니다.
        - 따라서, 각 요소의 순서가 보장되어야 하므로 `set` 자료형보다 `list` 자료형을 사용할 필요가 있습니다.
    - 예시 : Center -> `[ Key: Position(C), Value: List.Of(Position(E3), Position(E4)) ]`

### `NodeId` 할당

- 외곽 경로는 `String:"P" + index`로 표기됩니다.
- 내부 경로는 `String:"E" + index`로 표기됩니다.
- 중앙 경로는 `String:"C"`로 표기됩니다.
    - 중앙 경로는 다른 분기점에 비해 특수한 연결 로직이 필요하므로 NodeId를 따로 설정합니다.
- 예시 : `P1`, `E1`, `C` 등으로 표기됩니다.

### 분기점에서 다음 위치 계산

- Board 탐색은 `BoardManager:setPreviousPosition(Position currentPosition, int moveCount, Piece piece)` 메서드가 모든 책임을 갖습니다.
- 이 메서드는 다음과 같은 로직으로 동작합니다.
    - `currentPosition`을 기준으로 `moveCount`만큼 이동한 다음 위치를 반환합니다.
    - 사용된 `piece`의 경로 정보에 사용되는 `previousPosition`을 저장합니다.
    - `piece`는 `GameManager`에서 관리되고 사용되는 `Piece`입니다.
- 세부적인 분기 탐색 로직은 다음과 같습니다.
    - 메서드가 시작되기 전 지역 변수로 `boolean:startSign = true`를 선언합니다.
    - 이 변수를 바탕으로 분기를 결정합니다.
        - 이전에 선언된 Map 자료형에서 `currentPosition`을 Key로 사용해 `next position list <- List<Position>`을 가져옵니다.
        - `startSign`이 `true`인 경우
            - `next position list`에서 마지막 요소를 가져옵니다. -> `Node 저장 방식`에서 설명한 것처럼 최단 경로에 진입합니다.
            - `startSign`을 `false`로 변경합니다.
        - `startSign`이 `false`인 경우
            - `next position list`에서 첫 번째 요소를 가져옵니다.
        - 즉, 이동 시작 위치에서 최단 경로 분기가 가능하면 최단 경로를 선택하고, 이동 시작 위치가 아닌 경우 직선 경로를 선택합니다.

## Piece

- Piece는 고급 수준의 메서드가 없는 정보 저장 용도인 (구조체 형태의)클래스입니다.

### `PieceId` 할당

- `PieceId`는 소유자인 Player의 Player Number와 Grouping Size에 대한 정보를 포함합니다.
- `PieceId`는 처음 생성될 때 `String:"" + PlayerNumber`로 표기됩니다.
  - 예시 : `"1"`, `"2"` 등으로 표기됩니다.
- `Grouping`이 발생하면 `PieceId = me.PieceId + other.PieceId`로 표기됩니다.
  - 예시 : `"1" + "11" = "111"`으로 표기됩니다.

### 자신이 지나온 경로

- `Piece`는 현재 경로를 저장하는 대신 자신의 이전 위치를 `previousPosition`으로 저장합니다.
- `Piece`의 현재 위치는 `GameManager`에서 관리되며, 그렇기에 `Piece`는 자신의 이동에 대한 책임이 없습니다.

### Piece의 이동

- 앞서 말했듯, `Piece`는 자신의 이동에 대한 책임이 없습니다. 따라서 이 내용은 `Piece` 클래스가 아닌 다른 클래스에서의 내용입니다.
- `Piece`의 앞(Forward) 방향 이동은 BoardManager의 `setPreviousPosition` 메서드 로직과 동일하지만, 뒤(Backward) 방향 이동은 `Piece`의 `previousPosition`을 사용합니다.
- 뒤 방향 이동인 빽도가 발생했을 때 boardManager의 `setPreviousPosition` 메서드는 다음과 같이 동작합니다.
  - `Piece`의 `previousPosition`을 임시로 `tempPosition`에 저장합니다. : `tempPosition <- piece.previousPosition`
  - `Piece`의 `previousPosition`을 `currentPosition`으로 변경합니다. : `piece.previousPosition <- currentPosition`
  - 반환 값으로 `piece`가 이전에 가지고 있던 `previousPosition`을 반환합니다. : `return tempPosition`

## Position

> 한줄 요약 : `String:NodeId 전시대`

- `Position` 클래스는 단순히 `String`으로 표현된 `NodeId`를 저장하는 클래스입니다.
- 따라서 `Position` 클래스는 단순히 `String`을 저장하는 역할만 하는 `Position 전시대`같은 거라서 단순히 `Position` 클래스를 없애고 `String`으로 대체해도 무방합니다...오히려 `Position` 클래스가 존재해서 복잡성이 늘어난 부분이 좀 있습니다.
- 한자기 유일한 목적이라면 `Position` 클래스로 선언하고 NodeId를 저장함으로써 `NodeId`를 _**외부에서 수정할 수 없다**_ 는 점입니다.

## YutManager, YutResult, YutResultType

> `YutManager`는 윷 던지기와 관련된 로직을 관리하는 클래스입니다. 주로 윷의 결과를 계산하고 저장하는 역할을 합니다.
>> `YutResult`는 윷의 결과를 저장하는 클래스이며, `YutResultType`은 윷의 결과 타입을 정의하는 열거형입니다.

## GameManager

> `GameManager`는 **게임의 상[JavafxUI.java](../../src/view/JavafxUI.java)태**를 관리하는 클래스입니다. 주로 게임판의 상태와 기타 로직을 담당합니다.
> 
> `GameController`는 더 거시적인 관점에서 **게임의 흐름**을 관리하는 클래스입니다. 게임이 어떤 단계(윷 던지기 단계, 말 이동 단계)인지 관리하고 GameManager를 통해 게임의 상태를 업데이트합니다.

### `config.txt` 에서 읽어오는 정보

- `GameManager`는 `config.txt`에서 읽어오는 정보를 바탕으로 게임을 시작합니다.
    - palyer: 플레이어 수
    - piece: 말 수
    - mode: 게임 모드

### 플레이어 관리

- `GameManager`는 턴 관리와 게임 종료 여부, 플레이어의 말 상태(START, END)를 관리하며, 이 과정에서 playerIndex를 사용합니다.
- `playerIndex`는 현재 턴을 가진 플레이어의 인덱스를 나타내며, `현재 플레이어의 id - 1`로 계산합니다.
- 예시 : 플레이어 1의 턴일 때 `playerIndex = 0`, 플레이어 2의 턴일 때 `playerIndex = 1` 등으로 설정됩니다.

### 말의 위치 저장

- 말의 위치는 `GameManager`에서 `Map<String, Piece>` 자료형을 사용해 관리합니다.
  - 여기서 `Key`는 위치를 나타내는 NodeId(String), `Value`는 해당 위치에 있는 `Piece` 객체입니다.
- 모든 말의 이동, 잡기(Capture), 그룹핑(Grouping) 등 위치 관련 로직은 이 맵을 통해 처리됩니다.
- 예시: `positionPieceMap.put("P1", piece)`는 `"P1"` 위치에 `piece`가 있다는 것을 의미합니다.
- 말이 아동할 때는 기존 위치의 엔트리를 삭제하고, 새로운 위치에 추가합니다.
  - 예시: `positionPieceMap.remove(startPosition); positionPieceMap.put(targetPosition, piece);`
- START와 END 위치는 별도로 관리되며, 각 플레이어별로 시작 위치(countOfPieceAtStart)와 도착 위치(countOfPieceAtEnd)에 남아있는 말의 개수를 배열로 저장합니다.
- 이를 통해 게임판의 모든 말의 상태와 위치를 일관성 있게 추적할 수 있습니다.

## GameManager: 말 이동

> 내용이 많을 것 같아 따로 작성합니다.
> 
> 말 이동은 `GameController`가 정해주는 게임 흐름에 따라 `GameManager`가 처리합니다.

### START와 END 위치 관리

> START와 END 위치를 별도로 관리해야 하는 이유
>
> Map<String, Piece> 자료형을 사용하면 하나의 노드에 하나의 Piece만 저장할 수 있습니다. 하지만 START와 END 위치는 여러 개의 Piece가 동시에 존재할 수 있습니다. 따라서 별도로 관리해야 합니다.

- START와 END 위치는 기본적으로 말이 할당되어 있지 않으며, 말의 이동이 발생할 때에만 START 위치에 임시적으로 `Map<String, Piece>`에 할당되고 이동 종료 후 START 위치를 제거합니다.
- 따라서 START 위치와 END 위치에 존재하는 말은 `countOfPieceAtStart`와 `countOfPieceAtEnd` 배열로 관리됩니다.
- 예시 : `countOfPieceAtStart[0]`는 플레이어 1의 START 위치에 있는 말의 개수를 나타냅니다. (부가 설명이 필요할 경우 GameManager: 플레이어 관리 참고)

### 말 이동 로직

- Piece의 이동은 `말의 위치 저장`에서 설명했듯이 `Map<String, Piece>` 자료형을 사용해 관리됩니다.
- 말의 이동은 `GameController`에서 `startPosition`과 `targetPosition`을 전달받아 처리합니다. 이때 `startPosition`과 `targetPosition`은 유효한 `Position`이라고 가정합니다.
- 간략한 이동 로직은 다음과 같습니다.
  1. `startPosition`에서 `Piece`를 찾습니다.
  2. `startPosition`과 `targetPosition`을 비교해서 `Capture`, `Grouping`, `NONE`(단순 이동)을 체크하고 사전 작업을 수행합니다.
  3. `startPosition`에서 `Piece`를 제거합니다.
  4. `targetPosition`에 `Piece`를 추가합니다.
  5. `yutHistory`에서 이동에 사용된 윷 정보를 제거합니다.

### 말 이동 로직 : startPosition이 START 위치인 경우

- 앞서 말했듯, START 위치는 기본적으로 `Map`에 할당되어 있지 않으며 말의 이동이 발생할 때에만 임시적으로 할당됩니다.
- 따라서 말 이동 로직에서 `startPosition`이 START 위치인 경우에는 `말 이동 로직 1.`에서 이에 대한 예외 처리가 필요하며, 다음과 같이 처리됩니다.
  1. `startPosition`이 START 위치인 경우, `countOfPieceAtStart`에서 해당 플레이어의 START 위치에 있는 말의 개수를 감소시킵니다.
  2. START 위치에 해당 플레이어의 말을 임시로 할당합니다.
  3. 이동 로직을 수행합니다.

### 말 이동 로직 : targetPosition이 END 위치인 경우

- END 위치는 말이 도착하는 위치로, Map에 할당되지 않습니다. 
- 따라서 `targetPosition`이 END 위치인 경우에는 startPosition인 말을 제거하고 END 위치에 해당 플레이어의 말을 추가합니다.

### GameManager: Capture, Grouping, NONE

- 말의 이동은 `Capture`, `Grouping`, `NONE`(단순 이동)으로 구분됩니다.
- 각각의 경우에 따라 처리 로직이 다르며, `targetPosition`에 담긴 정보에 따라 다르게 처리됩니다.
- 각각의 경우에 대한 처리 로직은 다음과 같습니다.
- `Capture`
  - `targetPosition`에 이미 다른 플레이어의 말이 있는 경우, 해당 플레이어의 말을 잡습니다.
  - 잡힌 말을 `Map<String, Piece>`에서 제거합니다. 
  - `countOfPieceAtStart`에서 해당 플레이어의 START 위치에 있는 말의 개수를 잡힌 말의 개수(grouping size)만큼 증가시킵니다.
  - targetPosition에 해당 플레이어의 말을 추가합니다.
- `Grouping`
  - `targetPosition`에 이미 같은 플레이어의 말이 있는 경우, 해당 플레이어의 말을 그룹핑합니다.
  - `targetPosition`에 있는 말의 `pieceId`에 움직일 말의 `pieceId`를 추가합니다.
  - `startPosition`에 있는 말을 제거합니다.
- `NONE`
  - `targetPosition`에 다른 플레이어의 말이 없는 경우, 단순히 이동합니다.
  - `startPosition`에 있는 말을 제거하고, `targetPosition`에 해당 플레이어의 말을 추가합니다.
- 이후 `yutHistory`에서 이동에 사용된 윷 정보를 제거합니다.

### `startPosition`과 `targetPosition`의 윷 관계를 어떻게 알아내는가?

> `BoardManager`에서 게임 판의 각 노드는 다음 위치에 대한 정보를 알 뿐, **어떤 노드가 자신과 무슨 관계인지 알지 못합니다.**

- `startPosition`과 `targetPosition`의 윷 관계는 `GameManager`의 `getMoveCount(String startPosition, String targetPosition)` 메서드를 통해 확인합니다.
- `getMoveCount` 메서드는 `BoardManager`의 `setPreviousPosition` 메서드를 사용해 `startPosition`에서 `targetPosition`까지의 이동 거리를 계산합니다.
- 이 과정에서 임시적인 `Piece`인 `marker`를 생성합니다. `marker`는 `startPosition` 에 위치한 `Piece`의 복사본입니다. 따라서, 동일한 `previousPosition`을 갖지만 Map에 저장되지 않습니다.
- 세부적인 로직은 다음과 같습니다.
  1. `marker`를 생성합니다.
  2. `for`문을 사용해 `BoardManager`의 `setPreviousPosition`에 `startPosition`, `moveCount = int[-1, 1, 2, 3 ,4, 5]`, `marker`를 전달합니다.
  3. `setPreviousPosition`으로부터 얻은 `nextPosition`을 `targetPosition`과 비교합니다.
  4. `nextPosition`이 `targetPosition`과 같으면 해당 `moveCount`를 반환합니다.
- 이러한 방법으로 `startPosition`과 `targetPosition`의 윷 관계를 확인할 수 있습니다.

### `startPosition`이 유효한 `Position`인지 확인하는 방법

- `StartPosition`이 유효한지 확인하는 로직은 `GameManager`의 `isCurrentPlayersPiecePresent(String positionId)` 메서드에서 처리합니다.
- 이 메서드는 다음과 같은 방식으로 동작합니다.
  - START 위치인 경우, 현재 플레이어의 `countOfPieceAtStart` 값이 1 이상인지 확인합니다.
  - END 위치인 경우, 선택할 수 없는 위치이므로 항상 false를 반환합니다.
  - 그 외의 경우, 해당  위치에 말이 존재하고 그 말이 현재 플레이어의 것인지 확인합니다.
- 이를 통해 현재 플레이어가 실제로 선택할 수 있는 말의 위치만 유효한 `startPosition`으로 간주합니다.

### `targetPosition`이 유효한 `Position`인지 확인하는 방법

- `targetPosition`이 유효한지 확인하는 로직은 `GameManager`의 `isValidMove(String startPosition, String targetPosition)` 메서드에서 처리합니다.
- 이 메서드는 다음과 같은 방식으로 동작합니다.
  1. `startPosition`에서 이동 가능한 모든 윷 값(`YutHistory`에 저장된)에 대해, 실제로 해당 칸 수만큼 이동했을 때의 위치를 계산합니다. 즉, 이동가능한 모든 `Piece`에 대해 `marker`를 생성하고 `YutHistory`만큼 이동시킨 `nextPosition`들 중 `targetPosition`이 존재하는 지 확인합니다.
  2. 계산된 위치가 `targetPosition`과 일치하고, 해당 윷 값이 현재 이동 가능한 윷 값(`YutHistory`)에 포함되어 있으면 유효한 위치로 판단합니다.
- 즉, 현재 이동할 수 있는 윷 값과 게임판의 경로 규칙을 모두 만족하는 경우에만 targetPosition을 유효한 위치로 간주합니다.
