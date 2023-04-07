package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "room_facility_group")
@Getter
@Setter
public class RoomFacilityGroupEntity{
    @Id
    private String code;
    private String name;

    @OneToMany(mappedBy = "roomFacilityGroup")
    private Set<FacilityEntity> facilities = new LinkedHashSet<FacilityEntity>();
}
