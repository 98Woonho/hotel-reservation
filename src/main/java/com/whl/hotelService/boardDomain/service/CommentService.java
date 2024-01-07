package com.whl.hotelService.boardDomain.service;

import com.whl.hotelService.boardDomain.dto.CommentDto;
import com.whl.hotelService.boardDomain.entity.BoardEntity;
import com.whl.hotelService.boardDomain.entity.CommentEntity;
import com.whl.hotelService.boardDomain.repository.BoardRepositoy;
import com.whl.hotelService.boardDomain.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepositoy boardRepositoy;

    public Long save(CommentDto commentDto) {
        Optional<BoardEntity> optionalBoardEntity = boardRepositoy.findById(commentDto.getBoardId());
        if (optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.DtoToSaveEntity(commentDto, boardEntity);
            return commentRepository.save(commentEntity).getId();
        }else {
            return null;
        }
    }

    public List<CommentDto> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepositoy.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList){
            CommentDto commentDto = CommentDto.EntityToCommentDto(commentEntity, boardId);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }
}
