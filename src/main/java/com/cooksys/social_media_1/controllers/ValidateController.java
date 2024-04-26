package com.cooksys.social_media_1.controllers;

import com.cooksys.social_media_1.services.ValidateService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;
    @GetMapping("/tag/exists/{label}")
    boolean validateLabelExistence(@PathVariable("label") String label)
    {
        //Return True/False
        return false;
        //return null;
    }
    @GetMapping("/username/exists/@{username}")
    boolean validateUsernameExistence(@PathVariable("username") String username)
    {
        return validateService.validateUsernameExistence(username);
    }

    @GetMapping("/username/available/@{username}")
    boolean validateUsernameAvailable(@PathVariable("username") String username)
    {
        return validateService.validateUsernameAvailable(username);
    }
}
