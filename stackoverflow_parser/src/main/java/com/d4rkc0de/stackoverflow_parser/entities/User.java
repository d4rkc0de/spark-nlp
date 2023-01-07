package com.d4rkc0de.stackoverflow_parser.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Map<String, Integer> badge_counts;
    private Integer account_id;
    private Boolean is_employee;
    private Integer last_modified_date;
    private Integer last_access_date;
    private Integer reputation_change_year;
    private Integer reputation_change_quarter;
    private Integer reputation_change_month;
    private Integer reputation_change_week;
    private Integer reputation_change_day;
    private Integer reputation;
    private Integer creation_date;
    private String user_type;
    private Integer user_id;
    private String link;
    private String profile_image;
    private String display_name;
}
