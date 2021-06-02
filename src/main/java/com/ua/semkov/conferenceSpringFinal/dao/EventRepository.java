package com.ua.semkov.conferenceSpringFinal.dao;



import com.ua.semkov.conferenceSpringFinal.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface EventRepository extends CrudRepository<Event,Long> {
}


