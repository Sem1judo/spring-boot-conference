package com.ua.semkov.conferenceSpringFinal.validation;

import com.ua.semkov.conferenceSpringFinal.dao.EventRepository;
import com.ua.semkov.conferenceSpringFinal.entity.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class ValidatorTopicEventId extends AbstractValidatorReference<Event> {
    private static final String CLASS_NAME = Event.class.getSimpleName();
    private final EventRepository eventRepository;

    @Override
    protected String getNameOfEntity() {
        return CLASS_NAME;
    }

    @Override
    protected Optional<Event> getEntity(String id) {
        return eventRepository.findById(Long.parseLong(id));
    }
}
