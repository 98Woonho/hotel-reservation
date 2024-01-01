package com.whl.hotelService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class UserController {
    @GetMapping(value = "login")
    public void getLogin() {
        log.info("getLogin()");
    }
}
