package com.example.oop.gui;

import com.example.oop.dto.MemberRequestDto;
import com.example.oop.entity.Member;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

public class MemberApp extends Application {

    private TableView<Member> memberTable = new TableView<>();
    private ObservableList<Member> members = FXCollections.observableArrayList();
    private RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/api/members";

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox(10);

        // 회원 추가 폼
        TextField nameField = new TextField();
        DatePicker birthDateField = new DatePicker();
        TextField phoneField = new TextField();
        Button addButton = new Button("Add Member");

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name.isEmpty() || birthDateField.getValue() == null || phoneField.getText().isEmpty()) {
                showAlert("Error", "All fields must be filled!");
                return;
            }

            String phone = phoneField.getText();
            int birthYear = birthDateField.getValue().getYear();
            int birthMonth = birthDateField.getValue().getMonthValue();
            int birthDay = birthDateField.getValue().getDayOfMonth();

            // DTO 형식 맞춰서 요청 보내기
            MemberRequestDto.SignRequestDTO requestDTO = new MemberRequestDto.SignRequestDTO(name, phone, birthYear, birthMonth, birthDay);
            try {
                restTemplate.postForObject(BASE_URL, requestDTO, MemberRequestDto.SignRequestDTO.class);
                loadMembers();  // 회원 목록 새로고침
            } catch (Exception ex) {
                showAlert("Error", "Failed to add member.");
            }
        });


        // 테이블 설정
        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Member, String> birthDateColumn = new TableColumn<>("Birth Date");
        birthDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBirthDate().toString()));

        TableColumn<Member, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        memberTable.getColumns().addAll(nameColumn, birthDateColumn, phoneColumn);
        layout.getChildren().addAll(new Label("Name:"), nameField, new Label("Birth Date:"), birthDateField, new Label("Phone:"), phoneField, addButton, memberTable);

        loadMembers(); // 애플리케이션 시작 시 회원 목록 로드

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("Member Management");
        stage.setScene(scene);
        stage.show();
    }

    private void loadMembers() {
        try {
            members.clear();
            ResponseEntity<Member[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, HttpEntity.EMPTY, Member[].class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                members.addAll(response.getBody());
                memberTable.setItems(members);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load members.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
