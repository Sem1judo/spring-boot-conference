package com.ua.semkov.conferenceSpringFinal.dao;

import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.entity.UserEventRegistration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Semkov.S
 */
@Repository
public interface UserEventRegistrationRepository extends CrudRepository<UserEventRegistration, Long> {


    List<Event> findAllByUser(User user);


    @Query(value = "SELECT COUNT(*) FROM users_events WHERE event_id =?1 and user_id = ?2", nativeQuery = true)
    int findJoinedUserEvent(Event event, User user);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_events where user_id=?1 and event_id = ?2", nativeQuery = true)
    void deleteUserEvent(Long userId, Long eventId);

    @Query(value = "SELECT  event_id FROM users_events WHERE user_id = ?1", nativeQuery = true)
    List<Long> findAllEventsByUser(Long userId);


}
