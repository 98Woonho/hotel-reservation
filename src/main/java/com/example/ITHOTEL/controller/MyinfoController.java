package com.example.ITHOTEL.controller;

import com.example.ITHOTEL.config.auth.jwt.JwtProperties;
import com.example.ITHOTEL.config.auth.jwt.JwtTokenProvider;
import com.example.ITHOTEL.config.auth.jwt.TokenInfo;

import com.example.ITHOTEL.domain.common.entity.Payment;
import com.example.ITHOTEL.domain.common.dto.BoardDto;
import com.example.ITHOTEL.domain.common.dto.CommentDto;
import com.example.ITHOTEL.domain.common.entity.Reservation;
import com.example.ITHOTEL.service.BoardService;
import com.example.ITHOTEL.service.ReservationService;
import com.example.ITHOTEL.domain.user.dto.UserDto;
import com.example.ITHOTEL.service.MyinfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class MyinfoController {

    @Autowired
    MyinfoService myinfoService;
    @Autowired
    BoardService boardService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    HttpServletResponse response;

    @GetMapping("informationInfo")
    public void getInformationInfo(@RequestParam(value="function", defaultValue = "read") String function,
                                   @RequestParam(value="userid_msg", required = false) String userid_msg,
                                   @RequestParam(value="name_msg", required = false) String name_msg,
                                   @RequestParam(value="email_msg", required = false) String email_msg,
                                   @RequestParam(value="phone_msg", required = false) String phone_msg,
                                   @RequestParam(value="zipcode_msg", required = false) String zipcode_msg,
                                   @RequestParam(value="addr1_msg", required = false) String addr1_msg,
                                   @RequestParam(value="msg", required = false) String msg,
                                   @RequestParam(value="data", required = false) String data,
                                   @RequestParam(value="href", required = false) String href,
                                   HttpServletRequest request, Model model, Authentication authentication){
        model.addAttribute("auth_msg", "회원정보 수정을 위해 비밀번호가 필요합니다.");
        model.addAttribute("function", function);

        model.addAttribute("userid_msg", userid_msg);
        model.addAttribute("name_msg", name_msg);
        model.addAttribute("email_msg", email_msg);
        model.addAttribute("phone_msg", phone_msg);
        model.addAttribute("zipcode_msg", zipcode_msg);
        model.addAttribute("addr1_msg", addr1_msg);
        model.addAttribute("msg", msg);

        model.addAttribute("data", data);
        model.addAttribute("href", href);

        if(!Objects.equals(function, "delete"))
            myinfoService.ReadMyinfo(authentication.getName(), model);

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(Objects.equals(cookie.getName(), "InfoAuth") && Objects.equals(function, "update"))
                model.addAttribute("auth", true);
            else if(!Objects.equals(function, "update")){
                cookie.setPath("/user");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

    }

    @PostMapping("infoAuth/{password}")
    public @ResponseBody JSONObject infoAuth(@PathVariable String password, Authentication authentication, HttpServletResponse response) throws IOException {
        JSONObject obj = new JSONObject();
        boolean isValid = myinfoService.isValid(password, authentication);
        if(isValid) {
            TokenInfo tokenInfo = jwtTokenProvider.generateToken("InfoAuth", password, true);
            Cookie cookie = new Cookie("InfoAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
            cookie.setPath("/user");
            response.addCookie(cookie);
            obj.put("success", true);
        }
        else {
            obj.put("success", false);
            obj.put("massage", "비밀번호가 다릅니다.");
        }
        return obj;
    }

    @PostMapping("updateinfo")
    public String updateInfo(@Valid UserDto dto, BindingResult bindingResult, Authentication authentication,
                             RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                redirectAttributes.addAttribute(error.getField() + "_msg", error.getDefaultMessage());
            }
            return "redirect:/user/informationInfo?function=update";
        }
        boolean isUpdate = myinfoService.UpdateInfo(dto, authentication, redirectAttributes, request);
        if(isUpdate) {
            myinfoService.IdUpdate(dto);
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies){
                cookie.setPath("/user");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return "redirect:/";
        } else
            return "redirect:/";
    }

    @PostMapping("deleteinfo")
    public String deleteInfo(String password, String word, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        boolean isDelete = myinfoService.DeleteInfo(password, word, authentication, redirectAttributes);
        if(isDelete) {
            redirectAttributes.addAttribute("data", "회원탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.");
            redirectAttributes.addAttribute("href", "/logout");
            return "redirect:/user/informationInfo?function=delete";
        }else
            return "redirect:/user/informationInfo?function=delete";
    }

    @GetMapping("reservationInfo")
    public void ReservationInfo(@RequestParam(value="function", defaultValue = "read") String function, Model model, Authentication authentication){
        model.addAttribute("function", function);
        List<Reservation> reservationList = myinfoService.FindUserReservation(authentication);
        model.addAttribute("reservationList", reservationList);
    }

    public String getAccessToken(){

        //URL
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("imp_key", "7582034642764268");
        params.add("imp_secret", "JxMwheK2PKBrxFxOifDLwwZvdyzjwDERKj4TzStgSZ06Wmg3oQp7h3WjK3nOfdjXsSXF0ZNgCbBWyPrV");

        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity(params,headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ImportAccessTokenResponse> resp =  restTemplate.exchange(url, HttpMethod.POST,entity,ImportAccessTokenResponse.class);

        return resp.getBody().getResponse().getAccess_token();
    }

    @DeleteMapping("deleteReservation/{id}")
    public @ResponseBody ResponseEntity<String> DeleteReservation(@PathVariable Long id){
        Payment payment = myinfoService.FindUserPayment(id);

        String imp_uid = payment.getImpUid();

        LocalDate now_time = LocalDate.now();
        LocalDate check_in = LocalDate.parse(payment.getReservation().getCheckin());
        Period period = Period.between(now_time, check_in);

        String access_token = getAccessToken();

        String url = "https://api.iamport.kr/payments/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+access_token);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("imp_uid", imp_uid);
        if(period.getYears() > 0 || period.getMonths() > 0 || period.getDays() >= 2)        // 체크인 2일 이전에 환불시
            params.add("amount", String.valueOf(payment.getPaidAmount()));                   // 100% 환불
        else if(period.getYears() == 0 && period.getMonths() == 0 && period.getDays() == 1) // 체크인 하루 전에 환불시
            params.add("amount", String.valueOf(payment.getPaidAmount() * 0.8));          // 80% 환불
        else
            return new ResponseEntity<>("fail", HttpStatus.OK);

        params.add("reason", "고객 요청에 의한 환불요청");

        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity(params,headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> resp =  restTemplate.exchange(url, HttpMethod.POST,entity,String.class);

        reservationService.deleteReservationRoomCount(id);

        if(reservationService.deleteReservation(id)) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("null", HttpStatus.OK);
    }

    @GetMapping("inquiryInfo")
    public void inquiryInfo(@RequestParam(value="function", required = false) String function,
                             @PageableDefault(page = 0, size = 10)Pageable pageable,
                             Authentication authentication, Model model){
        List<String> hotelnames = boardService.searchHotelname();
        Page<BoardDto> boardList = boardService.userBoardList(pageable, authentication);
        Page<CommentDto> commentList = boardService.commentList(pageable);
        model.addAttribute("commentList", commentList);
        model.addAttribute("boardList", boardList);
        model.addAttribute("hotelnames", hotelnames);
        model.addAttribute("function", function);
    }
    @GetMapping("/inquiryInfo/{id}") // 나의 문의 게시판 조회
    public String inquiryBoardDetail(@PathVariable Long id, Model model) {
        BoardDto board = boardService.inquiryBoardDetail(id);
        List<CommentDto> commentDtos = boardService.commentList(id);
        model.addAttribute("comments", commentDtos);
        model.addAttribute("board", board);
        model.addAttribute("id", id);
        return "user/inquiryInfoDetail";

    }
}

@Data
class ImportAccessTokenResponse {
    private float code;
    private String message;
    Response response;
}

@Data
class Response {
    private String access_token;
    private float now;
    private float expired_at;
}
