package com.ua.semkov.conferenceSpringFinal.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users_events")
public class UserEventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @Column
    @Builder.Default
    private LocalDateTime registeredAt = LocalDateTime.now();
    @Column
    @Builder.Default
    private boolean attendance = false;
}
