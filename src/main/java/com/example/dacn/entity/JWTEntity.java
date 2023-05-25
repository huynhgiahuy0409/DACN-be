package com.example.dacn.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "jwt")
@Data
public class JWTEntity extends BaseEntity{

    @Column(columnDefinition = "TEXT")
    private String token;
    @Column
    private Date tokenExpirationDate;

    public JWTEntity(String token, Date tokenExpirationDate) {
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }


    public JWTEntity() {

    }
}
