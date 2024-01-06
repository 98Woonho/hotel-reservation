package com.whl.hotelService.boardDomain.dto;

import com.whl.hotelService.boardDomain.entity.BoardEntity;
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

    public BoardDto(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDto EntityToDto(BoardEntity boardEntity) { //빌더 패턴 사용
        BoardDto boardDto = BoardDto.builder()
                .id(boardEntity.getId())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContents(boardEntity.getBoardContents())
                .boardHits(boardEntity.getBoardHits())
                .boardCreatedTime(boardEntity.getCreatedTime())
                .boardPassword(boardEntity.getBoardPassword())
                .boardWriter(boardEntity.getBoardWriter())
                .boardUpdatedTime(boardEntity.getUpdatedTime())
                .build();
        return boardDto;
    }

}
