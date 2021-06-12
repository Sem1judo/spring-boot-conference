package com.ua.semkov.conferenceSpringFinal.validation;


import lombok.AllArgsConstructor;

import java.util.Optional;


@AllArgsConstructor
public abstract class AbstractValidatorReference<T> {

    public String validateReference(String id) {
        StringBuilder message = new StringBuilder("");

        boolean isEmpty = false;

        if (id.isEmpty()) {
            message.append(getNameOfEntity())
                    .append(" ID can't be empty. ");

            isEmpty = true;
        }

        if (!isEmpty && getEntity(id).isEmpty()) {
            message.append(getNameOfEntity())
                    .append(" ID: ")
                    .append(id).append(" is invalid. ");

        }


        return message.toString();
    }

    protected abstract String getNameOfEntity();


    protected abstract Optional<T> getEntity(String id);
}
