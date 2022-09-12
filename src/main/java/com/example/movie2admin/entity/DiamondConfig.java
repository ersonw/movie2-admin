package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "diamond_config")
@Cacheable
@ToString(includeFieldNames = true)
public class DiamondConfig {
    public DiamondConfig() {}
    public DiamondConfig(String name, String val) {
        this.name = name;
        this.val = val;
        this.addTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String val;
    private long addTime;
    private long updateTime;
}
