package com.ua.semkov.conferenceSpringFinal.service;

import com.ua.semkov.conferenceSpringFinal.dao.EventRepository;
import com.ua.semkov.conferenceSpringFinal.dao.UserRepository;
import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.Paged;
import com.ua.semkov.conferenceSpringFinal.entity.Paging;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.exceptions.ServiceException;
import com.ua.semkov.conferenceSpringFinal.validation.ValidatorEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ejb.NoSuchEntityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final ValidatorEntity<Event> validator;


    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id event.";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such event";

    public List<Event> getAll() {
        log.debug("Trying to get all events");
        try {
            return (List<Event>) eventRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Events is not exist");
            throw new NoSuchEntityException("Doesn't exist such event");
        } catch (DataAccessException e) {
            log.error("Failed to get all events", e);
            throw new ServiceException("Failed to get list of events", e);
        }
    }

    public Paged<Event> getPage(int pageNumber, int size,String fieldSortName) {
        log.debug("Trying to get pageable events");
        PageRequest request = PageRequest.of(pageNumber - 1, size, new Sort(Sort.Direction.ASC, fieldSortName));
        Page<Event> postPage = eventRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public void create(Event event, Long userId) {
        log.debug("Trying to create event: {}", event);
        log.debug("Trying to get user byId with id: {}", userId);

        getUser(event, userId);

        validator.validate(event);
        try {
            eventRepository.save(event);
        } catch (DataAccessException e) {
            log.error("Failed to create event: {}", event, e);
            throw new ServiceException("Failed to create event", e);
        }
    }

    public void deleteById(long id) {
        log.debug("Trying to delete events with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing event with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete event with id={}", id, e);
            throw new ServiceException("Failed to delete event by id", e);
        }
    }

    public void delete(Event event) {
        log.debug("Trying to delete event = {}", event);

        if (event == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            eventRepository.delete(event);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing event = {}", event);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete event = {}", event, e);
            throw new ServiceException("Failed to delete event", e);
        }
    }

    public Event getById(long id) {
        log.debug("Trying to get event with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        Event event;
        try {
            event = eventRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid event ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing event with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve event with id={}", id, e);
            throw new ServiceException("Failed to retrieve event with such id", e);
        }
        return event;
    }

    public void update(Event event, Long userId) {
        log.debug("Trying to update event: {}", event);
        log.debug("Trying to get user byId with id: {}", userId);

        if (event.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        getUser(event, userId);

        validator.validate(event);
        try {
            eventRepository.findById(event.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing event: {}", event);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve event: {}", event);
            throw new ServiceException("Failed to retrieve event: ", e);
        }
        try {
            eventRepository.save(event);
        } catch (DataAccessException e) {
            log.error("Failed to update event: {}", event);
            throw new ServiceException("Problem with updating event");
        }
    }

    private void getUser(Event event, long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid user ID"));
            event.setOrganizer(user);
        } catch (NoSuchEntityException e) {
            log.error("Failed to retrieve cause invalid user ID: {}", userId);
            throw new ServiceException("Failed to retrieve user from such id: ", e);
        }
    }
}



