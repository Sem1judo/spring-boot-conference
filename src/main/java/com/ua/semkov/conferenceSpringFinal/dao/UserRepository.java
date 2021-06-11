package com.ua.semkov.conferenceSpringFinal.dao;

import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.User;
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
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);


    @Query(value = "SELECT COUNT(*) FROM users_events WHERE event_id =?1 and user_id = ?2", nativeQuery = true)
    int findJoinedUserEvent(Event event, User user);


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users_events (user_id, event_id) " +
            "SELECT u.id,  e.id  FROM users u , events e " +
            "WHERE u.id = ?1 AND e.id = ?2", nativeQuery = true)
    void addUserEvent(Long userId, Long eventId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_events where user_id=?1 and  event_id = ?2", nativeQuery = true)
    void deleteUserEvent(Long userId, Long eventId);


}
