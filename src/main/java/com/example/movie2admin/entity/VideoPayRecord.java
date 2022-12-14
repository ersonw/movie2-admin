package com.example.movie2admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_pay_record")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoPayRecord {
    @Id
    @GeneratedValue
    private long id;
    private long userId;
    private long payId;
    private String ip;
    private long addTime;
}
