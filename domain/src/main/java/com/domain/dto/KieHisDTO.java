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
public class KieHisDTO {
    private String id;
    private String kieBaseModelName;
    private String packageName;
    private String content;
}
