package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@Getter
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long todoId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Log(String action, Long userId, Long todoId){
        this.action = action;
        this.userId = userId;
        this.todoId = todoId;
        this.createdAt = LocalDateTime.now();
    }
}
