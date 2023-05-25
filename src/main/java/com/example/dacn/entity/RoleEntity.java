package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleEntity{
    @Id
    private String code;

    private String name;

    @OneToMany(mappedBy = "role")
    private Set<UserEntity> users = new LinkedHashSet<>();
}
