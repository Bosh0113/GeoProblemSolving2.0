package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule;



import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_concept.Rule_Concept;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_concept.Rule_ConceptDao;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_elementrelation.Rule_ElementRelation;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_elementrelation.Rule_ElementRelationDao;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_enum.Aspect;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_process.Rule_Process;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_process.Rule_ProcessDao;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_property.Rule_Property;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_property.Rule_PropertyDao;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_shape.Rule_Shape;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_shape.Rule_ShapeDao;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_spaceposition.Rule_SpacePosition;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_spaceposition.Rule_SpacePositionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoRuleService {

    @Autowired
    GeoRuleDao geoRuleDao;
    @Autowired
    Rule_ConceptDao rule_conceptDao;
    @Autowired
    Rule_ElementRelationDao rule_elementRelationDao;
    @Autowired
    Rule_ProcessDao rule_processDao;
    @Autowired
    Rule_PropertyDao rule_propertyDao;
    @Autowired
    Rule_ShapeDao rule_shapeDao;
    @Autowired
    Rule_SpacePositionDao rule_spacePositionDao;

    public List<GeoRule> findRulesByKey(String key){

        List<GeoRule> list = new ArrayList<>();

        List<Rule_Concept> list0 = rule_conceptDao.findAllByFrom(key);
        for (int i = 0; i < list0.size(); i++) {
            GeoRule t = new GeoRule();
            t.setFrom(list0.get(i).getFrom());
            t.setTo(list0.get(i).getTo());
//            t.setType(list0.get(i).getType().toString());
            t.setDescription(list0.get(i).getDescription());
            t.setAspect(Aspect.values()[0]);
            list.add(t);
        }
        List<Rule_SpacePosition> list1 = rule_spacePositionDao.findByFrom(key);
        for (int i = 0; i < list1.size(); i++) {
            GeoRule t = new GeoRule();
            t.setFrom(list1.get(i).getFrom());
            t.setTo(list1.get(i).getTo());
//            t.setType(list1.get(i).getType().toString());
            t.setDescription(list1.get(i).getDescription());
            t.setAspect(Aspect.values()[1]);
            list.add(t);
        }
        List<Rule_Shape> list2 = rule_shapeDao.findByFrom(key);
        for (int i = 0; i < list2.size(); i++) {
            GeoRule t = new GeoRule();
            t.setFrom(list2.get(i).getFrom());
            t.setTo(list2.get(i).getTo());
//            t.setType(list2.get(i).getType().toString());
            t.setDescription(list2.get(i).getDescription());
            t.setAspect(Aspect.values()[2]);
            list.add(t);
        }
        List<Rule_Process> list3 = rule_processDao.findByFrom(key);
        for (int i = 0; i < list3.size(); i++) {
            GeoRule t = new GeoRule();
            t.setFrom(list3.get(i).getFrom());
            t.setTo(list3.get(i).getTo());
//            t.setType(list3.get(i).getType().toString());
            t.setDescription(list3.get(i).getDescription());
            t.setAspect(Aspect.values()[3]);
            list.add(t);
        }
        List<Rule_ElementRelation> list4 = rule_elementRelationDao.findByFrom(key);
        for (int i = 0; i < list4.size(); i++) {
            GeoRule t = new GeoRule();
            t.setFrom(list4.get(i).getFrom());
            t.setTo(list4.get(i).getTo());
            t.setType(list4.get(i).getType().toString());
            t.setDescription(list4.get(i).getDescription());
            t.setAspect(Aspect.values()[4]);
            list.add(t);
        }
        List<Rule_Property> list5 = rule_propertyDao.findByFrom(key);
        for (int i = 0; i < list5.size(); i++) {
            GeoRule t = new GeoRule();
            t.setFrom(list5.get(i).getFrom());
            t.setTo(list5.get(i).getTo());
//            t.setType(list5.get(i).getType().toString());
            t.setDescription(list5.get(i).getDescription());
            t.setAspect(Aspect.values()[5]);
            list.add(t);
        }
        return list;
    }

}
