package com.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author carl
 */
@Getter
@Setter
@ToString
public class UserDTO {
    private String id;
    private Integer age;
    private String gender;
}
