package com.whl.hotelService.domain.dto;

import com.whl.hotelService.domain.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

//DTO(Data Transfer Object) 파라미터들을 전송하기 위한 Dto
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드를 매개변수로 하는 생성자
public class BoardDto {
    private Long id;
    private String boardWriter; //작성자
    private String boardPassword; //비밀번호
    private String boardTitle; //제목
    private String boardContents; //내용
    private int boardHits; //조회수
    private LocalDateTime boardCreatedTime; // 작성시간
    private LocalDateTime boardUpdatedTime; // 수정시간


    public static BoardDto EntityToDto(BoardEntity boardEntity){
        BoardDto boardDto = new BoardDto();
        boardDto.setId(boardEntity.getId());
        boardDto.setBoardTitle(boardEntity.getBoardTitle());
        boardDto.setBoardWriter(boardEntity.getBoardWriter());
        boardDto.setBoardPassword(boardEntity.getBoardPassword());
        boardDto.setBoardContents(boardEntity.getBoardContents());
        boardDto.setBoardHits(boardEntity.getBoardHits());
        boardDto.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDto.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        return boardDto;
    }
}
