package com.whl.hotelService.boardDomain.entity;

import com.whl.hotelService.boardDomain.dto.BoardDto;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "board_table")
public class BoardEntity extends DateEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(length = 20, nullable = false) // 길이가 20이고 not null
    private String boardWriter;
    @Column(length = 20, nullable = false)
    private String boardPassword;
    @Column(length = 20, nullable = false)
    private String boardTitle;
    @Column(length = 500, nullable = false)
    private String boardContents;
    private int boardHits;

    public static BoardEntity DtoToEntity(BoardDto boardDto){ //DTO에 담긴 값들을 Entity 로 옮기는 코드 즉 DTO를 Entity로 변환하는 코드
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDto.getBoardWriter());
        boardEntity.setBoardPassword(boardDto.getBoardPassword());
        boardEntity.setBoardTitle(boardDto.getBoardTitle());
        boardEntity.setBoardContents(boardDto.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }
}
