package com.whl.hotelService.domain.service;

import com.whl.hotelService.domain.dto.BoardDto;
import com.whl.hotelService.domain.entity.BoardEntity;
import com.whl.hotelService.domain.repository.BoardRepositoy;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepositoy boardRepositoy;

    //게시글 쓰기
    @Transactional
    public Long savePost(@NotNull BoardDto boardDto) {
        return boardRepositoy.save(boardDto.toEntity()).getId();
    }

    //게시글 조회
    @Transactional
    public List<BoardDto> getBoardlist() {
        List<BoardEntity> boardEntities = boardRepositoy.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntities) {
            BoardDto boardDTO = BoardDto.builder()
                    .id(boardEntity.getId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .writer(boardEntity.getWriter())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();

            boardDtoList.add(boardDTO);
        }

        return boardDtoList;
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardEntityWrapper = boardRepositoy.findById(id.intValue());
        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDTO = BoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .createdDate(boardEntity.getCreatedDate())
                .build();

        return boardDTO;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepositoy.deleteById(id.intValue());
    }


}

