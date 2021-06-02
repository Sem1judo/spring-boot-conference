package com.ua.semkov.conferenceSpringFinal.dao;

import com.ua.semkov.conferenceSpringFinal.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 *
 * @author Semkov.S
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
