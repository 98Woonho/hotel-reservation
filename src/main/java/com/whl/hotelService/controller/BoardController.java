package com.whl.hotelService.controller;

import com.whl.hotelService.boardDomain.dto.BoardDto;
import com.whl.hotelService.boardDomain.service.BoardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @GetMapping("/save") // 글작성 폼
    public String saveForm(){
        return "board/save";
    }
    @PostMapping("/save") //글작성
    public String save(@Valid BoardDto boardDto) throws Exception{
        System.out.println("boardDTO = " +boardDto);
        boardService.save(boardDto);
        return "redirect:/board/list";
    }
    @GetMapping("/list")  // 목록화면
    public String findAll(@PageableDefault(page = 0, size = 10) Pageable pageable, String searchText, Model model){

        Page<BoardDto> boardDtos = null;
        if (searchText == null){
            boardDtos = boardService.findAll(pageable);
        }else {
            boardDtos = boardService.findByBoardTitleContainingOrBoardWriterContaining(searchText, searchText, pageable);
        }

        int startPage = Math.max(1, boardDtos.getPageable().getPageNumber() - 5);
        int endPage = Math.min(boardDtos.getTotalPages(), boardDtos.getPageable().getPageNumber() + 5);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boardList", boardDtos);
        return "board/list";
    }


    @GetMapping("/{id}") // 조회수 올리기
    public String findById(@PathVariable Long id, Model model) throws Exception {
        boardService.updateHits(id); // 조회수를 하나 올리고 게시글 데이터를 가져와서 detail.html 출력
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("board", boardDto);
        return "board/detail";
    }
    @GetMapping("/update/{id}") //수정화면
    public String updateForm(@PathVariable Long id, Model model){
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDto);
        return "board/update";
    }
    @PostMapping("/update") //수정
    public String update(BoardDto boardDto, Model model) throws Exception{
        BoardDto board = boardService.update(boardDto);
        model.addAttribute("board", board);
        return "board/detail";
    }
    @GetMapping("/delete/{id}") //삭제
    public String delete(@PathVariable Long id) throws Exception{
        boardService.delete(id);
        return "redirect:/board/list";
    }


}
