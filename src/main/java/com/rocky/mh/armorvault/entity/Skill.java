package com.rocky.mh.armorvault.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @Column(name = "id")
    private Integer id; // 來自 CSV 的原始 ID

    @Column(name = "name_en", nullable = false)
    private String nameEn; // 英文名稱

    @Column(name = "name_zh")
    private String nameZh; // 繁體中文名稱

    @Column(name = "max_level")
    private Integer maxLevel; // 該技能的最高等級

    @Column(columnDefinition = "TEXT")
    private String description; // 技能描述
}