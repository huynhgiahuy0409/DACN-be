package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "province")
@Getter
@Setter
public class ProvinceEntity extends BaseEntity{
    private String _code;
    private String _name;
    private String _domain;

    @OneToMany(mappedBy = "province")
    private Set<DistrictEntity> districts = new LinkedHashSet<DistrictEntity>();

    @OneToMany(mappedBy = "province")
    private Set<WardEntity> wards = new LinkedHashSet<WardEntity>();

    @OneToMany(mappedBy = "province")
    private Set<AddressEntity> addresses = new LinkedHashSet<AddressEntity>();
}
