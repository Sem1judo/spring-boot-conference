package com.ua.semkov.conferenceSpringFinal.service;

import com.ua.semkov.conferenceSpringFinal.dao.EventRepository;
import com.ua.semkov.conferenceSpringFinal.dao.UserEventRegistrationRepository;
import com.ua.semkov.conferenceSpringFinal.dao.UserRepository;
import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.Topic;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.entity.UserEventRegistration;
import com.ua.semkov.conferenceSpringFinal.exceptions.ServiceException;

import com.ua.semkov.conferenceSpringFinal.validation.ValidatorEntity;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.ejb.NoSuchEntityException;
import java.util.*;


@Slf4j
@Service
@AllArgsConstructor
@ToString
public class UserEventRegistrationService {


    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final UserEventRegistrationRepository userEventRepository;


    private final ValidatorEntity<UserEventRegistration> validator;

    public List<Event> getEventsByUser(User user) {
        log.debug("Obtained user id= {}", user.getId());

        List<Event> events = new ArrayList<>();
        try {
            List<Long> eventsID = userEventRepository.findAllEventsByUser(user.getId());
            if (!eventsID.isEmpty()) {
                for (Long id : eventsID) {
                    Event event = eventRepository.findById(id)
                            .orElseThrow(() -> new NoSuchEntityException("Invalid event ID"));
                    events.add(event);
                }
            }
        } catch (
                DataAccessException e) {
            log.error("Failed to retrieve events for user {}", user, e);
            throw new ServiceException("Failed to retrieve events for user ", e);
        }
        return events;
    }

    public boolean isJoinedUserEvent(Event event, User user) {
        log.debug("Obtained user = {}", user);
        log.debug("Obtained event = {}", event);

        try {
            return Optional.of((0 < userEventRepository.findJoinedUserEvent(event, user))).orElse(false);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve joined user {} to event {}", user, event, e);
            throw new ServiceException("Failed to retrieve joined user to event ", e);
        }

    }

    public void addEventUser(UserEventRegistration userEventRegistration, Long eventId, User user) {
        log.debug("Trying to create topic: {}", userEventRegistration);
        log.debug("Trying to get event byId with id: {}", eventId);

        setEventUser(userEventRegistration, eventId, user);

        validator.validate(userEventRegistration);
        try {
            userEventRepository.save(userEventRegistration);
        } catch (DataAccessException e) {
            log.error("Failed to create topic: {}", userEventRegistration, e);
            throw new ServiceException("Failed to create topic", e);
        }
    }

    public void deleteEventUser(User user, Long eventId) {

        log.debug("Obtained event id= {}", eventId);
        log.debug("Obtained user = {}", user);

        try {
            userEventRepository.deleteUserEvent(user.getId(), eventId);
        } catch (DataAccessException e) {
            log.error("Failed to delete event for user {}", user, e);
            throw new ServiceException("Failed to delete event for user ", e);
        }
    }


    private void getUser(UserEventRegistration userEventRegistration, long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid user ID"));
            userEventRegistration.setUser(user);
        } catch (NoSuchEntityException e) {
            log.error("Failed to retrieve cause invalid user ID: {}", userId);
            throw new ServiceException("Failed to retrieve user from such id: ", e);
        }
    }

    private void setEventUser(UserEventRegistration userEventRegistration, long eventId, User user) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid event ID"));
            userEventRegistration.setEvent(event);
            userEventRegistration.setUser(user);
        } catch (NoSuchEntityException e) {
            log.error("Failed to retrieve cause invalid event ID: {}", eventId);
            throw new ServiceException("Failed to retrieve event from such id: ", e);


        }
    }
}
