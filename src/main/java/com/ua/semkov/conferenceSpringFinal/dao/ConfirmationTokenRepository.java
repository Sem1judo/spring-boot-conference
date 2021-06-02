package com.ua.semkov.conferenceSpringFinal.dao;

import com.ua.semkov.conferenceSpringFinal.entity.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Semkov.S
 */
@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}
