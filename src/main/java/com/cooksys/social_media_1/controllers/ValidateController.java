package com.cooksys.social_media_1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media_1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {
	
	private final ValidateService validateService;

    @GetMapping("/tag/exists/{label}")
    boolean validateLabelExistence(@PathVariable("label") String label)
    {
    	return validateService.validateLabelExistence(label);
    }
    
    @GetMapping("/username/exists/@{username}")
    boolean validateUserExistence(@PathVariable("username") String username)
    {
        //
       // return null;
        return false;
    }

    @GetMapping("/username/available/@{username}")
    boolean validateUserAvailable(@PathVariable("username") String username)
    {
        //Just call above method
        //return null;
        return false;
    }
}
