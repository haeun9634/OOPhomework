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

import java.time.LocalDate;
import java.util.Arrays;

public class MemberApp extends Application {

    private TableView<Member> memberTable = new TableView<>(); //회원 정보를 표시할 테이블
    private ObservableList<Member> members = FXCollections.observableArrayList(); //테이블에 바인딩할 회원 목록
    private RestTemplate restTemplate = new RestTemplate(); //REST API 호출
    private static final String BASE_URL = "http://localhost:8080/api/members"; //백엔드 API 주소

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox(10);

        // 회원 추가 기능 UI 생성 및 등록
        VBox addMemberBox = createAddMemberUI();

        // 회원 조회 기능 UI 생성
        Label listMembersLabel = new Label("회원 조회");
        Button loadMembersButton = new Button("회원 목록 불러오기");
        loadMembersButton.setOnAction(e -> loadMembers());

        VBox listMembersBox = new VBox(5, listMembersLabel, loadMembersButton);

        // 회원 검색 기능 UI 생성
        VBox searchBox = createSearchMemberUI();

        // 테이블 설정
        setupMemberTable();

        // 레이아웃에 요소 추가
        layout.getChildren().addAll(addMemberBox, listMembersBox, searchBox, memberTable);

        // 씬 생성 및 설정
        Scene scene = new Scene(layout, 600, 600);
        stage.setTitle("회원 관리 시스템");
        stage.setScene(scene);
        stage.show();
    }

    //회원 추가
    private VBox createAddMemberUI() {
        Label addMemberLabel = new Label("회원 추가");
        TextField nameField = new TextField();
        nameField.setPromptText("이름 입력");

        DatePicker birthDateField = new DatePicker();
        birthDateField.setPromptText("생년월일 선택");

        TextField phoneField = new TextField();
        phoneField.setPromptText("전화번호 입력");

        Button addButton = new Button("회원 추가");
        addButton.setOnAction(e -> addMember(nameField, birthDateField, phoneField));

        return new VBox(5, addMemberLabel, nameField, birthDateField, phoneField, addButton);
    }

    //회원 추가 API 연결
    private void addMember(TextField nameField, DatePicker birthDateField, TextField phoneField) {
        String name = nameField.getText();
        if (name.isEmpty() || birthDateField.getValue() == null || phoneField.getText().isEmpty()) {
            showAlert("Error", "모든 항목을 채워주세요!");
            return;
        }

        LocalDate birthDate = birthDateField.getValue();
        String phone = phoneField.getText();

        MemberRequestDto.SignRequestDTO requestDTO = new MemberRequestDto.SignRequestDTO(name, phone, birthDate);
        try {
            Member newMember = restTemplate.postForObject(BASE_URL, requestDTO, Member.class);
            if (newMember != null) {
                members.clear();
                members.add(newMember);
                memberTable.setItems(members);
                showAlert("Success", "회원 추가가 완료되었습니다!");
            }
        } catch (Exception ex) {
            showAlert("Error", "회원 추가에 실패하였습니다.");
        }
    }


    //회원 목록 API 연결
    private void loadMembers() {
        try {
            members.clear();
            ResponseEntity<Member[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, HttpEntity.EMPTY, Member[].class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                members.addAll(Arrays.asList(response.getBody()));
                memberTable.setItems(members);
            }
        } catch (Exception e) {
            showAlert("Error", "회원 목록을 조회하는데 실패하였습니다.");
        }
    }

    //테이블 설정
    private void setupMemberTable() {
        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Member, String> birthDateColumn = new TableColumn<>("Birth Date");
        birthDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBirthDate().toString()));

        TableColumn<Member, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        memberTable.getColumns().addAll(nameColumn, birthDateColumn, phoneColumn);
    }

    //회원 검색 UI 설정
    private VBox createSearchMemberUI() {
        Label searchMemberLabel = new Label("이름으로 회원 검색하기");
        TextField searchField = new TextField();
        searchField.setPromptText("검색할 이름을 입력하세요.");
        Button searchButton = new Button("검색");

        searchButton.setOnAction(e -> {
            String searchName = searchField.getText();
            if (searchName.isEmpty()) {
                showAlert("Error", "검색할 이름을 입력하세요!");
                return;
            }
            searchMembers(searchName);
        });

        return new VBox(5, searchMemberLabel, searchField, searchButton);
    }

    //회원 검색 API 연결
    private void searchMembers(String name) {
        try {
            ResponseEntity<Member[]> response = restTemplate.exchange(
                    BASE_URL + "/search?name=" + name, HttpMethod.GET, HttpEntity.EMPTY, Member[].class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                members.clear();
                members.addAll(Arrays.asList(response.getBody()));
                memberTable.setItems(members);
            }
        } catch (Exception e) {
            showAlert("Error", "회원 검색에 실패하였습니다.");
        }
    }

    //알림창 띄우기
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
