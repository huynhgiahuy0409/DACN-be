package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "district")
@Getter
@Setter
public class DistrictEntity extends BaseEntity{
    private String _name;
    private String _prefix;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private ProvinceEntity province;

    @OneToMany(mappedBy = "district")
    private Set<AddressEntity> addresses = new LinkedHashSet<AddressEntity>();
}
