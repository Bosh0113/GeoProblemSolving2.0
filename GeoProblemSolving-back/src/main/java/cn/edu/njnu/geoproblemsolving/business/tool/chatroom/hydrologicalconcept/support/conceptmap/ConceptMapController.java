package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.conceptmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhiyi
 * @Date 2020/6/29  10:35
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/conceptMap")
public class ConceptMapController {
    @Autowired
    ConceptMapService conceptMapService;

    @RequestMapping(value = "/inquiry/{conceptId}",method = RequestMethod.GET)
    public Object readProject(@PathVariable("conceptId") String conceptId) {
        return conceptMapService.getConceptMapByConceptId(conceptId);
    }

}
