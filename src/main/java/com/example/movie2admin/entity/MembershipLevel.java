package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "membership_level")
@Cacheable
@ToString(includeFieldNames = true)
public class MembershipLevel {
    @Id
    @GeneratedValue
    private long id;
    private int level;
    private long experience;
    private long addTime;
    private long updateTime;
}
