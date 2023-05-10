package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "benefit")
@Getter
@Setter
public class BenefitEntity extends BaseEntity{
    private String name;
    private String code;

    @ManyToMany(mappedBy = "benefits")
    private Set<RoomEntity> rooms = new LinkedHashSet<RoomEntity>();
}
