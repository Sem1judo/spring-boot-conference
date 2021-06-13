package com.ua.semkov.conferenceSpringFinal.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Event entity.
 *
 * @author S.Semkov
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Title in event may not be blank")
    @Size(min = 3, max = 255,
            message = "Title in event must be between 3 and 255 characters")
    @Pattern(regexp = "^[\\w\\s\\.\\,\\-\\'\\!\\?\\+\\#\\*А-Яа-яЪъЇї]*$",
            message = "Title in event must not have forbidden characters ")
    private String title;

    @Column
    @NotBlank(message = "Description in event may not be blank")
    @Size(min = 3, max = 1024,
            message = "Description in event must be between 8 and 1024 characters")
    @Pattern(regexp = "^[\\w\\s\\.\\,\\-\\'\\!\\?\\+\\#\\*А-Яа-яЪъЇї]*$",
            message = "Description in event  must not have forbidden characters ")
    private String description;


    @NotBlank(message = "Location in event may not be blank")
    @Size(min = 3, max = 255,
            message = "Location in event must be between 3 and 255 characters")
    @Pattern(regexp = "^[\\w\\s\\,\\.\\-]*$",
            message = "Location in event  must not have forbidden characters ")
    @Column
    private String location;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd, HH:mm")
    @NotNull
    private LocalDateTime startTime;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd, HH:mm")
    @NotNull
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PLANNED;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Topic> topics = new ArrayList<>();


    @OneToMany(mappedBy = "event")
    private Set<UserEventRegistration> registrations;

    @Column
    private Long participants;

}
