package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "user_robot")
@Cacheable
@ToString(includeFieldNames = true)
public class UserRobot {
    @Id
    @GeneratedValue
    private long id;
    private long userId;
    private long addTime;
    private long updateTime;
}
