package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ward")
@Getter
@Setter
public class WardEntity extends BaseEntity{
    private String _name;
    private String _prefix;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private ProvinceEntity province;

    @OneToMany(mappedBy = "ward")
    private Set<AddressEntity> addresses = new LinkedHashSet<AddressEntity>();

}
