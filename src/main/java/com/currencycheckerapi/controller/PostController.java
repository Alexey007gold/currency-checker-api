package com.currencycheckerapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @PostMapping("/post")
    public void post(@RequestBody String body) {
        System.out.println("----------POST");
        System.out.println(body);
        System.out.println("----------END POST");
    }
}
