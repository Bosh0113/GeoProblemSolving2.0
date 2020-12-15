package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept;


import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.conceptmap.ConceptMap;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.conceptmap.ConceptMapService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.concepts.Concepts;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.concepts.ConceptsService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.geoicon.GeoIconService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.GeoRule;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.GeoRuleService;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.userimage.UserImage;
import cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.userimage.UserImageService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Description: HydrologicalConcept
 * <p>
 * Created by LYC on 2019/12/23 11:39
 */
@Service
public class AnsjSegService {
    @Value("${spring.data.mongodb.conceptCollection}")
    String ConceptSemantic;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    GeoIconService geoIconService;
    @Autowired
    ConceptMapService conceptMapService;

    @Autowired
    ConceptsService conceptsService;

    @Autowired
    UserImageService userImageService;

    @Autowired
    GeoRuleService geoRuleService;


    List<Concepts> Words = new ArrayList<>();
    int SUM = 0;

    public String processInfo(String info) {
        String result = "";
        Result output = NlpAnalysis.parse(info);
        if (output.size() != 0) {
            JSONArray wordArray = new JSONArray();
            for (int i = 0; i < output.size(); i++) {
                String word = output.get(i).getName(); //拿到词
                String natureStr = output.get(i).getNatureStr(); //拿到词性

//                System.out.println(word + "," + "词性是:" + natureStr);
                wordArray.add(word);
                if (i < output.size() - 1) {
                    String w = output.get(i).getName().concat(output.get(i + 1).getName());
                    wordArray.add(w);
                }
            }
//            System.out.println(wordArray.toString());
            result = wordArray.toString();
        } else {
            result = "empty";
        }
        return result;
    }


    public String elasticSearch(String info) {
        JSONArray wordArray = JSONObject.parseArray(info);
        String searchResult = "";

        int size = wordArray.size();
        if (wordArray.size() != 0) {
            //搜索概念
            JSONArray arr1 = new JSONArray();
            //搜索概念图
            JSONArray arr3 = new JSONArray();
            JSONArray arr5 = new JSONArray();
            //搜索规则
            JSONArray arr2 = new JSONArray();
            //存储库中包含的概念
            List<Concepts> conceptsArr = new ArrayList<>();
            JSONArray arr4 = new JSONArray();

            for (int i = 0; i < size; i++) {
                String name = wordArray.getString(i);

                Concepts concept = conceptsService.findByName(name);
                if (concept != null) {
                    arr1.add(concept);
                    conceptsArr.add(concept);
                    SUM++;

                    List<UserImage> images = userImageService.findByConceptId(concept.getConceptID());
                    for (int l = 0; l < images.size(); l++) {
                        UserImage image = images.get(l);
                        arr3.add(image);
                    }

                    ConceptMap conceptMap = conceptMapService.getConceptMapByConceptId(concept.getConceptID());
                    if (conceptMap != null) {
                        arr5.add(conceptMap);
                    }
                }
                //搜索规则
                List<GeoRule> rules = geoRuleService.findRulesByKey(name);
                for (int j = 0; j < rules.size(); j++) {
                    arr2.add(rules.get(j));
                    List<String> to = rules.get(j).getTo();
                    for (int k = 0; k < to.size(); k++) {
                        String n = to.get(k);
                        Concepts c = conceptsService.findByName(n);
                        if (c != null) {
                            arr1.add(c);
                            List<UserImage> images = userImageService.findByConceptId(c.getConceptID());
                            for (int l = 0; l < images.size(); l++) {
                                UserImage image = images.get(l);
                                arr3.add(image);
                            }
                        }
                    }
                }

            }

            arr4 = wordFrequency(conceptsArr);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("conceptMap", arr5);
            jsonObject.put("frequency", arr4);
            jsonObject.put("sum", SUM);
            jsonObject.put("type", "concepts");
            if (SUM == 0) {
                searchResult = "";
            } else {
                searchResult = jsonObject.toString();
            }
        }
        return searchResult;
    }

    public JSONArray wordFrequency(List<Concepts> arr) {
        for (int i = 0; i < arr.size(); i++) {
            Concepts newWord = new Concepts();
            newWord = arr.get(i).myclone();
            newWord.setFrequency(1);
            boolean flag = false;
            for (int j = 0; j < Words.size(); j++) {
                Concepts word = Words.get(j);
                if (newWord.getName().equals(word.getName())) {
                    word.setFrequency(word.getFrequency() + 1);
                    flag = true;
                }
            }
            if (!flag) {
                Words.add(newWord);
            }
        }
        JSONArray wordsReturn = new JSONArray();
        for (int i = 0; i < Words.size(); i++) {
            Concepts word = Words.get(i);
            wordsReturn.add(word);
        }
        return wordsReturn;
    }
}
