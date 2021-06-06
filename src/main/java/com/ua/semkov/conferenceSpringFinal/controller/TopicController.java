package com.ua.semkov.conferenceSpringFinal.controller;


import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.Topic;
import com.ua.semkov.conferenceSpringFinal.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Slf4j
@Controller
@AllArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/topics")
    public ModelAndView getAllTopics() {
        ModelAndView mav = new ModelAndView("topic/list_topics");

        mav.addObject("topics", topicService.getAll());

        return mav;
    }

    @GetMapping("/topicProfile/{id}")
    public ModelAndView getTopic(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/topic/topic_profile");

        mav.addObject("topic", topicService.getById(id));

        return mav;
    }


    @GetMapping("/createTopicForm")
    public ModelAndView createTopicForm() {
        ModelAndView mav = new ModelAndView("topic/createTopicForm");

        mav.addObject("topic", new Topic());

        return mav;
    }

    @PostMapping("/addTopic")
    public ModelAndView addTopic(@ModelAttribute @Valid Topic topic,
                                 BindingResult bindingResult,
                                 @RequestParam long userId,
                                 @RequestParam long eventId) {
        ModelAndView mav = new ModelAndView("redirect:/" + "topics");

        if (bindingResult.hasErrors()) {
            mav.setViewName("topic/createTopicForm");
        } else {
            topicService.create(topic, userId, eventId);
            mav.addObject("topics", topicService.getAll());
        }
        return mav;
    }

    @GetMapping(value = "/deleteTopic/{id}")
    public ModelAndView deleteTopic(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "topics");

        topicService.deleteById(id);

        mav.addObject("topics", topicService.getAll());

        return mav;
    }

    @GetMapping("/editTopic/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long topicId) {

        ModelAndView mav = new ModelAndView("topic/updateTopicForm");

        Topic topic = topicService.getById(topicId);

        mav.addObject("topic", topic);

        return mav;
    }

    @PostMapping("/updateTopic/{id}")
    public ModelAndView updateTopic(@PathVariable("id") Long id,
                                    @Valid Topic topic,
                                    BindingResult bindingResult,
                                    @RequestParam long userId,
                                    @RequestParam long eventId) {

        ModelAndView mav = new ModelAndView("redirect:/" + "editTopic/" + id);

        if (bindingResult.hasErrors()) {
            mav.setViewName("topic/updateTopicForm");
        } else {
            topicService.update(topic, userId, eventId);
            mav.addObject("topics", topicService.getAll());
        }

        return mav;
    }


    @GetMapping("/notActiveTopics")
    public ModelAndView list_events_client(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        ModelAndView mav = new ModelAndView("moderator/moderatorProfile");

        mav.addObject("topics", topicService.getPage(pageNumber, size));
        return mav;
    }

}

