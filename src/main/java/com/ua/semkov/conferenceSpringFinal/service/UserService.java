package com.ua.semkov.conferenceSpringFinal.service;

import com.ua.semkov.conferenceSpringFinal.dao.EventRepository;
import com.ua.semkov.conferenceSpringFinal.dao.UserRepository;
import com.ua.semkov.conferenceSpringFinal.entity.ConfirmationToken;
import com.ua.semkov.conferenceSpringFinal.entity.Event;
import com.ua.semkov.conferenceSpringFinal.entity.User;
import com.ua.semkov.conferenceSpringFinal.exceptions.ServiceException;
import com.ua.semkov.conferenceSpringFinal.validation.ValidatorEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ejb.NoSuchEntityException;
import java.text.MessageFormat;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final ValidatorEntity<User> validator;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSenderService emailSenderService;


    void sendConfirmationMail(String userMail, String token) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Mail Confirmation Link!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "Thank you for registering. Please click on the below link to activate your account." + "http://localhost:8080/sign-up/confirm?token="
                        + token);

        emailSenderService.sendEmail(mailMessage);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("OBTAINED email = {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));

    }

    public void signUpUser(User user, long eventId) {

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        getEvent(user, eventId);

        final User createdUser = userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());

    }

    public void confirmUser(ConfirmationToken confirmationToken) {

        final User user = confirmationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());

    }


    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id user.";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such user";

    public List<User> getAll() {
        log.debug("Trying to get all users");
        try {
            return (List<User>) userRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("User is not exist");
            throw new NoSuchEntityException("Doesn't exist such users");
        } catch (DataAccessException e) {
            log.error("Failed to get all users", e);
            throw new ServiceException("Failed to get list of users", e);
        }
    }

    public void create(User user, long eventId) {
        log.debug("Trying to create user: {}", user);

        getEvent(user, eventId);
        validator.validate(user);
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            log.error("Failed to create user: {}", user, e);
            throw new ServiceException("Failed to create user", e);
        }
    }

    public void deleteById(long id) {
        log.debug("Trying to delete user with id={}", id);
        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing user with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete user with id={}", id, e);
            throw new ServiceException("Failed to delete user by id", e);
        }
    }

    public void delete(User user) {
        log.debug("Trying to delete user = {}", user);

        if (user == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            userRepository.delete(user);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing user = {}", user);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete user = {}", user, e);
            throw new ServiceException("Failed to delete user", e);
        }
    }

    public User getById(long id) {
        log.debug("Trying to get user with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        User user;
        try {
            user = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid user ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing event with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user with id={}", id, e);
            throw new ServiceException("Failed to retrieve user with such id", e);
        }
        return user;
    }

    public void update(User user) {
        log.debug("Trying to update user: {}", user);

        if (user.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        validator.validate(user);
        try {
            userRepository.findById(user.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing user: {}", user);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user: {}", user);
            throw new ServiceException("Failed to retrieve user: ", e);
        }
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            log.error("Failed to update user: {}", user);
            throw new ServiceException("Problem with updating user");
        }
    }

    private void getEvent(User user, long eventId) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid event ID"));
            user.setEvent(event);
        } catch (NoSuchEntityException e) {
            log.error("Failed to retrieve cause invalid event ID: {}", eventId);
            throw new ServiceException("Failed to retrieve event from such id: ", e);
        }
    }

}


