package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_produced")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoProduced {
    public VideoProduced() {}
    public VideoProduced(String name, int status) {
        this.name = name;
        this.status = status;
        this.addTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int status;
    private long addTime;
    private long updateTime;
}
