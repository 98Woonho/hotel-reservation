package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String email;



    @Builder  //Entity를 Dto로 변환해서 리턴
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedTime();
        this.username = board.getUser().getUser_id();
        this.email = board.getUser().getEmail();
    }
}
