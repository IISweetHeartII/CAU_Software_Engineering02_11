# 윷놀이 게임 프로젝트

이 프로젝트는 중앙대학교 소프트웨어공학 02분반 11팀의 윷놀이 게임을 구현한 것입니다.

이 프로젝트는 윷놀이의 규칙을 따르며, 사용자 인터페이스를 통해 게임을 즐길 수 있도록 설계되었습니다.

---

## ⚠️ (필독) 브랜치 관리

이 프로젝트는 다음과 같은 브랜치 관리 방식을 따릅니다:
- `main` 브랜치: 현재 프로젝트의 diagram과 같은 코드 이외의 파일이 포함되어 있습니다.
- `release` 브랜치: .jar 파일로 배포하기 위한 브랜치입니다. _**이 브랜치는 diagram과 같은 코드 이외의 파일이 포함되어 있지 않습니다. 평가시에 main브랜치인지 확인해주세요.**_

## 프로그램 실행

### 사전 준비 사항

이 프로젝트를 실행하기 위해서는 다음과 같은 사전 준비가 필요합니다:

- Java Development Kit (JDK) 21 이상
- javaFx 21
- IntelliJ IDEA 2025.1.1.1
  - .jar 파일이 아닌 IDEA에서 직접 실행하는 것을 권장합니다.

### 사전 주의 사항

IDE와 .jar 파일로 실행하는 방법이 다릅니다.

- IDE에서 실행할 경우, `main` 브랜치 파일을 사용해주세요.
- .jar 파일로 실행할 경우, `release` 브랜치 파일을 사용해주세요.

### IDE에서 실행할 경우

1. javaFx 21을 사용하기위한 IDE 설정을 수행합니다.
2. IntelliJ IDEA를 열고 `main` 브랜치의 프로젝트를 엽니다.
3. data 폴더에 있는 `config.txt` 파일을 수정하여 게임 설정을 변경할 수 있습니다.
4. main 패키지에서 `Main.java` 파일을 찾습니다.
5. `Main.java` 파일을 열고, 상단의 실행 버튼(녹색 화살표)을 클릭하여 프로그램을 실행합니다.

### .jar 파일로 실행할 경우

1. 만약 .jar 파일을 직접 얻고자 한다면, `release` 브랜치의 프로젝트를 ItelliJ IDEA에서 빌드하여 .jar 파일을 생성합니다.
2. 생성된 .jar 파일을 `data` 폴더와 동일한 위치에 두고, 명령 프롬프트 또는 터미널에서 다음 명령어를 실행합니다. `your-java-fx/lib`는 JavaFX 라이브러리가 위치한 경로로 변경해야 합니다.
```bash
java --module-path "your-java-fx/lib" --add-modules javafx.controls,javafx.fxml -jar SE_Project_02_11.jar
```

위 실행 방법들은 모두 로컬 테스트를 완료했으며, 정상적으로 작동하는 것을 확인했습니다. 만약 실행 중 문제가 발생한다면, 해당 문제를 이슈로 등록해 주시기 바랍니다.

---

## 게임 규칙

### 말의 이동

윷놀이 게임에서 말의 이동할 때의 사용자 입력은 다음과 같습니다.

- START 위치와 END 위치는 별도로 관리됩니다:
  - START 위치는 Score 부분의 UI를 클릭하여 선택합니다.
  - END 위치는 윷 결과 UI를 클릭하여 선택합니다.


- 사용자가 말을 이동하는 방법은 다음과 같습니다:

  - 윷을 던진 후, 이동할 말을 선택합니다.
  - 이동할 말을 선택한 후, 이동할 위치를 선택합니다.
  - 만약 선택한 말을 다시 선택한다면, 해당 말의 이동을 취소하고 다시 이동할 말을 선택할 수 있습니다.
  - 자신이 얻은 윷 결과를 모두 사용할 때까지 이동할 수 있습니다.

### 빽도

- 만약 `START 위치`에서 빽도를 사용하고자 한다면 게임판의 `마지막 위치(END 이전 위치)`에 말을 놓을 수 있습니다.
- `도(P1)` 위치에서 빽도를 사용하면 게임판의 `마지막 위치(END 이전 위치)`에 말을 놓을 수 있습니다.
- 빽도는 말의 이전 위치로 이동하는 것이며, `개(P2)` 위치에서 빽도를 사용하면 `도(P1)` 위치로 이동할 수 있습니다.
  - 이때 다시 빽도를 사용하면 `개(P2)` 위치로 이동할 수 있습니다.

### 중앙 분기

- 말이 중앙에서 이동을 시작할 경우, 게임판의 마지막 위치로의 최단 경로로 이동합니다.
- 특수한 상태인 오각형에서의 중앙 분기 또는 중앙 넘김은 함께 제출하는 시연 영상을 참고해주세요.
  - 텍스트로 설명드리자면, `P5` 위치에서 시작한 중앙 넘김은 `P20` 경로로 이동합니다.
  - `P10` 위치에서 시작한 중앙 넘김은 `P25` 경로로 이동합니다.
  - `P15` 위치에서 시작한 중앙 넘김은 `P35` 경로로 이동합니다.

### 게임 종료

- 승자 표시는 게임판의 `Turn` UI에 표시됩니다.