package com.ua.semkov.conferenceSpringFinal.controller;


import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.Topic;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.exceptions.ServiceException;
import com.ua.semkov.conferenceSpringFinal.service.EventService;
import com.ua.semkov.conferenceSpringFinal.service.TopicService;
import com.ua.semkov.conferenceSpringFinal.service.UserService;
import com.ua.semkov.conferenceSpringFinal.validation.AbstractValidatorReference;
import com.ua.semkov.conferenceSpringFinal.validation.ValidatorTopicEventId;
import com.ua.semkov.conferenceSpringFinal.validation.ValidatorTopicUserId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;


@Slf4j
@Controller
@AllArgsConstructor
public class TopicController {

    public static final String Path_REDIRECT = "redirect:/";
    public static final String TOPICS = "topics";
    private final TopicService topicService;
    private final AbstractValidatorReference<User> validatorTopicUserId;
    private final AbstractValidatorReference<Event> validatorTopicEventId;

    @GetMapping("/topics")
    public ModelAndView getAllTopics() {
        ModelAndView mav = new ModelAndView("topic/list_topics");

        mav.addObject(TOPICS, topicService.getAll());

        return mav;
    }

    @GetMapping("/topicProfile/{id}")
    public ModelAndView getTopic(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/topic/topic_profile");

        mav.addObject("topic", topicService.getById(id));

        return mav;
    }


    @GetMapping("/createTopic/createTopicForm")
    public ModelAndView createTopicForm() {
        ModelAndView mav = new ModelAndView("topic/createTopicForm");

        mav.addObject("topic", new Topic());

        return mav;
    }

    @PostMapping("/createTopic/addTopic")
    public ModelAndView addTopic(@ModelAttribute @Valid Topic topic,
                                 BindingResult bindingResult,
                                 @RequestParam @NotEmpty String userId,
                                 @RequestParam @NotEmpty String eventId) {
        ModelAndView mav = new ModelAndView(Path_REDIRECT + TOPICS);


        getGlobalError(bindingResult, userId, eventId);

        if (bindingResult.hasErrors()) {
            mav.setViewName("topic/createTopicForm");
        } else {
            topicService.create(topic, Long.parseLong(userId), Long.parseLong(eventId));
            mav.addObject(TOPICS, topicService.getAll());
        }
        return mav;
    }

    @PostMapping(value = "/delete/deleteTopic/{id}")
    public ModelAndView deleteTopic(@PathVariable("id") long id, HttpServletRequest request) {


        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        topicService.deleteById(id);

        return mav;
    }

    @GetMapping("/update/editTopic/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long topicId) {

        ModelAndView mav = new ModelAndView("topic/updateTopicForm");

        Topic topic = topicService.getById(topicId);

        mav.addObject("topic", topic);

        return mav;
    }

    @PostMapping("/update/updateTopic/{id}")
    public ModelAndView updateTopic(@PathVariable("id") Long id,
                                    @Valid Topic topic,
                                    BindingResult bindingResult,
                                    @RequestParam String userId,
                                    @RequestParam String eventId) {

        ModelAndView mav = new ModelAndView();

        getGlobalError(bindingResult, userId, eventId);

        if (bindingResult.hasErrors()) {
            topic = topicService.getById(id);
            mav.setViewName("topic/updateTopicForm");
            mav.addObject("topicCopy", topic);
            return mav;
        }
        if (!bindingResult.hasErrors()) {
            topicService.update(topic, Long.parseLong(userId), Long.parseLong(eventId));
            mav.setViewName(Path_REDIRECT + "update/editTopic/" + id);
        }
        return mav;
    }


    @GetMapping("/moderator/notActiveTopics")
    public ModelAndView list_events_client(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        ModelAndView mav = new ModelAndView("moderator/moderatorProfile");

        mav.addObject(TOPICS, topicService.getPage(pageNumber, size));
        return mav;
    }

    private void getGlobalError(BindingResult bindingResult, String userId, String eventId) {
        String userIdErrorMessage = validatorTopicUserId.validateReference(userId);
        String eventIdErrorMessage = validatorTopicEventId.validateReference(eventId);
        if (!userIdErrorMessage.isEmpty()) {
            ObjectError userIdError = new ObjectError("globalError", userIdErrorMessage);
            bindingResult.addError(userIdError);
        }
        if (!eventIdErrorMessage.isEmpty()) {
            ObjectError eventIdError = new ObjectError("globalError", eventIdErrorMessage);
            bindingResult.addError(eventIdError);
        }
    }

}

