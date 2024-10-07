package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.QTodoSearchResult;
import org.example.expert.domain.todo.dto.response.TodoSearchResult;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class TodoQuerydslRepositoryImpl implements TodoQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResult> search(TodoSearchCondition condition, Pageable pageable) {
        List<TodoSearchResult> content = (List<TodoSearchResult>) queryFactory
                .select(new QTodoSearchResult(   // QTodoSearchResult는 @QueryProjection으로 생성된 프로젝션
                        todo.id,
                        todo.title,
                        todo.managers.size(),  // 담당자 수
                        todo.comments.size(),  // 댓글 수
                        todo.createdAt
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(
                        titleContains(condition.getKeyword()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate()),
                        assigneeNicknameContains(condition.getAssigneeNickname())
                )
                .groupBy(todo.id) // 중복 제거를 위한 그룹화
                .orderBy(todo.createdAt.desc()) // 생성일 기준 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 생성
        JPAQuery<Long> countQuery = queryFactory
                .select(todo.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(condition.getKeyword()),
                        createdAtBetween(condition.getStartDate(), condition.getEndDate()),
                        assigneeNicknameContains(condition.getAssigneeNickname())
                );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 제목 검색 조건
    private BooleanExpression titleContains(String keyword) {
        return keyword != null ? todo.title.containsIgnoreCase(keyword) : null;
    }

    // 생성일 범위 검색 조건
    private BooleanExpression createdAtBetween(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;

        if (start != null && end != null) {
            return todo.createdAt.between(start, end);
        } else if (start != null) {
            return todo.createdAt.goe(start);
        } else if (end != null) {
            return todo.createdAt.lt(end);
        }
        return null;
    }

    // 담당자 닉네임 검색 조건
    private BooleanExpression assigneeNicknameContains(String nickname) {
        return nickname != null ? user.nickname.containsIgnoreCase(nickname) : null;
    }
}
