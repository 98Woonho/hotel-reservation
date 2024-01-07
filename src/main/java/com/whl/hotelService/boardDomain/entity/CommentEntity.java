package com.whl.hotelService.boardDomain.entity;

import com.whl.hotelService.boardDomain.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment_table")
public class CommentEntity extends DateEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String commentWriter;
    @Column
    private String commentContents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;


    public static CommentEntity DtoToSaveEntity(CommentDto commentDto, BoardEntity boardEntity) {
        CommentEntity commentEntity = CommentEntity.builder()
                .commentWriter(commentDto.getCommentWriter())
                .commentContents(commentDto.getCommentContents())
                .boardEntity(boardEntity)
                .build();
        return commentEntity;
    }
}
