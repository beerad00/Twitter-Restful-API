package com.cooksys.social_media_1.controllers;

import com.cooksys.social_media_1.entities.Hashtag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {
    @GetMapping
    public List<Hashtag> retrieveHashtags()
    {
        //Return list of all tags
        return null;

    }
    @GetMapping("/{label}")
    public List<Hashtag> retriveLabeledHashtag(@PathVariable("label") String label)
    {
        //return list of labeled tags
        return null;
    }
}
