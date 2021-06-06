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

    private final EventService eventServices;

    @GetMapping("/events")
    public ModelAndView events(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                               @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                               @RequestParam(value = "sort", required = false, defaultValue = "id") String sort) {
        ModelAndView mav = new ModelAndView("event/list_events");

        mav.addObject("events", eventServices.getPage(pageNumber, size, sort));
        mav.addObject("sort", sort);
        return mav;
    }

    @GetMapping("/list_events_client")
    public ModelAndView list_events_client(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                           @RequestParam(value = "sort", required = false, defaultValue = "startTime") String sort) {
        ModelAndView mav = new ModelAndView("client/list_events_client");

        mav.addObject("sort", sort);
        mav.addObject("events", eventServices.getPage(pageNumber, size, sort));
        return mav;
    }


    @GetMapping("/eventProfile/{id}")
    public ModelAndView getEvent(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/event/event_profile");

        mav.addObject("event", eventServices.getById(id));

        return mav;
    }


    @GetMapping("/createEventForm")
    public ModelAndView createEventForm() {
        ModelAndView mav = new ModelAndView("event/createEventForm");

        mav.addObject("event", new Event());
        // 2021-04-08, 12:30
        return mav;
    }

    @PostMapping("/addEvent")
    public ModelAndView addEvent(@ModelAttribute @Valid Event event,
                                 BindingResult bindingResult,
                                 @RequestParam long userId) {
        ModelAndView mav = new ModelAndView("redirect:/" + "events");

        if (bindingResult.hasErrors()) {
            mav.setViewName("event/createEventForm");
        } else {
            eventServices.create(event, userId);
            mav.addObject("events", eventServices.getAll());
        }
        return mav;
    }

    @GetMapping(value = "/deleteEvent/{id}")
    public ModelAndView deleteEvent(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "events");

        eventServices.deleteById(id);

        mav.addObject("events", eventServices.getAll());

        return mav;
    }

    @GetMapping("/editEvent/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long eventId) {

        ModelAndView mav = new ModelAndView("event/updateEventForm");

        Event event = eventServices.getById(eventId);

        mav.addObject("event", event);

        return mav;
    }

    @PostMapping("/updateEvent/{id}")
    public ModelAndView updateEvent(@PathVariable("id") Long id,
                                    @Valid Event event,
                                    BindingResult bindingResult,
                                    @RequestParam long userId) {

        ModelAndView mav = new ModelAndView("redirect:/" + "events");

        if (bindingResult.hasErrors()) {
            mav.setViewName("event/updateEventForm");
        } else {
            eventServices.update(event, userId);
            mav.addObject("events", eventServices.getAll());
        }

        return mav;
    }


    // work with data (form) - input type - datetime-local
//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
//            @Override
//            public void setAsText(String text) throws IllegalArgumentException{
//                setValue(LocalDateTime.parse(text.replace("T",", "), DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm")));
//            }
//
//            @Override
//            public String getAsText() throws IllegalArgumentException {
//                return DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm").format((LocalDateTime) getValue());
//            }
//        });
//    }

}

