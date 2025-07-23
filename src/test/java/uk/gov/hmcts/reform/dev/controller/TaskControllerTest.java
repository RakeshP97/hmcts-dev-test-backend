package uk.gov.hmcts.reform.dev.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Ensures test-specific properties are loaded (e.g., in-memory H2)
public class TaskControllerTest {

    @Value("${TEST_URL:https://localhost}")
    private String testUrl;
    @LocalServerPort
    private int port;

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = testUrl;
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
        taskRepository.deleteAll();
    }

    @Test
    void createNewTask_savedTheNewTask() {
        Task task1 = new Task("Test", "Test Task", "new", LocalDate.now());
        taskRepository.save(task1);
        given()
            .contentType(ContentType.JSON)
            .body(task1)
            .when()
            .post("/api/task/createNewTask")
            .then()
            .statusCode(201)
            .body("status", equalTo("new"))
            .body("title", equalTo("Test"));

    }


    @Test
    void getAllTask_shouldReturnAllTheSavedTasks() {
        Task task = new Task("Test", "Test Task", "new", LocalDate.now());
        taskRepository.save(task);
        Task task1 = new Task("Test2", "Test Task", "new", LocalDate.now());
        taskRepository.save(task1);

        int count = taskRepository.findAll().size();

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/task/getAllTasks")
            .then()
            .statusCode(200) // HTTP 200 OK
            .body("$", hasSize(count)) // Expect two tasks in the list
            .body("[0].title", equalTo("Test"))
            .body("[1].title", equalTo("Test2"));
    }


    @Test
    void getTaskById_shouldReturnAllTheSavedTasks() {
        Task task1 = new Task("Test2", "Test Task", "new", LocalDate.now());
        taskRepository.save(task1);
        given()
            .queryParam("id", task1.getTaskId())
            .when()
            .get("/api/task/getTaskById")
            .then()
            .log().all()
            .statusCode(200) // HTTP 200 OK
            .body("title", equalTo("Test2"));
        assertThat(taskRepository.findById(task1.getTaskId())).isNotEmpty();
    }

    @Test
    void getTaskById_WhenNoMatchingTaskFound() {
        given()
            .queryParam("id", 2L)
            .when()
            .get("/api/task/getTaskById")
            .then()
            .log().all()
            .statusCode(204); // HTTP 204 No Content
    }

    @Test
    void updateTask_BasedOnIdWithStatus() {
        Task task1 = new Task("Test2", "Test Task", "new", LocalDate.now());
        taskRepository.saveAndFlush(task1);
        given()
            .queryParam("id", task1.getTaskId())
            .queryParam("status", "Inprogress")
            .when()
            .post("/api/task/UpdateTaskStatus")
            .then()
            .statusCode(200) // HTTP 200 OK
            .body("status", equalTo("Inprogress"));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask_whenFound() {
        given()
            .queryParam("id", 4L)
            .queryParam("status", "Inprogress")
            .when()
            .post("/api/task/UpdateTaskStatus")
            .then()
            .statusCode(204);// HTTP 500 OK;
    }

    @Test
    void deleteTask_shouldReturnNoContent_whenDeleted() {
        Task task1 = new Task("Test2", "Test Task", "new", LocalDate.now());
        taskRepository.saveAndFlush(task1);
        given()
            .when()
            .delete("/api/task/deleteTask/{id}", task1.getTaskId())
            .then()
            .statusCode(200); // HTTP 204 No Content

        // Verify it's deleted from the database
        assertFalse(taskRepository.existsById(task1.getTaskId()));
    }

}
