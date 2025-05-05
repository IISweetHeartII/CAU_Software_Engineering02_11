# JUnit 5 cheat sheet

---
## Annotations
| 메서드                                             | 설명                 |
|-------------------------------------------------|--------------------|
| `assertEquals(expected, actual)`                | 예상 값과 실제 값 비교      |
| `assertNotEquals(expected, actual)`             | 예상 값과 실제 값이 다름을 확인 |
| `assertTrue(condition)`                         | 조건이 true인지 확인      |
| `assertFalse(condition)`                        | 조건이 false인지 확인     |
| `assertNull(value)`                             | 값이 null인지 확인       |
| `assertNotNull(value)`                          | 값이 null이 아님을 확인    |
| `assertThrows(Exception.class, () -> {...})`    | 예외 발생 확인           |
| `assertAll(() -> ..., () -> ...)`               | 여러 assert를 한번에 실행  |
| `assertArrayEquals(expectedArray, actualArray)` | 배열 비교              |
| `fail("message")`                               | 테스트 실패 강제          |

---
## Assertions
| 메서드                                             | 설명                 |
|-------------------------------------------------|--------------------|
| `assertEquals(expected, actual)`                | 예상 값과 실제 값 비교      |
| `assertNotEquals(expected, actual)`             | 예상 값과 실제 값이 다름을 확인 |
| `assertTrue(condition)`                         | 조건이 true인지 확인      |
| `assertFalse(condition)`                        | 조건이 false인지 확인     |
| `assertNull(value)`                             | 값이 null인지 확인       |
| `assertNotNull(value)`                          | 값이 null이 아님을 확인    |
| `assertThrows(Exception.class, () -> {...})`    | 예외 발생 확인           |
| `assertAll(() -> ..., () -> ...)`               | 여러 assert를 한번에 실행  |
| `assertArrayEquals(expectedArray, actualArray)` | 배열 비교              |
| `fail("message")`                               | 테스트 실패 강제          |


---
## Test Lifecycle
| 어노테이션                        | 설명          |
|------------------------------|-------------|
| `@RepeatedTest(n)`           | n번 반복 실행    |
| `@ParameterizedTest`         | 매개변수 기반 테스트 |
| `@CsvSource`, `@ValueSource` | 파라미터 제공 소스  |
