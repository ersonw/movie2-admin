package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_concentration_list")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoConcentrationList {
    public VideoConcentrationList(){}
    public VideoConcentrationList(long concentrationId, long videoId){
        this.concentrationId = concentrationId;
        this.videoId = videoId;
        this.addTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private long concentrationId;
    private long videoId;
    private long addTime;
}
