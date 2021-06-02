package com.ua.semkov.conferenceSpringFinal.dao;

import com.ua.semkov.conferenceSpringFinal.entity.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {
}
