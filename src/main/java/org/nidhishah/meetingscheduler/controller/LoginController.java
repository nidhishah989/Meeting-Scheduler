package org.nidhishah.meetingscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage()
    {
        return "login";
    }


    @PostMapping("/login")
    public String loginprocess()
    {
        return "file";
    }

}
