package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_concentration")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoConcentration {
    public VideoConcentration(){}
    public VideoConcentration(String name, int px){
        this.px = px;
        this.name = name;
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
