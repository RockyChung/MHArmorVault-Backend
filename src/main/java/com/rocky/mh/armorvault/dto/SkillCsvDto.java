package com.rocky.mh.armorvault.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class SkillCsvDto {
    @CsvBindByName(column = "id")
    private Integer id;

    @CsvBindByName(column = "name_en")
    private String nameEn;

    @CsvBindByName(column = "max_level")
    private Integer maxLevel;
}