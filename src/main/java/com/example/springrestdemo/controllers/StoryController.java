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
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @RequestMapping(value = "/get-by-id/{id}",method = RequestMethod.GET)
    public ResponseEntity getStoryById(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id){

        try {
            Story story = storyService.getStoryById(id);
            if(story == null){
                ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "No story is found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            return ResponseEntity.ok(story);

        } catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/get-by-user-id/{userId}",method = RequestMethod.GET)
    public ResponseEntity getAllStoriesByUserId(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Long userId){

        try {
            List<Story> storyList = storyService.getAllStoriesByUser(userId);


            return ResponseEntity.ok(storyList);

        } catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

}
