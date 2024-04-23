package controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class HashtagController {
    @GetMapping
    public List<Hashtag> retrieveHashtags()
    {
        //Return list of all tags
        return null;

    }
    @GetMapping("/{label}")
    public List<HashTag> retriveLabeledHashtag(@PathVariable("label") String label)
    {
        //return list of labeled tags
        return null;
    }
}
