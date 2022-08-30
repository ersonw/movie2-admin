package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_produced_record")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoProducedRecord {
    public VideoProducedRecord() {}
    public VideoProducedRecord(long producedId, long videoId) {
        this.producedId = producedId;
        this.videoId = videoId;
        this.addTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private long producedId;
    private long videoId;
    private long addTime;
    private long updateTime;
}
