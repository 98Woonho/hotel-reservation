package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.BoardDto;
import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.service.BoardService;
import com.whl.hotelService.domain.common.service.BoardServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value ="board")
public class BoardController {
    @Autowired
    private BoardServiceImpl boardService;

    @GetMapping("/write")
    public String writeForm(){
        return "board/write";
    }


    @PostMapping("/write") // 게시판 글쓰기 로그인된 유저만 글을 쓸수 있음
    public String write(BoardWriteRequestDto boardWriteRequestDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boardService.saveBoard(boardWriteRequestDto, userDetails.getUsername());

        return "redirect:/";
    }

    @GetMapping("/{id}") // 게시판 조회
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.boardDetail(id);
        model.addAttribute("board", board);
        model.addAttribute("id", id);

        return "board/detail";
    }

    @GetMapping("/list") // 게시판 전체 조회
    public String boardList(Model model){
        List<BoardResponseDto> boardList = boardService.boardList();
        model.addAttribute("boardList", boardList);

        return "board/list";
    }

    @GetMapping("/{id}/update") // 게시판 업데이트
    public String boardUpdateForm(@PathVariable Long id, Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //로그인된 회원을 조회해서
        BoardResponseDto board = boardService.boardDetail(id);
        if (!(board.getUsername().equals(userDetails.getUsername()))) { //확인해서 같으면 수정페이지 이동
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        }
        else {
            model.addAttribute("board", board);
            model.addAttribute("id", id);

            return "board/update";
        }
    }

    @PostMapping("/{id}/update")
    public String boardUpdate(@PathVariable Long id, BoardWriteRequestDto boardWriteRequestDto) {
        boardService.boardUpdate(id, boardWriteRequestDto);

        return "redirect:/board/" + id;
    }

    @GetMapping("/{id}/remove")
    public String boardRemove(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BoardResponseDto board = boardService.boardDetail(id);
        if (!(board.getUsername().equals(userDetails.getUsername()))) {
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        }
        else {
            boardService.boardRemove(id);
            return "redirect:/board/list";
        }
    }
}