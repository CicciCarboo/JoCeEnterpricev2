package se.joce.springv2.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.joce.springv2.todo.model.TodoItem;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    int countAllByCompleted(boolean completed);
    List<TodoItem> findAllByCompleted(boolean completed);
}
