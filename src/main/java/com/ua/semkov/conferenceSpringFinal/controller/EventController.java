package com.ua.semkov.conferenceSpringFinal.controller;


import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.service.EventService;
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
public class EventController {

    public static final String EVENTS = "events";
    public static final String EVENT = "event";
    private final EventService eventServices;


    @GetMapping("/events")
    public ModelAndView getEvents(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                               @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                               @RequestParam(value = "sort", required = false, defaultValue = "id") String sort) {
        ModelAndView mav = new ModelAndView("event/list_events");

        mav.addObject(EVENTS, eventServices.getPage(pageNumber, size, sort));
        mav.addObject("sort", sort);
        return mav;
    }


    @GetMapping("/eventProfile/{id}")
    public ModelAndView getEvent(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/event/event_profile");

        mav.addObject(EVENT, eventServices.getById(id));

        return mav;
    }


    @GetMapping("/create/createEventForm")
    public ModelAndView createEventForm() {
        ModelAndView mav = new ModelAndView("event/createEventForm");

        mav.addObject(EVENT, new Event());
        // 2021-04-08, 12:30
        return mav;
    }

    @PostMapping("/create/addEvent")
    public ModelAndView addEvent(@ModelAttribute @Valid Event event,
                                 BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView("redirect:/" + EVENTS);

        if (bindingResult.hasErrors()) {
            mav.setViewName("event/createEventForm");
        } else {

            eventServices.create(event);
            mav.addObject(EVENTS, eventServices.getAll());
        }
        return mav;
    }

    @PostMapping(value = "/delete/deleteEvent/{id}")
    public ModelAndView deleteEvent(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + EVENTS);

        eventServices.deleteById(id);

        mav.addObject(EVENTS, eventServices.getAll());

        return mav;
    }

    @GetMapping("/update/editEvent/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long eventId) {

        ModelAndView mav = new ModelAndView("event/updateEventForm");

        Event event = eventServices.getById(eventId);

        mav.addObject(EVENT, event);

        return mav;
    }

    @PostMapping("/update/updateEvent/{id}")
    public ModelAndView updateEvent(@PathVariable("id") Long id,
                                    @Valid Event event,
                                    BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + EVENTS);

        if (bindingResult.hasErrors()) {
            mav.setViewName("event/updateEventForm");
        } else {
            eventServices.update(event);
            mav.addObject(EVENTS, eventServices.getAll());
        }

        return mav;
    }
}

