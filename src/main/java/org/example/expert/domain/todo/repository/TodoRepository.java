package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoQuerydslRepository {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);


    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user " +
            "WHERE (:weather IS NULL OR t.weather = :weather) " +
            "AND (:startDate IS NULL OR DATE(t.modifiedAt) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(t.modifiedAt) <= :endDate) " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodosByWeatherAndDateRange(
            @Param("weather") String weather,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
