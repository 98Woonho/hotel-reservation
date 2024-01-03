package com.whl.hotelService.controller;

import com.whl.hotelService.boardDomain.dto.BoardDto;
import com.whl.hotelService.boardDomain.service.BoardService;
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
    @GetMapping("")
    public String write(){
        return "board/index";
    }
    @GetMapping("/save")
    public String saveForm(){
        return "board/save";
    }
    @PostMapping("/save")
    public String save(BoardDto boardDto){
        System.out.println("boardDTO = " +boardDto);
        boardService.save(boardDto);
        return "board/index";
    }
    @GetMapping("/list")
    public String findAll(Model model){
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDto> boardDtoList = boardService.findAll();
        model.addAttribute("boardList", boardDtoList);
        return "board/list";
    }
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) throws Exception {
        boardService.updateHits(id); // 조회수를 하나 올리고 게시글 데이터를 가져와서 detail.html 출력
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("board", boardDto);
        return "board/detail";
    }
}
