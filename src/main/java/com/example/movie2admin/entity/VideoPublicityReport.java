package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_publicity_report")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoPublicityReport {
    public VideoPublicityReport() {}
    public VideoPublicityReport(long publicityId, long userId, String ip) {
        this.publicityId = publicityId;
        this.userId = userId;
        this.ip = ip;
        this.addTime = System.currentTimeMillis();
    }
    @Id
    @GeneratedValue
    private long id;
    private long publicityId;
    private long userId;
    private String ip;
    private long addTime;
}
