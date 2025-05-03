# Cursor 작업 지침

# Cursor 작업 지시 사항
- 내가 파일을 열고 `// TODO`를 적으면, 그 주석에 맞게 구현 코드를 작성해줘.
- UML 구조를 기반으로 클래스 간 의존성이 있도록 설계해줘.
- `Main.java`는 엔트리포인트로만 사용하고, 실제 로직은 `GameEngine.java`, `Board.java`, `Player.java` 등으로 분리해줘.
- UI 변경이 최소화되도록 로직과 UI는 확실히 분리해서 설계해.

## 코드 생성 시 지침
1. 모든 Java 클래스는 Google Java Style Guide를 따릅니다.
2. 모든 public 메소드와 클래스에는 Javadoc 주석을 포함합니다.
3. 변수명은 camelCase를 사용합니다.
4. 클래스명은 PascalCase를 사용합니다.

## 코드 리뷰 시 지침
1. 코드의 가독성을 최우선으로 합니다.
2. 불필요한 주석은 제거합니다.
3. 메소드는 단일 책임 원칙을 따릅니다.
4. 테스트 코드는 모든 public 메소드를 커버해야 합니다. 