package com.ua.semkov.conferenceSpringFinal.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Topic entity.
 *
 * @author S.Semkov
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "topics")
public class Topic  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Name of topic may not be blank")
    @Size(min = 3, max = 255,
            message = "Name of topic must be between 3 and 255 characters")
    @Pattern(regexp = "^[\\w\\s\\.\\,\\-\\'\\!\\?\\+\\#\\*А-Яа-яЪъЇї]*$",
            message = "Name of topic must not have forbidden characters ")
    private String name;

    @Column
    @NotBlank(message = "Description of topic may not be blank")
    @Size(min = 8, max = 1024,
            message = "Description of topic must be between 3 and 16 characters")
    @Pattern(regexp = "^[\\w\\s\\.\\,\\-\\'\\!\\?\\+\\#\\*А-Яа-яЪъЇї]*$",
            message = "Description of topic must not have forbidden characters ")
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


}
