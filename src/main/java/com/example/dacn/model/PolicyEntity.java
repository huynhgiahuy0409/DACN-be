package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "policy")
@Getter
@Setter
public class PolicyEntity extends BaseEntity{
}
