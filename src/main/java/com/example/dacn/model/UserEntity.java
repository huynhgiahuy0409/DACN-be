package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity{
    @Id
    private String username;

    private String password;
    private String fullName;
    private String email;
    private Date dob;
    private String gender;
    private String phone;
    private String status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user")
    private Set<ReservationEntity> reservations = new LinkedHashSet<ReservationEntity>();
}
