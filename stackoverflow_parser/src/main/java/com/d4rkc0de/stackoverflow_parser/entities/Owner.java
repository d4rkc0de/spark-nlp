package com.d4rkc0de.stackoverflow_parser.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner {
    private Integer reputation;
    private Integer user_id;
    private String user_type;
    private Integer accept_rate;
    private String profile_image;
    private String display_name;
    private String link;
}
