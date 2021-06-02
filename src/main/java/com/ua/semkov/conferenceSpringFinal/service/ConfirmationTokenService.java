package com.ua.semkov.conferenceSpringFinal.service;

import com.ua.semkov.conferenceSpringFinal.dao.ConfirmationTokenRepository;
import com.ua.semkov.conferenceSpringFinal.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Semkov.S
 */

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {

        confirmationTokenRepository.save(confirmationToken);
    }

    public void deleteConfirmationToken(Long id) {

        confirmationTokenRepository.deleteById(id);
    }


    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {

        return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
    }

}

