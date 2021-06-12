package com.ua.semkov.conferenceSpringFinal.validation;


import com.ua.semkov.conferenceSpringFinal.dao.UserRepository;
import com.ua.semkov.conferenceSpringFinal.entity.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class ValidatorTopicUserId extends AbstractValidatorReference<User> {

    private static final String CLASS_NAME = User.class.getSimpleName();
    private final UserRepository userRepository;

    @Override
    protected String getNameOfEntity() {
        return CLASS_NAME;
    }

    @Override
    protected Optional<User> getEntity(String id) {
        return userRepository.findById(Long.parseLong(id));
    }
}
