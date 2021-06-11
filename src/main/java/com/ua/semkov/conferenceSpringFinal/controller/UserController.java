package com.ua.semkov.conferenceSpringFinal.controller;

import com.ua.semkov.conferenceSpringFinal.entity.ConfirmationToken;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.service.ConfirmationTokenService;
import com.ua.semkov.conferenceSpringFinal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    ModelAndView signIn() {
        return new ModelAndView("/user/sign-in");
    }

    @GetMapping("/sign-up")
    ModelAndView signUpPage(User user) {
        return new ModelAndView("/user/sign-up");
    }

    @PostMapping("/sign-up")
    ModelAndView signUp(User user) {

        ModelAndView mav = new ModelAndView("redirect:/sign-in");

        userService.signUpUser(user);

        return mav;
    }

    @GetMapping("/sign-up/confirm")
    ModelAndView confirmMail(@RequestParam("token") String token) {

        ModelAndView mav = new ModelAndView("redirect:/sign-in");

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

        optionalConfirmationToken.ifPresent(userService::confirmUser);

        return mav;
    }


    @GetMapping("/userProfile")
    public ModelAndView getEvent() {
        ModelAndView mav = new ModelAndView("/user/user_profile");

        User user = getCurrentUser();

        mav.addObject("events", userService.getEventsByUser(user));
        mav.addObject("user", user);

        return mav;
    }

    @GetMapping(value = "/deleteEventUser/{id}")
    public ModelAndView deleteEventUser(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "userProfile");

        userService.deleteById(id);

        return mav;
    }


    @GetMapping("/joinEvent/{id}")
    public ModelAndView joinEvent(@PathVariable("id") Long eventId, HttpServletRequest request) {


        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        User user = getCurrentUser();
        userService.addEventUser(user, eventId);

        mav.addObject("events", userService.getEventsByUser(user));
        return mav;

    }

    @GetMapping("/unJoinEvent/{id}")
    public ModelAndView unJoinEvent(@PathVariable("id") Long eventId, HttpServletRequest request) {


        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        User user = getCurrentUser();
        userService.deleteEventUser(user, eventId);

        mav.addObject("events", userService.getEventsByUser(user));
        return mav;

    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByEmail(authentication.getName());
    }
}
