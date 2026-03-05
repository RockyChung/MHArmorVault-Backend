package com.rocky.mh.armorvault.etl;

@Slf4j
@Service
public class SkillImportService {

    @Autowired
    private SkillRepository skillRepository;

    @Transactional
    public void importSkills() {
        try {
            log.info("開始整合三個 CSV 檔案匯入技能...");

            // 1. 先計算各技能的最大等級 (來自 skill_levels.csv)
            Map<String, Integer> maxLevelMap = new HashMap<>();
            try (CSVReader reader = new CSVReader(new InputStreamReader(
                    new ClassPathResource("data/skill_levels.csv").getInputStream(), StandardCharsets.UTF_8))) {
                reader.readNext(); // Skip header
                String[] line;
                while ((line = reader.readNext()) != null) {
                    String nameEn = line[0]; // base_name_en
                    int level = Integer.parseInt(line[1]);
                    maxLevelMap.merge(nameEn, level, Math::max);
                }
            }

            // 2. 載入中文翻譯 (來自 skill_base_translations.csv)
            Map<String, String[]> translationMap = new HashMap<>();
            try (CSVReader reader = new CSVReader(new InputStreamReader(
                    new ClassPathResource("data/skill_base_translations.csv").getInputStream(), StandardCharsets.UTF_8))) {
                reader.readNext();
                String[] line;
                while ((line = reader.readNext()) != null) {
                    // line[0]=name_en, line[22]=name_zh, line[23]=description_zh
                    translationMap.put(line[0], new String[]{line[22], line[23]});
                }
            }

            // 3. 讀取主檔並合併 (來自 skill_base.csv)
            List<Skill> skillsToSave = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new InputStreamReader(
                    new ClassPathResource("data/skill_base.csv").getInputStream(), StandardCharsets.UTF_8))) {
                reader.readNext();
                String[] line;
                while ((line = reader.readNext()) != null) {
                    String nameEn = line[0];

                    Skill skill = Skill.builder()
                            .id(nameEn)      // ID 設為英文名稱
                            .nameEn(nameEn)  // name_en 欄位也設為相同值
                            .maxLevel(maxLevelMap.getOrDefault(nameEn, 0))
                            .build();

                    if (translationMap.containsKey(nameEn)) {
                        skill.setNameZh(translationMap.get(nameEn)[0]);
                        skill.setDescription(translationMap.get(nameEn)[1]);
                    }
                    skillsToSave.add(skill);
                }
            }

            skillRepository.saveAll(skillsToSave);
            log.info("成功匯入 " + skillsToSave.size() + " 筆技能資料。");

        } catch (Exception e) {
            log.error("匯入過程出錯：", e);
        }
    }
}
