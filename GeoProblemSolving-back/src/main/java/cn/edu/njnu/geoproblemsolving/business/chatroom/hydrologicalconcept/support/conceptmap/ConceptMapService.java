package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.conceptmap;


import cn.edu.njnu.geoproblemsolving.business.chatroom.MxGraphUtils;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.AnsjSegService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.concepts.Concepts;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.concepts.ConceptsService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.GeoRule;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.GeoRuleService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_enum.Aspect;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.userimage.UserImage;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.userimage.UserImageService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ConceptMapService {

    @Autowired
    ConceptMapDao conceptMapDao;

    @Autowired
    ConceptsService conceptsService;

    @Value("${resourcePath}")
    String resourcePath;

    @Autowired
    AnsjSegService ansjSegService;
    @Autowired
    GeoRuleService geoRuleService;
    @Autowired
    UserImageService userImageService;

    MongoTemplate mongoTemplate;

    public void uploadConceptMap(ConceptMap conceptMap) {
        conceptMapDao.insert(conceptMap);
    }


    public void updateConceptMap(ConceptMap conceptMap) throws Exception {

        //获取conceptId
        Concepts c = conceptsService.findByName(conceptMap.getName());
        if (c == null){
            c = new Concepts();
            c.setName(conceptMap.getName());
            c.setConceptID(UUID.randomUUID().toString());
            c.setDefinition(conceptMap.getConcept().get(0).getDefinition());
            c.setConceptType(conceptMap.getMapClass());

            conceptsService.createNewConcept(c);
        }
        conceptMap.setConceptId(c.getConceptID());

        String name = new Date().getTime() + "_conceptMap.png";
        MxGraphUtils mxGraphUtils = new MxGraphUtils();
        mxGraphUtils.exportImage(conceptMap.getWidth(), conceptMap.getHeight(), conceptMap.getXml(), resourcePath+"/conceptMap/", name);

        conceptMap.setPathUrl("/static/conceptMap/" + name);
        Boolean f = conceptMapDao.existsByGeoId(conceptMap.getGeoId());
        if (f){
            ConceptMap map = conceptMapDao.findConceptMapByGeoId(conceptMap.getGeoId());
            conceptMap.setId(map.getId());
            conceptMapDao.save(conceptMap);
        }else {
            conceptMapDao.insert(conceptMap);
        }
    }

    public ConceptMap getConceptMapByGeoId(String geoId){
        ConceptMap concept =conceptMapDao.findConceptMapByGeoId(geoId);
        return concept;
    }

    public ConceptMap getConceptMapByConceptId(String conceptId){
        ConceptMap concept =conceptMapDao.findConceptMapByConceptId(conceptId);
        return concept;
    }

    public List<ConceptMapDTO> getConceptMapByDescriptionContains(String key){
        return conceptMapDao.findConceptMapByDescriptionContains(key);
    }

    public JSONArray searchFromAll(String conceptId, int aspect){
        Concepts concept = conceptsService.findByConceptId(conceptId);
        JSONArray info = new JSONArray();
        List<UserImage> list = new ArrayList<>();
        List<GeoRule> ruleList = new ArrayList<>();
        //搜索规则
        List<GeoRule> rules = geoRuleService.findRulesByKey(concept.getName());
        if (aspect == 6){
            ruleList = rules;
            for (int i = 0; i < rules.size(); i++) {
                list = findImageByRule(rules.get(i));
            }
        }else{
            for (int i = 0; i < rules.size(); i++) {
                if (rules.get(i).getAspect() == Aspect.values()[aspect]){
                    ruleList.add(rules.get(i));
                    list = findImageByRule(rules.get(i));
                }
            }
        }

        info.add(list);
        info.add(ruleList);
        return info;
    }

    public List<UserImage> findImageByRule(GeoRule geoRule){
        List<UserImage> list = new ArrayList<>();
        List<String> to = geoRule.getTo();
        for (int k = 0; k < to.size(); k++) {
            String n = to.get(k);
            Concepts c = conceptsService.findByName(n);
            if (c != null) {
                List<UserImage> images = userImageService.findByConceptId(c.getConceptID());
                for (int l = 0; l < images.size(); l++) {
                    UserImage image = images.get(l);
                    list.add(image);
                }
            }
        }
        //概念本身所关联的六维度图
        Concepts c = conceptsService.findByName(geoRule.getFrom());
        List<UserImage> images = userImageService.findByConceptId(c.getConceptID());
        for (int l = 0; l < images.size(); l++) {
            UserImage image = images.get(l);
            list.add(image);
        }
        return list;
    }

    public List<ConceptMap> searchConceptMapByKey(String key) {
        List<ConceptMap> list = conceptMapDao.findAllByNameContains(key);
        return list;
    }
}
