package com.example.movie2admin.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VideoPlayUrl {
    private long size;
    private String resolution;
    private String url;
}
