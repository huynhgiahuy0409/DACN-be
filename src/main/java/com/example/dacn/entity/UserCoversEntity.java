package com.example.dacn.entity;
import com.example.dacn.enums.ImageStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_covers")
@Getter
@Setter
public class UserCoversEntity extends BaseEntity {
    private String imageUrl;
    private ImageStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserCoversEntity(String imageUrl, ImageStatus status, UserEntity user) {
        this.imageUrl = imageUrl;
        this.status = status;
        this.user = user;
    }

    public UserCoversEntity() {
    }
}
