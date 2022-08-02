package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_like")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoLike {
    public VideoLike(){}
    public VideoLike(long userId, long videoId, String ip){
        this.userId = userId;
        this.videoId = videoId;
        this.ip = ip;
        this.addTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private long userId;
    private long videoId;
    private String ip;
    private long addTime;
}
