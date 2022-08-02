package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "cash_order")
@Cacheable
@ToString(includeFieldNames = true)
public class CashOrder {
    @Id
    @GeneratedValue
    private long id;
    private String orderNo;
    private long userId;
    private long amount;
    private long buttonId;
    private int status;
    private long addTime;
    private long updateTime;
}
