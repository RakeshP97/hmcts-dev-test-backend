package uk.gov.hmcts.reform.dev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task("title", "Test Task", "New", LocalDate.now());
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetAllTasks() {
        Task task = new Task("title", "Test Task", "New", LocalDate.now());
        Task task1 = new Task("title", "Test Task", "New", LocalDate.now());

        List<Task> tasks = List.of(task, task1);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> allTasks = taskService.getAllTasks();

        assertEquals(2, allTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        Task task = new Task("title", "Test Task", "New", LocalDate.now());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.getTaskById(1L);

        assertTrue(foundTask.isPresent());
        assertEquals(task, foundTask.get());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTaskStatus() {
        Task task = new Task("title", "Test Task", "New", LocalDate.now());
        task.setStatus("oldStatus");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = taskService.updateTaskStatus(1L, "newStatus");

        assertNotNull(updatedTask);
        assertEquals("newStatus", updatedTask.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
