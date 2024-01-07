package com.whl.hotelService.boardDomain.dto;

import com.whl.hotelService.boardDomain.entity.BoardEntity;
import com.whl.hotelService.boardDomain.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드를 매개변수로 하는 생성자
public class CommentDto {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDto EntityToCommentDto(CommentEntity commentEntity, Long boardId) {
        CommentDto commentDto = CommentDto.builder()
                .id(commentEntity.getId())
                .commentWriter(commentEntity.getCommentWriter())
                .commentContents(commentEntity.getCommentContents())
                .commentCreatedTime(commentEntity.getCreatedTime())
                .boardId(boardId)
                .build();
        return commentDto;
    }
}
