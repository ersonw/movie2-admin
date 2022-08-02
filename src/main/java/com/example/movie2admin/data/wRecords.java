package com.example.movie2admin.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class wRecords {
    private long count;
    private boolean hasMore;
    private wRecord list;
}
