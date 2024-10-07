package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResult;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoQuerydslRepository {
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
    Page<TodoSearchResult> search(TodoSearchCondition condition, Pageable pageable);
}
