package controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidateController {

    @GetMapping("/tag/exists/{label}")
    boolean validateLabelExistence(@PathVariable("label") String label)
    {
        //Return True/False
        return null;
    }
    @GetMapping("/username/exists/@{username}")
    boolean validateUserExistence(@PathVariable("username") String username)
    {
        //
        return null;
    }

    @GetMapping("/username/available/@{username}")
    boolean validateUserExistence(@PathVariable("username") String username)
    {
        //Just call above method
        return null;
    }
}
