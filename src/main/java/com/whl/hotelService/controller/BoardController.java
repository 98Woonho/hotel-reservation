package com.whl.hotelService.controller;

import com.whl.hotelService.domain.dto.BoardDto;
import com.whl.hotelService.domain.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value ="board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    //게시글 페이지
    @GetMapping(value = "")
    public String list(Model model) {
        List<BoardDto> boardList = boardService.getBoardlist();

        model.addAttribute("boardList", boardList);
        log.info("GET ......... board/list");
        return "board/list";
    }

    //게시글 쓰기 페이지
    @GetMapping(value ="post")
    public String write() {
        log.info("GET ......... board/post");
        return "board/write";
    }

    //게시글 쓰기
    @PostMapping(value = "post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        log.info("Post ......... board/post");
        return "redirect:/home/homepage";
    }

    //게시글 조회 페이지
    @GetMapping(value = "post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDTO = boardService.getPost(id);

        model.addAttribute("boardDto", boardDTO);
        log.info("GET ......... post/{id}");
        return "board/detail";
    }

    //게시글 수정 페이지
    @GetMapping(value = "post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDTO = boardService.getPost(id);

        model.addAttribute("boardDto", boardDTO);
        log.info("GET ......... post/edit/{id}");
        return "board/update";
    }

    //게시글 수정
    @PutMapping(value = "post/edit/{id}")
    public String update(BoardDto boardDTO) {
        boardService.savePost(boardDTO);

        log.info("PUT ......... post/edit/{id}");
        return "redirect:/home/homepage";
    }

    //게시글 삭제
    @DeleteMapping(value = "post/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);

        log.info("Delete ......... post/{id}");
        return "redirect:/home/homepage";
    }

}
