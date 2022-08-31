package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_ppvod")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoPpvod {
    public VideoPpvod() {}
    public VideoPpvod(String name, String value) {
        this.name = name;
        this.val = value;
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
