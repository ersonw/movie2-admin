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
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private long addTime;
    private long updateTime;
}
