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

> 이 부분은 제가 Yut 클래스에서 YutManager 클래스로 이름만 바꿨지 처음과 동일합니다.
> -> 이 부분은 현정님께 물어보세요 전 잘 모르겠어요 😊

## GameManager

- 이 부분은 내용이 많습니다... 이따 다시 작성할게요
- 그래도 코드는 300줄도 안되긴 합니다. 🙃