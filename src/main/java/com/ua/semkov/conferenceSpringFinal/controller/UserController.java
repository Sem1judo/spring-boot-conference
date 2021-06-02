package com.ua.semkov.conferenceSpringFinal.controller;

import com.ua.semkov.conferenceSpringFinal.entity.ConfirmationToken;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.service.ConfirmationTokenService;
import com.ua.semkov.conferenceSpringFinal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * @author Semkov.S
 */

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("/sign-in")
    String signIn() {

        return "/user/sign-in";
    }

    @GetMapping("/sign-up")
    String signUpPage(User user) {

        return "/user/sign-up";
    }

    @PostMapping("/sign-up")
    String signUp(User user, @RequestParam("eventId") long eventId) {

        userService.signUpUser(user, eventId);

        return "redirect:/user/sign-in";
    }

    @GetMapping("/sign-up/confirm")
    String confirmMail(@RequestParam("token") String token) {

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

        optionalConfirmationToken.ifPresent(userService::confirmUser);

        return "redirect:/user/sign-in";
    }


    @GetMapping("/userProfile/{id}")
    public ModelAndView getEvent(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/user/user_profile");

        mav.addObject("user", userService.getById(id));

        return mav;
    }

}
