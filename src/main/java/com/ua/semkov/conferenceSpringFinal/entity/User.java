package com.ua.semkov.conferenceSpringFinal.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Semkov.S
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Email may not be blank")
    @Size(min = 2, max = 45,
            message = "Email must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = "Email must be valid")
    private String email;

    @Column
    @NotBlank(message = "Password may not be blank")
    @Size(min = 4, max = 126,
            message = "Password must be between 4 and 32 characters")
    private String password;

    @Column
    @NotBlank(message = "First name may not be blank")
    @Size(min = 2, max = 45,
            message = "First name must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-ZА-Яа-яЇї]+$",
            message = "First name must be alphanumeric with no spaces")
    private String firstName;

    @Column
    @NotBlank(message = "Last name may not be blank")
    @Size(min = 2, max = 45,
            message = "Last name must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-ZА-Яа-яЇї]+$",
            message = "Last name must be alphanumeric with no spaces")
    private String lastName;

    @Column
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.CLIENT;

    @OneToMany(mappedBy = "user")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Topic> topics;


    @Builder.Default
    @Transient
    private Boolean locked = false;

    @Builder.Default
    @Transient
    private Boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
