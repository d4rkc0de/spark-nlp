package com.d4rkc0de.stackoverflow_parser.controllers;

import com.d4rkc0de.stackoverflow_parser.entities.Question;
import com.d4rkc0de.stackoverflow_parser.entities.User;
import com.d4rkc0de.stackoverflow_parser.services.ApiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionsController {

    @Autowired
    private ApiParser questionsApi;

    @GetMapping(value = "/questions")
    public @ResponseBody List<Question> getQuestions(@RequestParam String tagName, @RequestParam String page, @RequestParam String pageSize) {
        return questionsApi.getQuestions(tagName, page, pageSize);
    }

    @GetMapping(value = "/users")
    public @ResponseBody User getUserInfo(@RequestParam String id) {
        return questionsApi.getUserInfo(id);
    }

}
