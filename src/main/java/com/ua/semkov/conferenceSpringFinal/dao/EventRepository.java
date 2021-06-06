package com.ua.semkov.conferenceSpringFinal.dao;


import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findEventsByUsers(User user);



}


