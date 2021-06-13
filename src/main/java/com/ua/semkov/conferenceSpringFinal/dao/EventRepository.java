package com.ua.semkov.conferenceSpringFinal.dao;


import com.ua.semkov.conferenceSpringFinal.entity.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {


}


