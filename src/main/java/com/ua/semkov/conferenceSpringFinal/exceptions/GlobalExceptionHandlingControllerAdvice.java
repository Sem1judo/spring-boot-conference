package com.ua.semkov.conferenceSpringFinal.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.NoSuchEntityException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlingControllerAdvice {


    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        log.error("Request raised a DataIntegrityViolationException");
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError(Exception exception) {
        log.error("Request raised exception" + exception.getClass().getSimpleName());
        return "exceptions/database";
    }

    @ExceptionHandler({ServiceException.class, NoSuchEntityException.class})
    public ModelAndView handleError(HttpServletRequest req, Exception exception)
            throws Exception {

        if (AnnotationUtils.findAnnotation(exception.getClass(),
                ResponseStatus.class) != null)
            throw exception;

        log.error("Request: " + req.getRequestURI() + " raised " + exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", new Date().toString());
        mav.addObject("status", 500);

        mav.setViewName("exceptions/error");
        return mav;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        log.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }


//    @ExceptionHandler(value = NoHandlerFoundException.class)
//    public String exception(NoHandlerFoundException e, HttpServletRequest req, RedirectAttributes redirectAttributes) {
//        log.error("Request raised exception " + e.getClass().getSimpleName());
//        return "exceptions/notFound";
//    }
}
