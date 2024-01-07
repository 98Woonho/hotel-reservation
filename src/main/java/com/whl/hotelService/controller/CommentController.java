package com.whl.hotelService.controller;

import com.whl.hotelService.boardDomain.dto.CommentDto;
import com.whl.hotelService.boardDomain.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("/save")
    public ResponseEntity save(CommentDto commentDto){
        System.out.println("commentDto : " + commentDto);
        Long saveResult = commentService.save(commentDto);
        if (saveResult != null){
            // 작성 성공 로직 댓글 목록을 가져와서 리턴
           List<CommentDto> commentDtoList = commentService.findAll(commentDto.getBoardId());
           return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
