package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "game_order")
@Cacheable
@ToString(includeFieldNames = true)
public class GameOrder {
    @Id
    @GeneratedValue
    private long id;
    private String orderNo;
    private long userId;
    private long amount;
    private long price;
    private long addTime;
}
