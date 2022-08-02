package com.example.movie2admin.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VideoData {
    private long length;
    private long bitrate;
    private String resolution;
}
