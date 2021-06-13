package com.ua.semkov.conferenceSpringFinal.controller;


import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.entity.UserEventRegistration;
import com.ua.semkov.conferenceSpringFinal.service.UserEventRegistrationService;
import com.ua.semkov.conferenceSpringFinal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserEventController {

    private final UserService userService;
    private final UserEventRegistrationService userEventService;

    @PostMapping(value = "/deleteEventUser/{id}")
    public ModelAndView deleteEventUser(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "userProfile");

        userService.deleteById(id);

        return mav;
    }


    @PostMapping("/joinEvent/{id}")
    public ModelAndView joinEvent(@PathVariable("id") Long eventId, HttpServletRequest request) {


        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);


        userEventService.addEventUser(new UserEventRegistration(), eventId, getCurrentUser());

        return mav;

    }

    @PostMapping("/unJoinEvent/{id}")
    public ModelAndView unJoinEvent(@PathVariable("id") Long eventId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        userEventService.deleteEventUser(getCurrentUser(), eventId);

        return mav;

    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByEmail(authentication.getName());
    }
}
