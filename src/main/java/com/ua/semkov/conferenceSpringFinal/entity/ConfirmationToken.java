package com.ua.semkov.conferenceSpringFinal.entity;

import com.ua.semkov.conferenceSpringFinal.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Semkov.S
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String confirmationToken;
    @Column
    private LocalDate createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        this.createdDate = LocalDate.now();
        this.confirmationToken = UUID.randomUUID().toString();
    }
}