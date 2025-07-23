package uk.gov.hmcts.reform.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(final Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task updateTaskStatus(Long id, String status) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task oldTask = existingTask.get();
            oldTask.setStatus(status);
            return taskRepository.save(oldTask);
        }
        return null;
    }

    public void deleteTask(final Long taskId) {
        taskRepository.deleteById(taskId);
    }

}
