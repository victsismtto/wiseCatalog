package com.code.elevate.wise.catalog.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectDTO {
    private String key;
    private String name;
    @JsonProperty("subject_type")
    private String subjectType;
    @JsonProperty("work_count")
    private int workCount;
    private List<WorkDTO> works;
}
