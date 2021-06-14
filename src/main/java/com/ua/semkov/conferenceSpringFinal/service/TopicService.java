package com.ua.semkov.conferenceSpringFinal.service;

import com.ua.semkov.conferenceSpringFinal.dao.EventRepository;
import com.ua.semkov.conferenceSpringFinal.dao.TopicRepository;
import com.ua.semkov.conferenceSpringFinal.dao.UserRepository;
import com.ua.semkov.conferenceSpringFinal.entity.*;
import com.ua.semkov.conferenceSpringFinal.entity.paging.Paged;
import com.ua.semkov.conferenceSpringFinal.entity.paging.Paging;
import com.ua.semkov.conferenceSpringFinal.exceptions.ServiceException;
import com.ua.semkov.conferenceSpringFinal.validation.ValidatorEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ejb.NoSuchEntityException;
import java.util.List;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TopicService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final TopicRepository topicRepository;


    private final ValidatorEntity<Topic> validator;


    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id topic.";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such topic";


    public List<Topic> getAll() {
        log.debug("Trying to get all topics");
        try {
            return (List<Topic>) topicRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Topics is not exist");
            throw new NoSuchEntityException("Doesn't exist such topics");
        } catch (DataAccessException e) {
            log.error("Failed to get all topics", e);
            throw new ServiceException("Failed to get list of topics", e);
        }
    }

    public Paged<Topic> getPage(int pageNumber, int size) {
        var request = PageRequest.of(pageNumber - 1, size, new Sort(Sort.Direction.ASC, "active"));
        Page<Topic> postPage = topicRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public void create(Topic topic, Long userId, Long eventId) {
        log.debug("Trying to create topic: {}", topic);
        log.debug("Trying to get user byId with id: {}", userId);
        log.debug("Trying to get event byId with id: {}", eventId);

        getUser(topic, userId);
        getEvent(topic, eventId);

        validator.validate(topic);
        try {
            topicRepository.save(topic);
        } catch (DataAccessException e) {
            log.error("Failed to create topic: {}", topic, e);
            throw new ServiceException("Failed to create topic", e);
        }
    }

    public void deleteById(long id) {
        log.debug("Trying to delete topic with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            topicRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing topic with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete topic with id={}", id, e);
            throw new ServiceException("Failed to delete topic by id", e);
        }
    }

    public void delete(Topic topic) {
        log.debug("Trying to delete topic = {}", topic);

        if (topic == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            topicRepository.delete(topic);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing topic = {}", topic);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete topic = {}", topic, e);
            throw new ServiceException("Failed to delete topic", e);
        }
    }

    public Topic getById(long id) {
        log.debug("Trying to get topic with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        Topic topic;
        try {
            topic = topicRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid topic ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing topic with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve topic with id={}", id, e);
            throw new ServiceException("Failed to retrieve topic with such id", e);
        }
        return topic;
    }

    public void update(Topic topic, Long userId, Long eventId) {
        log.debug("Trying to create topic: {}", topic);
        log.debug("Trying to get user byId with id: {}", userId);
        log.debug("Trying to get event byId with id: {}", eventId);


        if (topic.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        getUser(topic, userId);
        getEvent(topic, eventId);

        validator.validate(topic);
        try {
            topicRepository.findById(topic.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing topic: {}", topic);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve topic: {}", topic);
            throw new ServiceException("Failed to retrieve topic: ", e);
        }
        try {
            topicRepository.save(topic);
        } catch (DataAccessException e) {
            log.error("Failed to update topic: {}", topic);
            throw new ServiceException("Problem with updating topic");
        }
    }

    private void getUser(Topic topic, long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid user ID"));
            topic.setUser(user);
        } catch (NoSuchEntityException e) {
            log.error("Failed to retrieve cause invalid user ID: {}", userId);
            throw new ServiceException("Failed to retrieve user from such id: ", e);
        }
    }

    private void getEvent(Topic topic, long eventId) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid event ID"));
            topic.setEvent(event);
        } catch (NoSuchEntityException e) {
            log.error("Failed to retrieve cause invalid event ID: {}", eventId);
            throw new ServiceException("Failed to retrieve event from such id: ", e);
        }
    }
}



