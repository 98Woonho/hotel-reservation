package com.example.ITHOTEL.controller;


import com.example.ITHOTEL.domain.common.dto.BoardDto;
import com.example.ITHOTEL.domain.common.dto.BoardFileDto;
import com.example.ITHOTEL.domain.common.dto.HotelDto;
import com.example.ITHOTEL.domain.common.entity.Hotel;
import com.example.ITHOTEL.domain.common.entity.NoticeBoard;
import com.example.ITHOTEL.domain.common.entity.NoticeBoardFileInfo;
import com.example.ITHOTEL.service.BoardService;
import com.example.ITHOTEL.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "board")
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private HotelService hotelService;

    @GetMapping("/inquiryForm")
    public void inquiryForm(Model model) {

        List<String> hotelnames = boardService.searchHotelname();
        model.addAttribute("hotelnames", hotelnames);
    }

    @PostMapping("/inquiryForm") // 문의 게시판 글쓰기 로그인된 유저만 글을 쓸수 있음
    public String inquiryFromWrite(BoardDto boardDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boardService.saveInquiryBoard(boardDto, userDetails.getUsername());
        return "redirect:/user/inquiryInfo?function=read";
    }

    @GetMapping("/question/{id}") // 자주하는 질문 조회 수정
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardDto board = boardService.questionBoardDetail(id);
        model.addAttribute("board", board);
        model.addAttribute("id", id);

        return "board/questionDetail";
    }

    @GetMapping("/question") // 게시판 전체 조회 + paging 처리 + 검색처리 + 답변완료 처리
    public void boardList(@PageableDefault(page = 0, size = 10)Pageable pageable, Authentication authentication, Model model, String keyword, String type) {

        Page<BoardDto> boardList = boardService.questionBoardList(pageable);
        Page<BoardDto> boardSerchList = boardService.searchingQuestionBoardList(keyword, type, pageable);
        if (keyword == null) {
            model.addAttribute("boardList", boardList);
        } else {
            model.addAttribute("boardList", boardSerchList);
        }
    }

    @GetMapping("/question/{id}/update") // 게시판 업데이트화면 이동
    public String boardUpdateForm(@PathVariable Long id, Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //로그인된 회원을 조회해서
        BoardDto board = boardService.questionBoardDetail(id);
        if (!(board.getUserid().equals(userDetails.getUsername()))) { //확인해서 같으면 수정페이지 이동
            return "redirect:/";
        } else {
            model.addAttribute("board", board);
            model.addAttribute("id", id);

            return "board/questionUpdate";
        }
    }

    @PostMapping("/question/{id}/update") // 게시글 업데이트
    public String boardUpdate(@PathVariable Long id, BoardDto boardDto) {
        boardService.questionBoardUpdate(id, boardDto);

        return "redirect:/board/question/{id}";
    }

    @GetMapping("/question/{id}/remove") // 게시판 삭제
    public String boardRemove(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BoardDto board = boardService.questionBoardDetail(id);
        if (!(board.getUserid().equals(userDetails.getUsername()))) {
            return "redirect:/";
        } else {
            boardService.questionBoardRemove(id);
            return "redirect:/board/question";
        }
    }

    @GetMapping("/basic")
    public void boardBasic(){

    }
    @GetMapping("hotelContact")
    public void hotelContact(HotelDto hotelDto, Model model){
        List<Hotel> hotels = hotelService.hotelContact(hotelDto);
        model.addAttribute("hotels", hotels);
    }
    @GetMapping("noticeList")
    public void noticeBoardList(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
                                BoardFileDto boardFileDto,
                                Model model) throws IOException {
        Page<BoardFileDto> noticeBoardList = boardService.noticeBoardList(pageable, boardFileDto);

        model.addAttribute("noticeBoardList", noticeBoardList);
    }

    @GetMapping("/noticeList/{id}")
    public String noticeBoardDetail(@PathVariable Long id,  Model model) {
        NoticeBoardFileInfo noticeBoardFileInfo = boardService.noticeBoardFileDetail(id);
        NoticeBoard noticeBoard = boardService.noticeBoard(id);
            model.addAttribute("noticeBoard", noticeBoard);
            model.addAttribute("noticeBoardFileInfo", noticeBoardFileInfo);

        return "board/noticeBoardDetail";
    }

    //파일 다운로드 할때 이거 쓸것!!!!
    @GetMapping("/noticeList/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            String fileName = request.getParameter("fileName");
            String filePath = "c:\\" + File.separator + "noticeBoardImage" + File.separator;

            File dFile = new File(filePath, fileName);
            int fSize = (int) dFile.length();
            if (fSize > 0) {
                String encodeFilename = "attachment; filename*=" + "UTF-8" + "''" + URLEncoder.encode(fileName, "UTF-8");

                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader("Content-Disposition", encodeFilename);
                response.setContentLengthLong(fSize);

                BufferedInputStream in = null;
                BufferedOutputStream out = null;

                in = new BufferedInputStream(new FileInputStream(dFile));
                out = new BufferedOutputStream(response.getOutputStream());

                try {
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;

                    while ((bytesRead = in .read(buffer)) != -1){
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                } finally {
                    in.close();
                    out.close();
                }
            }else {
                throw new FileNotFoundException("파일이 없습니다.");
            }
        }catch (Exception e){
            e.getMessage();
        }
    }
    @DeleteMapping("delete")
    @ResponseBody
    public String deleteNotice(@RequestParam(value = "id") Long id) {
        boardService.deleteNotice(id);

        return "SUCCESS";
    }

}
