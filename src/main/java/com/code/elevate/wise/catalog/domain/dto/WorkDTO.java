package com.code.elevate.wise.catalog.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class WorkDTO {
    private String key;
    private String title;
    @JsonProperty("first_publish_year")
    private int firstPublishYear;
    private List<AuthorDTO> authors;
    private List<String> subject;
}
