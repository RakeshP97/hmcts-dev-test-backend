package uk.gov.hmcts.reform.dev.controller;

import jakarta.validation.Valid;
import org.codehaus.groovy.tools.shell.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    Logger log = Logger.create(TaskController.class);
    @Autowired
    private TaskService taskService;


    @PostMapping("/createNewTask")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }


    @GetMapping("/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/getTaskById")
    public ResponseEntity<Task> getTaskById(@RequestParam Long id) {
        return taskService.getTaskById(id)
            .map(task -> ResponseEntity.ok(task))
            .orElse(ResponseEntity.noContent().build());
    }


    @PostMapping("/UpdateTaskStatus")
    public ResponseEntity<Object> updateTaskStatus(@RequestParam("id") Long id, @RequestParam String status) {
        try {
            Task updatedTask = taskService.updateTaskStatus(id, status);
            log.warn(String.format(
                "Task with id: %s got updated with status: %s",
                id,
                status
            ));
            return updatedTask != null
                ? new ResponseEntity<>(updatedTask, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            log.error(String.format(
                "Exception occurred while update status for given task id: %s, error message: %s",
                id,
                e.getCause()
            ));
            return new ResponseEntity<>(e.getCause(), HttpStatus.NO_CONTENT);
        }

    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>("Requested Task got deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error(String.format(
                "Exception occurred while deleting task with id: %s, error message: %s",
                id,
                e.getCause()
            ));
            return new ResponseEntity<>(e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
