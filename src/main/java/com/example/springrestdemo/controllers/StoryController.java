package com.example.springrestdemo.controllers;

import com.example.springrestdemo.model.Story;
import com.example.springrestdemo.model.User;
import com.example.springrestdemo.model.response.ErrorRes;
import com.example.springrestdemo.model.response.LoginRes;
import com.example.springrestdemo.services.StoryService;
import com.example.springrestdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/rest/story")
public class StoryController {

    private final StoryService storyService;
    private final UserService userService;

    public StoryController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "/get-all",method = RequestMethod.GET)
    public ResponseEntity getAllStories(HttpServletRequest request, HttpServletResponse response){

        try {
            List<Story> storyList = storyService.getAllStories();

            return ResponseEntity.ok(storyList);

        } catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }
    @ResponseBody
    @RequestMapping(value = "/create-story",method = RequestMethod.POST)
    public ResponseEntity createStory(HttpServletRequest request, HttpServletResponse response, @RequestBody Story story){

        try {
            String userEmail = request.getUserPrincipal().getName();
            User user = userService.getUserByEmail(userEmail);
            if(user == null){
                ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid user");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            Long userId = user.getId();

            story.setUserId(userId);

            Date date = new Date();
            Timestamp currentTimeStamp = new Timestamp(date.getTime());
            story.setCreatedAt(currentTimeStamp);

            Story newStory = storyService.createStory(story);

            return ResponseEntity.ok(newStory);

        } catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

}
