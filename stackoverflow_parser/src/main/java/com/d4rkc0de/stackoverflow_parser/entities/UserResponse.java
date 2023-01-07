package com.d4rkc0de.stackoverflow_parser.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private List<User> items;
    private Boolean has_more;
    private Integer quota_max;
    private Integer quota_remaining;
}
