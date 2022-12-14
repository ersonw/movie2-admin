package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "user_spread_record")
@Cacheable
@ToString(includeFieldNames = true)
public class UserSpreadRecord {
    @Id
    @GeneratedValue
    private long id;
    private long shareUserId;
    private long userId;
    private String ip;
    private long addTime;
}
