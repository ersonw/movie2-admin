package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_class")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoClass {
    public VideoClass() {}
    public VideoClass(String name,int px) {
        this.name = name;
        this.px = px;
        this.addTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int px;
    private long addTime;
    private long updateTime;
}
