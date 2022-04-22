package se.joce.springv2.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoItemDto {
    long id;
    String title;
    boolean completed;
}
