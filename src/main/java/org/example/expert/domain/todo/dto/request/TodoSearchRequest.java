package org.example.expert.domain.todo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoSearchRequest {
    private String weather;
    private LocalDate startDate;
    private LocalDate endDate;
}
