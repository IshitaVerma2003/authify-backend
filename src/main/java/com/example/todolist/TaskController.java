package com.example.todolist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repo;

    // CREATE TASK
    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody Task task) {

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Task title cannot be empty");
        }

        Task savedTask = repo.save(task);
        return ResponseEntity.ok(savedTask);
    }

    // READ ALL TASKS
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(repo.findAll());
    }

    // DELETE TASK
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task not found");
            }

            repo.deleteById(id);
            return ResponseEntity.ok("Task deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting task");
        }
    }

    // UPDATE TASK
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        try {
            Task task = repo.findById(id).orElseThrow();

            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            task.setAssignee(updatedTask.getAssignee());
            task.setStartTime(updatedTask.getStartTime());
            task.setDueDate(updatedTask.getDueDate());
            task.setEndTime(updatedTask.getEndTime());

            return ResponseEntity.ok(repo.save(task));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task not found");
        }
    }
}