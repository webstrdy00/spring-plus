package org.example.expert.domain.todo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoSearchCondition {
    private String keyword;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assigneeNickname;
}
