package com.ua.semkov.conferenceSpringFinal.validation;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.Set;

@Component
public class ValidatorEntity<T> {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorEntity.class);

    private Validator getValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public void validate(@Valid T t) {
        logger.debug("Trying to check whether object is valid : {}", t);
        Set<ConstraintViolation<T>> constraintViolations = getValidatorInstance().validate(t);
        if (!constraintViolations.isEmpty()) {
            logger.warn("Object is not valid: {}", t);
            throw new ServiceException("Data is not valid: " + constraintViolations.iterator().next());
        }
    }
}
