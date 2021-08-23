package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import com.alibaba.fastjson.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @ClassName TagUtil
 * @Description 标签工具类：处理用于与资源标签
 * @Author zhngzhng
 * @Date 2021/8/19
 **/
public class TagUtil {
    public static HashMap<String, String> recoveryResTag(String tagStr){
        HashMap<String, String> resTagMap = new HashMap<>();
        String[] tagArray = tagStr.split("O517");
        //type and suffix 只能有一种
        resTagMap.put("type", tagArray[0]);
        resTagMap.put("suffix", tagArray[1]);
        return resTagMap;
    }

    public static HashMap<String, HashSet<String>> recoveryUserTag(String tagStr){
        HashMap<String, HashSet<String>> userTagMap = new HashMap<>();
        String[] tagArray = tagStr.split("O517");
        //三类判断条件
        String roles =  tagArray[0];
        String domains = tagArray[1];
        String organizations = tagArray[2];
        HashSet<String> roleSet = new HashSet<>(Arrays.asList(roles.split("#")));
        HashSet<String> domainsSet = new HashSet<>(Arrays.asList(domains.split("#")));
        HashSet<String> organizationsSet = new HashSet<>(Arrays.asList(organizations.split("#")));

        userTagMap.put("role", roleSet);
        userTagMap.put("domain", domainsSet);
        userTagMap.put("organization", organizationsSet);
        return userTagMap;
    }

    public static String setResourceTag(ResourceEntity res){
        String type = res.getType();
        String suffix = res.getSuffix();
        return addFlagInTags(type, suffix);
    }

    public static String setResourceTag(HashMap<String,String> res){
        String type = res.get("type");
        String suffix = res.get("suffix");
        return addFlagInTags(type, suffix);
    }

    public static String setUserTag(String role, JSONArray domainArray, JSONArray orgArray){
        String domain = array2String(domainArray);
        String organization = array2String(orgArray);
        return addFlagInTags(role, domain, organization);
    }

    /**
     * 为不同类型字符串添加分隔符
     * @param separator
     * @param tags
     * @return
     */
    public static String addFlag(String separator, String... tags){
        String tagStr = "";
        for (int i = 0; i < tags.length; i++) {
            tagStr += (i == tags.length - 1) ? tags[i] : tags[i] + separator;
        }
        return tagStr;
    }

    public static String addFlagInTags(String... tags){
        return addFlag("O517", tags);
    }

    /**
     * 将 jsonArray 转为带分隔符的字符串
     * @param separator
     * @param array
     * @return
     */
    public static String jsonArrayToString(String separator, JSONArray array){
        String arrayStr = "";
        if (array != null && array.size() != 0){
            for (int i = 0; i < array.size(); i++) {
                String item = (String)array.get(i);
                arrayStr += (i == array.size() - 1) ? item : item +separator;
            }
        }
        return arrayStr;
    }

    public static String array2String(JSONArray array){
        return jsonArrayToString("#", array);
    }

    /**
     * 返回两个 Set 是否有相同的元素
     * @param source
     * @param target
     * @return
     */
    public static boolean checkTagContain(HashSet source, HashSet target){
        int oriSize = source.size();
        //若 source 中包含 target 中内容会被移除
        source.removeAll(target);
        return oriSize != source.size();
    }






}