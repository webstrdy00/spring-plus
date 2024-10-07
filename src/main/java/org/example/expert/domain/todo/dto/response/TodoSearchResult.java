package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TodoSearchResult {
    private Long id;
    private String title;
    private int managerCount;
    private int commentCount;
    private LocalDateTime createAt;

    @QueryProjection // @QueryProjection을 사용하여 QueryDSL이 이 생성자를 인식하고 타입 안전한 프로젝션을 생성할 수 있게 합니다.
    public TodoSearchResult(Long id, String title, int managerCount, int commentCount, LocalDateTime createAt) {
        this.id = id;
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
        this.createAt = createAt;
    }
}
