module SE_Project_02_11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    exports view;  // JavaFX UI가 있는 패키지
    exports main;  // 메인 클래스가 있는 패키지
}