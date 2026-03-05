package com.rocky.mh.armorvault.repository;

import com.rocky.mh.armorvault.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    // 未來可以增加按名稱搜尋的方法
    java.util.Optional<Skill> findByNameZh(String nameZh);
}