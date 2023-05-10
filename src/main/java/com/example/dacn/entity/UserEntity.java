package com.example.dacn.entity;

import com.example.dacn.enums.Gender;
import com.example.dacn.enums.Role;
import com.example.dacn.enums.UserStatus;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String username;
    private String password;
    private String fullName;
    private String email;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    private String phone;
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private RoleEntity role;


    @OneToMany(mappedBy = "user")
    private Set<ReservationEntity> reservations = new LinkedHashSet<ReservationEntity>();
    @OneToMany(mappedBy = "user")
    private Set<UserRating> ratings = new LinkedHashSet<UserRating>();
}
