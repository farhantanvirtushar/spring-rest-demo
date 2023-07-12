package com.example.springrestdemo.services;

import com.example.springrestdemo.model.Story;
import com.example.springrestdemo.repositories.StoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

    private final StoryRepository storyRepository;

    public StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Story createStory(Story story){
        Story newStory = storyRepository.save(story);
        storyRepository.flush();
        return newStory;
    }

    public Story getStoryById(Long id){
        Optional<Story> optionalStory = storyRepository.findById(id);
        return optionalStory.orElse(null);
    }

    public List<Story> getAllStoriesByUser(Long userId){
        List<Story> storyList = storyRepository.findAllByUser(userId);
        return storyList;
    }

    public List<Story> getAllStories(){
        List<Story> storyList = storyRepository.findAll();
        return storyList;
    }
}
